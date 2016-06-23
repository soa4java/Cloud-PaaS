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

package_home=${BIN_HOME_PATH}/Redis/packages
redis_name=redis
redis_version=3.0.2
redis_package=${package_home}/${INSTALL_SRC}/${redis_name}-${redis_version}.tar.gz
redis_build=${TEMP_HOME_PATH}/${redis_name}-${redis_version}.tar.gz

# Directory for unzip software packages
redis_src=${package_home}/temp/${redis_name}-${redis_version}

program_home=${PROGRAME_HOME_PATH}/Redis
redis_program=${program_home}/${redis_name}-${redis_version}

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
	# print variables
	echo "package_home = ${package_home}"
	echo "program_home = ${program_home}"
	
	# clean
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	mkdir -p ${program_home}
	
	if [ ! -d ${package_home}/temp ]; then
		mkdir -p ${package_home}/temp
	fi
	
	_return "[`date`] Begin install redis."
	
	# check package
	if [ ! -f ${redis_package} ]; then
		_fail
		_return "[`date`] Can not install redis, ${redis_package} not exists."
		exit 0
	fi
	# clean
	if [ -d ${redis_src} ]; then
		_return "[`date`] ${redis_src} already exist, then remove it."
		rm -rf ${redis_src}
	fi
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${redis_package}."
	cd ${package_home}/temp
	tar -zvxf ${redis_package}
	_return "[`date`] End tar -zvxf ${redis_package}."
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# make
		_return "[`date`] Begin make ${redis_src}."
		cd ${redis_src}
		make PREFIX=${redis_program}
		_return "[`date`] End make ${redis_src}."
	fi
	
	# build mode
	if ${BUILD_MODE} ; then
		_return "[`date`] Begin tar -czf ${redis_name}-${redis_version}."
		cd ${package_home}/temp
		tar -czf ${redis_build} ${redis_name}-${redis_version}
		_return "[`date`] End tar -czf ${redis_name}-${redis_version}."
	fi

	# make install
	_return "[`date`] Begin make install ${redis_src}."
	cd ${redis_src}
	make install PREFIX=${redis_program}
	_return "[`date`] End make install ${redis_src}."
	
	# clean
	_return "[`date`] Begin clean, remove ${redis_src}."
	rm -rf ${redis_src}
	_return "[`date`] End clean, remove ${redis_src}."
	
	# check installation
	if [ ! -d ${redis_program} ]; then
		_fail
		_return "[`date`] Install redis error."
		exit 0
	fi
	_return "[`date`] End install redis."
	
	_return "[`date`] chmod +x ${redis_program}/bin/*"
	chmod +x ${redis_program}/bin/*

	# clean stdout.txt
	echo "[`date`] OK." > ${FILE_PATH_STDOUT}	
	
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh