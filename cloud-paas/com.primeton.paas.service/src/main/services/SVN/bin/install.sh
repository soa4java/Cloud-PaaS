#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#
# Install SVN service
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
# software packages
package_home=${BIN_HOME_PATH}/SVN/packages/${INSTALL_SRC}
apr_name=apr
apr_version=1.4.6
apr_package=${package_home}/${apr_name}-${apr_version}.tar.gz
apr_util_name=apr-util
apr_util_version=1.4.1
apr_util_package=${package_home}/${apr_util_name}-${apr_util_version}.tar.gz
db_name=db
db_version=5.1.29
db_package=${package_home}/${db_name}-${db_version}.tar.gz
expat_name=expat
expat_version=2.1.0
expat_package=${package_home}/${expat_name}-${expat_version}.tar.gz
httpd_name=httpd
httpd_version=2.4.2
httpd_package=${package_home}/${httpd_name}-${httpd_version}.tar.gz
openssl_name=openssl
openssl_version=0.9.8y
openssl_package=${package_home}/${openssl_name}-${openssl_version}.tar.gz
pcre_name=pcre
pcre_version=8.30
pcre_package=${package_home}/${pcre_name}-${pcre_version}.tar.gz
sqlite_name=sqlite-autoconf
sqlite_version=3071201
sqlite_package=${package_home}/${sqlite_name}-${sqlite_version}.tar.gz
subversion_name=subversion
subversion_version=1.7.5
subversion_package=${package_home}/${subversion_name}-${subversion_version}.tar.gz
zlib_name=zlib
zlib_version=1.2.7
zlib_package=${package_home}/${zlib_name}-${zlib_version}.tar.gz


# Directory for unzip software packages
src_temp=${BIN_HOME_PATH}/SVN/packages/temp
apr_src=${src_temp}/${apr_name}-${apr_version}
apr_util_src=${src_temp}/${apr_util_name}-${apr_util_version}
db_src=${src_temp}/${db_name}-${db_version}
expat_src=${src_temp}/${expat_name}-${expat_version}
httpd_src=${src_temp}/${httpd_name}-${httpd_version}
openssl_src=${src_temp}/${openssl_name}-${openssl_version}
pcre_src=${src_temp}/${pcre_name}-${pcre_version}
sqlite_src=${src_temp}/${sqlite_name}-${sqlite_version}
subversion_src=${src_temp}/${subversion_name}-${subversion_version}
zlib_src=${src_temp}/${zlib_name}-${zlib_version}

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/SVN
sqlite_program=${program_home}/${sqlite_name}-${sqlite_version}
zlib_program=${program_home}/${zlib_name}-${zlib_version}
db_program=${program_home}/${db_name}-${db_version}
expat_program=${program_home}/${expat_name}-${expat_version}
apr_program=${program_home}/${apr_name}-${apr_version}
apr_util_program=${program_home}/${apr_util_name}-${apr_util_version}
pcre_program=${program_home}/${pcre_name}-${pcre_version}
httpd_program=${program_home}/${httpd_name}-${httpd_version}
subversion_program=${program_home}/${subversion_name}-${subversion_version}
openssl_program=${program_home}/${openssl_name}-${openssl_version}

# Administrator user and password
svn_admin_user="admin"
svn_admin_password="000000"
svn_http_port=18880

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./install.sh"
    echo "-h) Print help"
    echo "-reqId) Request id"
    echo "-admin) SVN administrator user name, default value is admin"
    echo "-password) SVN administrator user password, default value is 000000"
    echo "-port) SVN HTTP port, default port is 18880"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-admin) svn_admin_user=$2;shift 2;;
		-password) svn_admin_password=$2;shift 2;;
		-port) svn_http_port=$2;shift 2;;
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
	
	_return "[`date`] Begin install SVN service."
	
	#
	# install sqlite
	#
	_return "[`date`] Begin install sqlite."
	
	# check package
	if [ ! -f ${sqlite_package} ]; then
		_fail
		_return "[`date`] Can not install sqlite, ${sqlite_package} not found."
		exit 0
	fi
	if [ -d ${sqlite_src} ]; then
		_return "[`date`] ${sqlite_src} already exist, then remove it."
		rm -rf ${sqlite_src}
	fi
	cd ${package_home}
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${sqlite_package}."
	cd ${src_temp}
	tar -zvxf ${sqlite_package}
	_return "[`date`] End tar -zvxf ${sqlite_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure ${sqlite_src}."
		cd ${sqlite_src}
		./configure --prefix=${sqlite_program}
		_return "[`date`] End configure ${sqlite_src}."
	
		# make
		_return "[`date`] Begin make ${sqlite_src}."
		make
		_return "[`date`] End make ${sqlite_src}."
	fi
