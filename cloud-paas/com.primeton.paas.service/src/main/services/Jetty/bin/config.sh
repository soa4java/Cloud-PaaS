#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#
# Generate jetty-server configuration
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
JETTY_CONF_TEMPLATE=${BIN_HOME_PATH}/Jetty/packages/etc/cfg/jetty.xml
JETTY_SESSION_CONF_TEMPLATE=${BIN_HOME_PATH}/Jetty/packages/etc/cfg/memcached/jetty.xml
JETTY_WEB_TEMPLATE=${BIN_HOME_PATH}/Jetty/packages/etc/cfg/memcached/jetty-web.xml
JETTY_NOSQL_MEMCACHED_JAR=jetty-nosql-memcached-0.3.2-jar-with-dependencies.jar
JETTY_NOSQL_MEMCACHED_JAR_TEMPLATE=${BIN_HOME_PATH}/Jetty/packages/etc/jar/${JETTY_NOSQL_MEMCACHED_JAR}

storagePath=/storage/app
enableSession=false
minThreadSize=10
maxThreadSize=200

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./config.sh"
    echo "-h) Print help"
    echo "-reqId) Request id"
    echo "-storagePath) storagePath"
    echo "-appName) appName"
    echo "-enableSession) enable/disable distribute session (true/false)"
    echo "-minThreadSize) minThreadSize (10)"
    echo "-maxThreadSize) maxThreadSize (200)"
    echo "-memcachedServers) memcachedServers (ip:port,ip:port...)"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-storagePath) storagePath=$2;shift 2;;
		-appName) appName=$2;shift 2;;
		-enableSession) enableSession=$2;shift 2;;
		-minThreadSize) minThreadSize=$2;shift 2;;
		-maxThreadSize) maxThreadSize=$2;shift 2;;
		-memcachedServers) memcachedServers=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	if [ -z ${appName} ]; then
		_return "appName is null or blank."
		_fail
		exit 0
	fi
	# if you not input memcachedServers then disable session
	if [ -z ${memcachedServers} ]; then
		enableSession=false
		_return "memcachedServers is null or blank, then disable memcached session."
	fi
	
	JETTY_HOME=${storagePath}/${appName}/jetty-8.1.10
	echo "JETTY_HOME=${JETTY_HOME}"
	
	JETTY_CONF=${JETTY_HOME}/etc/jetty.xml
	echo "JETTY_CONF=${JETTY_CONF}"
	
	JETTY_WEB=${JETTY_HOME}/webapps/default/WEB-INF/jetty-web.xml
	echo "JETTY_WEB=${JETTY_WEB}"
	
	# etc/jetty.xml and WEB-INF/jetty-web.xml configuration
	if [ "${enableSession}" = "true" ]; then
		# Copy memcached template file to here
		cp ${JETTY_SESSION_CONF_TEMPLATE} ${JETTY_HOME}/etc
		cp ${JETTY_WEB_TEMPLATE} ${JETTY_HOME}/webapps/default/WEB-INF
		cp ${JETTY_NOSQL_MEMCACHED_JAR_TEMPLATE} ${JETTY_HOME}/lib/ext
		# sed keyword.memcachedServers in jetty.xml
		sed -i -e "s/keyword.memcachedServers/${memcachedServers}/g" ${JETTY_CONF}
	else
		jarFile=${JETTY_HOME}/lib/ext/${JETTY_NOSQL_MEMCACHED_JAR}
		if [ -f ${jarFile} ]; then
			rm ${jarFile}
		fi
		if [ -f ${JETTY_WEB} ]; then
			rm ${JETTY_WEB}
		fi
		# Copy original template file to here
		cp ${JETTY_CONF_TEMPLATE} ${JETTY_HOME}/etc
	fi
	
	# Setting jetty threadpool size
	sed -i -e "s/keyword.minThreadSize/${minThreadSize}/g" ${JETTY_CONF}
	sed -i -e "s/keyword.maxThreadSize/${maxThreadSize}/g" ${JETTY_CONF}
	
	_return "ok"
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh