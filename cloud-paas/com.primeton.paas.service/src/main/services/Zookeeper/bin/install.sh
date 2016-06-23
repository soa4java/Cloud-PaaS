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

package_home=${BIN_HOME_PATH}/Zookeeper/packages/java
zk_package=${package_home}/zookeeper-3.4.6.tar.gz

zk_program=${PROGRAME_HOME_PATH}/Zookeeper/zookeeper-3.4.6

if [ -d ${zk_program} ]; then
	echo "###########################################################################################"
	echo "[WARN] zookeeper has been installed, please remove ${PROGRAME_HOME_PATH}/Zookeeper first."
	echo "###########################################################################################"
	exit 0
fi

mkdir -p ${PROGRAME_HOME_PATH}/Zookeeper

cd ${PROGRAME_HOME_PATH}/Zookeeper
tar -zvxf ${zk_package}

# default configuration
cp ${zk_program}/conf/zoo_sample.cfg ${zk_program}/conf/zoo.cfg

# chmod +x scripts
chmod +x ${zk_program}/bin/*.sh

echo "Finish install, see ${zk_program}."