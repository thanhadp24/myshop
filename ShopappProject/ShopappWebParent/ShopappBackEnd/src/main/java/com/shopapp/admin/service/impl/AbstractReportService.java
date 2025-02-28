package com.shopapp.admin.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.shopapp.admin.bean.ReportItem;
import com.shopapp.admin.enumm.ReportType;

public abstract class AbstractReportService {
	
	protected DateFormat df;

	public List<ReportItem> getReportDataLast7Days(ReportType reportType) {
		return getReportDataLastXDays(7, reportType);
	}
	
	public List<ReportItem> getReportDataLast28Days(ReportType reportType) {
		return getReportDataLastXDays(28, reportType);
	}
	
	
	protected List<ReportItem> getReportDataLastXDays(int days, ReportType reportType){
		Date endTime = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -(days-1));
		Date startTime = calendar.getTime();
		
		df = new SimpleDateFormat("yyyy-MM-dd");
		
		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}
	
	public List<ReportItem> getReportDataLast6Months(ReportType reportType) {
		return getReportDataLastXMonths(6, reportType);
	}
	
	public List<ReportItem> getReportDataLastYear(ReportType reportType) {
		return getReportDataLastXMonths(12, reportType);
	}
	
	
	protected List<ReportItem> getReportDataLastXMonths(int months, ReportType reportType){
		Date endTime = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -(months-1));

		Date startTime = calendar.getTime();
		System.out.println(">> check start time: " + startTime);
		df = new SimpleDateFormat("yyyy-MM");
		
		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}
	
	public List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime, ReportType reportType){
		df = new SimpleDateFormat("yyyy-MM-dd");
		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}
	
	protected abstract List<ReportItem> getReportDataByDateRangeInternal(Date startTime, 
			Date endTime, ReportType reportType);
}
