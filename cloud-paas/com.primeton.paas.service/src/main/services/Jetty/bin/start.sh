#!/bin/bash

# start jetty service

# author tenghao(mailto:tenghao@primeton.com)

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
ip=${PAAS_LOCAL_IP}
port=
appName=
minMemory=128
maxMemory=256
minPermSize=64
maxPermSize=128
debugMode=false
debugPort=8787
# runMode=develop
runMode=product
storagePath=/storage/app
vmArgs=

# zkConfig.xml
ZK_CONFIG_XML=${BIN_HOME_PATH}/Common/conf/zkConfig.xml

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 10001 -srvDefName Jetty -srvInstId 20001 -clusterName 20001 -port 7000 -appName app1 -storagePath /storage/app"
	echo "./start.sh -reqId 10002 -srvDefName Jetty -srvInstId 20001 -clusterName 20001 -port 7000 -appName app1 -storagePath /storage/app -ip 127.0.0.1 -minMemory 128 -maxMemory 256 -minPermSize 64 -maxPermSize 128"
    
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvInstId) service instance id"
	echo "-srvDefName) service type"
	echo "-clusterName) cluster name"
    
	echo "-ip) host ip"
	echo "-port) http Port"
	echo "-appName) application name"
	echo "-minMemory) jvm minMemory"
	echo "-maxMemory) jvm maxMemory"
	echo "-minPermSize) jvm minPermSize"
	echo "-maxPermSize) jvm maxPermSize"
	echo "-storagePath) jetty storagePath"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-ip) ip=$2;shift 2;;
		-port) port=$2;shift 2;;
		-appName) appName=$2;shift 2;;
		-minMemory) minMemory=$2;shift 2;;
		-maxMemory) maxMemory=$2;shift 2;;
		-minPermSize) minPermSize=$2;shift 2;;
		-maxPermSize) maxPermSize=$2;shift 2;;
		-storagePath) storagePath=$2;shift 2;;
        -vmArgs) vmArgs=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# check params
	if [ -z ${appName} ]; then
		_return "appName is null or blank."
		exit 0
	fi
	
	#_return "vmArgs : ${vmArgs}"
	#vmArgs=${vmArgs//%/  }
	#_return "vmArgs : ${vmArgs}"

	# prepare variables
	JETTY_HOME=${storagePath}/${appName}/jetty-8.1.10
	JETTY_CLASSPATH="${JETTY_HOME}/start.jar"
	WORK_DIR=${JETTY_HOME}/instances/${INSTANCE_ID}
	FILE_ROOT_DIR=${JETTY_HOME}/files
	
	# override zkConfig.xml
	if [ -d ${JETTY_HOME}/webapps/default/WEB-INF/config ]; then
		cp -f ${ZK_CONFIG_XML} ${JETTY_HOME}/webapps/default/WEB-INF/config/
	fi
    
	# jvm classpath
	for f in `find ${JETTY_HOME}/lib -name "*.jar"`;	do
		JETTY_CLASSPATH=${JETTY_CLASSPATH}:$f
	done
	
	export JETTY_CLASSPATH
	
	# check process
	FILE_PATH_STATE=${BIN_HOME_PATH}/Jetty/bin/state/${INSTANCE_ID}.txt
	PID_FILE=${BIN_HOME_PATH}/Jetty/instances/${INSTANCE_ID}/pid.txt
	if [ -f ${PID_FILE} ]; then
		if [ `ps -p ${pid} |grep -v PID | wc -l` -eq 1 ]; then
			_success
			_return "Jetty srvInstId ${INSTANCE_ID} is already running."
			exit 0
		fi
	fi
	
	# jvm params
	JAVA_OPTS="-Xms${minMemory}m -Xmx${maxMemory}m -XX:PermSize=${minPermSize}m -XX:MaxPermSize=${maxPermSize}m -XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true"
	
	if [[ ${debugMode} = "true" ]] && [ ${debugPort} -gt 0 ]; then
		JAVA_OPTS="${JAVA_OPTS} -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=${debugPort}"
	fi
	
	JAVA_OPTS="${JAVA_OPTS} -Dorg.eclipse.jetty.server.Request.maxFormContentSize=-1"
	JAVA_OPTS="${JAVA_OPTS} -Dpaas.srvType=${SERVICE_TYPE} -Dpaas.instId=${INSTANCE_ID} -Dpaas.clusterName=${CLUSTER_NAME} -Dpaas.appName=${appName}"
	JAVA_OPTS="${JAVA_OPTS} -Dpaas.ip=${ip} -Dpaas.httpPort=${port} -Dpaas.workDir=${WORK_DIR} -Dpaas.runMode=${runMode} -Dpaas.fileRootDir=${FILE_ROOT_DIR} -Dserver.home=${JETTY_HOME}"
    JAVA_OPTS="${JAVA_OPTS} ${vmArgs}"
	export JAVA_OPTS

	# start
	CMD="${JAVA_HOME}/bin/java ${JAVA_OPTS} -cp ${JETTY_CLASSPATH} org.eclipse.jetty.start.Main jetty.host=${ip} jetty.port=${port}"
	nohup ${CMD} > ${SCRIPT_OUT_PATH}/nohup.out 2>&1 &
	PID=$!
	_pid ${PID}
	
	# find process
	if [ $(ps -ef | grep ${PID} | grep -v grep | wc -l) -gt 0 ]; then
		_success
		_return "Jetty srvInstId ${INSTANCE_ID} start successfully, pid=${PID}."
		exit 0
	else
		_fail
		_error "Jetty srvInstId ${INSTANCE_ID} failed to start."
		exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh
