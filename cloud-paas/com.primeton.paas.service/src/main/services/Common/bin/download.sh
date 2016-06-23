#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#
# description
# For download service install package and unzip package.
#
# Arguments:
# -url http://127.0.0.1:7399/services [BaseURL]
# -name Memcached [ServiceName]
# 

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
url=
name=
binHome=

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./download.sh"
    echo "-h) Print help"
    echo "-reqId) Request id"
    echo "-url) Software repository HTTP URL"
    echo "-name) Service name"
    echo -e "\nExample:./download.sh -reqId 0 -url http://192.168.100.100:8080/repository/downloads -name HaProxy\n"
}

# Parse execute arguments if has
function _doparse() {
    while [ -n "$1" ]; do
        case $1 in
        -url) url=$2;shift 2;;
        -name) name=$2;shift 2;;
        *) break;;
        esac
    done
}

# Write your core/main code
function _doexecute() {
	if [ -z ${url} ]; then
		_return "url is null or blank."
		_fail
		exit 0
	fi
	if [ -z ${name} ]; then
		_return "name is null or blank."
		_fail
		exit 0
	fi
	
	url=${url}/${name}.zip
	binHome=${BIN_HOME_PATH}/${name}
	srvPackage=${TEMP_HOME_PATH}/${name}.zip
	
	echo "url=${url}"
	echo "binHome=${binHome}"
	
	# download
	if [ -f ${srvPackage} ]; then
		rm ${srvPackage}
	fi
	wget -P ${TEMP_HOME_PATH} ${url} -o ${TEMP_HOME_PATH}/wget.${name}.out
	
	if [ ! -f ${srvPackage} ]; then
		_fail
		_return "Download ${name}.zip from ${url} failure."
		exit 0
	fi
	_return "Download ${name}.zip from ${url} success."
	
	# unzip
	if [ -d ${binHome} ]; then
		rm -rf ${binHome}
	fi
	unzip ${srvPackage} -d ${BIN_HOME_PATH}
	
	if [ ! -d ${binHome} ]; then
		_fail
		_return "unzip ${srvPackage} to ${BIN_HOME_PATH} failure."
		exit 0
	fi
	_return "unzip ${srvPackage} to ${BIN_HOME_PATH} success."
	
	# chmod +x
	chmod +x ${binHome}/bin/*.sh
	_return "chmod +x ${binHome}/bin/*.sh success."
	
	# clean stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
	
	# clean service package
	rm -f ${srvPackage}
	
	_success
	exit 0
}

# import templates.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh