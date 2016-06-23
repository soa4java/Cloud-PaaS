#!/bin/bash

# author ZhongWen.Li(mailto:lizw@primeton.com)

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
# software packages
package_home=${BIN_HOME_PATH}/OpenAPI/packages/java
jetty_package=${package_home}/jetty.zip
war_package=${package_home}/default.war

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/OpenAPI
openapi_program=${program_home}/openapi

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./install.sh"
    echo "-h) Print help"
    echo "-reqId) Request id"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-arg0) arg0=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# print variables
	echo "package_home = ${package_home}"
	echo "jetty_package = ${jetty_package}"
	
	echo "program_home = ${program_home}"
	echo "openapi_program = ${openapi_program}"
	
	_return "[`date`] Begin install openapi."
	
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	
	_return "[`date`] Begin install openapi."
	
	# check package
	if [ ! -f ${jetty_package} ]; then
		_fail
		_return "[`date`] Can not install openapi, ${jetty_package} not found."
		exit 0
	fi
	if [ -d ${openapi_program} ]; then
		_return "[`date`] ${openapi_program} already exist, then remove it."
		rm -rf ${openapi_program}
	fi
	
	# mkdir openapi
	mkdir -p ${openapi_program}
	
	# unzip jetty.zip
	_return "[`date`] Begin unzip ${jetty_package}."
	unzip ${jetty_package} -d ${openapi_program}
	_return "[`date`] End unzip ${jetty_package}."
	
	# unzip default.war
	_return "[`date`] Begin unzip ${war_package}."
	mkdir -p ${openapi_program}/webapps/default
	unzip ${war_package} -d ${openapi_program}/webapps/default
	_return "[`date`] End unzip ${war_package}."

	# clear stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
			
	_return "[`date`] End install openapi."
	
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh