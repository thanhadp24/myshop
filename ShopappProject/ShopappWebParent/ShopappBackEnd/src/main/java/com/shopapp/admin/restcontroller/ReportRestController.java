package com.shopapp.admin.restcontroller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.admin.bean.ReportItem;
import com.shopapp.admin.enumm.ReportType;
import com.shopapp.admin.service.impl.AbstractReportService;
import com.shopapp.admin.service.impl.OrderDetailReportService;

@RestController
public class ReportRestController {

	@Autowired
	private AbstractReportService reportService;
	
	@Autowired
	private OrderDetailReportService detailReportService;

	@GetMapping("/reports/sales_by_date/{period}")
	public List<ReportItem> getReportByDatePeriod(@PathVariable("period") String period) {

		switch (period) {
		case "last_7_days": {
			return reportService.getReportDataLast7Days(ReportType.DAY);
		}
		case "last_28_days": {
			return reportService.getReportDataLast28Days(ReportType.DAY);
		}
		case "last_6_months": {
			return reportService.getReportDataLast6Months(ReportType.MONTH);
		}
		case "last_year": {
			return reportService.getReportDataLastYear(ReportType.MONTH);
		}
		default:
			return reportService.getReportDataLast7Days(ReportType.DAY);
		}
	}

	@GetMapping("/reports/sales_by_date/{startDate}/{endDate}")
	public List<ReportItem> getReportByDatePeriod(@PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = df.parse(startDate);
		Date endTime = df.parse(endDate);
		return reportService.getReportDataByDateRange(startTime, endTime, ReportType.DAY);
	}

	@GetMapping("/reports/{groupBy}/{period}")
	public List<ReportItem> getDataByCategoryOrProduct(@PathVariable("groupBy") String groupBy,
			@PathVariable("period") String period){
		ReportType reportType = ReportType.valueOf(groupBy.toUpperCase());
		
		switch (period) {
		case "last_7_days": {
			return detailReportService.getReportDataLast7Days(reportType);
		}
		case "last_28_days": {
			return detailReportService.getReportDataLast28Days(reportType);
		}
		case "last_6_months": {
			return detailReportService.getReportDataLast6Months(reportType);
		}
		case "last_year": {
			return detailReportService.getReportDataLastYear(reportType);
		}
		default:
			return detailReportService.getReportDataLast7Days(reportType);
		}
	}
	
	@GetMapping("/reports/{groupBy}/{startDate}/{endDate}")
	public List<ReportItem> getReportByDatePeriod(
			@PathVariable("groupBy") String groupBy,
			@PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate) throws ParseException {
		
		ReportType reportType = ReportType.valueOf(groupBy.toUpperCase());
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = df.parse(startDate);
		Date endTime = df.parse(endDate);
		return detailReportService.getReportDataByDateRange(startTime, endTime, reportType);
	}
}
