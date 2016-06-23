#!/bin/bash

# deploy app war

# author tenghao(mailto:tenghao@primeton.com)

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# Global variables, The variable $BIN_HOME_PATH must be defined
PATH=${PATH}:${JAVA_HOME}/bin:${PROGRAME_HOME_PATH}/Jetty/svnkit-1.7.4-v1/bin

# my variables
appName=app1
storagePath=/storage/app

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./deploy.sh -reqId 3001 -appName app1 -storagePath /storage/app -warName app1.war -warVersion 1.1.1"
    
	echo "-h) print help"
	echo "-reqId) request id"
    
	echo "-appName) application name"
	echo "-storagePath) application storage path"
	echo "-warName) war name"
	echo "-revision) war revision"
	echo "-svnUrl) svn repository url"
	echo "-svnUser) svn user"
	echo "-svnPassword) svn password"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-appName) appName=$2;shift 2;;
		-storagePath) storagePath=$2;shift 2;;
		-warName) warName=$2;shift 2;;
		-revision) revision=$2;shift 2;;
		-svnUrl) svnUrl=$2;shift 2;;
		-svnUser) svnUser=$2;shift 2;;
		-svnPassword) svnPassword=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# check params
	if [ -z ${appName} ]; then
		_return "Deploy error : -appName xxx parameter not found."
		_help
		exit 1
	fi
	
	if [ -z ${storagePath} ]; then
		_return "Deploy error : -storagePath xxx parameter not found."
		_help
		exit 1
	fi
	
	# prepare variables
	APP_ROOT_PATH=${storagePath}/${appName}
	JETTY_TEMPLATE_PATH=${PROGRAME_HOME_PATH}/Jetty/jetty-8.1.10
	JETTY_TARGET_PATH=${APP_ROOT_PATH}/jetty-8.1.10
	WAR_TEMPLATE_ROOT=${JETTY_TEMPLATE_PATH}/webapps
	WAR_TARGET_ROOT=${JETTY_TARGET_PATH}/webapps
	
	# clean temp
	rm -rf ${TEMP_HOME_PATH}/download/
	rm -rf ${TEMP_HOME_PATH}/${warName}/
	
	# download app war
	jsvn checkout ${svnUrl} --username ${svnUser} --password ${svnPassword} ${TEMP_HOME_PATH}/download --non-interactive
	jsvn update --revision ${revision} ${TEMP_HOME_PATH}/download/${warName} --username ${svnUser} --password ${svnPassword} --non-interactive
	if [ ! -f ${TEMP_HOME_PATH}/download/${warName} ]; then
		_error "File ${TEMP_HOME_PATH}/download/${warName} does not exist."
		_fail
		exit 0
	fi

	mv ${TEMP_HOME_PATH}/download/${warName} ${TEMP_HOME_PATH}/download/${warName}.zip
	if [ ! -d ${TEMP_HOME_PATH}/${warName}/ ]; then
		mkdir -p ${TEMP_HOME_PATH}/${warName}/
	fi
	rm -rf ${TEMP_HOME_PATH}/${warName}
	unzip -q ${TEMP_HOME_PATH}/download/${warName}.zip -d ${TEMP_HOME_PATH}/${warName}
	_success
	_return "Jetty appName ${appName} download warName ${warName} successfully."
	# download end

	# backup files dir to webapps
	mv ${WAR_TARGET_ROOT}/default/files ${WAR_TARGET_ROOT}
	
	# remove war
	rm -rf ${WAR_TARGET_ROOT}/default/*
        
	# copy war to corresponding ${WAR_TARGET_ROOT}
	cp -r ${TEMP_HOME_PATH}/${warName}/* ${WAR_TARGET_ROOT}/default

	# delete files in war
	rm -rf ${WAR_TARGET_ROOT}/default/files
	
	# restore files dir to default
	mv ${WAR_TARGET_ROOT}/files ${WAR_TARGET_ROOT}/default
	
	# delete temp war
	rm -rf ${TEMP_HOME_PATH}/download/
	rm -rf ${TEMP_HOME_PATH}/${warName}/

	_success
	_return "Jetty appName ${appName} deploy warName ${warName} successfully."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh