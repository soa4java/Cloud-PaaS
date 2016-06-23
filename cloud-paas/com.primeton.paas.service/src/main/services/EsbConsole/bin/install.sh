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

package_home=${BIN_HOME_PATH}/EsbConsole/packages/java
tomcat_package=${package_home}/apache-tomcat-6.0.44.tar.gz
governor_war=${package_home}/governor.war

program_home=${PROGRAME_HOME_PATH}/EsbConsole

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./install.sh -reqId uuid"   
	echo "-h) print help"
	echo "-reqId) request id"
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
	# clean
	if [ -d ${program_home} ]; then
		_return "[`date`] [WARN] Delete directory ${program_home}."
		rm -rf ${program_home}
	fi
	mkdir -p ${program_home}
		
	# unpack tomcat
	_return "[`date`] [INFO] unpack ${tomcat_package} to ${program_home}."
	# cd ${program_home}
	tar -zvxf ${tomcat_package} -C ${program_home}
	
	if [ ! -d ${program_home}/apache-tomcat-6.0.44/webapps ]; then
		_return "[`date`] [ERROR] unpack ${tomcat_package} to ${program_home} error."
		_fail
		exit 0
	fi
	
	# chmod
	chmod +x ${program_home}/apache-tomcat-6.0.44/bin/*.sh
	
	# deploy
	mkdir -p ${program_home}/apache-tomcat-6.0.44/webapps/governor
	
	unzip ${governor_war} -d ${program_home}/apache-tomcat-6.0.44/webapps/governor
	
	# copy jdbc driver (Oracle | MySQL | etc.) to Tomcat server lib
	if [ -d ${package_home}/driver ]; then
		cp -f ${package_home}/driver/*.jar ${program_home}/apache-tomcat-6.0.44/lib
	fi
	
	# clean stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
	
	_success
	_return "[`date`] [INFO] Install EsbConsole service success."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh