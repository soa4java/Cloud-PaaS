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
# software packages
package_home=${BIN_HOME_PATH}/Keepalived/packages

zlib_devel_rpm=${package_home}/${INSTALL_SRC}/zlib-devel-1.2.3-141.1.x86_64.rpm
openssl_rpm=${package_home}/${INSTALL_SRC}/libopenssl-devel-0.9.8j-0.26.1.x86_64.rpm

zlib_name=zlib
zlib_version=1.2.8
zlib_package=${package_home}/${INSTALL_SRC}/${zlib_name}-${zlib_version}.tar.gz
zlib_build=${TEMP_HOME_PATH}/${zlib_name}-${zlib_version}.tar.gz

popt_name=popt
popt_version=1.16
popt_package=${package_home}/${INSTALL_SRC}/${popt_name}-${popt_version}.tar.gz
popt_build=${TEMP_HOME_PATH}/${popt_name}-${popt_version}.tar.gz

keepalived_name=keepalived
keepalived_version=1.2.8
keepalived_package=${package_home}/${INSTALL_SRC}/${keepalived_name}-${keepalived_version}.tar.gz
keepalived_build=${TEMP_HOME_PATH}/${keepalived_name}-${keepalived_version}.tar.gz

# Directory for unzip software packages
zlib_src=${package_home}/temp/${zlib_name}-${zlib_version}
popt_src=${package_home}/temp/${popt_name}-${popt_version}
keepalived_src=${package_home}/temp/${keepalived_name}-${keepalived_version}

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/Keepalived
keepalived_program=${program_home}/${keepalived_name}-${keepalived_version}

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
	echo "zlib_devel_rpm = ${zlib_devel_rpm}"
	echo "openssl_rpm = ${openssl_rpm}"
	echo "zlib_package = ${zlib_package}"
	echo "popt_package = ${popt_package}"
	echo "keepalived_package = ${keepalived_package}"
	echo "zlib_src = ${zlib_src}"
	echo "popt_src = ${popt_src}"
	echo "keepalived_src = ${keepalived_src}"
	echo "program_home = ${program_home}"
	
	_return "[`date`] Begin install keepalived service."
	
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
	# install zlib-devel-1.2.3-141.1.x86_64.rpm
	#
	_return "[`date`] Begin install zlib-devel-1.2.3-141.1.x86_64.rpm."
	
	# check package
	if [ ! -f ${zlib_devel_rpm} ]; then
		_fail
		_return "[`date`] Can not install zlib-devel, ${zlib_devel_rpm} not found."
		exit 0
	fi
	
	rpm -ivh ${zlib_devel_rpm}
	_return "[`date`] End install ${zlib_devel_rpm}."
	
	
	#
	# install libopenssl-devel-0.9.8j-0.26.1.x86_64.rpm
	#
	_return "[`date`] Begin install ${openssl_rpm}."
	
	# check package
	if [ ! -f ${openssl_rpm} ]; then
		_fail
		_return "[`date`] Can not install zlib-devel, ${openssl_rpm} not found."
		exit 0
	fi
	
	rpm -ivh ${openssl_rpm}
	_return "[`date`] End install ${openssl_rpm}."
	
	
	#
	# install zlib
	#
	_return "[`date`] Begin install ${zlib_package}."
	
	if [ ! -f ${zlib_package} ]; then
		_return "[`date`] ${zlib_package} not found, then exit 1."
		_fail
		exit 1
	fi
	
	# tar -zvxf 
	_return "[`date`] Begin tar -zvxf ${zlib_package}."
	if [ -d ${zlib_src} ]; then
		_return "[`date`] ${zlib_src} is already exist, then remove it."
		rm -rf ${zlib_src}
	fi
	cd ${package_home}/temp
	tar -zvxf ${zlib_package}
	if [ ! -d ${zlib_src} ]; then
		_return "${zlib_src} not found, tar -zvxf ${zlib_package} error, then exit 1."
		_fail
		exit 1 
	fi
	_return "[`date`] End tar -zvxf ${zlib_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin zlib configure"
		cd ${zlib_src}
		./configure
		_return "[`date`] End zlib configure"
		# make
		_return "[`date`] Begin make ${zlib_src}."
		make
		_return "[`date`] End make ${zlib_src}."
	fi
	
	# build
	if ${BUILD_MODE} ; then
		_return "[`date`] Begin tar -czf ${zlib_build} ${zlib_name}-${zlib_version}."
		cd ${package_home}/temp
		tar -czf ${zlib_build} ${zlib_name}-${zlib_version}
		_return "[`date`] End tar -czf ${zlib_build} ${zlib_name}-${zlib_version}."
	fi
	
	# make install
	_return "[`date`] Begin make install ${zlib_src}."
	cd ${zlib_src}
	make install
	_return "[`date`] End make install ${zlib_src}."
	
	_return "[`date`] End install ${zlib_package}."

		
	#
	# install popt
	#
	_return "[`date`] Begin install ${popt_package}."
	
	if [ ! -f ${popt_package} ]; then
		_return "[`date`] ${popt_package} not found, then exit 1."
		_fail
		exit 1
	fi

	# tar -zvxf 
	_return "[`date`] Begin tar -zvxf ${popt_package}."
	if [ -d ${popt_src} ]; then
		_return "[`date`] ${popt_src} is already exist, then remove it."
		rm -rf ${popt_src}
	fi
	cd ${package_home}/temp
	tar -zvxf ${popt_package}
	if [ ! -d ${popt_src} ]; then
		_return "${popt_src} not found, tar -zvxf ${popt_package} error, then exit 1."
		_fail
		exit 1 
	fi
	
	_return "[`date`] End tar -zvxf ${popt_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin popt configure"
		cd ${popt_src}
		./configure
		_return "[`date`] End popt configure"
		# make
		_return "[`date`] Begin make ${popt_src}."
		make
		_return "[`date`] End make ${popt_src}."
	fi
	
	# build
	if ${BUILD_MODE} ; then
		_return "[`date`] Begin tar -czf ${popt_build} ${popt_name}-${popt_version}."
		cd ${package_home}/temp
		tar -czf ${popt_build} ${popt_name}-${popt_version}
		_return "[`date`] End tar -czf ${popt_build} ${popt_name}-${popt_version}."
	fi
	
	# make install
	_return "[`date`] Begin make install ${popt_src}."
	cd ${popt_src}
	make install
	_return "[`date`] End make install ${popt_src}."
	
	_return "[`date`] End install ${popt_package}."
	
	
	#
	# install keepalived
	#
	_return "[`date`] Begin install ${keepalived_package}."
	
	if [ ! -f ${keepalived_package} ]; then
		_return "[`date`] ${keepalived_package} not found, then exit 1."
		_fail
		exit 1
	fi
	# tar -zvxf 
	_return "[`date`] Begin tar -zvxf ${keepalived_package}."
	if [ -d ${keepalived_src} ]; then
		_return "[`date`] ${keepalived_src} is already exist, then remove it."
		rm -rf ${keepalived_src}
	fi
	cd ${package_home}/temp
	tar -zvxf ${keepalived_package}
	if [ ! -d ${keepalived_src} ]; then
		_return "${keepalived_src} not found, tar -zvxf ${keepalived_package} error, then exit 1."
		_fail
		exit 1 
	fi
	_return "[`date`] End tar -zvxf ${keepalived_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin keepalived configure"
		cd ${keepalived_src}
		./configure --prefix=${keepalived_program}
		_return "[`date`] End keepalived configure"
		# make
		_return "[`date`] Begin make ${keepalived_src}."
		make
		_return "[`date`] End make ${keepalived_src}."
	fi
	
	# build
	if ${BUILD_MODE} ; then
		_return "[`date`] Begin tar -czf ${keepalived_build} ${keepalived_name}-${keepalived_version}."
		cd ${package_home}/temp
		tar -czf ${keepalived_build} ${keepalived_name}-${keepalived_version}
		_return "[`date`] End tar -czf ${keepalived_build} ${keepalived_name}-${keepalived_version}."
	fi

	# make install
	_return "[`date`] Begin make install ${keepalived_src}."
	cd ${keepalived_src}
	make install
	_return "[`date`] End make install ${keepalived_src}."
	
	_return "[`date`] End install ${keepalived_package}."

	# clear stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
				
	_return "[`date`] End install keepalived service."
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh