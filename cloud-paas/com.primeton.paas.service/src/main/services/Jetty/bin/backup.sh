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
version=
backupUrl=

# Help, print help information to terminal.
function _dohelp() {
	echo "useage:"
	echo "./backup.sh"
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-appName) application name"
	echo "-storagePath) application storage path"
	echo "-version) application version"
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
_doexecute() {
	# check params
	if [ -z ${appName} ]; then
		echo "appName is null or blank."
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
	echo "[`date`] Begin checkout file from backup."
	rm -rf {WORKSPACE}/*
	echo "[`date`] Finish checkout file."
	
	# clear env
	rm -rf ${TEMP}/*
	
	# zip files
	if [ ! -d ${WEB_APPS} ]; then
		echo "${WEB_APPS} not found."
		_fail
		exit 0
	fi
	echo "[`date`] Begin zip ${WEB_APPS}/default."
	cd ${WEB_APPS}
	zip -r -q ${TEMP}/backup.zip default
	echo "[`date`] Finish zip ${WEB_APPS}/default."
	if [ -d ${JETTY}/files ]; then
		echo "[`date`] Begin zip ${JETTY}/files."
		cd ${JETTY}
		zip -r -q ${TEMP}/backup.zip files
		echo "[`date`] Finish zip ${JETTY}/files."
	fi
	
	# move to workspace
	isAdd=true
	if [ -f ${BACK_FILE} ]; then
		rm ${BACK_FILE}
		isAdd=false
	fi
	mv ${TEMP}/backup.zip ${WORKSPACE}
	
	# commit zip
	echo "[`date`] Begin commit file ${BACK_FILE}."
	
	#move zip to storage
	if [ isAdd ]; then
		# mount storage
		if [ "$backupUrl" != "" ]; then
			_return "[`date`] Begin move zip to storage['$backupUrl'-'${version}']."
			
			vPath=`echo $backupUrl | awk -F ',' '{print $1}'`
			mPath=`echo $backupUrl | awk -F ',' '{print $2}'`
			
			if [ ! -d ${mPath} ]; then
				mkdir -p ${mPath}
			fi
			
			sudo /sbin/service rpcbind start
			umount ${mPath}
			mount -t  nfs  ${vPath}  ${mPath}
			mkdir -p ${mPath}/${appName}
			umount ${mPath}
			
			mount -t  nfs  ${vPath}/${appName} ${mPath}
			mkdir -p ${mPath}/${version}
			mv ${BACK_FILE} ${mPath}/${version}
			umount ${mPath}
			
			_return "[`date`] End move zip to storage['$backupUrl'-'${version}']."
	    fi
	fi
	
	echo "[`date`] Finish commit file ${BACK_FILE}."
	_success
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh