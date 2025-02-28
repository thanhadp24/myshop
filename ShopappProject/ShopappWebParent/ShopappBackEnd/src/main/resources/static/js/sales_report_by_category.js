
var data;
var chartOptions;


$(document).ready(function() {
	settupBtnEventHandlers("_category", loadSalesReportByDate4Category)
})

function loadSalesReportByDate4Category(period){
	if(period == "custom"){
		startDate = $("#startDate_category").val();
		endDate = $("#endDate_category").val();
		requestURL = contextPath + "reports/category/" + startDate + "/" + endDate;		
	}else {
		requestURL = contextPath + "reports/category/" + period;
	}
	
	
	$.get(requestURL, function(responseJson){
		prepareChartData4SalesReportByCategory(responseJson);
		customizeChart4SalesReportByCategory();
		formatChartData(data, 1, 2);
		drawChart4SalesReportByCategory();
		setSalesAmount(period, "_category", "Total Products")
	})
}

function prepareChartData4SalesReportByCategory(responseJson){
	data = new google.visualization.DataTable();
	data.addColumn("string", "Category");
	data.addColumn("number", "Gross sales");
	data.addColumn("number", "Net sales");
	
	totalGrossSales = 0.0;
	totalNetSales = 0.0;
	totalItems = 0;
	
	$.each(responseJson, function(idx, reportItem){
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales]])
		totalGrossSales += parseFloat(reportItem.grossSales)
		totalNetSales += parseFloat(reportItem.netSales)
		totalItems += parseInt(reportItem.productsCount)
	})
}

function customizeChart4SalesReportByCategory(){
	chartOptions = {
		height: 360, 
		legend: {
			position: 'right'
		}		
	}
	
}

function drawChart4SalesReportByCategory(){
	
	var salesChart = new google.visualization.PieChart(document.getElementById("chart_sales_by_category"));
	salesChart.draw(data, chartOptions);
	
}
