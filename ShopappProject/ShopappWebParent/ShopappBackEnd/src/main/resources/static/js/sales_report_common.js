// sales report common

var MILLISECONDS_A_DAY = 24* 60 * 60 * 1000

function settupBtnEventHandlers(reportType, callbackFunction) {
	startDateField = document.getElementById("startDate" + reportType);
	endDateField = document.getElementById("endDate" + reportType);
	
	
	$(".btn_sales_by"+ reportType).on("click", function() {

		$(".btn_sales_by" + reportType).each(function() {
			$(this).removeClass("btn-primary").addClass("btn-light");
		})

		$(this).removeClass("btn-light").addClass("btn-primary")

		period = $(this).attr("period");
		if (period) {
			callbackFunction(period)
			$("#divCustomDateRange" + reportType).addClass("d-none")
		} else {
			$("#divCustomDateRange" + reportType).removeClass("d-none")
		}
	})

	initCustomDateRange(reportType);

	$("#btnViewReportByDateRange" + reportType).on("click", function() {
		validateDateRange(reportType, callbackFunction)
	})
}

function validateDateRange(reportType, callbackFunction){
	startDateField = document.getElementById("startDate" + reportType);
	endDateField = document.getElementById("endDate" + reportType);
	
	days = calculateDate(reportType);

	startDateField.setCustomValidity("")
	
	if(days >= 7 && days <= 30){
		callbackFunction("custom")
	}else {
		startDateField.setCustomValidity("Date must be in the range of 7...30days")
		startDateField.reportValidity()
	}
}

function calculateDate(reportType){
	startDateField = document.getElementById("startDate" + reportType);
	endDateField = document.getElementById("endDate" + reportType);
	
	startDate = startDateField.valueAsDate;
	endDate = endDateField.valueAsDate;
	
	differenceInMilliseconds = endDate - startDate;
	return differenceInMilliseconds / MILLISECONDS_A_DAY;
}
function initCustomDateRange(reportType){
	startDateField = document.getElementById("startDate" + reportType);
	endDateField = document.getElementById("endDate" + reportType);
		
	toDate = new Date();
	endDateField.valueAsDate = toDate;
	
	fromDate = new Date();
	fromDate.setDate(toDate.getDate() - 30);
	startDateField.valueAsDate = fromDate;
}



function formatCurrency(amount){
	formattedAmount = $.number(amount, decimalDigits, decimalPointType, thousandPointType);
	return prefixCurrencySymbol + formattedAmount + suffixCurrencySymbol;
}

function getChartTitle(period){
	if(period == "last_7_days") return "Sales in Last 7 days"
	if(period == "last_28_days") return "Sales in Last 28 days"
	if(period == "last_6_months") return "Sales in Last 6 months"
	if(period == "last_year") return "Sales in Last year"
	if(period == "custom") return "Sales in custome date range"
}

function getDenominator(period, reportType){
	if(period == "last_7_days") return 7
	if(period == "last_28_days") return 28
	if(period == "last_6_months") return 6
	if(period == "last_year") return 12
	if(period == "custom") return calculateDate(reportType)
}

function setSalesAmount(period, reportType, labelTotalItems){
	$("#textTotalGrossSales" + reportType).text(formatCurrency(totalGrossSales));
	$("#textTotalNetSales" + reportType).text(formatCurrency(totalNetSales));
	
	denominator = getDenominator(period, reportType);
	
	$("#textAvgGrossSales" + reportType).text(formatCurrency(totalGrossSales/denominator));
	$("#textAvgNetSales" + reportType).text(formatCurrency(totalNetSales/denominator));
	$("#labelTotalItems" + reportType).text(labelTotalItems)
	$("#textTotalItems" + reportType).text(totalItems);
}

function formatChartData(data, columnIdx1, columnIdx2){
	var formatter = new google.visualization.NumberFormat({
	    prefix: prefixCurrencySymbol,
		suffix: suffixCurrencySymbol,
		decimalSymbol: decimalPointType,
		groupingSymbol: thousandPointType,
		fractionDigits: decimalDigits
	});
	
	formatter.format(data, columnIdx1);
	formatter.format(data, columnIdx2);
}