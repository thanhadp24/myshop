
var data;
var chartOptions;
var totalGrossSales;
var totalNetSales;
var totalItems;

$(document).ready(function() {
	settupBtnEventHandlers("_date", loadSalesReportByDate)
})

function loadSalesReportByDate(period){
	if(period == "custom"){
		startDate = $("#startDate_date").val();
		endDate = $("#endDate_date").val();
		requestURL = contextPath + "reports/sales_by_date/" + startDate + "/" + endDate;		
	}else {
		requestURL = contextPath + "reports/sales_by_date/" + period;
	}
	
	
	$.get(requestURL, function(responseJson){
		prepareChartData4SalesReportByDate(responseJson);
		customizeChart4SalesReportByDate(period);
		formatChartData(data, 1, 2);
		drawChart4SalesReportByDate(period);
		setSalesAmount(period, "_date", "Orders total")
	})
}

function prepareChartData4SalesReportByDate(responseJson){
	data = new google.visualization.DataTable();
	data.addColumn("string", "Date");
	data.addColumn("number", "Gross sales");
	data.addColumn("number", "Net sales");
	data.addColumn("number", "Orders");
	
	totalGrossSales = 0.0;
	totalNetSales = 0.0;
	totalItems = 0;
	
	$.each(responseJson, function(idx, reportItem){
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales, 
			reportItem.ordersCount
		]])
		totalGrossSales += parseFloat(reportItem.grossSales)
		totalNetSales += parseFloat(reportItem.netSales)
		totalItems += parseInt(reportItem.ordersCount)
	})
}

function customizeChart4SalesReportByDate(period){
	chartOptions = {
		title: getChartTitle(period),
		'height': 360,
		legend: {position: 'top'},
		
		series: {
			0: {targetAxisIndex: 0},
			1: {targetAxisIndex: 0},
			2: {targetAxisIndex: 1},
		},
		
		vAxes: {
			0: {title: "sales amount", format: "currency"},
			1: {title: "Numbers of orders"}
		}
	}
	
}

function drawChart4SalesReportByDate(){
	
	var salesChart = new google.visualization.ColumnChart(document.getElementById("chart_sales_by_date"));
	salesChart.draw(data, chartOptions);
	
}
