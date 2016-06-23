#!/bin/bash

# undeploy app war

# author tenghao(mailto:tenghao@primeton.com)

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
appName=app1
storagePath=/storage/app

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./undeploy.sh -reqId 3001 -appName app1 -storagePath /storage/app"
    
	echo "-h) print help"
	echo "-reqId) request id"
    
	echo "-appName) application name"
	echo "-storagePath) application storage path"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-appName) appName=$2;shift 2;;
		-storagePath) storagePath=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# check params
	if [ -z ${appName} ]; then
		_return "Undeploy error : -appName xxx parameter not found."
		_help
		exit 0
	fi
	
	if [ -z ${storagePath} ]; then
		_return "Undeploy error : -storagePath xxx parameter not found."
		_help
		exit 0
	fi
	
	# prepare variables
	APP_ROOT_PATH=${storagePath}/${appName}
	JETTY_TEMPLATE_PATH=${PROGRAME_HOME_PATH}/Jetty/jetty-8.1.10
	JETTY_TARGET_PATH=${APP_ROOT_PATH}/jetty-8.1.10
	WAR_TEMPLATE_ROOT=${JETTY_TEMPLATE_PATH}/webapps
	WAR_TARGET_ROOT=${JETTY_TARGET_PATH}/webapps
	
	# delete app war
	if [ -d "${WAR_TARGET_ROOT}/default" ]; then
		# backup files dir to webapps
		mv ${WAR_TARGET_ROOT}/default/files ${WAR_TARGET_ROOT}
		# remove war
		rm -r ${WAR_TARGET_ROOT}/default
		_success
		_return "Jetty appName ${appName} undeploy app war successfully."
	else
    		_fail
		_error "Jetty appName ${appName} failed to undeploy app war, because it is not exists."
		exit 1
	fi


	# copy app template war
	if [ -d "${WAR_TEMPLATE_ROOT}/default" ]; then
		# copy war
		cp -r ${WAR_TEMPLATE_ROOT}/default ${WAR_TARGET_ROOT}
		# restore files dir to default
		mv ${WAR_TARGET_ROOT}/files ${WAR_TARGET_ROOT}/default
		_success
		_return "Jetty appName ${appName} deploy app template war successfully."
		exit 0
	else
    		_fail
		_error "Jetty appName ${appName} failed to deploy app template war, because it is not exists."
		exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh