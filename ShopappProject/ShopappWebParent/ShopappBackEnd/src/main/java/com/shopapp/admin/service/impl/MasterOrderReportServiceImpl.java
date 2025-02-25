package com.shopapp.admin.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopapp.admin.bean.ReportItem;
import com.shopapp.admin.repository.OrderRepository;
import com.shopapp.admin.service.MasterOrderReportService;
import com.shopapp.common.entity.order.Order;

@Service
public class MasterOrderReportServiceImpl implements MasterOrderReportService{

	@Autowired
	private OrderRepository orderRepository;
	private DateFormat df;
	
	@Override
	public List<ReportItem> getReportDataLast7Days() {
		System.out.println(">> service controller...");
		return getReportDataLastXDays(7);
	}
	
	private List<ReportItem> getReportDataLastXDays(int days){
		Date endTime = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -(days-1));
		Date startTime = calendar.getTime();
		
		df = new SimpleDateFormat("yyy-MM-dd");
		
		return getReportDataByDateRange(startTime, endTime);
	}
	
	private List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime){
		List<Order> ordersByDate = orderRepository.findByOrderTimeBetween(startTime, endTime);
		printRawData(ordersByDate);
		
		List<ReportItem> reportData = createReportData(startTime, endTime);
		
		calculateSalesForReportData(ordersByDate, reportData);
		printReportData(reportData);
		
		return null;
	}
	
	private void calculateSalesForReportData(List<Order> orders, List<ReportItem> reportData) {
		for(Order order: orders) {
			String orderDateString = df.format(order.getOrderTime());
			
			ReportItem reportItem = new ReportItem(orderDateString); 
			int itemIdx = reportData.indexOf(reportItem);
			if(itemIdx >= 0) {
				reportItem = reportData.get(itemIdx);
				
				reportItem.addGrossSales(order.getTotal());
				reportItem.addNetSales(order.getSubtotal() - order.getProductCost());
				reportItem.increaseOrderCount();
			}
		}
	}

	private void printReportData(List<ReportItem> reportData) {
		reportData.forEach(r -> {
			System.out.printf("%s, %10.2f, %10.2f, %d \n", r.getIdentifier(),
					r.getGrossSales(), r.getNetSales(), r.getOrdersCount());
		});
	}

	private List<ReportItem> createReportData(Date startTime, Date endTime) {
		List<ReportItem> res = new ArrayList<>();
		
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(startTime);
		
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(endTime);
		
		Date currentDate  = startDate.getTime();
		String dateString = df.format(currentDate);
		res.add(new ReportItem(dateString));
		
		do {
			startDate.add(Calendar.DAY_OF_MONTH, 1);
			currentDate = startDate.getTime();
			dateString = df.format(currentDate);
			res.add(new ReportItem(dateString));
		}while(startDate.before(endDate));
		
		return res;
	}

	private void printRawData(List<Order> ordersByDate) {
		ordersByDate.forEach(o -> {
			System.out.printf("%s | %10.2f | %10.2f\n", o.getOrderTime(), o.getTotal(), o.getSubtotal());
		});
	}
}
