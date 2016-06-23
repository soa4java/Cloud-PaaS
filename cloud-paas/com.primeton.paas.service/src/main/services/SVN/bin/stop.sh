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
	echo "./stop.sh -reqId 3001 -srvDefName SVN -srvInstId 20001 -clusterName cluster1"
	echo "-h) Print help"
	echo "-reqId) Request id"
	echo "-srvInstId) Service instance id"
	echo "-srvDefName) Service type"
	echo "-clusterName) Cluster name"
    
	# echo "-httpPort) http port"
	# echo "-httpsPort) https port"
	# echo "-basePath) httpd directory"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
			-arg0) arg0=$2;shift 2;;
		# -httpPort) http_port=$2;shift 2;;
		# -httpsPort) https_port=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
_doexecute() {
	which httpd > /dev/null 2>&1
	if [ $? -eq 0 ]; then
		httpd -f ${httpd_file} -k stop
		if [ -f ${httpd_pid_file} ]; then
			httpdPid=`cat ${httpd_pid_file}`
			kill -TERM ${httpdPid} 2 > /dev/null
			_return "Apache httpd successfully stoped, kill process ${httpdPid} success."
		else
			_return "Apache httpd is already stoped or ${httpd_pid_file} not found."
		fi
		_success
		exit $?
	else
		_error "Command 'httpd' not found."
		exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh