#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

# console log
# consoleType : console-app | console-platform
# logType : console | manage
# useage ./zip-consoleLog.sh consoleType logType

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

ERRLOG_HOME=${BIN_HOME_PATH}/Console/bin

consoleLogsHomeDir=$1
consoleType=$2
logType=$3


if [ -z ${consoleLogsHomeDir} ]; then
    echo "[`date`][ERROR] Zip log error : consoleLogsHomeDir parameter not specific." >> ${ERRLOG_HOME}/downloadError.txt
fi
if [ -z ${consoleType} ]; then
    echo "[`date`][ERROR] Zip log error : consoleType parameter not specific." >> ${ERRLOG_HOME}/downloadError.txt
fi
if [ -z ${logType} ]; then
    echo "[`date`][ERROR] Zip log error : logType parameter not specific." >> ${ERRLOG_HOME}/downloadError.txt
fi

#LOGS_HOME=${PROGRAME_HOME_PATH}/Console/${consoleType}/logs/
LOGS_HOME=$consoleLogsHomeDir

echo $consoleType
echo $logType
echo $LOGS_HOME

cd ${LOGS_HOME}/

# clear tmp file
rm -rf tmp

# zip file
mkdir tmp

if [ "$consoleType" = "console-app" ]; then
    if [ ! -f ${logType}.log ]; then
    	echo "[`date`][ERROR] Zip log error : params error." >> ${ERRLOG_HOME}/downloadError.txt
		exit 1;
	else
		zip tmp/${consoleType}_${logType}.zip ${logType}.log ${logType}.log.*
	fi		

else
    if [ "$consoleType" = "console-platform" ]; then
    	if [ ! -f ${logType}.log ]; then
    		echo "[`date`][ERROR] Zip log error : params error1." >> ${ERRLOG_HOME}/downloadError.txt
			exit 1;
		else
			zip tmp/${consoleType}_${logType}.zip ${logType}.log ${logType}.log.*
		fi		
	else
		echo "[`date`][ERROR] Zip log error : params error2." >> ${ERRLOG_HOME}/downloadError.txt
		exit 1;
	fi	
fi 
echo "[`date`] Finish"
exit 0
