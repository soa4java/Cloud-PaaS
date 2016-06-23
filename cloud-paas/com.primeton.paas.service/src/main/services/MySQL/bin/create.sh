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
user=paas
password=000000
schemaName=paas
characterSet=utf8

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./create.sh"
    echo "-h) print help"
    echo "-reqId) request id"
    echo "-user) mysql user"
    echo "-password) mysql password"
    echo "-schemaName) mysql schemaName"
    echo "-characterSet) mysql characterSet"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-user) user=$2;shift 2;;
		-password) password=$2;shift 2;;
		-schemaName) schemaName=$2;shift 2;;
		-characterSet) characterSet=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	echo "[`date`] Begin create mysql account."	
	
	sed -i 's/utf8/'${characterSet}'/g' ${mysql_program}/support-files/my-medium.cnf
	
	cp -f ${mysql_program}/support-files/my-medium.cnf /etc/my.cnf

	chown mysql:mysql /etc/my.cnf
	
	# start mysql
	service mysql start
	
	if [ -f ${mysqlPid} ]; then
		pid="`cat ${mysqlPid}`"
	fi
	
	# validate start
	if [ `ps -p "${pid}" | grep -v PID | wc -l` -eq 1 ]; then
			_pid ${pid}
			_return "MySQL server successfully started, pid is ${pid}."
	else
			_return "MySQL server  failure to start."
			_fail
			exit 1
	fi
	
	# create user and schema
	${mysql_program}/bin/mysql -uroot -e "grant all privileges on *.* to ${user}@'%' identified by '${password}'"
	_return "Create user ${user} success."
	
	${mysql_program}/bin/mysql -uroot -e "CREATE SCHEMA ${schemaName} DEFAULT CHARACTER SET ${characterSet}"
	_return "Create schema ${schemaName} success."
	
	# Stop mysql
	service mysql stop
	
	# validate stop
	if [ `ps -p "${pid}" | grep -v PID | wc -l` -eq 1 ]; then
		kill -9 ${pid}
	fi
	
	_return "Create user ${user}, schema ${schemaName} success."
	_success
	echo "[`date`] Finish install mysql service."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh