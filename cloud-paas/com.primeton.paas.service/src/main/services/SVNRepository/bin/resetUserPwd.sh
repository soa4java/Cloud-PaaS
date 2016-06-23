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

repo_name=
repo_user=
repo_pwd=

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage:"
    echo "./resetUserPwd.sh -reqId 3001 -repoName sample -user tiger -password 111111"
    echo "-h) Print help"
    echo "-reqId) Request id"
    echo "-repoName) Repository name"
    echo "-user) Repository user"
    echo "-password) New password"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-repoName) repo_name=$2;shift 2;;
		-user) repo_user=$2;shift 2;;
		-password) repo_pwd=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	if test -z "$repo_name"; then _error "The repo_name is null"; _return "arguments error"; exit 0; fi
	if test -z "$repo_user"; then _error "The repo_user is null"; _return "arguments error"; exit 0; fi
	if test -z "$repo_pwd"; then _error "The repo_pwd is null"; _return "arguments error"; exit 0; fi
	
	which htpasswd > /dev/null 2>&1
	if [ $? -eq 0 ]; then
		htpasswd -D ${auth_http_user_file} ${repo_user}
		if [ $? -ne 0 ]; then 
			errorMessage="htpasswd delete user:${repo_user} error";
			_error "${errorMessage}"
			_return "${errorMessage}"
			exit 0
		fi
		htpasswd -b ${auth_http_user_file} ${repo_user} ${repo_pwd}
		if [ $? -ne 0 ]; then 
			errorMessage="htpasswd add user:${repo_user} error";
			_error "${errorMessage}"
			_return "${errorMessage}"
			exit 0 
		fi
		
		_return "Reset $repo_user password success."
		_success
		exit $?
	else
		_error "Command 'htpasswd' not found."
		exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh