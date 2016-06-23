#!/bin/bash

# author liuyi(mailto:liu-yi@primeton.com)

#
# Install sms service
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
# software packages
package_home=${BIN_HOME_PATH}/SMS/packages/java
sms_package=${package_home}/sms.zip

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/SMS

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
	echo "sms_package = ${sms_package}"
	
	echo "program_home = ${program_home}"
	
	_return "[`date`] Begin install sms service."
	
	# check if installed
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	
	# mkdir ${program_home}
	mkdir -p ${program_home}
	
	#
	# install sms
	#
	_return "[`date`] Begin install sms."
	
	# check package
	if [ ! -f ${sms_package} ]; then
		_fail
		_return "[`date`] Can not install sms, ${sms_package} not found."
		exit 0
	fi
	if [ -d ${sms_program} ]; then
		_return "[`date`] ${sms_program} already exist, then remove it."
		rm -rf ${sms_program}
	fi
	cd ${package_home}
	
	# unzip
	_return "[`date`] Begin unzip ${sms_package}."
	unzip ${sms_package} -d ${program_home}
	_return "[`date`] End unzip ${sms_package}."

	# clear stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
			
	_return "[`date`] End install sms."
	
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh