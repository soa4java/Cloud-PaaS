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

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvInstId) service instance id"
	echo "-srvDefName) service type"
	echo "-clusterName) cluster name"
    
	echo "-ip) host ip"
	echo "-port) http Port"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-ip) ip=$2;shift 2;;
		-port) port=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# TODO XXX
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh
