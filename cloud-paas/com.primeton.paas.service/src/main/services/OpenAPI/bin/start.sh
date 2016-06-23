#!/bin/bash

# start jetty service

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
# runMode=develop
runMode=product

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 10001 -srvDefName OpenAPI -srvInstId 20001 -clusterName 20001 -port 7900"
	echo "./start.sh -reqId 10002 -srvDefName OpenAPI -srvInstId 20001 -clusterName 20001 -port 7900 -ip 127.0.0.1 -minMemory 128 -maxMemory 256 -minPermSize 64 -maxPermSize 128"
    
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
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-ip) ip=$2;shift 2;;
		-port) port=$2;shift 2;;
        -srvDefName) srvDefName=$2;shift 2;;
		-minMemory) minMemory=$2;shift 2;;
		-maxMemory) maxMemory=$2;shift 2;;
		-minPermSize) minPermSize=$2;shift 2;;
		-maxPermSize) maxPermSize=$2;shift 2;;
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
	OPENAPI_HOME=${PROGRAME_HOME_PATH}/OpenAPI/openapi
	OPENAPI_CLASSPATH="${OPENAPI_HOME}/start.jar"
	INST_WORK_DIR=${OPENAPI_HOME}/instances/${INSTANCE_ID}
	ZK_CONF_FILE=${BIN_HOME_PATH}/Common/conf/zkConfig.xml
    
	
	# create openapi instance work dir
	if [ ! -d "${INST_WORK_DIR}" ]; then
		mkdir -p ${INST_WORK_DIR}/default/{config,logs}
	fi
	cp -f ${ZK_CONF_FILE} ${INST_WORK_DIR}/default/config
	
	# jvm classpath
	for f in `find ${OPENAPI_HOME}/lib -name "*.jar"`;	do
		OPENAPI_CLASSPATH=${OPENAPI_CLASSPATH}:$f
	done
	
	export OPENAPI_CLASSPATH
	# echo ${OPENAPI_CLASSPATH}
	
	# check process
	FILE_PATH_STATE=${BIN_HOME_PATH}/OpenAPI/bin/state/${INSTANCE_ID}.txt
	PID_FILE=${BIN_HOME_PATH}/OpenAPI/instances/${INSTANCE_ID}/pid.txt
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
    
    JAVA_OPTS="${JAVA_OPTS} -Dorg.eclipse.jetty.server.Request.maxFormContentSize=-1"
	JAVA_OPTS="${JAVA_OPTS} -Dpaas.srvType=${SERVICE_TYPE} -Dpaas.instId=${INSTANCE_ID} -Dpaas.clusterName=${CLUSTER_NAME} "
	JAVA_OPTS="${JAVA_OPTS} -Dpaas.ip=${ip} -Dpaas.httpPort=${port} -Dpaas.instWorkDir=${INST_WORK_DIR} -Dpaas.runMode=${runMode}"
    JAVA_OPTS="${JAVA_OPTS} -Dpaas.appName=openapi -Djetty.home=${OPENAPI_HOME}"
    
	export JAVA_OPTS
    
	# start
    #CMD="${JAVA_HOME}/bin/java ${JAVA_OPTS} -cp ${OPENAPI_CLASSPATH} org.eclipse.jetty.start.Main jetty.host=${ip} jetty.port=${port}"
    JAVA_OPTS="${JAVA_OPTS} -Djetty.host=${ip} -Djetty.port=${port}"
    cd ${OPENAPI_HOME}
    CMD="${JAVA_HOME}/bin/java ${JAVA_OPTS} -jar ${OPENAPI_HOME}/start.jar"
    echo ${CMD}
    
	nohup ${CMD} > ${SCRIPT_OUT_PATH}/nohup.out 2>&1 &
	PID=$!
	_pid ${PID}
    
    
	# find process
	if [ $(ps -ef | grep ${PID} | grep -v grep | wc -l) -gt 0 ]; then
		_success
		_return "OpenAPI srvInstId ${INSTANCE_ID} start successfully, pid=${PID}."
		exit 0
	else
		_fail
		_error "OpenAPI srvInstId ${INSTANCE_ID} failed to start."
		exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh