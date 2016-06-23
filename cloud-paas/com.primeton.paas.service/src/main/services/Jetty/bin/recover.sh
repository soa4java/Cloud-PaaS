#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# @Deprecated
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# Global variables, The variable $SCRIPT_HOME_PATH must be defined
PATH=${PATH}:${PROGRAME_HOME_PATH}/Jetty/svnkit-1.7.4-v1/bin 

# my variables
appName=
storagePath=/storage/app
version=1
backupUrl=

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./backup.sh"
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-appName) application name"
	echo "-storagePath) application storage path"
	echo "-version) war version"
	echo "-backupUrl) application backupUrl. e.g: 192.168.100.252:/nfs/2013102316103880,/tmp/test"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-appName) appName=$2;shift 2;;
		-storagePath) storagePath=$2;shift 2;;
		-version) version=$2;shift 2;;
		-backupUrl) backupUrl=$2;shift 2;;
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
	
	# prepare variables
	JETTY=${storagePath}/${appName}/jetty-8.1.10
	WEB_APPS=${JETTY}/webapps
	WORKSPACE=${storagePath}/${appName}/workspace
	TEMP=${storagePath}/${appName}/temp
	BACK_FILE=${WORKSPACE}/backup.zip
	
	if [ ! -d ${WORKSPACE} ]; then
		mkdir -p ${WORKSPACE}
	fi
	if [ ! -d ${TEMP} ]; then
		mkdir -p ${TEMP}
	fi
	
	# checkout from svn
	_return "[`date`] Begin checkout from storage."

	rm -rf ${WORKSPACE}/*
	
	# mount storage
	if [ "$backupUrl" != "" ]; then
		_return "[`date`] Begin move zip."
		
		vPath=`echo $backupUrl | awk -F ',' '{print $1}'`
		mPath=`echo $backupUrl | awk -F ',' '{print $2}'`
		
		if [ ! -d ${mPath} ]; then
			mkdir -p ${mPath}
		fi
		
		sudo /sbin/service rpcbind start
		umount ${mPath}
		mount -t  nfs  ${vPath}/${appName}  ${mPath}
		cp ${mPath}/${version}/backup.zip ${WORKSPACE}/
		_return "[`date`] cp ${mPath}/${version}/backup.zip ${WORKSPACE}/."
		umount ${mPath}
		
		_return "[`date`] End move zip."
    fi
	
	_return "[`date`] Finish checkout."
	
	# clear env
	rm -rf ${TEMP}/*
	
	# unzip backup.zip
	if [ ! -f ${BACK_FILE} ]; then
		_return "${BACK_FILE} not found."
		_fail
		exit 0
	fi
	
	_return "[`date`] Begin unzip file ${BACK_FILE} to temp directory ${TEMP}."
	unzip -o ${BACK_FILE} -d ${TEMP}
	_return "[`date`] Finish unzip file ${BACK_FILE}."
	
	cd ${TEMP}
	if [ -d ${TEMP}/default ]; then
		_return "[`date`] Remove ${WEB_APPS}/default ."
		rm -rf ${WEB_APPS}/default
		_return "[`date`] Move ${TEMP}/default to ${WEB_APPS} ."
		mv default ${WEB_APPS}
	else
		_return "Error backup file ${BACK_FILE} ."
		_fail
		exit 0
	fi
	if [ -d ${TEMP}/files ]; then
		_return "[`date`] Remove ${JETTY}/files ."
		rm -rf ${JETTY}/files
		_return "[`date`] Move ${TEMP}/files to ${JETTY} ."
		mv files ${JETTY}
	fi
	
	_success
	_return "Recover version {${version}} success."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh