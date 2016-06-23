#!/bin/bash

# @Deprecated
#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#
# Description: 
# Generate HaProxy configuration file.
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
haproxyHome=${PROGRAME_HOME_PATH}/HaProxy/haproxy-1.4.24
front_ip=${PAAS_LOCAL_IP}
front_port=
# All service list, example: 192.168.10.123:8080,192.168.10.234:8081
back_urls=127.0.0.1:80
health_uri="/"
inter=10000
connection=1000
id=
balance="roundrobin"
sessionTimeout=30
protocal=http
contimeout=5000

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./haproxy-config.sh"
	echo "-h) display Usage"
	echo "-reqId) Request id"
	echo "-haproxyId) haproxy id"
	echo "-ip) haproxy ip"
	echo "-port) haproxy port"
	echo "-backUrls) back_urls. e.g: 192.168.2.21:8080,192.168.2.22:8088"
	echo "-healthUrl) health_uri. to check haproxy is alived"
	echo "-urlHealthCheckInterval) timeout"
	echo "-maxConnectionSize) connect count"
	echo "-balance) balance"
	echo "-sessionTimeout) user session timeout"
	echo "-protocal) haproxy protocal: http / tcp"
	echo "-contimeout) haproxy connection timeout"
}

# Parse execute arguments if has
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-haproxyId)  id=$2;shift 2;;
		-ip) front_ip=$2;shift 2;;
		-port) front_port=$2;shift 2;;
		-backUrls) back_urls=$2;shift 2;;
		-healthUrl) health_uri=$2;shift 2;;
		-urlHealthCheckInterval) inter=$2;shift 2;;
		-maxConnectionSize) connection=$2;shift 2;;
		-protocal) protocal=$2;shift 2;;
		-contimeout) contimeout=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	#¡¡HaProxy configuration file
	config=${haproxyHome}/conf/${id}.haproxy
	# Delete old configuration file
	if [ -f "${config}" ]; then
		rm "${config}"
	fi
	
	# If configuration direction not exists, then create it
	if [ ! -d ${haproxyHome}/conf ]; then
		mkdir -p ${haproxyHome}/conf
	fi
	
	touch ${config}
	echo "" > "${config}"

	echo "global" >> "${config}"
	echo "	maxconn ${connection}" >> "${config}"
	echo "	daemon" >> "${config}"

	echo "defaults" >> "${config}"
	echo "	log 	global" >> "${config}"
	echo "	mode 	${protocal}" >> "${config}"
	echo "	contimeout 	${contimeout}" >> "${config}"
	echo "	clitimeout 	50000" >> "${config}"
	echo "	srvtimeout 	50000" >> "${config}"

	echo "listen ${id}" >> "${config}"
	echo "	bind *:${front_port}" >> "${config}"
	echo "	maxconn ${connection}" >> "${config}"
	echo "	balance ${balance}" >> "${config}"
	echo "	appsession JSESSIONID len 128 timeout ${sessionTimeout}m request-learn" >> "${config}"
	echo "	mode ${protocal}" >> "${config}"
	echo "	option httpchk HEAD $health_uri" >> "${config}"

	url_array=(`echo $back_urls | tr "," "\n"`)
	index=1
	for url in ${url_array[@]}; do
	 		echo "	server server_$index $url check inter ${inter}" >> "${config}"
	        (( index ++ ))
	done

	_return "Generate HaProxy ${id} configuration file [${config}] success."
	_success
}

# import template script
source ${BIN_HOME_PATH}/Common/bin/template.sh