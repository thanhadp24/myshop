package com.shopapp.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopapp.admin.bean.ReportItem;
import com.shopapp.admin.enumm.ReportType;
import com.shopapp.admin.repository.OrderDetailRepository;
import com.shopapp.common.entity.order.OrderDetail;

@Service
public class OrderDetailReportService extends AbstractReportService{

	@Autowired
	private OrderDetailRepository detailRepository;
	
	@Override
	protected List<ReportItem> getReportDataByDateRangeInternal(Date startTime, Date endTime, ReportType reportType) {
		List<OrderDetail> orderDetails = null;
		
		if(reportType.equals(ReportType.CATEGORY)) {
			orderDetails = detailRepository.findWithCategoryAndTimeBetween(startTime, endTime);
		}else if(reportType.equals(ReportType.PRODUCT)) {
			orderDetails = detailRepository.findWithProductAndTimeBetween(startTime, endTime);
		}
		
		//printRawData(orderDetails);
		
		List<ReportItem> reportItems = new ArrayList<>();
		
		for(OrderDetail detail: orderDetails) {
			String identifier = "";
			if(reportType.equals(ReportType.CATEGORY)) {
				identifier = detail.getProduct().getCategory().getName();
			}else if(reportType.equals(ReportType.PRODUCT)) {
				identifier = detail.getProduct().getShortName();
			}
			ReportItem reportItem = new ReportItem(identifier);
			
			float grossSales = detail.getSubtotal() + detail.getShippingCost();
			float netSales = detail.getSubtotal() - detail.getProductCost();
			
			int itemIdx = reportItems.indexOf(reportItem);
			if(itemIdx >= 0) {
				reportItem = reportItems.get(itemIdx);
				reportItem.addGrossSales(grossSales);
				reportItem.addNetSales(netSales);
				reportItem.increaseProductCount(detail.getQuantity());
			}else {
				reportItems.add(new ReportItem(identifier, grossSales, netSales, detail.getQuantity()));
			}
		}
		System.out.println(">> check start: " + startTime);
		System.out.println(">> check end: " + endTime);
		//printReportData(reportItems);
		
		return reportItems;
	}

	private void printReportData(List<ReportItem> reportItems) {
		reportItems.forEach(i -> {
			System.out.printf("%-20s, %10.2f, %10.2f, %d\n", i.getIdentifier(), 
					i.getGrossSales(), i.getNetSales(), i.getProductsCount());
		});
	}

	private void printRawData(List<OrderDetail> orderDetails) {
		orderDetails.forEach(od -> {
			System.out.printf("%s, %d, %10.2f, %10.2f, %10.2f\n", od.getProduct().getShortName().substring(0,  20),
					od.getQuantity(), od.getProductCost(), od.getShippingCost(), od.getSubtotal());
		});
	}
}
