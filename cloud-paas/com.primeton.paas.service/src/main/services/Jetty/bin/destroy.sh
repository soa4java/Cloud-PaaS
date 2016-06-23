#!/bin/bash

# destroy jetty service

# author tenghao(mailto:tenghao@primeton.com)

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
ip=${PAAS_LOCAL_IP}
port=
appName=
storagePath=/storage/app

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./destroy.sh -reqId 3001 -instId 20001 -appName app1 -storagePath /storage/app"
    
	echo "-h) print help"
	echo "-reqId) request id"
    
	echo "-instId) service instance id"
	echo "-appName) application name"
	echo "-storagePath) application storage path"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-instId) instId=$2;shift 2;;
		-appName) appName=$2;shift 2;;
		-storagePath) storagePath=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# check params
	if [ -z ${instId} ]; then
		_return "Destroy Jetty error : -instId xxx parameter not found."
		_help
		exit 0
	fi

	if [ -z ${appName} ]; then
		_return "Destroy Jetty error : -appName xxx parameter not found."
		_help
		exit 0
	fi
	
	if [ -z ${storagePath} ]; then
		_return "Destroy Jetty error : -storagePath xxx parameter not found."
		_help
		exit 0
	fi
	
	# prepare variables
	APP_ROOT_PATH=${storagePath}/${appName}
	JETTY_TEMPLATE_PATH=${PROGRAME_HOME_PATH}/Jetty/jetty-8.1.10 
	JETTY_TARGET_PATH=${APP_ROOT_PATH}/jetty-8.1.10
	JETTY_INST_WORK_DIR=${JETTY_TARGET_PATH}/instances/${instId}
	
	# destroy jetty instance work dir
	if [ -d "${JETTY_INST_WORK_DIR}" ]; then
		rm -r ${JETTY_INST_WORK_DIR}
		_success
		_return "Jetty instId ${instId} appName ${appName} destroy successfully."
	else
    		_fail
		_error "Jetty instId ${instId} appName ${appName} failed to destroy, because it is not exists."
		exit 0
	fi
	
	# if all instances has been destroied , then destroy app
	if [ -z $(ls ${JETTY_TARGET_PATH}/instances/ ) ]; then
		rm -r ${APP_ROOT_PATH}
		_success
		_return "Jetty appName ${appName} destroy successfully."
	fi
	
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh