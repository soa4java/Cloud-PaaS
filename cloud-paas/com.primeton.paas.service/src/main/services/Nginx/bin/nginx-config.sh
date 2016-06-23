#!/bin/bash

#
# @Deprecated
#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
program_home=${PROGRAME_HOME_PATH}/Nginx
nginxHome=${program_home}/nginx-1.4.4 
nginxId=
# Example
# hosts=10001,10002,10003
# haproxyUrls=192.168.100.11:8000,192.168.100.12:8001*192.168.100.11:8001*192.168.100.11:8002,192.168.100.12:8002
# serverNames=test.primeton.com,bug.primeton.com,erp.primeton.com
hosts=
haproxyUrls=
serverNames=
port=80
# equal CPU core number [best configuration]
worker_processes=1
# worker processor connection pool size [default 1024]
# connection_sum = ${worker_processes} * ${worker_connections}
worker_connections=1024
# keepalive timeout (second) [Nginx support long connection]
keepalive_timeout=65
# types_hash_max_size
types_hash_max_size=2048
# allow access nginx monitor hosts
allow_access_hosts=${PAAS_LOCAL_IP}
# client_max_body_size (MB)
client_max_body_size=100

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage: ./nginx-config.sh"
	echo "-h) Print help"
	echo "-reqId) Request id"
	echo "-nginxId) Nginx id"
	echo "-hosts) all application's host of a nginx"
	echo "-haproxyUrls) all haproxy's urls"
	echo "-serverNames) all application's domain of a nginx"
	echo "-port) the visit port of nginx support"
	echo "-worker_processes) worker processes number, default 1."
	echo "-worker_connections) worker connections, default 1024"
	echo "-keepalive_timeout) keepalive timeout, default 65"
	echo "-allow_access_hosts) allow access nginx monitor hosts, example:192.168.100.10,192.168.100.12,192.168.100.100, default localhost"
	echo "-client_max_body_size) client max body size (MB), default 100"
	echo "-types_hash_max_size) types_hash_max_size, default 2048"
	echo ""
	echo "Example:"
	echo "./nginx-config.sh -reqId 0 -nginxId 10000 -hosts 10001,10002 -haproxyUrls 192.168.100.21:8080,192.168.100.22:8000*192.168.100.11:8000 -serverNames test.primeton.com,bug.primeton.com -port 80 -worker_processes 2 -worker_connections 2048 -keepalive_timeout 75 -allow_access_hosts 192.168.189.1,192.168.189.128 -client_max_body_size 1000"
}

# Parse execute arguments if has
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-nginxId) nginxId=$2;shift 2;;
		-hosts) hosts=$2;shift 2;;
		-haproxyUrls) haproxyUrls=$2;shift 2;;
		-serverNames) serverNames=$2;shift 2;;
		-port) port=$2;shift 2;;
		-worker_processes) worker_processes=$2;shift 2;;
		-worker_connections) worker_connections=$2;shift 2;;
		-keepalive_timeout) keepalive_timeout=$2;shift 2;;
		-allow_access_hosts) allow_access_hosts=$2;shift 2;;
		-client_max_body_size) client_max_body_size=$2;shift 2;;
		-types_hash_max_size) types_hash_max_size=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	config=${nginxHome}/config
	if [ ! -d ${config} ];then
		mkdir -p ${config}
	fi

	confFile=${config}/${nginxId}.conf
	logDir=${nginxHome}/logs
	pidFile=${logDir}/${nginxId}.pid
	accessLog=${logDir}/${nginxId}.access.log
	errorLog=${logDir}/${nginxId}.error.log

	echo confFile=${confFile}
	echo hosts=${hosts}
	echo haproxyUrls=${haproxyUrls}
	echo serverNames=${serverNames}
	
	# Delete Nginx old configuration file
	if [ -f ${confFile} ]; then
		rm ${confFile}
	fi

	# Create Nginx configuration file
	touch ${confFile}

	# worker processes
	echo "##" >> ${confFile}
	echo "## worker processes number" >> ${confFile}
	echo "##" >> ${confFile}
	echo "worker_processes ${worker_processes};" >> ${confFile}
	echo "## pid" >> ${confFile}
	echo "pid ${pidFile};" >> ${confFile}
	echo -e >> ${confFile}
	
	# worker_rlimit_nofile (greater than ${worker_connections})
	echo "##" >> ${confFile}
	echo "## worker connection number limit size" >> ${confFile}
	echo "##" >> ${confFile}
	echo "worker_rlimit_nofile ${worker_connections};" >> ${confFile}
	echo -e >> ${confFile}

	# events
	echo "##" >> ${confFile}
	echo "## events module" >> ${confFile}
	echo "##" >> ${confFile}
	echo "events {" >> ${confFile}
	echo "	worker_connections ${worker_connections};" >> ${confFile}
	echo "}" >> ${confFile}
	echo -e >> ${confFile}

	echo "##" >> ${confFile}
	echo "## http module" >> ${confFile}
	echo "##" >> ${confFile}
	echo "http {" >> ${confFile}
	
	echo "	##" >> ${confFile}
	echo "	## Basic settings" >> ${confFile}
	echo "	##" >> ${confFile}
	echo -e >> ${confFile}

	echo "	sendfile on;" >> ${confFile}
	echo "	tcp_nopush on;" >> ${confFile}
	echo "	tcp_nodelay on;" >> ${confFile}
	echo "	fastcgi_intercept_errors on;" >> ${confFile}
	echo -e >> ${confFile}
	
	echo "	keepalive_timeout ${keepalive_timeout};" >> ${confFile}
	echo "	types_hash_max_size ${types_hash_max_size};" >> ${confFile}
	echo -e >> ${confFile}

	echo "	include ${nginxHome}/conf/mime.types;" >> ${confFile}
	echo "	default_type application/octet-stream;" >> ${confFile}
	echo -e >> ${confFile}

	echo "	error_page 500 502 503 504 = /50x.html;" >> ${confFile}
	echo -e >> ${confFile}

	echo "	##" >> ${confFile}
	echo "	## log setting" >> ${confFile}
	echo "	##" >> ${confFile}
	# request log
	echo "	access_log ${accessLog};" >> ${confFile}
	# [ debug | info | notice | warn | error | crit ]
	echo "	error_log ${errorLog} info;" >> ${confFile}
	echo -e >> ${confFile}

	# upstream
	echo "	##" >> ${confFile}
	echo "	## upstream module" >> ${confFile}
	echo "	##" >> ${confFile}
	host_array=(`echo ${hosts} | tr "," "\n"`)
	urls_array=(`echo ${haproxyUrls} | tr "*" "\n"`)
	length=${#host_array[@]}
	url_length=
	for ((i = 0; i < length; i++)); do
		echo "	upstream ${host_array[$i]} {" >> ${confFile}

		url_array=(`echo ${urls_array[$i]} | tr "," "\n"`) 
		url_length=${#url_array[@]}
		for (( j = 0; j < $url_length; j++ )); do
			if [[ $j -eq 0 ]]; then
				echo "		server ${url_array[$j]};" >> ${confFile}
			else
				echo "		server ${url_array[$j]} backup;" >> ${confFile}
			fi
		done

		echo "	}" >> ${confFile}
	done

	echo -e >> ${confFile}

	# default server & monitor
	echo "	##" >> ${confFile}
	echo "	## default server && monitor" >> ${confFile}
	echo "	##" >> ${confFile}
	echo "	server {" >> ${confFile}
	echo "		listen ${port} default_server;" >> ${confFile}
	echo "		location /nginx_status {" >> ${confFile}
	echo "			stub_status on;" >> ${confFile}
	echo "			access_log off;" >> ${confFile}
	# echo "			allow ${PAAS_LOCAL_IP};" >> ${confFile}
	ips_length=0
	ip_array=(`echo ${allow_access_hosts} | tr "," "\n"`)
	ips_length=${#ip_array[@]}
	for ((i = 0; i < ${ips_length}; i++)); do
		echo "			allow ${ip_array[$i]};" >> ${confFile}
	done
	echo "			deny all;" >> ${confFile}
	echo "		}" >> ${confFile}
	echo "		location / {" >> ${confFile}
	echo "			return 404;" >> ${confFile}
	echo "		}" >> ${confFile}
	echo "	}" >> ${confFile}

	echo -e >> ${confFile}

	# server
	echo "	##" >> ${confFile}
	echo "	## server module" >> ${confFile}
	echo "	##" >> ${confFile}
	server_array=(`echo ${serverNames} | tr "," "\n"`)
	server_length=${#server_array[@]}
	for (( i = 0; i < ${server_length}; i++ )); do
		echo "	server {" >> ${confFile}
		echo "		server_name ${server_array[$i]};" >> ${confFile}
		echo "		listen ${port};" >> ${confFile}
		echo "		location / {" >> ${confFile}
		echo "			proxy_pass http://${host_array[$i]};" >> ${confFile}
		echo "			proxy_set_header Host "'$host;' >> ${confFile}
		echo "			client_max_body_size ${client_max_body_size}m;" >> ${confFile}
		echo "		}" >> ${confFile}
		echo "	}" >> ${confFile}
	done
	echo "}" >> ${confFile}
	
	# Validate configuration file
	result=`${nginxHome}/sbin/nginx -t -c ${confFile} 2>&1| grep "ok"`

	if [[ ${result} == "" ]];then
        	_return `${nginxHome}/sbin/nginx -t -c ${confFile} 2>&1`
        	_fail
        	exit 0
	fi

	_return "Generate nginx configuration file [${confFile}] success."
	_success
}

# import template script
source ${BIN_HOME_PATH}/Common/bin/template.sh