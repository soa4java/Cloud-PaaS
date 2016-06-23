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

package_home=${BIN_HOME_PATH}/Rabbitmq/packages
rabbitmq_package=${package_home}/source/rabbitmq-server-generic-unix-3.5.4.tar.gz
erlang_package=${package_home}/source/otp_src_R16B03-1.tar.gz

echo "rabbitmq_package = ${rabbitmq_package}"
echo "erlang_package = ${erlang_package}"

erlang_upack_src=${package_home}/temp/otp_src_R16B03-1
if [ ! -d ${package_home}/temp ]; then
	mkdir -p ${package_home}/temp
fi
# clean old source if exists
if [ -d ${erlang_upack_src} ]; then
	rm -rf ${erlang_upack_src}
fi

rabbitmq_program=${PROGRAME_HOME_PATH}/Rabbitmq/rabbitmq_server-3.5.4
erlang_program=${PROGRAME_HOME_PATH}/Rabbitmq/otp_R16B03

echo "rabbitmq_program = ${rabbitmq_program}"
echo "erlang_program = ${erlang_program}"

# if installed then exit
if [ -d ${rabbitmq_program} ]; then
	if [ -d ${erlang_program} ]; then
		echo "############################################################################################"
		echo "[WARN] rabbitmq_server has installed, please remove ${PROGRAME_HOME_PATH}/Rabbitmq first."
		echo "############################################################################################"
		exit 0
	fi
fi

mkdir -p ${PROGRAME_HOME_PATH}/Rabbitmq

cd ${PROGRAME_HOME_PATH}/Rabbitmq
# unpack rabbitmq_server
tar -zvxf ${rabbitmq_package}

# chmod +x scripts
chmod +x ${rabbitmq_program}/sbin/*

# install erlang
# go temp
cd ${package_home}/temp

# unpack erlang
tar -zvxf ${erlang_package}

# configure
if [ ! -d ${erlang_upack_src} ]; then
	echo "[`date`] Erlang source files erlang_upack_src not found, unpack ${erlang_package} error."
	exit 1
fi
cd ${erlang_upack_src}
./configure --prefix=${erlang_program}

# make
make

# make install
make install

# clean source
rm -rf ${erlang_upack_src}

if [ ! -d ${erlang_program} ]; then
	echo "[`date`] Can not install erlang from ${rabbitmq_package}."
	exit 1
fi

# modify sbin/rabbitmq-defaults
# configure erlang environment
sed -i 's/^ERL_DIR=/ERL_DIR='"`echo ${erlang_program//\//\\\/}`\/bin\/"'/g' ${rabbitmq_program}/sbin/rabbitmq-defaults

# enable manangement plugin
# ${rabbitmq_program}/sbin/rabbitmq-plugins enable rabbitmq_management

echo ""
echo "##############################################################"
echo "Finish install, see ${rabbitmq_program}."
echo "You need to create a user after start rabbitmq_server."
echo ""
echo "Example:"
echo "${rabbitmq_program}/sbin/rabbitmqctl  add_user paas 000000"
echo "${rabbitmq_program}/sbin/rabbitmqctl  set_user_tags paas administrator"
echo "More help see {rabbitmq.home}/sbin/rabbitmqctl help"
echo ""
echo "Enable rabbitmq_server plugins:"
echo "${rabbitmq_program}/sbin/rabbitmq-plugins enable rabbitmq_management"
echo "http://www.rabbitmq.com/"
echo "##############################################################"
echo ""