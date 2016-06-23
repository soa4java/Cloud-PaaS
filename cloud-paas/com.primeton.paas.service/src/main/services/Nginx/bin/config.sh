#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#
# Generate nginx configuration file.
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
program_home=${PROGRAME_HOME_PATH}/Nginx
nginxHome=${program_home}/nginx-1.4.4
sslCertificatePath=${nginxHome}/certificates
nginxId=
# Example
# hosts=10001,10002,10003
# haproxyUrls=192.168.100.11:8000,192.168.100.12:8001*192.168.100.11:8001*192.168.100.11:8002,192.168.100.12:8002
# (ip:port,ip:port...*ip:port,ip:port...)
# serverNames=test.primeton.com,bug.primeton.com,erp.primeton.com

#default_certificate=${nginxHome}/conf/default.crt
#default_certificate_key=${nginxHome}/conf/default_nopass.key
#default_ca_certificate=${nginxHome}/conf/client.crt

# HTTP + HTTPS
hosts=
haproxyUrls=
serverNames=
certificates=
certificateKeys=

# HTTP
httpHosts=
httpHaproxyUrls=
httpServerNames=

# HTTPS
httpsHosts=
httpsHaproxyUrls=
httpsServerNames=
httpsCertificates=
httpsCertificateKeys=

# mHTTPS
mHttpsHosts=
mHttpsHaproxyUrls=
mHttpsServerNames=
mHttpsCertificates=
mHttpsCertificateKeys=
mHttpsCaCertificates=
mHttpsSslLevels=

# mHTTP + mHTTPS
mHosts=
mHaproxyUrls=
mServerNames=
mCertificates=
mCertificateKeys=
mCaCertificates=
mSslLevels=

port=80
sslPort=443

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
	echo "Usage: ./config.sh"
	echo "-h) Print help"
	echo "-reqId) Request id"
	echo "-nginxId) Nginx id"
	
	echo -e "\n---------------------------------------------------------------"
	echo "-hosts) [HTTP+HTTPS] all application's host of a nginx"
	echo "-haproxyUrls) [HTTP+HTTPS] all haproxy's urls"
	echo "-serverNames) [HTTP+HTTPS] all application's domain of a nginx"
	echo "-certificates) [HTTP+HTTPS] all application's certificates"
	echo "-certificateKeys) [HTTP+HTTPS] all application's certificate keys"
	
	echo -e "\n---------------------------------------------------------------"
	echo "-httpHosts) [HTTP] all application's host of a nginx"
	echo "-httpHaproxyUrls) [HTTP] all haproxy's urls"
	echo "-httpServerNames) [HTTP] all application's domain of a nginx"
	
	echo -e "\n---------------------------------------------------------------"
	echo "-httpsHosts) [HTTPS] all application's host of a nginx"
	echo "-httpsHaproxyUrls) [HTTPS] all haproxy's urls"
	echo "-httpsServerNames) [HTTPS] all application's domain of a nginx"
	echo "-httpCertificates) [HTTPS] all application's certificates"
	echo "-httpCertificateKeys) [HTTPS] all application's certificate keys"
    
	echo -e "\n---------------------------------------------------------------"
	echo "-mHttpsHosts) [mHTTPS] all application's host of a nginx"
	echo "-mHttpsHaproxyUrls) [mHTTPS] all haproxy's urls"
	echo "-mHttpsServerNames) [mHTTPS] all application's domain of a nginx"
	echo "-mHttpsCertificates) [mHTTPS] all application's certificates"
	echo "-mHttpsCertificateKeys) [mHTTPS] all application's certificate keys"	
	echo "-mHttpsCaCertificates) [mHTTPS] all application's certificate keys"
	echo "-mHttpsSslLevels) [mHTTPS] all application's ssl level"
	
	echo -e "\n---------------------------------------------------------------"
	echo "-mHosts) [mHTTP+mHTTPS] all application's host of a nginx"
	echo "-mHaproxyUrls) [mHTTP+mHTTPS] all haproxy's urls"
	echo "-mServerNames) [mHTTP+mHTTPS] all application's domain of a nginx"
	echo "-mCertificates) [mHTTP+mHTTPS] all application's certificates"
	echo "-mCertificateKeys) [mHTTP+mHTTPS] all application's certificate keys"	
	echo "-mCaCertificates) [mHTTP+mHTTPS] all application's certificate keys"
	echo "-mSslLevels) [mHTTP+mHTTPS] all application's ssl Levels"
	
	echo -e "\n---------------------------------------------------------------"
	echo "-port) the visit http port of nginx support"
	echo "-sslPort) the visit https port of nginx support"
	
	echo "-sslCertificatePath) SSL Certificate Root Path"
	echo "-worker_processes) worker processes number, default 1."
	echo "-worker_connections) worker connections, default 1024"
	echo "-keepalive_timeout) keepalive timeout, default 65"
	echo "-allow_access_hosts) allow access nginx monitor hosts, example:192.168.100.10,192.168.100.12,192.168.100.100, default localhost"
	echo "-client_max_body_size) client max body size (MB), default 100"
	echo "-types_hash_max_size) types_hash_max_size, default 2048"
	
	echo -e "\n---------------------------------------------------------------"
	echo "Example:"
	echo "./config.sh -reqId 0 -nginxId 10000 -hosts 10001,10002 -haproxyUrls 192.168.100.21:8080,192.168.100.22:8000*192.168.100.11:8000 -serverNames test.primeton.com,bug.primeton.com -port 80 -worker_processes 2 -worker_connections 2048 -keepalive_timeout 75 -allow_access_hosts 192.168.189.1,192.168.189.128 -client_max_body_size 1000"
}

