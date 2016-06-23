#!/bin/bash

# start sms service

# author liyanping(mailto:liyp@primeton.com)

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
ip=${PAAS_LOCAL_IP}
port=80

minMemory=128
maxMemory=256
minPermSize=64
maxPermSize=128
debugMode=false
debugPort=8787

username=
password=
remoteAddr=
remotePort=
serviceCode=
extendCode=
protocol=
registeredDelivery=
waittime=

SMS_HOME=${PROGRAME_HOME_PATH}/SMS/sms

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 10001 -srvDefName SMS -srvInstId 10101 -clusterName 10201 -port 8010 "
	echo "./start.sh -reqId 10001 -srvDefName SMS -srvInstId 10101 -clusterName 10201 -port 8010 -ip 127.0.0.1 "
    
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
    
	echo "-username=) sms userName"
	echo "-password) sms password"
	echo "-remoteAddr) 95516 ip"
	echo "-remotePort) 95516 port"
	echo "-serviceCode) service code"
	echo "-protocol) protocol,such as mmpp2.0"
	echo "-extendCode) extend Code"
	echo "-registeredDelivery) registered Delivery :0 or 1"
	echo "-waittime) wait time "
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
        
		-remoteAddr) remoteAddr=$2;shift 2;;
		-remotePort) remotePort=$2;shift 2;;
		-username) username=$2;shift 2;;
		-serviceCode) serviceCode=$2;shift 2;;
		-protocol) protocol=$2;shift 2;;
		-extendCode) extendCode=$2;shift 2;;
		-registeredDelivery) registeredDelivery=$2;shift 2;;
		-waittime) waittime=$2;shift 2;;
		-username) username=$2;shift 2;;
		-password) password=$2;shift 2;;
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
	SMS_CLASSPATH="${SMS_HOME}/lib/com.primeton.paas.sms.server.jar"
    	
	
	# jvm classpath
	#for f in `find ${SMS_HOME}/lib -name "*.jar"`;	do
	#	SMS_CLASSPATH=${SMS_CLASSPATH}:$f
	#done
	
	export SMS_CLASSPATH
	
	# copy zkConfig.xml to conf
	cp -f ${BIN_HOME_PATH}/Common/conf/zkConfig.xml ${SMS_HOME}/conf
    	
	# check process
	FILE_PATH_STATE=${BIN_HOME_PATH}/SMS/bin/state/${INSTANCE_ID}.txt
	PID_FILE=${BIN_HOME_PATH}/SMS/instances/${INSTANCE_ID}/pid.txt
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
	
	JAVA_OPTS="${JAVA_OPTS} -DSMS_HOME=${SMS_HOME} -DSMS_HOST=${ip} -DSMS_PORT=${port}"
	JAVA_OPTS="${JAVA_OPTS} -DUSER_NAME=${username} -DPASSWORD=${password}"
	JAVA_OPTS="${JAVA_OPTS} -DREMOTE_ADDR=${remoteAddr} -DREMOTE_PORT=${remotePort}"
	JAVA_OPTS="${JAVA_OPTS} -DSERVICE_CODE=${serviceCode} -DEXTEND_CODE=${extendCode} -DPROTOCAL=${protocal}"
	JAVA_OPTS="${JAVA_OPTS} -Dupaas.srvType=${SERVICE_TYPE} -Dupaas.instId=${INSTANCE_ID} -Dupaas.clusterName=${CLUSTER_NAME}"
	export JAVA_OPTS

	# start
	CMD="${JAVA_HOME}/bin/java ${JAVA_OPTS} -cp ${SMS_CLASSPATH} com.primeton.paas.sms.server.startup.SmsServerBootstrap start"
	nohup ${CMD} > ${SCRIPT_OUT_PATH}/nohup.out 2>&1 &
	PID=$!
	_pid ${PID}
	
	# find process
	if [ $(ps -ef | grep ${PID} | grep -v grep | wc -l) -gt 0 ]; then
		_success
		_return "SMS srvInstId ${INSTANCE_ID} start successfully, pid=${PID}."
		exit 0
	else
		_fail
		_error "SMS srvInstId ${INSTANCE_ID} failed to start."
		exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh