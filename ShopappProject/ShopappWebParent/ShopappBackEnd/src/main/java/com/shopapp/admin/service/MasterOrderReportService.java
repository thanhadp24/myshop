package com.shopapp.admin.service;

import java.util.List;

import com.shopapp.admin.bean.ReportItem;

public interface MasterOrderReportService {
	
	List<ReportItem> getReportDataLast7Days();
}
