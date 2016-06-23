package com.primeton.paas.console.common.stretch;

import java.util.ArrayList;
import java.util.List;


/**
 * 伸缩策略页面初始化时，保存各配置项的可选项值
 * @author liyanping(mailto:liyp@primeton.com)
 */
public class StrategyItemInfo {
	
	//List 形式
	private List<Integer> cpuUsageList = null;
	private List<Integer> memUsageList = null;
	private List<Integer> netTrafficList= null;
	private List<Integer> ioRateList = null;
	private List<Integer> stretchScaleList= null;
	private List<Integer> durationList = null;
	private List<Integer> ignoreTimeList = null;
	private List<String> lbList = null;
	
	//Array 形式
	private Integer[] cpuUsageArr = null;
	private Integer[] memUsageArr = null;
	private Integer[] netTrafficArr = null;
	private Integer[] ioRateArr = null;
	private Integer[] stretchScaleArr = null;
	private Integer[] durationArr = null;       
	private Integer[] ignoreTimeArr = null;   
	private String[] lbAttr = null;   
	
	public StrategyItemInfo(){
		cpuUsageList = new ArrayList<Integer>();
		memUsageList = new ArrayList<Integer>();
		netTrafficList = new ArrayList<Integer>();
		ioRateList = new ArrayList<Integer>();
		stretchScaleList = new ArrayList<Integer>();
		durationList = new ArrayList<Integer>();
		ignoreTimeList = new ArrayList<Integer>();
		lbList = new ArrayList<String>();
	}

	public Integer[] getCpuUsageArr() {
		return cpuUsageArr;
	}

	public void setCpuUsageArr(Integer[] cpuUsageArr) {
		this.cpuUsageArr = cpuUsageArr;
	}

	public List<Integer> getCpuUsageList() {
		return cpuUsageList;
	}

	public void setCpuUsageList(List<Integer> cpuUsageList) {
		this.cpuUsageList = cpuUsageList;
	}

	public Integer[] getDurationArr() {
		return durationArr;
	}

	public void setDurationArr(Integer[] durationArr) {
		this.durationArr = durationArr;
	}

	public List<Integer> getDurationList() {
		return durationList;
	}

	public void setDurationList(List<Integer> durationList) {
		this.durationList = durationList;
	}

	public Integer[] getIoRateArr() {
		return ioRateArr;
	}

	public void setIoRateArr(Integer[] ioRateArr) {
		this.ioRateArr = ioRateArr;
	}

	public List<Integer> getIoRateList() {
		return ioRateList;
	}

	public void setIoRateList(List<Integer> ioRateList) {
		this.ioRateList = ioRateList;
	}

	public Integer[] getMemUsageArr() {
		return memUsageArr;
	}

	public void setMemUsageArr(Integer[] memUsageArr) {
		this.memUsageArr = memUsageArr;
	}

	public List<Integer> getMemUsageList() {
		return memUsageList;
	}

	public void setMemUsageList(List<Integer> memUsageList) {
		this.memUsageList = memUsageList;
	}

	public Integer[] getNetTrafficArr() {
		return netTrafficArr;
	}

	public void setNetTrafficArr(Integer[] netTrafficArr) {
		this.netTrafficArr = netTrafficArr;
	}

	public List<Integer> getNetTrafficList() {
		return netTrafficList;
	}
	
	public void setLbArr(String[] lbAttr) {
		this.lbAttr = lbAttr;
	}
	
	public String[] getLbArr() {
		return lbAttr;
	}

	public void setLbList(List<String> lbList) {
		this.lbList = lbList;
	}
	
	public List<String> getLbList() {
		return lbList;
	}

	public void setNetTrafficList(List<Integer> netTrafficList) {
		this.netTrafficList = netTrafficList;
	}

	public Integer[] getStretchScaleArr() {
		return stretchScaleArr;
	}

	public void setStretchScaleArr(Integer[] stretchScaleArr) {
		this.stretchScaleArr = stretchScaleArr;
	}

	public List<Integer> getStretchScaleList() {
		return stretchScaleList;
	}

	public void setStretchScaleList(List<Integer> stretchScaleList) {
		this.stretchScaleList = stretchScaleList;
	}

	public Integer[] getIgnoreTimeArr() {
		return ignoreTimeArr;
	}

	public void setIgnoreTimeArr(Integer[] ignoreTimeArr) {
		this.ignoreTimeArr = ignoreTimeArr;
	}

	public List<Integer> getIgnoreTimeList() {
		return ignoreTimeList;
	}

	public void setIgnoreTimeList(List<Integer> ignoreTimeList) {
		this.ignoreTimeList = ignoreTimeList;
	}
}
