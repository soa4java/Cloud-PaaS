#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ---------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

package_home=${BIN_HOME_PATH}/Gateway/packages
# easn.zip contain source and binary
easn_package=${package_home}/${PACKAGE_SRC}/easn.zip

# template configuration files
sysctl_cfg_file=${package_home}/conf/sysctl.conf
limits_cfg_file=${package_home}/conf/limits.conf

program_home=${PROGRAME_HOME_PATH}/Gateway
easn_program=${program_home}/easn

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
	
	if [ ! -d ${package_home}/conf ]; then
		mkdir -p ${package_home}/conf
	fi
	if [ ! -f ${sysctl_cfg_file} ]; then
		echo "Copy /etc/sysctl.conf to ${package_home}/conf as template."
		cp /etc/sysctl.conf ${package_home}/conf
	fi
	cp -f ${sysctl_cfg_file} /etc
	
	# modify /etc/sysctl.conf
	echo "" >> /etc/sysctl.conf
	echo "# Linux 2.6.22 or higher" >> /etc/sysctl.conf
	echo "net.nf_conntrack_max =365536" >> /etc/sysctl.conf
	
	# Reload sysctl.conf
	echo "Execute : modprobe bridge; sysctl -p"
	modprobe bridge
	sysctl -p
	
	if [ ! -f ${limits_cfg_file} ]; then
		echo "Copy /etc/security/limits.conf to ${package_home}/conf as template."
		cp /etc/security/limits.conf ${package_home}/conf
	fi
	cp -f ${limits_cfg_file} /etc/security
	
	# modify limits.conf
	echo "" >> /etc/security/limits.conf
	echo "# limit user process number" >> /etc/security/limits.conf
	echo "* softnofile 505536" >> /etc/security/limits.conf
	echo "* hardnofile 505536" >> /etc/security/limits.conf
	
	# clean
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	mkdir -p ${program_home}
	
	_return "[`date`] Begin install easn."
	
	# check package
	if [ ! -f ${easn_package} ]; then
		_fail
		_return "[`date`] Can not install easn, ${easn_package} not exists."
		exit 0
	fi
	# clean
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	
	# unzip
	_return "[`date`] Begin unzip ${easn_package}."
	unzip ${easn_package} -d ${program_home}
	_return "[`date`] End unzip ${easn_package}."

	# source
	if [ "${INSTALL_SRC}x" = "${PACKAGE_SRC}x" ]; then
		# make fes_698tcps and processmanager
		_return "[`date`] Begin make ${easn_program}/fes_698tcps."
		cd ${easn_program}/fes_698tcps
		make clean
		make
		_return "[`date`] End make ${easn_program}/fes_698tcps."
	fi
	if [ ! -f ${easn_program}/fes_698tcps/fes_698tcps ]; then
		_return "make ${easn_program}/fes_698tcps error"
		_fail
		exit 0
	fi
	
	# chmod 
	chmod +x ${easn_program}/fes_698tcps/fes_698tcps
	
	_return "[`date`] End install easn."

	# clean stdout.txt
	echo "[`date`] OK." > ${FILE_PATH_STDOUT}
	
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh