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

rabbitmq_program=${PROGRAME_HOME_PATH}/Rabbitmq/rabbitmq_server-3.5.4

if [ `ps -ef | grep ${rabbitmq_program}/sbin | grep -v grep | wc -l` -eq 0 ]; then
	echo "[INFO] rabbitmq_server is not running. Please start rabbitmq_server before execute this script."
	exit 0
fi

# Please see https://www.rabbitmq.com/documentation.html

USERNAME=paas
PASSWORD=000000

echo "##################################################"
echo "USERNAME=${USERNAME}"
echo "PASSWORD=${PASSWORD}"
echo "PORT=5672"
echo "MANAGER_PORT=15672"
echo "##################################################"

${rabbitmq_program}/sbin/rabbitmq-plugins enable rabbitmq_management

${rabbitmq_program}/sbin/rabbitmqctl add_user ${USERNAME} ${PASSWORD}
${rabbitmq_program}/sbin/rabbitmqctl set_user_tags ${USERNAME} administrator
${rabbitmq_program}/sbin/rabbitmqctl list_users

${rabbitmq_program}/sbin/rabbitmqctl add_vhost /paas/log
${rabbitmq_program}/sbin/rabbitmqctl add_vhost /paas/monitor
${rabbitmq_program}/sbin/rabbitmqctl add_vhost /paas/agent
${rabbitmq_program}/sbin/rabbitmqctl add_vhost /paas/mail
${rabbitmq_program}/sbin/rabbitmqctl list_vhosts

${rabbitmq_program}/sbin/rabbitmqctl set_permissions -p /paas/agent ${USERNAME} ".*" ".*" ".*"
${rabbitmq_program}/sbin/rabbitmqctl set_permissions -p /paas/agent guest ".*" ".*" ".*"
${rabbitmq_program}/sbin/rabbitmqctl list_permissions -p /paas/agent

${rabbitmq_program}/sbin/rabbitmqctl set_permissions -p /paas/log ${USERNAME} ".*" ".*" ".*"
${rabbitmq_program}/sbin/rabbitmqctl set_permissions -p /paas/log guest ".*" ".*" ".*"
${rabbitmq_program}/sbin/rabbitmqctl list_permissions -p /paas/log

${rabbitmq_program}/sbin/rabbitmqctl set_permissions -p /paas/monitor ${USERNAME} ".*" ".*" ".*"
${rabbitmq_program}/sbin/rabbitmqctl set_permissions -p /paas/monitor guest ".*" ".*" ".*"
${rabbitmq_program}/sbin/rabbitmqctl list_permissions -p /paas/monitor

${rabbitmq_program}/sbin/rabbitmqctl set_permissions -p /paas/mail ${USERNAME} ".*" ".*" ".*"
${rabbitmq_program}/sbin/rabbitmqctl set_permissions -p /paas/mail guest ".*" ".*" ".*"
${rabbitmq_program}/sbin/rabbitmqctl list_permissions -p /paas/mail

echo "#############################################################"
echo "Please see https://www.rabbitmq.com/documentation.html"
echo "#############################################################"
