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

# software packages
phpmyadmin_package=${BIN_HOME_PATH}/PhpMyAdmin/packages/etc/phpmyadmin.zip

# reference
# https://www.digitalocean.com/community/tutorials/how-to-install-linux-apache-mysql-php-lamp-stack-on-centos-6
#

ip=${PAAS_LOCAL_IP}

if test -z "${ip}"; then
	ip=192.168.100.0
fi

ip1=`echo "${ip}"|awk -F '.' '{print $1}' `
ip2=`echo "${ip}"|awk -F '.' '{print $2}' `
ip3=`echo "${ip}"|awk -F '.' '{print $3}' `

echo ${ip1}
echo ${ip2}
echo ${ip3}

ipb=$((${ip1}*1000000000+${ip2}*1000000+${ip3}*1000+1))
ipe=$((${ip1}*1000000000+${ip2}*1000000+${ip3}*1000+255))
ipSegment="${ip1}.${ip2}.${ip3}."

echo "${ipb} - ${ipe}"
echo "${ipSegment}" 

if [ ! -f ${phpmyadmin_package} ]; then
	echo "${phpmyadmin_package} not found, then exit 1."
	exit 1
fi

yum -y install httpd mysql-server php php-mysql php-*

# /etc/httpd/conf/httpd.conf
if [ ! -f /etc/httpd/conf/httpd.conf ]; then
	echo "[`date`]ERROR /etc/httpd/conf/httpd.conf not found, then exit 1."
	exit 1
fi
echo "ServerName ${ip}" >> /etc/httpd/conf/httpd.conf

# unzip
unzip ${phpmyadmin_package} -d "/var/www/html"

if [ ! -f /var/www/html/config.inc.php ]; then
	echo "[`date`] ERROR /var/www/html/config.inc.php not found, then exit 1."
	exit 1
fi

# config.inc.php
sed -i 's/192168100001/'"${ipb}"'/g' /var/www/html/config.inc.php
sed -i 's/192168100255/'"${ipe}"'/g' /var/www/html/config.inc.php
# config host

sed -i "s/host'] = '192.168.100.'.\$j;/host'] = '$ipSegment'.\$j;/g" /var/www/html/config.inc.php
# config only_db
sed -i "s/only_db'] = 'paas_db'/only_db'] = 'paas'/g" /var/www/html/config.inc.php
# config AllowRoot
# sed -i "s/AllowRoot'] = true;/AllowRoot'] = false;/g" /var/www/html/config.inc.php

# run

chkconfig httpd on

service httpd start
