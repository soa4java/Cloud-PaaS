#!/bin/bash

# restart jetty service

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

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./restart.sh -reqId 3001 -srvDefName Jetty -srvInstId 20001 -clusterName cluster1 -port 7000 -appName app1 -storagePath /storage/app/app1/jetty"
	echo "./restart.sh -reqId 3001 -srvDefName Jetty -srvInstId 20001 -clusterName cluster1 -port 7000 -appName app1 -storagePath /storage/app/app1/jetty -ip 127.0.0.1 -minMemory 128 -maxMemory 256 -minPermSize 64 -maxPermSize 128"
    
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
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# check params
	if [ -z ${INSTANCE_ID} ]; then
		_return "Restart Jetty error : -srvInstId xxx parameter not found."
		_help
		exit 1
	fi
	
	if [ -z ${appName} ]; then
		_return "Restart Jetty error : -appName xxx parameter not found."
		_help
		exit 1
	fi
	
	# stop process
	PID_FILE=${BIN_HOME_PATH}/Jetty/instances/${INSTANCE_ID}/pid.txt
	if [ ! -f ${PID_FILE} ]; then
		_fail
		_error "Jetty srvInstId ${INSTANCE_ID} pid file ${PID_FILE} is not exist."
		exit 0
	fi
	
	pid=`cat ${PID_FILE}`
	if [ `ps -p ${pid} |grep -v PID | wc -l` -eq 0 ]; then
		_success
		_return "Jetty srvInstId ${INSTANCE_ID} is not running."
		exit 0
	else
		kill `cat ${PID_FILE}`
	fi
	
	# check process
	i=0
	while test $i -ne 10 ; do
		if [ `ps -p ${pid} |grep -v PID | wc -l` -eq 0 ]; then
			_success
			_return "Jetty srvInstId ${INSTANCE_ID}  succeeded to stop."
			exit 0
		fi
		echo -n "-"
		i=`expr $i + 1`
		sleep 1
	done
	
	if [ `ps -p ${pid} |grep -v PID | wc -l` -eq 1 ]; then
			kill -9 `cat ${pid}`
			_success
			_return "Jetty srvInstId ${INSTANCE_ID}  succeeded to force stop."
	fi
	
	# prepare variables
	JETTY_HOME=${storagePath}/${appName}/jetty-8.1.10
	JETTY_CLASSPATH="${JETTY_HOME}/start.jar"
	INST_WORK_DIR=${JETTY_HOME}/instances/${INSTANCE_ID}
	FILE_ROOT_DIR=${JETTY_HOME}/files
    
	# jvm classpath
	for f in `find ${JETTY_HOME}/lib -name "*.jar"`;	do
		JETTY_CLASSPATH=${JETTY_CLASSPATH}:$f
	done
	
	export JETTY_CLASSPATH
	
	# jvm params
	JAVA_OPTS="-Xms${minMemory}m -Xmx${maxMemory}m -XX:PermSize=${minPermSize}m -XX:MaxPermSize=${maxPermSize}m -XX:+HeapDumpOnOutOfMemoryError"
	
	if [[ ${debugMode} = "true" ]] && [ ${debugPort} -gt 0 ]; then
		JAVA_OPTS="${JAVA_OPTS} -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=${debugPort}"
	fi
	
	JAVA_OPTS="${JAVA_OPTS} -Dorg.eclipse.jetty.server.Request.maxFormContentSize=-1"
	JAVA_OPTS="${JAVA_OPTS} -Dpaas.srvType=${SERVICE_TYPE} -Dpaas.instId=${INSTANCE_ID} -Dpaas.clusterName=${CLUSTER_NAME} -Dpaas.appName=${appName}"
	JAVA_OPTS="${JAVA_OPTS} -Dpaas.ip=${ip} -Dpaas.httpPort=${port} -Dpaas.instWorkDir=${INST_WORK_DIR} -Dpaas.runMode=${runMode} -Dpaas.fileRootDir=${FILE_ROOT_DIR} -Djetty.home=${JETTY_HOME}"
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