#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#_help
function _help()
{
    _dohelp
    exit 0
}

#success
function _success()
{
    echo -n "__Success__" > ${FILE_PATH_STATUS}
}

#fail
function _fail()
{
    echo -n "__Fail__" > ${FILE_PATH_STATUS}
}


#error
function _error()
{
    echo -n "__Fail__" > ${FILE_PATH_STATUS}
    echo $@ >> ${FILE_PATH_ERROUT}
    exit $?
}

#pid
function _pid()
{
    echo -n $1 > ${FILE_PATH_PID}
}

#return
function _return()
{
    echo $@ >> ${FILE_PATH_RETURN}
}

# Parse arguments
if [ $# -eq 0 ]; then
	_help
fi

while [ -n "$1" ]; do
	case $1 in
	-h) _help;;
	-help) _help;;
	-reqId) REQUEST_ID=$2;shift 2;;
	-srvInstId) INSTANCE_ID=$2;shift 2;;
	-srvDefName) SERVICE_TYPE=$2;shift 2;;
	-clusterName) CLUSTER_NAME=$2;shift 2;;
	*) _doparse "$1" "$2";shift 1;;
	esac
done
    
if [ "${INSTANCE_ID}x" != "x" ]; then 
	if [ "${SERVICE_TYPE}x" != "x" ]; then
		SCRIPT_OUT_PATH=${BIN_HOME_PATH}/${SERVICE_TYPE}/instances/${INSTANCE_ID}
		if  [ ! -d ${BIN_HOME_PATH}/${SERVICE_TYPE}/instances ]; then
			mkdir -p ${BIN_HOME_PATH}/${SERVICE_TYPE}/instances
		fi
	fi
else
	SCRIPT_OUT_PATH=${BIN_HOME_PATH}/ShellResult/${REQUEST_ID} 
fi
mkdir -p ${SCRIPT_OUT_PATH}
    
# status, return, stdout, errout, pid
FILE_PATH_STATUS=${SCRIPT_OUT_PATH}/status.txt
FILE_PATH_RETURN=${SCRIPT_OUT_PATH}/return.txt
FILE_PATH_STDOUT=${SCRIPT_OUT_PATH}/stdout.txt
FILE_PATH_ERROUT=${SCRIPT_OUT_PATH}/errout.txt
FILE_PATH_PID=${SCRIPT_OUT_PATH}/pid.txt

# clean environment
if [ -f ${FILE_PATH_STATUS} ]; then
	rm ${FILE_PATH_STATUS}
fi
if [ -f ${FILE_PATH_RETURN} ]; then
	rm ${FILE_PATH_RETURN}
fi
if [ -f ${FILE_PATH_STDOUT} ]; then
	rm ${FILE_PATH_STDOUT}
fi
if [ -f ${FILE_PATH_ERROUT} ]; then
	rm ${FILE_PATH_ERROUT}
fi

# Execute core code
function execute()
{
    _doexecute
} 1>>${FILE_PATH_STDOUT} 2>>${FILE_PATH_ERROUT}

# Print global variables
function _print_variables()
{
    echo BIN_HOME_PATH=${BIN_HOME_PATH}
    echo REQUEST_ID=${REQUEST_ID}
    echo INSTANCE_ID=${INSTANCE_ID}
    echo SERVICE_TYPE=${SERVICE_TYPE}
    echo CLUSTER_NAME=${CLUSTER_NAME}
    echo SCRIPT_OUT_PATH=${SCRIPT_OUT_PATH}
    echo FILE_PATH_STATUS=${FILE_PATH_STATUS}
    echo FILE_PATH_RETURN=${FILE_PATH_RETURN}
    echo FILE_PATH_STDOUT=${FILE_PATH_STDOUT}
    echo FILE_PATH_ERROUT=${FILE_PATH_ERROUT}
    echo FILE_PATH_PID=${FILE_PATH_PID}
}

_print_variables

# Execute main code
execute
