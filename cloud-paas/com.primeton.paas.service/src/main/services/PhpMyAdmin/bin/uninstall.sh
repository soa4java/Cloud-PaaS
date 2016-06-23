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

zypper --non-interactive remove apache2
zypper --non-interactive remove mysql
zypper --non-interactive remove php5
zypper --non-interactive remove apache2-mod_php5
zypper --non-interactive remove php5-mysql
zypper --non-interactive remove php53-mbstring
zypper --non-interactive remove php5-mbstring

rm -rf /srv/www/htdocs/*
cp -r /srv/www/pas_tmp/* /srv/www/htdocs/
rm -rf /srv/www/pas_tmp