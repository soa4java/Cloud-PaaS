#!/bin/bash

# author liuyi(mailto:liu-yi@primeton.com)

#
# Install jetty
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables

# software packages
# /opt/upaas/bin/Memcached/packages/source[binary]
package_home=${BIN_HOME_PATH}/Jetty/packages/java
jetty_package=${package_home}/jetty-8.1.10.zip
svn_package=${package_home}/svnkit_1.7.4.zip

# Directory for unzip software packages

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/Jetty
jetty_program=${program_home}/jetty-8.1.10
svn_program=${program_home}/svnkit-1.7.4-v1

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
	echo "svn_package = ${svn_package}"
	
	echo "program_home = ${program_home}"
	echo "jetty_program = ${jetty_program}"
	echo "svn_program = ${svn_program}"
	
	
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	# mkdir ${program_home}
	mkdir -p ${program_home}
	
	cd ${program_home} 
	
	_return "[`date`] Begin unzip ${jetty_package}."
	unzip ${jetty_package}
	if [ ! -d ${jetty_program} ]; then
		_return "[`date`]${jetty_program} not found, unzip ${jetty_package} error, then exit 1."
		_fail
		exit 1 
	fi
	
	_return "[`date`] Begin unzip ${svn_package}."
	unzip ${svn_package}
	if [ ! -d ${svn_program} ]; then
		_return "[`date`]${svn_program} not found, unzip ${svn_package} error, then exit 1."
		_fail
		exit 1 
	fi
	
	_return "[`date`] chmod +x -R ${svn_program}/bin"
	chmod +x -R ${svn_program}/bin
	#_return "[`date`] mv svnkit-1.7.4-v1 svnkit"
	#mv svnkit-1.7.4-v1 svnkit
	
	_return "[`date`] End install jetty."

	# clear stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
				
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh