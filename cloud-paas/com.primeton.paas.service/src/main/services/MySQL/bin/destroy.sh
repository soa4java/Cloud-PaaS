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

# my variables
mysql_program=${PROGRAME_HOME_PATH}/MySQL/mysql-5.5.25
mysqlPid=${mysql_program}/data/${HOSTNAME}.pid
instanceId=undefined
schemaName=paas
user=paas

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage: ./stop.sh"
	echo "-h) print help"	
	echo "-reqId) request id"
	echo "-id) instance id"
	echo "-schemaName) mysql schema name, default paas" 
	echo "-user) mysql schema name, default paas" 
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-id) instanceId=$2;shift 2;;
		-user) user=$2;shift 2;;
		-schemaName) schemaName=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# start mysql
	service mysql start
	
	if [ -f ${mysqlPid} ]; then
		pid="`cat ${mysqlPid}`"
	fi
	if [ `ps -p ${pid} | grep -v PID | wc -l` -ge 1 ]; then
		${mysql_program}/bin/mysql -uroot -e "DROP SCHEMA ${schemaName}"
		${mysql_program}/bin/mysql -uroot -e "USE mysql; DELETE FROM USER WHERE HOST='%' and USER='${user}'"
		
		service mysql stop
		
		i=0
		while test $i -ne 10 ; do
			if [ `ps -p ${pid} |grep -v PID | wc -l` -eq 0 ]; then
				_return "MySQL server success stopped !!."
				break;
			fi
			echo -n "-"
			i=`expr $i + 1`
			sleep 1
		done
		if test $i -ge 10 ; then
			kill -9 ${pid}
			_return "MySQL server force stopped!"
		fi
	fi
	
	instancePath=${BIN_HOME_PATH}/MySQL/instances/${instanceId}
	if [ -d ${instancePath} ]; then
		rm -rf ${instancePath}
	fi
	
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh