#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

# root
HOME_PATH=$(cd $(dirname ${0})/../../..; pwd)
# bin
BIN_HOME_PATH=$(cd $(dirname ${0})/../..; pwd)
# programs
PROGRAME_HOME_PATH=${HOME_PATH}/programs
# workspace
WORKSPACE_HOME_PATH=${HOME_PATH}/workspace
# temp
TEMP_HOME_PATH=${HOME_PATH}/temp
# templates
TEMPLATE_HOME_PATH=${HOME_PATH}/templates

# Installation source [source/binary]
# Example: 
# haproxy-1.4.24.tar.gz (source, download from provider) 
# -> unpackage -> configure -> make -> package -> end (binary)
# Constants [source/binary]
PACKAGE_SRC=source
PACKAGE_BIN=binary
# Default
INSTALL_SRC=${PACKAGE_BIN}
# FIXME : Fast install, unpack to target directory is ok (not execute make install command).

# if use build mode ${INSTALL_SRC} must be source
BUILD_MODE=false

# mkdir
mkdir -p ${HOME_PATH}/{programs,temp,templates,workspace}

# JAVA_HOME
if test -z "${JAVA_HOME}"; then
	JAVA_HOME=${PROGRAME_HOME_PATH}/JDK/jdk1.6.0_45
	if [ ! -d ${JAVA_HOME} ]; then
		echo "[WARN] JAVA_HOME=${JAVA_HOME}, not exists."
	fi
fi

# local host
PAAS_LOCAL_IP=
# Use network device eth0 as default
# Please fix here by product installation environment
if test -z "${PAAS_LOCAL_IP}"; then
	__OS=`uname`
	case $__OS in
		Linux) PAAS_LOCAL_IP=`ifconfig eth0 | grep 'inet addr:'| grep -v '127.0.0.1' | cut -d: -f2 | awk '{ print $1}'`;;
		# FreeBSD|OpenBSD) PAAS_LOCAL_IP=`ifconfig eth0 | grep -E 'inet.[0-9]' | grep -v '127.0.0.1' | awk '{ print $2}'` ;;
		# SunOS) PAAS_LOCAL_IP=`ifconfig -a | grep inet | grep -v '127.0.0.1' | awk '{ print $2} '` ;;
		*) PAAS_LOCAL_IP="127.0.0.1";;
	esac
fi