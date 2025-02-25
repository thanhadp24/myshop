package com.shopapp.admin.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.admin.bean.ReportItem;
import com.shopapp.admin.service.MasterOrderReportService;

@RestController
public class ReportRestController {

	
	@Autowired private MasterOrderReportService reportService;
	
	@GetMapping("/reports/sales_by_date/{period}")
	public List<ReportItem> getReportByDatePeriod(@PathVariable("period") String period){
		System.out.println(">> rest controller...");
		return reportService.getReportDataLast7Days();
	}
}
