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
package_home=${BIN_HOME_PATH}/MySQL/packages/${INSTALL_SRC}

cmake_name=cmake
cmake_version=2.8.7

mysql_name=mysql
mysql_version=5.5.25

cmake_package=${package_home}/${cmake_name}-${cmake_version}.tar.gz
mysql_package=${package_home}/${mysql_name}-${mysql_version}.tar.gz

# Directory for unzip software packages
src_temp=${BIN_HOME_PATH}/MySQL/packages/temp
cmake_src=${src_temp}/${cmake_name}-${cmake_version}
mysql_src=${src_temp}/${mysql_name}-${mysql_version}

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/MySQL
cmake_program=${program_home}/${cmake_name}-${cmake_version}
mysql_program=${program_home}/${mysql_name}-${mysql_version}

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
	echo "cmake_package = ${cmake_package} "
	echo "mysql_package = ${mysql_package} "
	echo "program_home = ${program_home} "
	echo "cmake_program = ${cmake_program} "
	echo "mysql_program = ${mysql_program} "
	
	# check
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	
	# mkdir ${program_home}
	mkdir -p ${program_home}
	
	_return "[`date`] Begin install mysql."
	
	# main install

	# install cmake
	_return "[`date`] Begin install ${cmake_package}."
	_return "[`date`] Begin tar ${cmake_package}"
	cd ${src_temp}
	tar -zvxf ${cmake_package}
	_return "[`date`] End tar ${cmake_package}"
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# configure
		_return "[`date`] Begin configure ${cmake_src}."
		cd ${cmake_src}
		./configure --prefix=${cmake_program}
		_return "[`date`] End configure ${cmake_src}."
		
		# make
		_return "[`date`] Begin make ${cmake_src}."
		make
		_return "[`date`] End make ${cmake_src}."
	fi
	
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin tar -czf ${cmake_name}-${cmake_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${cmake_name}-${cmake_version}.tar.gz ${cmake_name}-${cmake_version}/
		_return "[`date`] End tar -czf ${cmake_name}-${cmake_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${cmake_name}-${cmake_version}.tar.gz ]; then
			_return "[`date`] tar -czf ${cmake_name}-${cmake_version} src error."
			exit 1
		fi
	fi
	
	# make install
	_return "[`date`] Begin make install ${cmake_src}."
	cd ${cmake_src}
	make install
	_return "[`date`] End make install ${cmake_src}."
	# clear
	_return "[`date`] Begin clear, remove ${cmake_src}."
	rm -rf ${cmake_src}
	_return "[`date`] End clear, remove ${cmake_src}."
	
	# check installation
	if [ ! -d ${cmake_program} ]; then
		_fail
		_return "[`date`] cmake install error."
		exit 0
	fi
	_return "[`date`] End install cmake."
	
	
	# install mysql
	_return "[`date`] Begin install ${mysql_package}."
	cd ${src_temp}
	tar -zvxf ${mysql_package}
	
	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		cd ${mysql_src}/support-files
		# [client]
		n=`awk '/\[client\]/{print NR}' my-medium.cnf.sh`
		n=$[${n}+3]
		sed -i ${n}' a\default-character-set = utf8' my-medium.cnf.sh
		# [mysqld]
		sed -i '/^myisam_sort_buffer_size = 8M/ a\lower_case_table_names = 1\nskip-name-resolve\nwait_timeout = 2880000\ninteractive_timeout = 28800000\n' my-medium.cnf.sh
		sed -i '/^interactive_timeout = 28800000/ a\init-connect = '"'SET NAMES utf8'"'\ncharacter-set-server = utf8' my-medium.cnf.sh
		sed -i 's/^log-bin=mysql-bin/#log-bin=mysql-bin/g' my-medium.cnf.sh
		# [mysql]
		sed -i '/^no-auto-rehash/ a\default-character-set = utf8' my-medium.cnf.sh
		
		# configure
		_return "[`date`] Begin cmake ${mysql_src}."
		cd ${mysql_src}
		${cmake_program}/bin/cmake -DCMAKE_INSTALL_PREFIX=${mysql_program} -DMYSQL_DATADIR=${mysql_program}/data -DDEFAULT_CHARSET=utf8 -DDEFAULT_COLLATION=utf8_general_ci -DEXTRA_CHARSETS=all -DENABLED_LOCAL_INFILE=1
		_return "[`date`] End cmake ${mysql_src}."
		# make
		_return "[`date`] Begin make ${mysql_src}."
		make
		_return "[`date`] End make ${mysql_src}."
	fi
	
	# package
	if [ "${BUILD_MODE}" = "true" ]; then
		_return "[`date`] Begin tar -czf ${mysql_name}-${mysql_version} src"
		cd ${src_temp}
		tar -czf ${TEMP_HOME_PATH}/${mysql_name}-${mysql_version}.tar.gz ${mysql_name}-${mysql_version}/
		_return "[`date`] End tar -czf ${mysql_name}-${mysql_version} src"
		cd ${TEMP_HOME_PATH}
		if [ ! -f ${mysql_name}-${mysql_version}.tar.gz ]; then
			_return "[`date`] tar -czf ${mysql_name}-${mysql_version} src error."
			exit 1
		fi
	fi
	
	# make install
	_return "[`date`] Begin make install ${mysql_src}."
	cd ${mysql_src}
	make install
	_return "[`date`] End make install ${mysql_src}."
	
	# clear
	_return "[`date`] Begin clear, remove ${mysql_src}."
	rm -rf ${mysql_src}
	_return "[`date`] End clear, remove ${mysql_src}."
	
	# check installation
	if [ ! -d ${mysql_program} ]; then
		_fail
		_return "[`date`] mysql install error."
		exit 0
	fi

	# Grant privileges
	# clean environment
	userdel -r mysql
	groupdel mysql
	
	_return "[`date`] add group mysql."	
	groupadd mysql
	_return "[`date`] add user mysql."
	useradd -r -g mysql mysql
	
	cd ${mysql_program}
	_return "[`date`] chown -R mysql ."
	# chown to mysql group
	chown -R root:mysql .
	_return "[`date`] default config cp support-files/my-medium.cnf /etc/my.cnf"
	cp -f support-files/my-medium.cnf /etc/my.cnf
	_return "[`date`] ${mysql_program}/scripts/mysql_install_db --user=mysql --datadir=${mysql_program}/data"
	${mysql_program}/scripts/mysql_install_db --user=mysql --datadir=${mysql_program}/data
	_return "[`date`] cp support-files/mysql.server  /etc/init.d/mysql"
	cp -f support-files/mysql.server  /etc/init.d/mysql
	# End Grant privileges
	
	_return "[`date`] End install ${mysql_package}"

	# clear stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
			
	_return "[`date`] End install mysql."
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh