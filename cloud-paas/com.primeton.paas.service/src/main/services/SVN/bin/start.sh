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

# Global variables
program_home=${PROGRAME_HOME_PATH}/SVN
httpd_program=${program_home}/httpd-2.4.2
subversion_program=${program_home}/subversion-1.7.5
PATH=${PATH}:${httpd_program}/bin:${subversion_program}/bin

# variables
svn_workspace=${WORKSPACE_HOME_PATH}/svn
authz_svn_access_file="${svn_workspace}/conf/authz-svn-access"
auth_http_user_file="${svn_workspace}/conf/auth-http-user"
repos_root_dir="${svn_workspace}/repos"
repo_templates_root_dir="${svn_workspace}/repo-templates"
httpd_file="${svn_workspace}/httpd/conf/httpd.conf"
httpd_pid_file="${svn_workspace}/httpd/logs/httpd.pid"
http_port=18880
https_port=18443
base_path=${httpd_program}

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 3001 -srvDefName SVN -srvInstId 20001 -clusterName cluster1"
	echo "-h) Print help"
	echo "-reqId) Request id"
	echo "-srvInstId) Service instance id"
	echo "-srvDefName) Service type"
	echo "-clusterName) Cluster name"
    
	# echo "-httpPort) http port"
	# echo "-basePath) httpd directory"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
			-arg0) arg0=$2;shift 2;;
			#-httpPort) http_port=$2;shift 2;;
			#-httpsPort) https_port=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# if command not exists
	if [ ! -f ${httpd_program}/bin/httpd ]; then
		_fail
		_return "${httpd_program}/bin/httpd not exists."
		exit 1
	fi
	# if has been started
	if [ -f ${httpd_pid_file} ]; then
		httpd_pid=$(cat ${httpd_pid_file})
		if [ `ps -p ${httpd_pid} | grep -v PID | wc -l` -gt 0 ]; then
			_success
			_pid ${httpd_pid}
			_return "httpd has been started."
			exit 0
		fi
	fi
	
	# start
	${httpd_program}/bin/httpd -f ${httpd_file}
	
	i=0
	while test $i -ne 300 ; do
		if test -s "${httpd_pid_file}"
		then
			break
		fi
		i=`expr $i + 1`
		echo "#"
		sleep 1
	done
	
	if [ $i -eq 300 ]; then
		_error "${httpd_pid_file} not found."
		exit 0
	fi
	
	httpd_pid=$(cat ${httpd_pid_file})
	_pid ${httpd_pid}
	_return "Apache httpd successfully started, pid=${httpd_pid}, port=${http_port}, ssl.port=${https_port}"
	_success
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh