#!/bin/bash

# author ZhongWen.Li(mailto:lizw@primeton.com)

AGENT_HOME="$(cd "$(dirname "${0}")"/../; pwd)"

TYPE=${1}

if [ ! -d ${AGENT_HOME} ]; then
	mkdir -p ${AGENT_HOME}
fi
if [ ! -d ${AGENT_HOME}/work/host ]; then
	mkdir -p ${AGENT_HOME}/work/host
fi

CPU=${AGENT_HOME}/work/host/CPU
MEM=${AGENT_HOME}/work/host/MEM
LOAD=${AGENT_HOME}/work/host/LOAD
IO=${AGENT_HOME}/work/host/IO
STO=${AGENT_HOME}/work/host/STO

if [[ ${TYPE} == "Cpu" ]]; then 
	echo -n "`top -bcisSH -n 2 | grep Cpu`" > ${CPU}
elif [[ ${TYPE} == "Mem" ]]; then 
	echo -n "`top -bcisSH -n 1 | grep Mem`" > ${MEM}
elif [[ ${TYPE} == "Load" ]]; then 
	echo -n "`top -bcisSH -n 1 | grep 'load average:'`" > ${LOAD}
elif [[ ${TYPE} == "IO" ]]; then 
	out="`vmstat 1 2 -S K`"
	echo -n ${out} > ${IO}
elif [[ ${TYPE} == "Sto" ]]; then
	if [ -d "/storage" ]; then
		echo -n "`df -h | grep -E '/dev/hda|/storage/'`" > ${STO}
	else
		echo -n ${STO}
	fi
fi
