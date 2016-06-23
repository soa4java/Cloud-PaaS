#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

JETTY_HOME=${PROGRAME_HOME_PATH}/Console/console-platform

JETTY_PORT=7082
DEBUG_PORT=8787

CONSOLE_TYPE="platform"

dest=${JETTY_HOME}/webapps/default/WEB-INF/classes/config/app
dest_db=${JETTY_HOME}/webapps/default/WEB-INF/classes

if [ ! -d ${dest} ]; then
        mkdir -p ${dest}
fi

# Update configuration files
cp -f ${BIN_HOME_PATH}/Common/conf/zkConfig.xml ${dest}
cp -f ${BIN_HOME_PATH}/Console/conf/db.properties ${dest_db}

JAVA_OPTS="  -Xms256m -Xmx512m -Xmn128m -XX:MaxPermSize=256m -XX:-UseGCOverheadLimit -XX:+UseParallelGC -XX:ParallelGCThreads=4 -XX:+UseParallelOldGC -Djava.net.preferIPv4Stack=true -Djava.awt.headless=true -Djetty.home=${JETTY_HOME} -Djetty.port=${JETTY_PORT} -XX:+HeapDumpOnOutOfMemoryError -Duser.timezone=GMT+08 "
# language and region
JAVA_OPTS="${JAVA_OPTS} -Duser.language=zh -Duser.region=CN "
# console type
JAVA_OPTS="${JAVA_OPTS} -DCONSOLE_TYPE=${CONSOLE_TYPE}"

if [ "$1" = "-debug" ]; then
    export JAVA_OPTS="${JAVA_OPTS} -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=${DEBUG_PORT} "
fi

JETTY_OPTS="-jar ${JETTY_HOME}/start.jar jetty.port=${JETTY_PORT} STOP.PORT=3342 STOP.KEY=3332 "

# if it has been started then exit
if [ -f ${JETTY_HOME}/pid ]; then
	PID="`cat ${JETTY_HOME}/pid`"
fi
if [ ! -z ${PID} ]; then
	if [ `ps -p ${PID} | grep -v PID | wc -l` -gt 0 ]; then
		echo "###############################################################################"
		echo "[INFO] Primeton PAAS console-platform has been started. PID = ${PID}"
		echo "###############################################################################"
		exit 0
	fi
fi
keyword1="${JETTY_HOME}/start.jar"
keyword2="jetty.port=${JETTY_PORT}"
keyword3="STOP.PORT=3341"
keyword4="STOP.KEY=3331"
if [ `ps -ef | grep ${keyword1} | grep ${keyword2} | grep ${keyword3} | grep ${keyword4} | grep -v grep | wc -l` -gt 0 ]; then
	echo "####################################################################"
	echo "[INFO] Primeton PAAS console-platform has been started."
	echo "####################################################################"
	exit 0
fi

echo ${JAVA_HOME}/bin/java ${JAVA_OPTS} ${JETTY_OPTS}

nohup ${JAVA_HOME}/bin/java ${JAVA_OPTS} ${JETTY_OPTS} > ${JETTY_HOME}/nohup.out 2>&1 &

PID=$!
echo -n ${PID} > ${JETTY_HOME}/pid

echo "###########################################################################################"
echo "Primeton PAAS console-platform start success, pid ${PID}, listen *:${JETTY_PORT}."
echo "###########################################################################################"