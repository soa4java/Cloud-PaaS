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

# my variables
# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/Gateway
bin_home=${BIN_HOME_PATH}/Gateway

sysctl_cfg_file=${bin_home}/packages/conf/sysctl.conf
limits_cfg_file=${bin_home}/packages/conf/limits.conf

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./uninstall.sh -reqId uuid"
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
	echo "program_home = ${program_home}"
	echo "bin_home = ${bin_home}"
	
	# recover default
	if [ -f ${sysctl_cfg_file} ]; then
		cp -f ${sysctl_cfg_file} /etc
		# Reload sysctl.conf
		echo "Execute : modprobe bridge; sysctl -p"
		modprobe bridge
		sysctl -p
	fi
	if [ -f ${limits_cfg_file} ]; then
		cp -f ${limits_cfg_file} /etc/security
	fi
	
	_return "[`date`] Begin uninstall Gateway."
	
	# remove program
	_return "[`date`] Remove ${program_home}."
	if [ -d ${program_home} ]; then
		rm -rf ${program_home}
	fi
	
	# remove bin
	_return "[`date`] Remove ${bin_home}."
	if [ -d ${bin_home} ]; then
		rm -rf ${bin_home}
	fi
	
	_return "[`date`] End uninstall Gateway."
	
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh