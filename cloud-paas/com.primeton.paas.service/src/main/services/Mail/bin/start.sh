#!/bin/bash

# author liuyi(mailto:liu-yi@primeton.com)

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

SERVER_HOME=${PROGRAME_HOME_PATH}/Mail/mail
minMemory=128
maxMemory=256
minPermSize=64
maxPermSize=128
debugMode=false
debugPort=8787
jdbcDriver=com.mysql.jdbc.Driver
jdbcUrl=jdbc:mysql://127.0.0.1/paas
jdbcUser=root
jdbcPassword=000000
jdbcMinPoolSize=5
jdbcMaxPoolSize=10

#my additional variables
maxMailWorkerNum=10

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 10001 -srvDefName Mail -srvInstId 10101 -clusterName 1024 -port 8010 "
	echo "./start.sh -reqId 10001 -srvDefName Mail -srvInstId 10101 -clusterName 10201 -port 8010 -ip 127.0.0.1 -minMemory 128 -maxMemory 256 -minPermSize 64 -maxPermSize 128 -jdbcDriver com.mysql.jdbc.Driver -jdbcUrl jdbc:mysql://192.168.100.221:3306/upaas -jdbcUser root -jdbcPassword 000000 -jdbcMinPoolSize 5 -jdbcMaxPoolSize 10"
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvInstId) service instance id"
	echo "-srvDefName) service type"
	echo "-clusterName) cluster name"
 
	echo "-ip) host ip"
	echo "-port) http Port"
	echo "-minMemory) jvm minMemory"
	echo "-maxMemory) jvm maxMemory"
	echo "-minPermSize) jvm minPermSize"
	echo "-maxPermSize) jvm maxPermSize"
	echo "-jdbcDriver) jdbc driver"
	echo "-jdbcUrl) jdbc url"
	echo "-jdbcUser) jdbc user"
	echo "-jdbcPassword) jdbc password"
	echo "-jdbcMinPoolSize) jdbc min pool size"
	echo "-jdbcMaxPoolSize) jdbc max pool size"
	echo "-maxMailWorkerNum) mailworker max thread number"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in	
		-ip) ip=$2;shift 2;;
		-port) port=$2;shift 2;;
		-minMemory) minMemory=$2;shift 2;;
		-maxMemory) maxMemory=$2;shift 2;;
		-minPermSize) minPermSize=$2;shift 2;;
		-maxPermSize) maxPermSize=$2;shift 2;;
		-jdbcDriver) jdbcDriver=$2;shift 2;;
		-jdbcUrl) jdbcUrl=$2;shift 2;;
		-jdbcUser) jdbcUser=$2;shift 2;;
		-jdbcPassword) jdbcPassword=$2;shift 2;;
		-jdbcMinPoolSize) jdbcMinPoolSize=$2;shift 2;;
		-jdbcMaxPoolSize) jdbcMaxPoolSize=$2;shift 2;;
		-maxMailWorkerNum) maxMailWorkerNum=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# prepare variables
	MAIL_HOME=${PROGRAME_HOME_PATH}/Mail/mail
	ATTACHMENT_HOME=${PROGRAME_HOME_PATH}/Mail/mailAttachment
	MAIL_CLASSPATH="${MAIL_HOME}/lib/com.primeton.paas.mail.server.jar"

	# copy zkConfig.xml to conf
	cp -f ${BIN_HOME_PATH}/Common/conf/zkConfig.xml ${MAIL_HOME}/conf
	
	# jvm classpath
	for f in `find ${MAIL_HOME}/lib -name "*.jar"`;	do
		MAIL_CLASSPATH=${MAIL_CLASSPATH}:$f
	done
	
	export MAIL_CLASSPATH
	
	# check process
	PID_FILE=${BIN_HOME_PATH}/Mail/instances/${INSTANCE_ID}/pid.txt
	pid=`cat ${PID_FILE}`
	if [ -f ${PID_FILE} ]; then
		if [ `ps -p ${pid} | grep -v PID | wc -l` -eq 1 ]; then
			_success
			_return "Mail srvInstId ${INSTANCE_ID} is already running."
			exit 0
		fi
	fi
	
	
	# jvm params
	JAVA_OPTS="-Xms${minMemory}m -Xmx${maxMemory}m -XX:PermSize=${minPermSize}m -XX:MaxPermSize=${maxPermSize}m -XX:+HeapDumpOnOutOfMemoryError"
	
	if [[ ${debugMode} = "true" ]] && [ ${debugPort} -gt 0 ]; then
		JAVA_OPTS="${JAVA_OPTS} -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=${debugPort}"
	fi
    
	JAVA_OPTS="${JAVA_OPTS} -DMAIL_HOME=${MAIL_HOME} -DATTACHMENT_HOME=${ATTACHMENT_HOME} -DMAIL_HOST=${ip} -DMAIL_PORT=${port}"
	JAVA_OPTS="${JAVA_OPTS} -DJDBC_DRIVER=${jdbcDriver} -DJDBC_URL=${jdbcUrl}"
	JAVA_OPTS="${JAVA_OPTS} -DJDBC_USER=${jdbcUser} -DJDBC_PASSWORD=${jdbcPassword}"
	JAVA_OPTS="${JAVA_OPTS} -DJDBC_MIN_POOL_SIZE=${jdbcMinPoolSize} -DJDBC_MAX_POOL_SIZE=${jdbcMaxPoolSize}"
	JAVA_OPTS="${JAVA_OPTS} -DMAX_MAILWORKER_NUM=${maxMailWorkerNum}"
	JAVA_OPTS="${JAVA_OPTS} -Dpaas.srvType=${SERVICE_TYPE} -Dpaas.instId=${INSTANCE_ID} -Dpaas.clusterName=${CLUSTER_NAME}"
	export JAVA_OPTS
    
	# start
	Main_Class="com.primeton.paas.mail.server.startup.BootStrap"
	CMD="${JAVA_HOME}/bin/java ${JAVA_OPTS} -classpath ${MAIL_CLASSPATH} ${Main_Class}"
	cd ${SERVER_HOME}/bin
	nohup ${CMD} > ${SCRIPT_OUT_PATH}/nohup.out 2>&1 &
	PID=$!
	_pid ${PID}
	
	
	# find process
	if [ $(ps -ef | grep ${PID} | grep -v grep | wc -l) -gt 0 ]; then
		_success
		_return "Mail srvInstId ${INSTANCE_ID} start successfully, pid=${PID}."
		exit 0
	else
		_fail
		_error "Mail srvInstId ${INSTANCE_ID} failed to start."
		exit 0
	fi	
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh