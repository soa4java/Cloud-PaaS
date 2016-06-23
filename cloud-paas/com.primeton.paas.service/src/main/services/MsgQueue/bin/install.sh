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

package_home=${BIN_HOME_PATH}/MsgQueue/packages
rabbitmq_package=${package_home}/source/rabbitmq-server-generic-unix-3.5.4.tar.gz
erlang_package=${package_home}/source/otp_src_R16B03-1.tar.gz

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./install.sh"
    echo "-h) Print help"
    echo "-reqId) Request id"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-arg0) arg0=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
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

	rabbitmq_program=${PROGRAME_HOME_PATH}/MsgQueue/rabbitmq_server-3.5.4
	erlang_program=${PROGRAME_HOME_PATH}/MsgQueue/otp_R16B03

	echo "rabbitmq_program = ${rabbitmq_program}"
	echo "erlang_program = ${erlang_program}"
	
	# clean
	if [ -d ${PROGRAME_HOME_PATH}/MsgQueue ]; then
		rm -rf ${PROGRAME_HOME_PATH}/MsgQueue
	fi

	mkdir -p ${PROGRAME_HOME_PATH}/MsgQueue

	cd ${PROGRAME_HOME_PATH}/MsgQueue
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
		_error "[`date`] Erlang source files erlang_upack_src not found, unpack ${erlang_package} error."
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
		_error "[`date`] Can not install erlang from ${rabbitmq_package}."
		exit 1
	fi

	# modify sbin/rabbitmq-defaults
	# configure erlang environment
	sed -i 's/^ERL_DIR=/ERL_DIR='"`echo ${erlang_program//\//\\\/}`\/bin\/"'/g' ${rabbitmq_program}/sbin/rabbitmq-defaults

	# enable management
	# ${rabbitmq_program}/sbin/rabbitmq-server -detached
	# sleep 5
	# ${rabbitmq_program}/sbin/rabbitmq-plugins enable rabbitmq_management
	# ${rabbitmq_program}/sbin/rabbitmqctl stop
	
	# clean stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}	
			
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh