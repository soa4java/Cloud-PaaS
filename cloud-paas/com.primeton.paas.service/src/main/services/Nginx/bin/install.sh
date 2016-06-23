#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#
# Install nginx service
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
# software packages
package_home=${BIN_HOME_PATH}/Nginx/packages/${INSTALL_SRC}
openssl_name=openssl
openssl_version=0.9.8y
openssl_package=${package_home}/${openssl_name}-${openssl_version}.tar.gz

nginx_name=nginx
nginx_version=1.4.4
nginx_package=${package_home}/${nginx_name}-${nginx_version}.tar.gz

pcre_name=pcre
pcre_version=8.30
pcre_package=${package_home}/${pcre_name}-${pcre_version}.tar.gz
zlib_devel_rpm=${package_home}/zlib-devel-1.2.3-141.1.x86_64.rpm

# Directory for unzip software packages
src_temp=${BIN_HOME_PATH}/Nginx/packages/temp
openssl_src=${src_temp}/${openssl_name}-${openssl_version}
nginx_src=${src_temp}/${nginx_name}-${nginx_version}
pcre_src=${src_temp}/${pcre_name}-${pcre_version}

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/Nginx
openssl_program=${program_home}/${openssl_name}-${openssl_version}
nginx_program=${program_home}/${nginx_name}-${nginx_version}
pcre_program=${program_home}/${pcre_name}-${pcre_version}

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
	
	_return "[`date`] Begin install nginx service."
	
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	# mkdir ${program_home}
	mkdir -p ${program_home}	
	
	#
	# install zlib-devel-1.2.3-141.1.x86_64.rpm
	#
	_return "[`date`] Begin install ${zlib_devel_rpm}."
	if [ ! -f ${zlib_devel_rpm} ]; then
		_return "${zlib_devel_rpm} not found, then exit 1."
		_fail
		exit 1
	fi
	rpm -ivh ${zlib_devel_rpm}
	_return "[`date`] End install ${zlib_devel_rpm}."
	
	#
	# install pcre-8.30.tar.gz
	#
	_return "[`date`] Begin install ${pcre_package}."
	
	# check environment
	if [ ! -f ${pcre_package} ]; then
		_return "[`date`] ${pcre_package} not found, then exit 1."
		_fail
		exit 1
	fi
	if [ -d ${pcre_src} ]; then
		rm -rf ${pcre_src}
	fi
	if [ -d ${pcre_program} ]; then
		_return "[`date`] [WARN] ${pcre_program} already installed, then remove it."
		rm -rf ${pcre_program}
	fi
	
	# tar -zvxf 
	#cd ${package_home}
	_return "[`date`] Begin tar -zvxf ${pcre_package}."
	cd ${src_temp}
	tar -zvxf ${pcre_package}
	if [ ! -d ${pcre_src} ]; then
		_return "${pcre_src} not found, tar -zvxf ${pcre_package} error, then exit 1."
		_fail
		exit 1 
	fi
	_return "[`date`] End tar -zvxf ${pcre_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure --prefix=${pcre_program}"
		cd ${pcre_src}
		./configure --prefix=${pcre_program}
		_return "[`date`] End configure --prefix=${pcre_program}"
		
		# make
		_return "[`date`] Begin make ${pcre_src}."
		make
		_return "[`date`] End make ${pcre_src}."
	fi
	
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin zip ${pcre_name}-${pcre_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${pcre_name}-${pcre_version}.tar.gz ${pcre_name}-${pcre_version}/
		_return "[`date`] End zip ${pcre_name}-${pcre_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${pcre_name}-${pcre_version}.tar.gz ]; then
			_return "[`date`] Zip ${pcre_name}-${pcre_version} src error."
			exit 1
		fi
	fi

	# make install
	_return "[`date`] Begin make install ${pcre_src}."
	cd ${pcre_src}
	make install
	_return "[`date`] End make install ${pcre_src}."
	
	# check install result
	if [ ! -d ${pcre_program} ]; then
		_return "[`date`] ${pcre_package} install error, ${pcre_program} not found, then exit 1."
		_fail
		exit 1
	fi

	# clear
	# TODO | rm -rf ${pcre_src}
	
	_return "[`date`] End install ${pcre_package}."
	
	
	#
	# install openssl-0.9.8y
	#
	_return "[`date`] Begin install ${openssl_package}."
	
	# check environment
	if [ ! -f ${openssl_package} ]; then
		_return "[`date`] ${openssl_package} not found, then exit 1."
		_fail
		exit 1
	fi
	if [ -d ${openssl_src} ]; then
		rm -rf ${openssl_src}
	fi
	if [ -d ${openssl_program} ]; then
		_return "[`date`] [WARN] ${openssl_program} is already installed, then remove it."
		rm -rf ${openssl_program}
	fi
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${openssl_package}."
	#cd ${package_home}
	cd ${src_temp}
	tar -zvxf ${openssl_package}
	_return "[`date`] End tar -zvxf ${openssl_package}."

	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure --prefix=${openssl_program}."
		cd ${openssl_src}
		./Configure linux-generic64 --prefix=${openssl_program}
		_return "[`date`] End configure --prefix=${openssl_program}."

		# make
		_return "[`date`] Begin make ${openssl_src}."
		make
		_return "[`date`] End make ${openssl_src}."
	fi
	
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin zip ${openssl_name}-${openssl_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${openssl_name}-${openssl_version}.tar.gz ${openssl_name}-${openssl_version}/
		_return "[`date`] End zip ${openssl_name}-${openssl_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${openssl_name}-${openssl_version}.tar.gz ]; then
			_return "[`date`] Zip ${openssl_name}-${openssl_version} src error."
			exit 0
		fi
	fi
	
	# make install
	_return "[`date`] Begin make install ${openssl_src}."
	cd ${openssl_src}
	make install
	_return "[`date`] End make install ${openssl_src}."
	
	# clear
	# TODO | rm -rf ${openssl_src}

	# check install result
	if [ ! -d ${openssl_program} ]; then
		_return "[`date`] ${openssl_package} install error, ${openssl_program} not found, then exit 1."
		_fail
		exit 1
	fi
	
	_return "[`date`] End install ${openssl_package}."
	
	
	#
	# install nginx-1.4.3 (stable version)
	#
	_return "[`date`] Begin install ${nginx_package}."
	
	# check
	if [ ! -f ${nginx_package} ]; then
		_return "[`date`] nginx_package not found, then exit 1."
		_fail
		exit 1
	fi
	if [ -d ${nginx_src} ]; then
		rm -rf ${nginx_src}
	fi
	if [ -d ${nginx_program} ]; then
		_return "${nginx_program} is already installed, then remove it."
		rm -rf ${nginx_program}
	fi
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${nginx_package}."
	#cd ${package_home}
	cd ${src_temp}
	tar -zvxf ${nginx_package}
	_return "[`date`] End tar -zvxf ${nginx_package}."

	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure --prefix=${nginx_program} --with-pcre=${pcre_src} --with-openssl=${openssl_src} --with-http_ssl_module --with-http_stub_status_module."
		cd ${nginx_src}
		./configure --prefix=${nginx_program} --with-pcre=${pcre_src} --with-openssl=${openssl_src} --with-http_ssl_module --with-http_stub_status_module
		_return "[`date`] End configure --prefix=${nginx_program} --with-pcre=${pcre_src} --with-openssl=${openssl_src} --with-http_ssl_module --with-http_stub_status_module."
	
		# make
		_return "[`date`] Begin make ${nginx_src}."
		make
		_return "[`date`] End make ${nginx_src}."
	fi

	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin zip ${nginx_name}-${nginx_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${nginx_name}-${nginx_version}.tar.gz ${nginx_name}-${nginx_version}/
		_return "[`date`] End zip ${nginx_name}-${nginx_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${nginx_name}-${nginx_version}.tar.gz ]; then
			_return "[`date`] Zip ${nginx_name}-${nginx_version} src error."
			exit 0
		fi
	fi

	# make install
	_return "[`date`] Begin make install ${nginx_src}."
	cd ${nginx_src}
	make install
	_return "[`date`] End make install ${nginx_src}."
		
	# clear
	rm -rf ${nginx_src}
	
	# check install result
	if [ ! -d ${nginx_program} ]; then
		_return "[`date`] ${nginx_package} install error, ${nginx_program} not found, then exit 1."
		_fail
		exit 1 
	fi
	
	_return "[`date`] End install ${nginx_package}."
	
	# Copy certificate to ${nginx.home}/conf
	_return "[`date`] Copy certificates to ${nginx_program}/conf directory."
	certificatesPath=${BIN_HOME_PATH}/Nginx/packages/etc/certificates
	if [ -f ${certificatesPath}/default_nopass.key ]; then
		cp ${certificatesPath}/default_nopass.key ${nginx_program}/conf
	fi
	if [ -f ${certificatesPath}/default.crt ]; then
		cp ${certificatesPath}/default.crt ${nginx_program}/conf
	fi
	
	# clear stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
			
	_return "[`date`] End install nginx service."
	_success
	exit 0	
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh