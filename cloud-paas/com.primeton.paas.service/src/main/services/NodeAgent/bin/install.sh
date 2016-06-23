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

package_home=${BIN_HOME_PATH}/NodeAgent/packages/java
nodeagent_package=${package_home}/agent-java.zip
nodeagent_program=${PROGRAME_HOME_PATH}/NodeAgent
	
echo "[`date`]Begin install NodeAgent."

# print
echo "nodeagent_package = ${nodeagent_package} "
echo "nodeagent_program = ${nodeagent_program} "
	
# check
if [ ! -f ${nodeagent_package} ]; then
	echo "${nodeagent_package} not found, then exit 1."
	exit 1
fi
if [ -d ${nodeagent_program}/agent-java ]; then
	echo "#######################################################################"
	echo "[`date`] NodeAgent has been installed, then exit."
	echo "#######################################################################"
	exit 0
fi
	
# mkdir ${program_home}
mkdir -p ${nodeagent_program}
	
# main install
echo "[`date`]Begin unzip to NodeAgent"
cd ${nodeagent_program}
unzip ${nodeagent_package}
echo "[`date`]End unzip to NodeAgent"

# bin
cp -f ${BIN_HOME_PATH}/NodeAgent/packages/etc/agent-java/bin/*.sh ${nodeagent_program}/agent-java/bin
# lib
cp -f ${BIN_HOME_PATH}/NodeAgent/packages/etc/agent-java/lib/*.jar ${nodeagent_program}/agent-java/lib
# work
cp -rf ${BIN_HOME_PATH}/NodeAgent/packages/etc/agent-java/work/* ${nodeagent_program}/agent-java/work

# chmod +x bin/*.sh
chmod +x ${nodeagent_program}/agent-java/bin/*.sh

echo "[`date`]End install NodeAgent."