# Parse execute arguments if has
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-nginxId) nginxId=$2;shift 2;;
		
		# [HTTP + HTTPS]
		-hosts) hosts=$2;shift 2;;
		-haproxyUrls) haproxyUrls=$2;shift 2;;
		-serverNames) serverNames=$2;shift 2;;
		-certificates) certificates=$2;shift 2;;
		-certificateKeys) certificateKeys=$2;shift 2;;
		
		# [HTTP]
		-httpHosts) httpHosts=$2;shift 2;;
		-httpHaproxyUrls) httpHaproxyUrls=$2;shift 2;;
		-httpServerNames) httpServerNames=$2;shift 2;;
		
		# [HTTPS]
		-httpsHosts) httpsHosts=$2;shift 2;;
		-httpsHaproxyUrls) httpsHaproxyUrls=$2;shift 2;;
		-httpsServerNames) httpsServerNames=$2;shift 2;;
		-httpsCertificates) httpsCertificates=$2;shift 2;;
		-httpsCertificateKeys) httpsCertificateKeys=$2;shift 2;; 
        
		# [mHTTPS]
		-mHttpsHosts) mHttpsHosts=$2;shift 2;;
		-mHttpsHaproxyUrls) mHttpsHaproxyUrls=$2;shift 2;;
		-mHttpsServerNames) mHttpsServerNames=$2;shift 2;;
		-mHttpsCertificates) mHttpsCertificates=$2;shift 2;;
		-mHttpsCertificateKeys) mHttpsCertificateKeys=$2;shift 2;;
		-mHttpsCaCertificates) mHttpsCaCertificates=$2;shift 2;;
		-mHttpsSslLevels) mHttpsSslLevels=$2;shift 2;;
        
		# [mHTTP + mHTTPS]
		-mHosts) mHosts=$2;shift 2;;
		-mHaproxyUrls) mHaproxyUrls=$2;shift 2;;
		-mServerNames) mServerNames=$2;shift 2;;
		-mCertificates) mCertificates=$2;shift 2;;
		-mCertificateKeys) mCertificateKeys=$2;shift 2;;
		-mCaCertificates) mCaCertificates=$2;shift 2;;   
		-mSslLevels) mSslLevels=$2;shift 2;;   
		
		# [HTTP]
		-port) port=$2;shift 2;;
		# [HTTPS]
		-sslPort) sslPort=$2;shift 2;;
		
		-sslCertificatePath) sslCertificatePath=$2;shift 2;;
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
	if [ ! -d ${sslCertificatePath} ]; then
		mkdir -p ${sslCertificatePath}
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

	################################################################
	## events
	################################################################
	echo "##" >> ${confFile}
	echo "## events module" >> ${confFile}
	echo "##" >> ${confFile}
	echo "events {" >> ${confFile}
	echo "	worker_connections ${worker_connections};" >> ${confFile}
	echo "}" >> ${confFile}
	echo -e >> ${confFile}

	################################################################
	## http module
	################################################################
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

	################################################################
	## log
	################################################################
	echo "	##" >> ${confFile}
	echo "	## log setting" >> ${confFile}
	echo "	##" >> ${confFile}
	# request log
	echo "	access_log ${accessLog};" >> ${confFile}
	# [ debug | info | notice | warn | error | crit ]
	echo "	error_log ${errorLog} info;" >> ${confFile}

	################################################################
	## upstream [HTTP + HTTPS]
	################################################################
	echo -e "\n" >> ${confFile}
	echo "	##" >> ${confFile}
	echo "	## upstream module" >> ${confFile}
	echo "	##" >> ${confFile}
	echo -e >> ${confFile}
	echo "	##" >> ${confFile}
	echo "	## HTTP + HTTPS" >> ${confFile}
	echo "	##" >> ${confFile}
	
	host_array=(`echo ${hosts} | tr "," "\n"`)
	urls_array=(`echo ${haproxyUrls} | tr "*" "\n"`)
	length=${#host_array[@]}
	url_length=
	for ((i = 0; i < $length; i++)); do
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
	
	################################################################
	## upstream [HTTP]
	################################################################
	echo -e "\n" >> ${confFile}
	echo "	##" >> ${confFile}
	echo "	## HTTP" >> ${confFile}
	echo "	##" >> ${confFile}
	
	http_host_array=(`echo ${httpHosts} | tr "," "\n"`)
	http_urls_array=(`echo ${httpHaproxyUrls} | tr "*" "\n"`)
	http_length=${#http_host_array[@]}
	http_url_length=
	for ((i = 0; i < $http_length; i++)); do
		echo "	upstream ${http_host_array[$i]} {" >> ${confFile}

		http_url_array=(`echo ${http_urls_array[$i]} | tr "," "\n"`) 
		http_url_length=${#http_url_array[@]}
		for (( j = 0; j < $http_url_length; j++ )); do
			if [[ $j -eq 0 ]]; then
				echo "		server ${http_url_array[$j]};" >> ${confFile}
			else
				echo "		server ${http_url_array[$j]} backup;" >> ${confFile}
			fi
		done

		echo "	}" >> ${confFile}
	done

	################################################################
	## upstream [HTTPS]
	################################################################
	echo -e "\n" >> ${confFile}
	echo "	##" >> ${confFile}
	echo "	## HTTPS" >> ${confFile}
	echo "	##" >> ${confFile}
	
	https_host_array=(`echo ${httpsHosts} | tr "," "\n"`)
	https_urls_array=(`echo ${httpsHaproxyUrls} | tr "*" "\n"`)
	https_length=${#https_host_array[@]}
	https_url_length=
	for ((i = 0; i < $https_length; i++)); do
		echo "	upstream ${https_host_array[$i]} {" >> ${confFile}

		https_url_array=(`echo ${https_urls_array[$i]} | tr "," "\n"`) 
		https_url_length=${#https_url_array[@]}
		for (( j = 0; j < $https_url_length; j++ )); do
			if [[ $j -eq 0 ]]; then
				echo "		server ${https_url_array[$j]};" >> ${confFile}
			else
				echo "		server ${https_url_array[$j]} backup;" >> ${confFile}
			fi
		done

		echo "	}" >> ${confFile}
	done
    
    
    	################################################################
	## upstream [mHTTPS]
	################################################################
	echo -e "\n" >> ${confFile}
	echo "	##" >> ${confFile}
	echo "	## mHTTPS" >> ${confFile}
	echo "	##" >> ${confFile}
	
	m_https_host_array=(`echo ${mHttpsHosts} | tr "," "\n"`)
	m_https_urls_array=(`echo ${mHttpsHaproxyUrls} | tr "*" "\n"`)
	m_https_length=${#m_https_host_array[@]}
	m_https_url_length=
	for ((i = 0; i < $m_https_length; i++)); do
		echo "	upstream ${m_https_host_array[$i]} {" >> ${confFile}

		m_https_url_array=(`echo ${m_https_urls_array[$i]} | tr "," "\n"`) 
		m_https_url_length=${#m_https_url_array[@]}
		for (( j = 0; j < $m_https_url_length; j++ )); do
			if [[ $j -eq 0 ]]; then
				echo "		server ${m_https_url_array[$j]};" >> ${confFile}
			else
				echo "		server ${m_https_url_array[$j]} backup;" >> ${confFile}
			fi
		done

		echo "	}" >> ${confFile}
	done
	
        ################################################################
	## upstream [mHTTP + mHTTPS]
	################################################################
	echo -e >> ${confFile}
	echo "	##" >> ${confFile}
	echo "	## mHTTP + mHTTPS" >> ${confFile}
	echo "	##" >> ${confFile}
	
	m_host_array=(`echo ${mHosts} | tr "," "\n"`)
	m_urls_array=(`echo ${mHaproxyUrls} | tr "*" "\n"`)
	m_length=${#m_host_array[@]}
	m_url_length=
	for ((i = 0; i < $m_length; i++)); do
		echo "	upstream ${m_host_array[$i]} {" >> ${confFile}

		m_url_array=(`echo ${m_urls_array[$i]} | tr "," "\n"`) 
		m_url_length=${#m_url_array[@]}
		for (( j = 0; j < $m_url_length; j++ )); do
			if [[ $j -eq 0 ]]; then
				echo "		server ${m_url_array[$j]};" >> ${confFile}
			else
				echo "		server ${m_url_array[$j]} backup;" >> ${confFile}
			fi
		done

		echo "	}" >> ${confFile}
	done
	
	################################################################
	## Default server configuration
	## Nginx monitor configuration
	################################################################
	echo -e "\n\n" >> ${confFile}
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

	echo -e "\n\n" >> ${confFile}

	################################################################
	## server module
	################################################################
	echo "	##" >> ${confFile}
	echo "	## server module" >> ${confFile}
	echo "	##" >> ${confFile}
	
	################################################################
	## server module [HTTP + HTTPS]
	################################################################
	echo -e "\n" >> ${confFile}
	echo "	##" >> ${confFile}
	echo "	## HTTP + HTTPS" >> ${confFile}
	echo "	##" >> ${confFile}
	server_array=(`echo ${serverNames} | tr "," "\n"`)
	server_length=${#server_array[@]}
	certificate_array=(`echo ${certificates} | tr "," "\n"`)
	certificateKey_array=(`echo ${certificateKeys} | tr "," "\n"`)
	
	for (( i = 0; i < ${server_length}; i++ )); do
		echo "	server {" >> ${confFile}
		echo "		server_name ${server_array[$i]};" >> ${confFile}
		echo "		listen ${port};" >> ${confFile}
		
		certificate=${certificate_array[$i]} #${sslCertificatePath}/${certificate_array[$i]}
        certificate_key=${certificateKey_array[$i]} #${sslCertificatePath}/${certificateKey_array[$i]}
		if [  -f ${certificate} ] && [  -f ${certificate_key} ] ; then
			echo "		listen ${sslPort} ssl;" >> ${confFile}
			echo "		ssl_certificate			${certificate};" >> ${confFile}
			echo "		ssl_certificate_key		${certificate_key};" >> ${confFile}
			echo "		ssl_protocols			SSLv3 TLSv1 TLSv1.1 TLSv1.2;" >> ${confFile}
			echo "		ssl_ciphers			HIGH:!aNULL:!MD5;" >> ${confFile}
		fi
		
		echo -e "\n" >> ${confFile}
		echo "		location / {" >> ${confFile}
		echo "			proxy_pass http://${host_array[$i]};" >> ${confFile}
		echo "			proxy_set_header Host "'$host;' >> ${confFile}
		echo "			client_max_body_size ${client_max_body_size}m;" >> ${confFile}
		echo "		}" >> ${confFile}
		echo "	}" >> ${confFile}
	done
	
	################################################################
	## server module [HTTP]
	################################################################
	echo -e "\n" >> ${confFile}
	echo "	##" >> ${confFile}
	echo "	## HTTP" >> ${confFile}
	echo "	##" >> ${confFile}
	http_server_array=(`echo ${httpServerNames} | tr "," "\n"`)
	http_server_length=${#http_server_array[@]}
	for (( i = 0; i < ${http_server_length}; i++ )); do
		echo "	server {" >> ${confFile}
		echo "		server_name ${http_server_array[$i]};" >> ${confFile}
		echo "		listen ${port};" >> ${confFile}
		echo "		location / {" >> ${confFile}
		echo "			proxy_pass http://${http_host_array[$i]};" >> ${confFile}
		echo "			proxy_set_header Host "'$host;' >> ${confFile}
		echo "			client_max_body_size ${client_max_body_size}m;" >> ${confFile}
		echo "		}" >> ${confFile}
		echo "	}" >> ${confFile}
	done
	
	################################################################
	## server module [HTTPS]
	################################################################
	echo -e "\n" >> ${confFile}
	echo "	##" >> ${confFile}
	echo "	## HTTPS" >> ${confFile}
	echo "	##" >> ${confFile}
	https_server_array=(`echo ${httpsServerNames} | tr "," "\n"`)
	https_server_length=${#https_server_array[@]}
	https_certificate_array=(`echo ${httpsCertificates} | tr "," "\n"`)
	https_certificateKey_array=(`echo ${httpsCertificateKeys} | tr "," "\n"`)
	
	for (( i = 0; i < ${https_server_length}; i++ )); do

		https_certificate=${https_certificate_array[$i]} #${sslCertificatePath}/${https_certificate_array[$i]}
		https_certificate_key=${https_certificateKey_array[$i]} #${sslCertificatePath}/${https_certificateKey_array[$i]}
		if [  -f ${https_certificate} ] && [  -f ${https_certificate_key} ] ; then
			echo "	server {" >> ${confFile}
			echo "		server_name ${https_server_array[$i]};" >> ${confFile}
			echo "		listen ${sslPort} ssl;" >> ${confFile}
			echo "		ssl_certificate			${https_certificate};" >> ${confFile}
			echo "		ssl_certificate_key		${https_certificate_key};" >> ${confFile}
			echo "		ssl_protocols			SSLv3 TLSv1 TLSv1.1 TLSv1.2;" >> ${confFile}
			echo "		ssl_ciphers			HIGH:!aNULL:!MD5;" >> ${confFile}
			echo -e >> ${confFile}
			echo "		location / {" >> ${confFile}
			echo "			proxy_pass http://${https_host_array[$i]};" >> ${confFile}
			echo "			proxy_set_header Host "'$host;' >> ${confFile}
			echo "			client_max_body_size ${client_max_body_size}m;" >> ${confFile}
			echo "		}" >> ${confFile}
			echo "	}" >> ${confFile}
		fi		

	done
	
	
	################################################################
	## server module [mHTTPS]
	################################################################
	echo -e "\n" >> ${confFile}
	echo "	##" >> ${confFile}
	echo "	## mHTTPS" >> ${confFile}
	echo "	##" >> ${confFile}
	m_https_server_array=(`echo ${mHttpsServerNames} | tr "," "\n"`)
	m_https_server_length=${#m_https_server_array[@]}
	m_https_certificate_array=(`echo ${mHttpsCertificates} | tr "," "\n"`)
	m_https_certificateKey_array=(`echo ${mHttpsCertificateKeys} | tr "," "\n"`)
	m_https_ca_certificate_array=(`echo ${mHttpsCaCertificates} | tr "," "\n"`)
	m_https_ca_sslLevels_array=(`echo ${mHttpsSslLevels} | tr "," "\n"`)
    
    
	for (( i = 0; i < ${m_https_server_length}; i++ )); do

		m_https_certificate=${m_https_certificate_array[$i]} #${sslCertificatePath}/${m_https_certificate_array[$i]}
		m_https_certificate_key=${m_https_certificateKey_array[$i]} #${sslCertificatePath}/${https_certificateKey_array[$i]}
        m_https_ca_certificate=${m_https_ca_certificate_array[$i]} 
        m_https_ca_sslLevels=${m_https_ca_sslLevels_array[$i]} 
		if [  -f ${m_https_certificate} ] && [  -f ${m_https_certificate_key} ] && [  -f ${m_https_ca_certificate} ]; then
			echo "	server {" >> ${confFile}
			echo "		server_name ${m_https_server_array[$i]};" >> ${confFile}
			echo "		listen ${sslPort} ssl;" >> ${confFile}
			
			echo "		ssl_certificate			${m_https_certificate};" >> ${confFile}
			echo "		ssl_certificate_key		${m_https_certificate_key};" >> ${confFile}
			echo "		ssl_protocols			SSLv3 TLSv1 TLSv1.1 TLSv1.2;" >> ${confFile}
			echo "		ssl_ciphers			HIGH:!aNULL:!MD5;" >> ${confFile}
				   
			echo -e >> ${confFile}
			echo "		#CA                             ">> ${confFile}
			echo "		ssl_client_certificate		${m_https_ca_certificate};" >> ${confFile}
			echo "		ssl_verify_client		on;" >> ${confFile}
			echo "		ssl_verify_depth		${m_https_ca_sslLevels};" >> ${confFile}

			echo -e >> ${confFile}
			echo "		location / {" >> ${confFile}
			echo "			proxy_pass http://${m_https_host_array[$i]};" >> ${confFile}
			echo "			proxy_set_header Host "'$host;' >> ${confFile}
			echo "			client_max_body_size ${client_max_body_size}m;" >> ${confFile}
			echo "		}" >> ${confFile}
			echo "	}" >> ${confFile}
			
		fi
                
	done	
	
	################################################################
	## server module [mHTTP + mHTTPS]
	################################################################
	echo -e "\n" >> ${confFile}
	echo "	##" >> ${confFile}
	echo "	## mHTTP + mHTTPS" >> ${confFile}
	echo "	##" >> ${confFile}
	m_server_array=(`echo ${mServerNames} | tr "," "\n"`)
	m_server_length=${#m_server_array[@]}
	m_certificate_array=(`echo ${mCertificates} | tr "," "\n"`)
	m_certificateKey_array=(`echo ${mCertificateKeys} | tr "," "\n"`)
    m_ca_certificate_array=(`echo ${mCaCertificates} | tr "," "\n"`)
    m_ca_sslLevel_array=(`echo ${mSslLevels} | tr "," "\n"`)
	
	for (( i = 0; i < ${m_server_length}; i++ )); do
		echo "	server {" >> ${confFile}
		echo "		server_name ${m_server_array[$i]};" >> ${confFile}
		echo "		listen ${port};" >> ${confFile}
		
		m_certificate=${m_certificate_array[$i]} #${sslCertificatePath}/${certificate_array[$i]}
		m_certificate_key=${m_certificateKey_array[$i]} #${sslCertificatePath}/${certificateKey_array[$i]}
        m_ca_certificate=${m_ca_certificate_array[$i]} #${sslCertificatePath}/${certificate_array[$i]}
        m_ca_sslLevel=${m_ca_sslLevel_array[$i]} #${sslLevel}/${sslLevel_array[$i]}
        
		if [  -f ${m_certificate} ] && [  -f ${m_certificate_key} ] && [  -f ${m_ca_certificate} ] ; then
			echo "		listen ${sslPort} ssl;" >> ${confFile}
			echo "		ssl_certificate			${m_certificate};" >> ${confFile}
			echo "		ssl_certificate_key		${m_certificate_key};" >> ${confFile}
			echo "		ssl_protocols			SSLv3 TLSv1 TLSv1.1 TLSv1.2;" >> ${confFile}
			echo "		ssl_ciphers			HIGH:!aNULL:!MD5;" >> ${confFile}
        
            echo -e >> ${confFile}
			echo "		#CA                             ">> ${confFile}
            echo "		ssl_client_certificate		${m_ca_certificate};" >> ${confFile}
            echo "		ssl_verify_client		on;" >> ${confFile}
            echo "		ssl_verify_depth		${m_ca_sslLevel};" >> ${confFile}
		fi
        		
                echo -e >> ${confFile}
		echo "		location / {" >> ${confFile}
		echo "			proxy_pass http://${m_host_array[$i]};" >> ${confFile}
		echo "			proxy_set_header Host "'$host;' >> ${confFile}
		echo "			client_max_body_size ${client_max_body_size}m;" >> ${confFile}
		echo "		}" >> ${confFile}
		echo "	}" >> ${confFile}
	done
    
    
    	# end
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