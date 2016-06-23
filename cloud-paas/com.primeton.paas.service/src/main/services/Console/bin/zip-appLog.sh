#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

# application log
# appName : application name
# logType : userall | user | system
# useage : ./zip-appLog appName logType LOGS_HOME

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

ERRLOG_HOME=${BIN_HOME_PATH}/Console/bin

appName=$1
logType=$2
LOGS_HOME=$3

echo $appName
echo $logType
echo $LOGS_HOME

if [ -z ${appName} ]; then
    echo "[`date`][ERROR] Zip log error : appName parameter not specific." >> ${ERRLOG_HOME}/downloadError.txt
fi
if [ -z ${logType} ]; then
    echo "[`date`][ERROR] Zip log error : logType parameter not specific." >> ${ERRLOG_HOME}/downloadError.txt
fi

if [ ! -d ${LOGS_HOME}/${appName}/ ]; then
	cd ${LOGS_HOME}
	echo "not found." > ${LOGS_HOME}/error.txt
	exit 0	
fi

cd ${LOGS_HOME}/${appName}/

# clear tmp file
rm -rf tmp

# zip file
mkdir tmp

if [ "$logType" = "userall" ]; then
	# all user log
	zip tmp/${appName}_${logType}.zip *.log *.log.* -x *.system.log *.system.log.*
	exit 0
fi

if [ "$logType" = "user" ]; then
	# zip user.log
	if [ ! -f *.user.log ]; then
		echo "not found." > ${LOGS_HOME}/error.txt
		exit 0
	fi
	zip  tmp/${appName}_${logType}.zip *.user.log  *.user.log.*
else
	if [ "$logType" = "system" ]; then
	 	# zip system.log
		if [ ! -f *.system.log ]; then
			echo "not found." > ${LOGS_HOME}/error.txt
			exit 0
		fi
		zip  tmp/${appName}_${logType}.zip *.system.log  *.system.log.*
	else
		# other type log	
		if [ ! -f *.${logType}.log ]; then
			echo "not found.">${LOGS_HOME}/error.txt
			exit 0
		fi
		zip  tmp/${appName}_${logType}.zip *.${logType}.log  *.${logType}.log.*
	fi
fi

if [ ! -f ${LOGS_HOME}/${appName}/tmp/${appName}_${logType}.zip ]; then
		echo "not found." > ${LOGS_HOME}/error.txt
		exit 0
fi
echo "[`date`] Finish."

exit 0
