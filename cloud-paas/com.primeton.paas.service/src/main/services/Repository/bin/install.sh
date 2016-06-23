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

package_home=${BIN_HOME_PATH}/Repository/packages/java
jetty_package=${package_home}/jetty.zip
war_package=${package_home}/default.war

jetty_program=${PROGRAME_HOME_PATH}/Repository

if [ -d ${jetty_program}/jetty ]; then
	echo "############################################################################"
	echo "[WARN] Repository has been installed, please remove ${jetty_program} first."
	echo "############################################################################"
	exit 0
fi

mkdir -p ${jetty_program}/jetty

unzip ${jetty_package} -d ${jetty_program}/jetty

mkdir -p ${jetty_program}/jetty/webapps/default

unzip ${war_package} -d ${jetty_program}/jetty/webapps/default

echo "Finish install, see ${jetty_program}."