#TODO ${sqlite_name}-${sqlite_version}
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin tar -czf ${sqlite_name}-${sqlite_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${sqlite_name}-${sqlite_version}.tar.gz ${sqlite_name}-${sqlite_version}/
		_return "[`date`] End tar -czf ${sqlite_name}-${sqlite_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${sqlite_name}-${sqlite_version}.tar.gz ]; then
			_return "[`date`] tar -czf ${sqlite_name}-${sqlite_version} src error."
			exit 1
		fi
	fi
	
	# make install
	_return "[`date`] Begin make install ${sqlite_src}."
	cd ${sqlite_src}
	make install
	_return "[`date`] End make install ${sqlite_src}."
	
	# clear
	_return "[`date`] Begin clear, remove ${sqlite_src}."
	rm -rf ${sqlite_src}
	_return "[`date`] End clear, remove ${sqlite_src}."
	
	# check installation
	if [ ! -d ${sqlite_program} ]; then
		_fail
		_return "[`date`] sqlite install error."
		exit 0
	fi
	_return "[`date`] End install sqlite."
	
	
	#
	# install zlib
	#
	
	_return "[`date`] Begin install zlib."
	
	# check package
	if [ ! -f ${zlib_package} ]; then
		_fail
		_return "[`date`] Can not install zlib, ${zlib_package} not found."
		exit 0
	fi
	if [ -d ${zlib_src} ]; then
		_return "[`date`] ${zlib_src} already exist, then remove it."
		rm -rf ${zlib_src}
	fi
	cd ${package_home}
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${zlib_package}."
	cd ${src_temp}
	tar -zvxf ${zlib_package}
	_return "[`date`] End tar -zvxf ${zlib_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure ${zlib_src}."
		cd ${zlib_src}
		./configure --prefix=${zlib_program}
		_return "[`date`] End configure ${zlib_src}."
	
		# make
		_return "[`date`] Begin make ${zlib_src}."
		make
		_return "[`date`] End make ${zlib_src}."
	fi
#TODO	${zlib_name}-${zlib_version}
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin tar -czf ${zlib_name}-${zlib_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${zlib_name}-${zlib_version}.tar.gz ${zlib_name}-${zlib_version}/
		_return "[`date`] End tar -czf ${zlib_name}-${zlib_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${zlib_name}-${zlib_version}.tar.gz ]; then
			_return "[`date`] tar -czf ${zlib_name}-${zlib_version} src error."
			exit 1
		fi
	fi

	# make install
	_return "[`date`] Begin make install ${zlib_src}."
	cd ${zlib_src}
	make install
	_return "[`date`] End make install ${zlib_src}."
	
	# clear
	_return "[`date`] Begin clear, remove ${zlib_src}."
	rm -rf ${zlib_src}
	_return "[`date`] End clear, remove ${zlib_src}."
	
	# check installation
	if [ ! -d ${zlib_program} ]; then
		_fail
		_return "[`date`] db install error."
		exit 0
	fi
	_return "[`date`] End install zlib."
	
	
	#
	# install db
	#
	
	_return "[`date`] Begin install db."
	
	# check package
	if [ ! -f ${db_package} ]; then
		_fail
		_return "[`date`] Can not install db, ${db_package} not found."
		exit 0
	fi
	if [ -d ${db_src} ]; then
		_return "[`date`] ${db_src} already exist, then remove it."
		rm -rf ${db_src}
	fi
	cd ${package_home}
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${db_package}."
	cd ${src_temp}
	tar -zvxf ${db_package}
	_return "[`date`] End tar -zvxf ${db_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure ${db_src}."
		cd ${db_src}/build_unix
		../dist/configure --prefix=${db_program}
		_return "[`date`] End configure ${db_src}."
	
		# make
		_return "[`date`] Begin make ${db_src}."
		make
		_return "[`date`] End make ${db_src}."
	fi
#TODO	${db_name}-${db_version}
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin tar -czf ${db_name}-${db_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${db_name}-${db_version}.tar.gz ${db_name}-${db_version}/
		_return "[`date`] End tar -czf ${db_name}-${db_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${db_name}-${db_version}.tar.gz ]; then
			_return "[`date`] tar -czf ${db_name}-${db_version} src error."
			exit 1
		fi
	fi
	
	# make install
	_return "[`date`] Begin make install ${db_src}."
	cd ${db_src}/build_unix
	make install
	_return "[`date`] End make install ${db_src}."
	
	# clear
	_return "[`date`] Begin clear, remove ${db_src}."
	rm -rf ${db_src}
	_return "[`date`] End clear, remove ${db_src}."
	
	# check installation
	if [ ! -d ${db_program} ]; then
		_fail
		_return "[`date`] db install error."
		exit 0
	fi
	_return "[`date`] End install db."	


	#
	# install expat
	#
	
	_return "[`date`] Begin install expat."
	
	# check package
	if [ ! -f ${expat_package} ]; then
		_fail
		_return "[`date`] Can not install expat, ${expat_package} not found."
		exit 0
	fi
	if [ -d ${expat_src} ]; then
		_return "[`date`] ${expat_src} already exist, then remove it."
		rm -rf ${expat_src}
	fi
	cd ${package_home}
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${expat_package}."
	cd ${src_temp}
	tar -zvxf ${expat_package}
	_return "[`date`] End tar -zvxf ${expat_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure ${expat_src}."
		cd ${expat_src}
		./configure --prefix=${expat_program}
		_return "[`date`] End configure ${expat_src}."
		# make
		_return "[`date`] Begin make ${expat_src}."
		make
		_return "[`date`] End make ${expat_src}."
	fi
#TODO	${expat_name}-${expat_version}
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin tar -czf ${expat_name}-${expat_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${expat_name}-${expat_version}.tar.gz ${expat_name}-${expat_version}/
		_return "[`date`] End tar -czf ${expat_name}-${expat_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${expat_name}-${expat_version}.tar.gz ]; then
			_return "[`date`] tar -czf ${expat_name}-${expat_version} src error."
			exit 1
		fi
	fi
	
	# make install
	_return "[`date`] Begin make install ${expat_src}."
	cd ${expat_src}
	make install
	_return "[`date`] End make install ${expat_src}."
	
	# clear
	_return "[`date`] Begin clear, remove ${expat_src}."
	rm -rf ${expat_src}
	_return "[`date`] End clear, remove ${expat_src}."
	
	# check installation
	if [ ! -d ${expat_program} ]; then
		_fail
		_return "[`date`] expat install error."
		exit 0
	fi
	_return "[`date`] End install expat."
	
	
	#
	# install apr
	#
	
	_return "[`date`] Begin install apr."
	
	# check package
	if [ ! -f ${apr_package} ]; then
		_fail
		_return "[`date`] Can not install apr, ${apr_package} not found."
		exit 0
	fi
	if [ -d ${apr_src} ]; then
		_return "[`date`] ${apr_src} already exist, then remove it."
		rm -rf ${apr_src}
	fi
	cd ${package_home}
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${apr_package}."
	cd ${src_temp}
	tar -zvxf ${apr_package}
	_return "[`date`] End tar -zvxf ${apr_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure ${apr_src}."
		cd ${apr_src}
		./configure --prefix=${apr_program}
		_return "[`date`] End configure ${apr_src}."
	
		# make
		_return "[`date`] Begin make ${apr_src}."
		make
		_return "[`date`] End make ${apr_src}."
	fi
#TODO ${apr_name}-${apr_version}
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin tar -czf ${apr_name}-${apr_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${apr_name}-${apr_version}.tar.gz ${apr_name}-${apr_version}/
		_return "[`date`] End tar -czf ${apr_name}-${apr_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${apr_name}-${apr_version}.tar.gz ]; then
			_return "[`date`] tar -czf ${apr_name}-${apr_version} src error."
			exit 1
		fi
	fi
	
	# make install
	_return "[`date`] Begin make install ${apr_src}."
	cd ${apr_src}
	make install
	_return "[`date`] End make install ${apr_src}."
	
	# clear
	_return "[`date`] Begin clear, remove ${apr_src}."
	rm -rf ${apr_src}
	_return "[`date`] End clear, remove ${apr_src}."
	
	# check installation
	if [ ! -d ${apr_program} ]; then
		_fail
		_return "[`date`] apr install error."
		exit 0
	fi
	_return "[`date`] End install apr."
	
	
	#
	# install apr-util
	#
	
	_return "[`date`] Begin install apr-util."
	
	# check package
	if [ ! -f ${apr_util_package} ]; then
		_fail
		_return "[`date`] Can not install apr_util, ${apr_util_package} not found."
		exit 0
	fi
	if [ -d ${apr_util_src} ]; then
		_return "[`date`] ${apr_util_src} already exist, then remove it."
		rm -rf ${apr_util_src}
	fi
	cd ${package_home}
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${apr_util_package}."
	cd ${src_temp}
	tar -zvxf ${apr_util_package}
	_return "[`date`] End tar -zvxf ${apr_util_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure ${apr_util_src}."
		cd ${apr_util_src}
		./configure --prefix=${apr_util_program} --with-apr=${apr_program}
		_return "[`date`] End configure ${apr_util_src}."
	
		# make
		_return "[`date`] Begin make ${apr_util_src}."
		make
		_return "[`date`] End make ${apr_util_src}."
	fi
#TODO ${apr_util_name}-${apr_util_version}
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin tar -czf ${apr_util_name}-${apr_util_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${apr_util_name}-${apr_util_version}.tar.gz ${apr_util_name}-${apr_util_version}/
		_return "[`date`] End tar -czf ${apr_util_name}-${apr_util_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${apr_util_name}-${apr_util_version}.tar.gz ]; then
			_return "[`date`] tar -czf ${apr_util_name}-${apr_util_version} src error."
			exit 1
		fi
	fi

	# make install
	_return "[`date`] Begin make install ${apr_util_src}."
	cd ${apr_util_src}
	make install
	_return "[`date`] End make install ${apr_util_src}."
	
	# clear
	_return "[`date`] Begin clear, remove ${apr_util_src}."
	rm -rf ${apr_util_src}
	_return "[`date`] End clear, remove ${apr_util_src}."
	
	# check installation
	if [ ! -d ${apr_util_program} ]; then
		_fail
		_return "[`date`] apr-util install error."
		exit 0
	fi
	_return "[`date`] End install apr-util."
	
	
	#
	# install pcre
	#
	
	_return "[`date`] Begin install pcre."
	
	# check package
	if [ ! -f ${pcre_package} ]; then
		_fail
		_return "[`date`] Can not install pcre, ${pcre_package} not found."
		exit 0
	fi
	if [ -d ${pcre_src} ]; then
		_return "[`date`] ${pcre_src} already exist, then remove it."
		rm -rf ${pcre_src}
	fi
	cd ${package_home}
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${pcre_package}."
	cd ${src_temp}
	tar -zvxf ${pcre_package}
	_return "[`date`] End tar -zvxf ${pcre_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure ${pcre_src}."
		cd ${pcre_src}
		./configure --prefix=${pcre_program}
		_return "[`date`] End configure ${pcre_src}."
	
		# make
		_return "[`date`] Begin make ${pcre_src}."
		make
		_return "[`date`] End make ${pcre_src}."
	fi
#TODO ${pcre_name}-${pcre_version}
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin tar -czf ${pcre_name}-${pcre_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${pcre_name}-${pcre_version}.tar.gz ${pcre_name}-${pcre_version}/
		_return "[`date`] End tar -czf ${pcre_name}-${pcre_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${pcre_name}-${pcre_version}.tar.gz ]; then
			_return "[`date`] tar -czf ${pcre_name}-${pcre_version} src error."
			exit 1
		fi
	fi

	# make install
	_return "[`date`] Begin make install ${pcre_src}."
	cd ${pcre_src}
	make install
	_return "[`date`] End make install ${pcre_src}."
	
	# clear
	_return "[`date`] Begin clear, remove ${pcre_src}."
	rm -rf ${pcre_src}
	_return "[`date`] End clear, remove ${pcre_src}."
	
	# check installation
	if [ ! -d ${pcre_program} ]; then
		_fail
		_return "[`date`] pcre install error."
		exit 0
	fi
	_return "[`date`] End install pcre."
	
	
	#
	# install httpd
	#
	
	_return "[`date`] Begin install httpd."
	
	# check package
	if [ ! -f ${httpd_package} ]; then
		_fail
		_return "[`date`] Can not install httpd, ${httpd_package} not found."
		exit 0
	fi
	if [ -d ${httpd_src} ]; then
		_return "[`date`] ${httpd_src} already exist, then remove it."
		rm -rf ${httpd_src}
	fi
	cd ${package_home}
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${httpd_package}."
	cd ${src_temp}
	tar -zvxf ${httpd_package}
	_return "[`date`] End tar -zvxf ${httpd_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure ${httpd_src}."
		cd ${httpd_src}
		./configure --prefix=${httpd_program} --with-apr=${apr_program} --with-apr-util=${apr_util_program} --with-pcre=${pcre_program} --enable-so --enable-dav --enable-maintainer-mode --enable-rewrite
		_return "[`date`] End configure ${httpd_src}."
	
		# make
		_return "[`date`] Begin make ${httpd_src}."
		make
		_return "[`date`] End make ${httpd_src}."
	fi
#TODO	${httpd_name}-${httpd_version}
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin tar -czf ${httpd_name}-${httpd_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${httpd_name}-${httpd_version}.tar.gz ${httpd_name}-${httpd_version}/
		_return "[`date`] End tar -czf ${httpd_name}-${httpd_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${httpd_name}-${httpd_version}.tar.gz ]; then
			_return "[`date`] tar -czf ${httpd_name}-${httpd_version} src error."
			exit 1
		fi
	fi

	# make install
	_return "[`date`] Begin make install ${httpd_src}."
	cd ${httpd_src}
	make install
	_return "[`date`] End make install ${httpd_src}."
	
	# clear
	_return "[`date`] Begin clear, remove ${httpd_src}."
	rm -rf ${httpd_src}
	_return "[`date`] End clear, remove ${httpd_src}."
	
	# check installation
	if [ ! -d ${httpd_program} ]; then
		_fail
		_return "[`date`] httpd install error."
		exit 0
	fi
	_return "[`date`] End install httpd."
	
	
	#
	# install subversion
	#
	
	_return "[`date`] Begin install subversion."
	
	# check package
	if [ ! -f ${subversion_package} ]; then
		_fail
		_return "[`date`] Can not install subversion, ${subversion_package} not found."
		exit 0
	fi
	if [ -d ${subversion_src} ]; then
		_return "[`date`] ${subversion_src} already exist, then remove it."
		rm -rf ${subversion_src}
	fi
	cd ${package_home}
	
	# tar -zvxf
	_return "[`date`] Begin tar -zvxf ${subversion_package}."
	cd ${src_temp}
	tar -zvxf ${subversion_package}
	_return "[`date`] End tar -zvxf ${subversion_package}."
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure ${subversion_src}."
		cd ${subversion_src}
		./configure --prefix=${subversion_program} --with-zlib=${zlib_program} --with-apxs=${httpd_program}/bin/apxs --with-apr=${apr_program} --with-apr-util=${apr_util_program} --with-sqlite=${sqlite_program} --enable-maintainer-mode
		_return "[`date`] End configure ${subversion_src}."
	
		# make
		_return "[`date`] Begin make ${subversion_src}."
		make
		_return "[`date`] End make ${subversion_src}."
	fi
#TODO	${subversion_name}-${subversion_version}
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin tar -czf ${subversion_name}-${subversion_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${subversion_name}-${subversion_version}.tar.gz ${subversion_name}-${subversion_version}/
		_return "[`date`] End tar -czf ${subversion_name}-${subversion_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${subversion_name}-${subversion_version}.tar.gz ]; then
			_return "[`date`] tar -czf ${subversion_name}-${subversion_version} src error."
			exit 1
		fi
	fi

	# make install
	_return "[`date`] Begin make install ${subversion_src}."
	cd ${subversion_src}
	make install
	_return "[`date`] End make install ${subversion_src}."
	
	# clear
	_return "[`date`] Begin clear, remove ${subversion_src}."
	rm -rf ${subversion_src}
	_return "[`date`] End clear, remove ${subversion_src}."
	
	# check installation
	if [ ! -d ${subversion_program} ]; then
		_fail
		_return "[`date`] subversion install error."
		exit 0
	fi
	_return "[`date`] End install subversion."
				
	
	# User Authorization
	_return "[`date`] Begin configure svn repository and user."
	if [ -d ${WORKSPACE_HOME_PATH}/svn ]; then
		rm -rf ${WORKSPACE_HOME_PATH}/svn
	fi
	mkdir -p ${WORKSPACE_HOME_PATH}/svn/{conf,repos,httpd}
	cd ${WORKSPACE_HOME_PATH}/svn
	
	# add admin user
	auth_http_user=${WORKSPACE_HOME_PATH}/svn/conf/auth-http-user
	touch ${auth_http_user}
	${httpd_program}/bin/htpasswd -b ${auth_http_user} ${svn_admin_user} ${svn_admin_password}
	
	# add admin access
	authz_svn_access=${WORKSPACE_HOME_PATH}/svn/conf/authz-svn-access
	touch ${authz_svn_access}
	
	echo "[/]" >> ${authz_svn_access}
	echo "# / is root dir" >> ${authz_svn_access}
	echo "# with @ is a group, without @ is a user" >> ${authz_svn_access}
	echo "@admin=rw" >> ${authz_svn_access}
	echo "[groups]" >> ${authz_svn_access}
	echo "# group=user1,user2" >> ${authz_svn_access}
	echo "admin=admin" >> ${authz_svn_access}
	
	_return "[`date`] End configure svn repository and user."
	
	_return "[`date`] Begin configure httpd.conf."
	cd ${WORKSPACE_HOME_PATH}/svn/httpd
	cp -r ${httpd_program}/conf .
	cp -r ${httpd_program}/htdocs/ .
	ln -s ${httpd_program}/modules/ modules
	mkdir logs
	
	# configure httpd.conf
	httpd_conf=${WORKSPACE_HOME_PATH}/svn/httpd/conf/httpd.conf
	
	echo "" >> ${httpd_conf}
	echo "" >> ${httpd_conf}
	echo "# Add content by install.sh" >> ${httpd_conf}
	echo "LoadModule dav_svn_module modules/mod_dav_svn.so" >> ${httpd_conf}
	echo "LoadModule authz_svn_module modules/mod_authz_svn.so" >> ${httpd_conf}
	echo "<Location /repos/>" >> ${httpd_conf}
	echo "DAV svn" >> ${httpd_conf}
	echo "SVNListParentPath on" >> ${httpd_conf}
	echo "SVNParentPath ${WORKSPACE_HOME_PATH}/svn/repos" >> ${httpd_conf}
	echo "AuthzSVNAccessFile ${authz_svn_access}" >> ${httpd_conf}
	echo "AuthType Basic" >> ${httpd_conf}
	echo "AuthName \"Please input username and password.\"" >> ${httpd_conf}
	echo "AuthUserFile ${auth_http_user}" >> ${httpd_conf}
	echo "Require valid-user" >> ${httpd_conf}
	echo "</Location>" >> ${httpd_conf}
	echo "RedirectMatch ^(/repos)$ \$1/" >> ${httpd_conf}
	echo "" >> ${httpd_conf}
	echo "" >> ${httpd_conf}
	echo "# ServerName" >> ${httpd_conf}
	echo "ServerName	${PAAS_LOCAL_IP}" >> ${httpd_conf}
	
	sed -i -e "s/Listen 80/Listen ${svn_http_port}/g" ${httpd_conf}
	sed -i -e "s/programs\/SVN\/httpd-2.4.2/workspace\/svn\/httpd/g" ${httpd_conf}
	
	mkdir -p ${WORKSPACE_HOME_PATH}/svn/repo-templates/default/{branches,tags,trunk,autodeploy,war}
	chown -R daemon ${WORKSPACE_HOME_PATH}/svn 
	
	_return "[`date`] End configure httpd.conf."

	# clear stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
			
	# OK	
	_return "[`date`] End install SVN service."
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh