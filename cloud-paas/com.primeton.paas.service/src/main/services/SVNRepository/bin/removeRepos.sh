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

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage:"
    echo "./removeRepos.sh -reqId 3001 -repoName sample"
    echo "-h) Print help"
    echo "-reqId) Request id"
    echo "-repoName) Repository name"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-repoName) repo_name=$2;shift 2;;
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
	
	echo rm -fr ${repos_root_dir}/${repo_name}
	rm -fr ${repos_root_dir}/${repo_name}
	
	# remove user from auth_http_user_file
	which htpasswd > /dev/null 2>&1
	if [ $? -ne 0 ]; then _error "command 'htpasswd' not found."; fi
 	__repo_users=`sed -n '/^'"${repo_name}"'=/p' ${authz_svn_access_file} | sed 's/^'"${repo_name}"'=//g' | sed 's/,/ /g'`
 	echo ${__repo_users}
	for __entry in ${__repo_users}
	do
		echo ${__entry}
		htpasswd -D ${auth_http_user_file} ${__entry}
		if [ $? -ne 0 ]; then _error "htpasswd delete user:${__entry} error"; fi
	done
 	
	
	# remove head and append tail from authz_svn_access_file
	current_timestamp=$(date +%F_%H-%M-%S)
	sed -e '/^@'"${repo_name}"'=/{n; /^$/d;}' ${authz_svn_access_file} | sed -e '/\['"${repo_name}"':\/\]/d' -e '/^@'"${repo_name}"'=/d' -e '/^'"${repo_name}"'=/d' > authz_svn_access_file.tmp.${current_timestamp}
	if [ $? -ne 0 ]; then _error "sed '${authz_svn_access_file}' error"; fi
	\cp authz_svn_access_file.tmp.${current_timestamp} ${authz_svn_access_file}
	if [ $? -ne 0 ]; then _error "\cp 'authz_svn_access_file.tmp.${current_timestamp}' to '${authz_svn_access_file}' error"; fi
	rm -fr authz_svn_access_file.tmp.${current_timestamp}
	
	_success
	_return "Remove repository ${repo_name} success"
	exit $?
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh