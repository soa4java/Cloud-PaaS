#!/bin/bash

# author liuyi(mailto:liu-yi@primeton.com)

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
# software packages
package_home=${BIN_HOME_PATH}/Mail/packages/java
mail_package=${package_home}/mail.zip

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/Mail

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
	echo "mail_package = ${mail_package}"
	
	echo "program_home = ${program_home}"
	
	_return "[`date`] Begin install mail service."
	
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	
	# mkdir ${program_home}
	mkdir -p ${program_home}
	
	#
	# install mail
	#
	_return "[`date`] Begin install mail."
	
	# check package
	if [ ! -f ${mail_package} ]; then
		_fail
		_return "[`date`] Can not install mail, ${mail_package} not found."
		exit 0
	fi
	if [ -d ${mail_program} ]; then
		_return "[`date`] ${mail_program} already exist, then remove it."
		rm -rf ${mail_program}
	fi
	cd ${package_home}
	
	# unzip
	_return "[`date`] Begin unzip ${mail_package}."
	unzip ${mail_package} -d ${program_home}
	_return "[`date`] End unzip ${mail_package}."
	
	_return "[`date`] End install mail."

	# clear stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
			
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh