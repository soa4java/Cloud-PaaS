#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#
# Install memcached service
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables

# software packages
# /opt/upaas/bin/Memcached/packages/source[binary]
package_home=${BIN_HOME_PATH}/Memcached/packages

libevent_name=libevent
libevent_version=2.0.20-stable
libevent_package=${package_home}/${INSTALL_SRC}/${libevent_name}-${libevent_version}.tar.gz
libevent_build=${TEMP_HOME_PATH}/${libevent_name}-${libevent_version}.tar.gz

memcached_name=memcached
memcached_version=1.4.15
memcached_package=${package_home}/${INSTALL_SRC}/${memcached_name}-${memcached_version}.tar.gz
memcached_build=${TEMP_HOME_PATH}/${memcached_name}-${memcached_version}.tar.gz

# Directory for unzip software packages
libevent_src=${package_home}/temp/${libevent_name}-${libevent_version}
memcached_src=${package_home}/temp/${memcached_name}-${memcached_version}

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/Memcached
libevent_program=${program_home}/${libevent_name}-${libevent_version}
memcached_program=${program_home}/${memcached_name}-${memcached_version}

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
	echo "libevent_package = ${libevent_package}"
	echo "memcached_package = ${memcached_package}"
	echo "libevent_src = ${libevent_src}"
	echo "memcached_src = ${memcached_src}"
	echo "program_home = ${program_home}"
	echo "libevent_program = ${libevent_program}"
	echo "memcached_program = ${memcached_program}"
	
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	# mkdir ${program_home}
	mkdir -p ${program_home}
	
	if [ ! -d ${package_home}/temp ] ; then
		mkdir ${package_home}/temp
	fi
	
	#
	# install libevent
	#
	_return "[`date`] Begin install libevent."
	
	# check package
	if [ ! -f ${libevent_package} ]; then
		_fail
		_return "[`date`] Can not install libevent, ${libevent_package} not found."
		exit 0
	fi
	if [ -d ${libevent_src} ]; then
		_return "[`date`] ${libevent_src} already exist, then remove it."
		rm -rf ${libevent_src}
	fi
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${libevent_package}."
	cd ${package_home}/temp
	tar -zvxf ${libevent_package}
	_return "[`date`] End tar -zvxf ${libevent_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		cd ${libevent_src}
		_return "[`date`] Begin configure ${libevent_src}, prefix=${libevent_program}."
		./configure --prefix=${libevent_program}
		_return "[`date`] End configure ${libevent_src}, prefix=${libevent_program}."
	
		# make
		_return "[`date`] Begin make ${libevent_src}."
		make
		_return "[`date`] End make ${libevent_src}."
	fi
	
	# build mode
	if ${BUILD_MODE} ; then
		_return "[`date`] Begin tar -czf ${libevent_build} ${libevent_name}-${libevent_version}."
		cd ${package_home}/temp
		tar -czf ${libevent_build} ${libevent_name}-${libevent_version}
		_return "[`date`] End tar -czf ${libevent_build} ${libevent_name}-${libevent_version}."
	fi

	# make install
	_return "[`date`] Begin make install ${libevent_src}."
	cd ${libevent_src}
	make install
	_return "[`date`] End make install ${libevent_src}."
	
	# clear
	_return "[`date`] Begin clear, remove ${libevent_src}."
	rm -rf ${libevent_src}
	_return "[`date`] End clear, remove ${libevent_src}."
	
	# check installation
	if [ ! -d ${libevent_program} ]; then
		_fail
		_return "[`date`] libevent install error."
		exit 0
	fi
	_return "[`date`] End install libevent."
	
	
	#
	# install memcached
	#
	_return "[`date`] Begin install memcached."
	
	# check package
	if [ ! -f ${memcached_package} ]; then
		_fail
		_return "[`date`] Can not install memcached, ${memcached_package} not found."
		exit 0
	fi
	if [ -d ${memcached_src} ]; then
		_return "[`date`] ${memcached_src} already exist, then remove it."
		rm -rf ${memcached_src}
	fi
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${memcached_package}."
	cd ${package_home}/temp
	tar -zvxf ${memcached_package}
	_return "[`date`] End tar -zvxf ${memcached_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		cd ${memcached_src}
		_return "[`date`] Begin ./configure --with-libevent=${libevent_program} --prefix=${memcached_program}."
		./configure --with-libevent=${libevent_program} --prefix=${memcached_program}
		_return "[`date`] End ./configure --with-libevent=${libevent_program} --prefix=${memcached_program}."
		
		# make
		_return "[`date`] Begin make ${memcached_src}."
		make
		_return "[`date`] End make ${memcached_src}."
	fi
	
	# build mode
	if ${BUILD_MODE} ; then
		_return "[`date`] Begin tar -czf ${memcached_build} ${memcached_name}-${memcached_version}."
		cd ${package_home}/temp
		tar -czf ${memcached_build} ${memcached_name}-${memcached_version}
		_return "[`date`] End tar -czf ${memcached_build} ${memcached_name}-${memcached_version}."
	fi
	
	# make install
	_return "[`date`] Begin make install ${memcached_src}."
	cd ${memcached_src}
	make install
	_return "[`date`] End make install ${memcached_src}."
	
	# clear
	_return "[`date`] Begin clear ${memcached_src}, remove it."
	rm -rf ${memcached_src}
	_return "[`date`] End clear ${memcached_src}, remove it."

	# check installation
	if [ ! -d ${memcached_program} ]; then
		_fail
		_return "[`date`] Install memcached error."
		exit 0
	fi
	
	_return "[`date`] End install memcached."

	# clear stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
			
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh