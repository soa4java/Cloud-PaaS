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
template_name="default"

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage:"
    echo "./createRepos.sh -reqId 3001 -repoName sample"
    echo "./createRepos.sh -reqId 3001 -repoName sample -templateName default"
    echo "-h) Print help"
    echo "-reqId) Request id"
    echo "-repoName) Repository name"
    echo "-templateName) Repository template name"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-repoName) repo_name=$2;shift 2;;
		-template_name) templateName=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	if test -z "$repo_name"; then 
		_return "The repo_name is null";
		_error "The repo_name is null"; 
		exit 0; 
	fi
	if [ -d "${repos_root_dir}/${repo_name}" ]; then 
		_return "The repo_name:${repo_name} is existed.";
		_error "The repo_name:${repo_name} is existed."; 
		exit 0;
	fi
	
	which svnadmin > /dev/null 2>&1
	if [ $? -eq 0 ]; then
		svnadmin create --fs-type fsfs ${repos_root_dir}/${repo_name}
		if [ $? -ne 0 ]; then 
			_return "svnadmin create '${repos_root_dir}/${repo_name}' error"; 
			_error "svnadmin create '${repos_root_dir}/${repo_name}' error";
			exit 0;
		fi
		
		#chown -R svn:svn ${repos_root_dir}/${repo_name}
		#if [ $? -ne 0 ]; then _error "chown '${repos_root_dir}/${repo_name}' error"; fi
		svn import ${repo_templates_root_dir}/${template_name} file://${repos_root_dir}/${repo_name} -m 'import'
		if [ $? -ne 0 ]; then 
			errorMessage="svn import '${repo_templates_root_dir}/${template_name}' to 'file://${repos_root_dir}/${repo_name}' error";
			_return ${errorMessage}
			_error ${errorMessage}
			exit 0
		fi
		
		# insert head and append tail
		current_timestamp=$(date +%F_%H-%M-%S)
		sed -e '1i\['"${repo_name}"':/]\n@'"${repo_name}"'=rw\n' -e '$a\'"${repo_name}"'=' ${authz_svn_access_file} > authz_svn_access_file.tmp.${current_timestamp}
		if [ $? -ne 0 ]; then _error "sed '${authz_svn_access_file}' error"; fi
		\cp authz_svn_access_file.tmp.${current_timestamp} ${authz_svn_access_file}
		if [ $? -ne 0 ]; then _error "\cp 'authz_svn_access_file.tmp.${current_timestamp}' to '${authz_svn_access_file}' error"; fi
		rm -fr authz_svn_access_file.tmp.${current_timestamp}

		chown -R daemon ${svn_workspace}
		
		_success
		_return "Create repository ${repo_name} success."
		exit $?
	else
		_error "command 'svnadmin' not found."
		_return "command 'svnadmin' not found."
		exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh