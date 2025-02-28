package com.shopapp.admin.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.shopapp.admin.bean.ReportItem;
import com.shopapp.admin.enumm.ReportType;
import com.shopapp.admin.repository.OrderRepository;
import com.shopapp.common.entity.order.Order;

@Service
@Primary
public class MasterOrderReportServiceImpl extends AbstractReportService{

	@Autowired
	private OrderRepository orderRepository;
	
	protected List<ReportItem> getReportDataByDateRangeInternal(Date startTime, Date endTime, ReportType reportType){
		List<Order> ordersByDate = orderRepository.findByOrderTimeBetween(startTime, endTime);
		printRawData(ordersByDate);
		
		List<ReportItem> reportData = createReportData(startTime, endTime, reportType);
		
		calculateSalesForReportData(ordersByDate, reportData);
		printReportData(reportData);
		
		return reportData;
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

	private List<ReportItem> createReportData(Date startTime, Date endTime, ReportType reportType) {
		List<ReportItem> res = new ArrayList<>();
		
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(startTime);
		
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(endTime);
		
		Date currentDate  = startDate.getTime();
		String dateString = df.format(currentDate);
		res.add(new ReportItem(dateString));
		
		do {
			if(reportType.equals(ReportType.DAY)){
				startDate.add(Calendar.DAY_OF_MONTH, 1);
			} else if(reportType.equals(ReportType.MONTH)) {
				startDate.add(Calendar.MONTH, 1);
			}
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
