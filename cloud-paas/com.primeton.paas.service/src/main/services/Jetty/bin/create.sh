#!/bin/bash

# create jetty service

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
	echo "./create.sh -reqId 3001 -instId 20001 -appName app1 -storagePath /storage/app"
    
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
		_return "Create Jetty error : -instId xxx parameter not found."
		_help
		exit 1
	fi
	
	if [ -z ${appName} ]; then
		_return "Create Jetty error : -appName xxx parameter not found."
		_help
		exit 1
	fi
	
	# prepare variables
	APP_ROOT_PATH=${storagePath}/${appName}
	JETTY_TEMPLATE_PATH=${PROGRAME_HOME_PATH}/Jetty/jetty-8.1.10
	JETTY_TARGET_PATH=${APP_ROOT_PATH}/jetty-8.1.10
	JETTY_INST_WORK_DIR=${JETTY_TARGET_PATH}/instances/${instId}
	ZK_CONF_FILE=${BIN_HOME_PATH}/Common/conf/zkConfig.xml
	
	# create app
	if [ ! -d "${APP_ROOT_PATH}" ]; then
		mkdir -p ${APP_ROOT_PATH}
	fi
	
	# create jetty
	if [ ! -d "${JETTY_TARGET_PATH}" ]; then
		# copy source to target
		cp -r ${JETTY_TEMPLATE_PATH}  ${APP_ROOT_PATH}
		_success
		_return "Jetty appName ${appName} create successfully."
	else
		_success
		_return "Jetty appName ${appName} already exists."
	fi
	
	# create jetty instance work dir
	if [ ! -d "${JETTY_INST_WORK_DIR}" ]; then
		mkdir -p ${JETTY_INST_WORK_DIR}/default/{config,logs}
		cp ${ZK_CONF_FILE} ${JETTY_INST_WORK_DIR}/default/config
		_success
		_return "Jetty instId ${instId} appName ${appName} create successfully."
		exit 0
	else
		_fail
		_return "Jetty instId ${instId} appName ${appName} failed to create, because it is already exists."
		exit 1
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh