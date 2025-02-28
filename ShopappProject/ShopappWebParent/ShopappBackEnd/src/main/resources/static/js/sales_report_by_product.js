
var data;
var chartOptions;


$(document).ready(function() {
	settupBtnEventHandlers("_product", loadSalesReportByDate4Product)
})

function loadSalesReportByDate4Product(period){
	if(period == "custom"){
		startDate = $("#startDate_product").val();
		endDate = $("#endDate_product").val();
		requestURL = contextPath + "reports/product/" + startDate + "/" + endDate;		
	}else {
		requestURL = contextPath + "reports/product/" + period;
	}
	
	
	$.get(requestURL, function(responseJson){
		prepareChartData4SalesReportByProduct(responseJson);
		customizeChart4SalesReportByProduct();
		formatChartData(data, 2, 3);
		drawChart4SalesReportByProduct();
		setSalesAmount(period, "_product", "Total Products")
	})
}

function prepareChartData4SalesReportByProduct(responseJson){
	data = new google.visualization.DataTable();
	data.addColumn("string", "Product");
	data.addColumn("number", "Quanity");
	data.addColumn("number", "Gross sales");
	data.addColumn("number", "Net sales");
	
	totalGrossSales = 0.0;
	totalNetSales = 0.0;
	totalItems = 0;
	
	$.each(responseJson, function(idx, reportItem){
		data.addRows([[reportItem.identifier, reportItem.productsCount, reportItem.grossSales, reportItem.netSales]])
		totalGrossSales += parseFloat(reportItem.grossSales)
		totalNetSales += parseFloat(reportItem.netSales)
		totalItems += parseInt(reportItem.productsCount)
	})
}

function customizeChart4SalesReportByProduct(){
	chartOptions = {
		height: 360, width: '80%',
		showRowNumber: true,
		page: 'enable',
		sortColumn: 2,
		sortAscending: false
	}
	
}

function drawChart4SalesReportByProduct(){
	
	var salesChart = new google.visualization.Table(document.getElementById("chart_sales_by_product"));
	salesChart.draw(data, chartOptions);
	
}
