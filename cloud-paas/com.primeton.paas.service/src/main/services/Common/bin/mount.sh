#!/bin/bash

# author liming(mailto:liming@primeton.com)

#
# description
# For mount and unmount share storage, for example NFS/NAS/ etc.
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
mountInfos=
umountInfo=

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage: ./mount.sh"
	echo "-h) Print help"
	echo "-reqId) Request id"
	echo "-mountInfos) mountInfos. e.g: 192.168.100.252:/nfs/2013102316103880,/tmp/test;192.168.100.252:/nfs/2013102316103880,/tmp/test2"
	echo "-umountInfo) mountInfos. e.g: 192.168.100.252:/nfs/2013102316103880,/tmp/test"
}

# Parse execute arguments if has
function _doparse() {
    while [ -n "$1" ]; do
        case $1 in
        -mountInfos) mountInfos=$2;shift 2;;
		-umountInfo) umountInfo=$2;shift 2;;
        *) break;;
        esac
    done
}

# Write your core/main code
function _doexecute() {
	config="/etc/fstab"
	template="/etc/fstab.template"
	
	echo "fstab file [${config}]"
	echo "fstab template [${config}]"
	
	if [ "${umountInfo}x" != "x" ]; then
		vPath=`echo $umountInfo | awk -F ',' '{print $1}'`
		mPath=`echo $umountInfo | awk -F ',' '{print $2}'`
		
		if [ `df -h|grep ${mPath}|wc -l` -eq 1 ]; then
			_return "[`date`] Begin umount '$umountInfo'."
			#fuser -km ${mPath}
			umount  ${mPath}
			_return "[`date`] End umount '$umountInfo'."
			_success
		fi
			
		#if [ -d ${mPath} ]; then
		#	rm -rf "${mPath}"
		#fi
    fi
    
    # Use original fstab file as template
	if [ ! -f ${template} ]; then
		cp ${config} ${template}
	fi
	
	# Delete old fstab file
	if [ -f ${config} ]; then
		rm ${config}
	fi
	# Copy template to here
	cp ${template} ${config}
	
	if [ ! -d ${confPath} ]; then
		mkdir -p ${confPath}
	fi
	
	info_array=(`echo $mountInfos | tr ";" "\n"`)
	for info in ${info_array[@]}; do
		vPath=`echo $info | awk -F ',' '{print $1}'`
		mPath=`echo $info | awk -F ',' '{print $2}'`
		
		_return "[`date`] Begin update the configuration to add '$info'."
	 	echo "${vPath} ${mPath} nfs defaults 0 0" >> "${config}"
		_return "[`date`] End update the configuration to add '$info'."
		
		if [ ! -d ${mPath} ]; then
			mkdir -p ${mPath}
		fi
			
	done
	
	_return "[`date`] Begin execute 'mount -a'."
	mount -a
	_return "[`date`] End execute 'mount -a'."
	
	_return "Generate SharedStorage  configuration file [${config}] success."
	_success
	exit 0
}

# import templates.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh