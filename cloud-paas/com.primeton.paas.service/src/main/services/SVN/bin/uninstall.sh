#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#
# Uninstall SVN service
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/SVN
bin_home=${BIN_HOME_PATH}/SVN
workspace_home=${WORKSPACE_HOME_PATH}/svn

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./uninstall.sh"
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
	echo "workspace_home = ${workspace_home}"
	
	_return "[`date`] Begin uninstall SVN."
	
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
	
	# remove workspace
	_return "[`date`] Remove ${workspace_home}."
	if [ -d ${workspace_home} ]; then
		rm -rf ${workspace_home}
	fi
	
	# remove Repository bin
	if [ -d ${BIN_HOME_PATH}/SVNRepository ]; then
		rm -rf ${BIN_HOME_PATH}/SVNRepository
	fi
	
	_return "[`date`] End uninstall SVN."
	
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh