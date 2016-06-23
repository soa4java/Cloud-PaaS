#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#
# Install HaProxy service
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
# software packages
package_home=${BIN_HOME_PATH}/HaProxy/packages
haproxy_name=haproxy
haproxy_version=1.4.24
haproxy_package=${package_home}/${INSTALL_SRC}/${haproxy_name}-${haproxy_version}.tar.gz
haproxy_build=${TEMP_HOME_PATH}/${haproxy_name}-${haproxy_version}.tar.gz

# Directory for unzip software packages
haproxy_src=${package_home}/temp/${haproxy_name}-${haproxy_version}

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/HaProxy
haproxy_program=${program_home}/${haproxy_name}-${haproxy_version}

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
	echo "program_home = ${program_home}"
	
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	# mkdir ${program_home}
	mkdir -p ${program_home}
	
	if [ ! -d ${package_home}/temp ]; then
		mkdir -p ${package_home}/temp
	fi
	
	#
	# install HaProxy
	#
	_return "[`date`] Begin install haproxy."
	
	# check package
	if [ ! -f ${haproxy_package} ]; then
		_fail
		_return "[`date`] Can not install HaProxy, ${haproxy_package} not found."
		exit 0
	fi
	if [ -d ${haproxy_src} ]; then
		_return "[`date`] ${haproxy_src} already exist, then remove it."
		rm -rf ${haproxy_src}
	fi
	cd ${package_home}
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${haproxy_package}."
	cd ${package_home}/temp
	tar -zvxf ${haproxy_package}
	_return "[`date`] End tar -zvxf ${haproxy_package}."
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# make
		_return "[`date`] Begin make ${haproxy_src}."
		cd ${haproxy_src}
		make TARGET=linux26 PREFIX=${haproxy_program}
		_return "[`date`] End make ${haproxy_src}."
	fi
	
	# build mode
	if ${BUILD_MODE} ; then
		_return "[`date`] Begin tar -czf ${haproxy_name}-${haproxy_version}."
		cd ${package_home}/temp
		tar -czf ${haproxy_build} ${haproxy_name}-${haproxy_version}
		_return "[`date`] End tar -czf ${haproxy_name}-${haproxy_version}."
	fi

	# make install
	_return "[`date`] Begin make install ${haproxy_src}."
	cd ${haproxy_src}
	make install PREFIX=${haproxy_program}
	_return "[`date`] End make install ${haproxy_src}."
	
	# clear
	_return "[`date`] Begin clear, remove ${haproxy_src}."
	rm -rf ${haproxy_src}
	_return "[`date`] End clear, remove ${haproxy_src}."
	
	# check installation
	if [ ! -d ${haproxy_program} ]; then
		_fail
		_return "[`date`] HaProxy install error."
		exit 0
	fi
	_return "[`date`] End install HaProxy."

	# clear stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}	
			
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh