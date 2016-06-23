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
# software packages
package_home=${BIN_HOME_PATH}/Console/resources
war_package=${package_home}/default.war
jetty_package=${package_home}/jetty.zip

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/Console
app_program=${program_home}/console-app
platform_program=${program_home}/console-platform

# if has been installed then exit
if [ -d ${app_program}/webapps/default ]; then
	echo "########################################################"
	echo "Primeton PAAS console-app has been installed."
	echo "########################################################"
	exit 0
fi
if [ -d ${platform_program}/webapps/default ]; then
	echo "########################################################"
	echo "Primeton PAAS console-platform has been installed."
	echo "########################################################"
	exit 0
fi

# check environment
if [ ! -f ${war_package} ]; then
	echo "${war_package} not found, exit 1."
	exit 1
fi
if [ ! -f ${jetty_package} ]; then
	echo "${jetty_package} not found, exit 1."
	exit 1
fi
if [ -d ${app_program} ]; then
	rm -rf ${app_program}
fi
if [ -d ${platform_program} ]; then
	rm -rf ${platform_program}
fi
mkdir ${program_home}

# mkdir console-app and console-platform
mkdir -p ${app_program}
mkdir -p ${platform_program}

# unzip jetty.zip
unzip ${jetty_package} -d ${app_program}
unzip ${jetty_package} -d ${platform_program}

# mkdir default
mkdir -p ${app_program}/webapps/default
mkdir -p ${platform_program}/webapps/default

# unzip default.war
unzip ${war_package} -d ${app_program}/webapps/default
unzip ${war_package} -d ${platform_program}/webapps/default

# replace login.jsp
mv ${platform_program}/webapps/default/login.jsp ${platform_program}/webapps/default/login-app.jsp
cp -f ${platform_program}/webapps/default/login-platform.jsp ${platform_program}/webapps/default/login.jsp

# remove com.primeton.upcloud.ws-1.0.0.jar in console-app
rm ${app_program}/webapps/default/WEB-INF/lib/com.primeton.upcloud.ws-*.jar

echo "#####################################################################"
echo "Finish install Primeton PAAS console-app and console-platform"
echo "#####################################################################"
