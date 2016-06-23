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

# my variables
program_home=${PROGRAME_HOME_PATH}/Keepalived/keepalived-1.2.8
keepalived=${program_home}/sbin/keepalived
fpid=${program_home}/sbin/keepalived.pid
fcfg=${program_home}/conf/keepalived.conf
cmd="${keepalived} -f ${fcfg} -p ${fpid}"


notificationEmail="admin@primeton.com"
notificationEmailFrom="admin@primeton.com"
smtpServer="mail.primeton.com"
smtpConnectTimeout=30
routerId="nginx_server_master"
vrrpScriptPath=${BIN_HOME_PATH}/Keepalived/bin/test.sh
vrrpScriptInterval=2
vrrpScriptWeight=2
vrrpState=MASTER
interface=eth0
virtualRouterId=51
mcastSrcIp=${PAAS_LOCAL_IP}
priority=100
advertInt=1
authType=PASS
authPass=1111
virtualIpAddress=192.168.100.100/24

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./start.sh"
    echo "-h) print help"
    echo "-reqId) request id"
    echo "-srvInstId) service instance id"
    echo "-srvDefName) service type"
    echo "-clusterName) cluster name"
	echo "-notificationEmail) Notification email"
	echo "-notificationEmailFrom) Notification email from"
	echo "-smtpServer) Smtp server"
	echo "-smtpConnectTimeout)"
	echo "-routerId)"
	echo "-vrrpScriptPath)"
	echo "-vrrpScriptInterval)"
	echo "-vrrpScriptWeight)"
	echo "-vrrpState)"
	echo "-interface)"
	echo "-virtualRouterId)"
	echo "-mcastSrcIp)"
	echo "-priority)"
	echo "-advertInt)"
	echo "-authType)"
	echo "-authPass)"
	echo "-virtualIpAddress)"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-notificationEmail) notificationEmail=$2;shift 2;;
		-notificationEmailFrom) notificationEmailFrom=$2;shift 2;;
		-smtpServer) smtpServer=$2;shift 2;;
		-smtpConnectTimeout) smtpConnectTimeout=$2;shift 2;;
		-routerId) routerId=$2;shift 2;;
		-vrrpScriptPath) vrrpScriptPath=$2;shift 2;;
		-vrrpScriptInterval) vrrpScriptInterval=$2;shift 2;;
		-vrrpScriptWeight) vrrpScriptWeight=$2;shift 2;;
		-vrrpState) vrrpState=$2;shift 2;;
		-interface) interface=$2;shift 2;;
		-virtualRouterId) virtualRouterId=$2;shift 2;;
		-mcastSrcIp) mcastSrcIp=$2;shift 2;;
		-priority) priority=$2;shift 2;;
		-advertInt) advertInt=$2;shift 2;;
		-authType) authType=$2;shift 2;;
		-authPass) authPass=$2;shift 2;;
		-virtualIpAddress) virtualIpAddress=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# check pid
	if [ -f ${fpid} ]; then
		pid=`cat ${fpid}`
		if [ ` ps -ef |grep ${pid} |grep -v "grep" | wc -l` -eq 0 ]; then
			rm ${fpid}
		else
			_pid ${pid}
			_return "Keepalived is already started."
			_success
			exit 0
		fi
	fi
	
	# Generate keepalived.conf
	if [ -f ${fcfg} ]; then
		rm ${fcfg}
	fi
	if [ ! -d ${program_home}/conf ]; then
		mkdir -p ${program_home}/conf
	fi
	
	touch ${fcfg}
	
	vipArray=(`echo ${virtualIpAddress} | tr "," "\n"`)
	vipNumber=${#vipArray[@]}
	
	# global_defs
	echo "global_defs {" >> ${fcfg}
	echo "	notification_email {" >> ${fcfg}
	echo "		${notificationEmail}" >> ${fcfg}
	echo "	}" >> ${fcfg}
	echo "	notification_email_from	${notificationEmailFrom}" >> ${fcfg}
	echo "	smtp_server	${smtpServer}" >> ${fcfg}
	echo "	smtp_connect_timeout	${smtpConnectTimeout}" >> ${fcfg}
	echo "	router_id	${routerId}" >> ${fcfg}
	echo "}" >> ${fcfg}
	
	# script
	echo "vrrp_script chk_http_port {" >> ${fcfg}
	echo "	script	${vrrpScriptPath}" >> ${fcfg}
	echo "	interval	${vrrpScriptInterval}" >> ${fcfg}
	echo "	weight	${vrrpScriptWeight}" >> ${fcfg}
	echo "}" >> ${fcfg}
	
	# vrrp_instance
	echo "vrrp_instance	VI_${INSTANCE_ID} {" >> ${fcfg}
	echo "	state	${vrrpState}" >> ${fcfg}
	echo "	interface	${interface}" >> ${fcfg}
	echo "	virtual_router_id	${virtualRouterId}" >> ${fcfg}
	echo "	mcast_src_ip	${mcastSrcIp}" >> ${fcfg}
	echo "	priority	${priority}" >> ${fcfg}
	echo "	advert_int	${advertInt}" >> ${fcfg}
	echo "	authentication {" >> ${fcfg}
	echo "		auth_type	${authType}" >> ${fcfg}
	echo "		auth_pass	${authPass}" >> ${fcfg}
	echo "	}" >> ${fcfg}
	echo "	track_script {" >> ${fcfg}
	echo "		chk_http_port" >> ${fcfg}
	echo "	}" >> ${fcfg}
	echo "	virtual_ipaddress {" >> ${fcfg}
	# echo "		${virtualIpAddress}" >> ${fcfg}
	for (( i=0; i<${vipNumber}; i++ )); do
		echo "		${vipArray[$i]}" >> ${fcfg}
	done
	echo "	}" >> ${fcfg}
	echo "}" >> ${fcfg}
	
	# Execute start keepalived
	${cmd}
	sleep 1
	
	# read pid
	if [ -f ${fpid} ]; then 
		pid=`cat ${fpid}`
	else
		_fail
		exit 0
	fi
	_pid ${pid}
	_success
	_return "keepalived successfully started."
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh