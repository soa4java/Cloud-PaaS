#!/bin/bash

# start cardbin service

# author tenghao(mailto:tenghao@primeton.com)

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
ip=${PAAS_LOCAL_IP}
port=
wsPort=8011
minMemory=128
maxMemory=256
minPermSize=64
maxPermSize=128
debugMode=false
debugPort=8787
jdbcDriver=com.mysql.jdbc.Driver
jdbcUrl=jdbc:mysql://192.168.100.211:3306/upaas
jdbcUser=root
jdbcPassword=000000
jdbcMinPoolSize=5
jdbcMaxPoolSize=10


#cardbin data synchronize
remoteIp=
remoteUser=
remotePwd=
hdfsFileUrl=
remoteFilePath=
tempFilePath=
destFilePath=
syncDay=
syncHour=
isSync=


# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 10001 -srvDefName CardBin -srvInstId 10101 -clusterName 10201 -port 8010 "
	echo "./start.sh -reqId 10001 -srvDefName CardBin -srvInstId 10101 -clusterName 10201 -port 8010 -ip 127.0.0.1 -wsPort 8011 -minMemory 128 -maxMemory 256 -minPermSize 64 -maxPermSize 128 -jdbcDriver com.mysql.jdbc.Driver -jdbcUrl jdbc:mysql://192.168.100.211:3306/upaas -jdbcUser root -jdbcPassword 000000 -jdbcMinPoolSize 5 -jdbcMaxPoolSize 10"
    
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


	echo "-remoteIp) remote host ip"
	echo "-remoteUser) remote host user"
	echo "-remotePwd) remote user password"
	echo "-hdfsFileUrl) hdfs file Url"
	echo "-remoteFilePath) remote data file path"
	echo "-tempFilePath) loca temp file path"
	echo "-destFilePath) local dest file path"
	echo "-syncDay) synchronize day of every month"
	echo "-syncHour) synchronize Hour of Day"
	echo "-isSync) is enable synchronize"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-ip) ip=$2;shift 2;;
		-port) port=$2;shift 2;;
		-wsPort) wsPort=$2;shift 2;;
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

		-remoteIp) remoteIp=$2;shift 2;;
		-remoteUser) remoteUser=$2;shift 2;;
		-remotePwd) remotePwd=$2;shift 2;;
		-hdfsFileUrl) hdfsFileUrl=$2;shift 2;;
		-remoteFilePath) remoteFilePath=$2;shift 2;;
		-tempFilePath) tempFilePath=$2;shift 2;;
		-destFilePath) destFilePath=$2;shift 2;;
		-syncDay) syncDay=$2;shift 2;;
		-syncHour) syncHour=$2;shift 2;;
		-isSync) isSync=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# check params
	if [ -z ${port} ]; then
		_return "port is null or blank."
		exit 0
	fi
	
	# prepare variables
	CARDBIN_HOME=${PROGRAME_HOME_PATH}/CardBin/cardbin
	#CARDBIN_CLASSPATH="${CARDBIN_HOME}/lib/com.primeton.paas.cardbin.server.jar"
    
	if [ ! -d ${CARDBIN_HOME} ]; then
		_fail
		_error "CARDBIN_HOME: ${CARDBIN_HOME} is not exists."
		exit 0
	fi
	
	# jvm classpath
	for f in `find ${CARDBIN_HOME}/lib -name "*.jar"`;	do
		CARDBIN_CLASSPATH=${CARDBIN_CLASSPATH}:$f
	done
	#CARDBIN_CLASSPATH=${CARDBIN_CLASSPATH}:${CARDBIN_HOME}/conf
	export CARDBIN_CLASSPATH
	
	# check process
	FILE_PATH_STATE=${BIN_HOME_PATH}/CardBin/bin/state/${INSTANCE_ID}.txt
	PID_FILE=${BIN_HOME_PATH}/CardBin/instances/${INSTANCE_ID}/pid.txt
	pid=`cat ${PID_FILE}`
	if [ -f ${PID_FILE} ]; then
		if [ `ps -p ${pid} |grep -v PID | wc -l` -eq 1 ]; then
			_success
			_return "SMS srvInstId ${INSTANCE_ID} is already running."
			exit 0
		fi
	fi
	
	# jvm params
	JAVA_OPTS="-Xms${minMemory}m -Xmx${maxMemory}m -XX:PermSize=${minPermSize}m -XX:MaxPermSize=${maxPermSize}m -XX:+HeapDumpOnOutOfMemoryError"
	
	if [[ ${debugMode} = "true" ]] && [ ${debugPort} -gt 0 ]; then
		JAVA_OPTS="${JAVA_OPTS} -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=${debugPort}"
	fi
	
	JAVA_OPTS="${JAVA_OPTS} -DCARDBIN_HOME=${CARDBIN_HOME} -DCARDBIN_HOST=${ip} -DCARDBIN_PORT=${port} -DCARDBIN_WS_PORT=${wsPort}"
	JAVA_OPTS="${JAVA_OPTS} -DJDBC_DRIVER=${jdbcDriver} -DJDBC_URL=${jdbcUrl}"
	JAVA_OPTS="${JAVA_OPTS} -DJDBC_USER=${jdbcUser} -DJDBC_PASSWORD=${jdbcPassword}"
	JAVA_OPTS="${JAVA_OPTS} -DJDBC_MIN_POOL_SIZE=${jdbcMinPoolSize} -DJDBC_MAX_POOL_SIZE=${jdbcMaxPoolSize}"
	JAVA_OPTS="${JAVA_OPTS} -Dupaas.srvType=${SERVICE_TYPE} -Dupaas.instId=${INSTANCE_ID} -Dupaas.clusterName=${CLUSTER_NAME}"

	JAVA_OPTS="${JAVA_OPTS} -DREMOTE_HOST=${remoteIp} -DREMOTE_USER=${remoteUser} -DREMOTE_PWD=${remotePwd}"
	JAVA_OPTS="${JAVA_OPTS} -DHDFS_FILE_URL=${hdfsFileUrl} -DREMOTE_FILE_PATH=${remoteFilePath} -DTMP_FILE_PATH=${tempFilePath} -DDEST_FILE_PATH=${destFilePath}"
	JAVA_OPTS="${JAVA_OPTS} -DSYNC_DAY_OF_MONTH=${syncDay} -DSYNC_HOUR_OF_DAY=${syncHour}"
	JAVA_OPTS="${JAVA_OPTS} -DSYNC_IS_ENABLE=${isSync}"
	export JAVA_OPTS

	# start
	CMD="${JAVA_HOME}/bin/java ${JAVA_OPTS} -cp ${CARDBIN_CLASSPATH} com.primeton.paas.cardbin.server.startup.FinanceCardBinServerBootstrap start"
	nohup ${CMD} > ${SCRIPT_OUT_PATH}/nohup.out 2>&1 &
	PID=$!
	_pid ${PID}
	
	# find process
	if [ $(ps -ef | grep ${PID} | grep -v grep | wc -l) -gt 0 ]; then
		_success
		_return "CardBin srvInstId ${INSTANCE_ID} start successfully, pid=${PID}."
		exit 0
	else
		_fail
		_error "CardBin srvInstId ${INSTANCE_ID} failed to start."
		exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh
