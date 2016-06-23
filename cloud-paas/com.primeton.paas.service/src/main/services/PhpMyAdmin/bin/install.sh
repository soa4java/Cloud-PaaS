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

port=7080
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

sleep 1

zypper --non-interactive install apache2
zypper --non-interactive install mysql
zypper --non-interactive install php5
zypper --non-interactive install apache2-mod_php5
zypper --non-interactive install php5-mysql
zypper --non-interactive install php5-gd
zypper --non-interactive install php53-mbstring
zypper --non-interactive install php5-mbstring

# config apache2
cd /etc/apache2/

# conf listen.conf
if [ ! -f /etc/apache2/listen.conf ]; then
	echo "[`date`]ERROR /etc/apache2/listen.conf not found, then exit 1."
	exit 1
fi
sed -i 's/^Listen 80/Listen '"${port}"'/g' listen.conf

# config mod_mime-defaults.conf
# check config file
if [ ! -f /etc/apache2/mod_mime-defaults.conf ]; then
	echo "[`date`]ERROR /etc/apache2/listen.conf not found, then exit 1."
	exit 1
fi
sed -i '/^AddType application\/x-gzip .gz .tgz/ a\AddType application\/x-httpd-php .php .phtml .php3 .inc\nAddType application\/x-httpd-php-source .phps\n' mod_mime-defaults.conf

# httpd.conf
sed -i 's/^DirectoryIndex index.html/DirectoryIndex index.php index.html index.var/g' httpd.conf

# install phpmyadmin
cd /srv/www/htdocs/
mkdir /srv/www/pas_tmp/
cp -r . /srv/www/pas_tmp

# unzip
unzip ${phpmyadmin_package} -d "/srv/www/htdocs/"

if [ ! -f /srv/www/htdocs/config.inc.php ]; then
	echo "[`date`] ERROR /srv/www/htdocs/config.inc.php not found, then exit 1."
	exit 1
fi

# config config.inc.php

cd /srv/www/htdocs/
# sed -i 's/$i = [0-9][0-9][0-9][0-9][0-9][0-9];/$i = '"${ipb}"';/g' config.inc.php
# sed -i 's/$i <= [0-9][0-9][0-9][0-9][0-9][0-9];/$i <= '"${ipe}"';/g' config.inc.php
sed -i 's/192168100001/'"${ipb}"'/g' config.inc.php
sed -i 's/192168100255/'"${ipe}"'/g' config.inc.php
# config host

sed -i "s/host'] = '192.168.100.'.\$j;/host'] = '$ipSegment'.\$j;/g" config.inc.php
# config only_db
sed -i "s/only_db'] = 'paas_db'/only_db'] = 'paas'/g" config.inc.php
# config AllowRoot
sed -i "s/AllowRoot'] = true;/AllowRoot'] = false;/g" config.inc.php
