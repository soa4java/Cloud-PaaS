#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

PATH=${PATH}:${JAVA_HOME}/bin:${PROGRAME_HOME_PATH}/Tomcat/svnkit-1.7.4-v1/bin
export PATH
if [ -d ${JAVA_HOME} ]; then
	export ${JAVA_HOME}
fi

svnUser=admin
svnPassword=000000

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./deploy.sh -reqId 0 -srvId 20001 -appName app1 -warName app1.war -revision 1.1.1 -svnUrl http://192.168.2.123:18880/repos/app1/war/ -svnUser admin -svnPassword 000000"
    echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvId) tomcat service id"    
	echo "-appName) application name"
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
		-srvId) srvId=$2;shift 2;;
		-appName) appName=$2;shift 2;;
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
	if [ -z ${srvId} ]; then
		_error "srvId is null or blank."		
		exit 0
	fi
	
	if [ -z ${warName} ]; then
		warName="${appName}.war"
	fi
	
	# prepare variables
	TOMCAT_HOME=${PROGRAME_HOME_PATH}/Tomcat/${srvId}/apache-tomcat-7.0.62
	
	# clean
	rm -rf ${TEMP_HOME_PATH}/download
	
	# checkout war file from svn
	jsvn checkout ${svnUrl} --username ${svnUser} --password ${svnPassword} ${TEMP_HOME_PATH}/download --non-interactive
	if [ ! -z ${revision} ]; then
		jsvn update --revision ${revision} ${TEMP_HOME_PATH}/download/${warName} --username ${svnUser} --password ${svnPassword} --non-interactive
	fi
	
	if [ ! -f ${TEMP_HOME_PATH}/download/${warName} ]; then
		_error "War file ${TEMP_HOME_PATH}/download/${warName} not exists."
		_fail
		exit 0
	fi
	
	# clean
	rm -rf ${TOMCAT_HOME}/webapps/default/*
	# deploy
	unzip -q ${TEMP_HOME_PATH}/download/${warName} -d ${TOMCAT_HOME}/webapps/default
	
	_success
	_return "Jetty appName ${appName} download warName ${warName} successfully."
	
	# clean
	rm -rf ${TEMP_HOME_PATH}/${warName}

	_success
	_return "Deploy ${appName}, ${warName} successfully."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh