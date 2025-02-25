var fieldProductCost;
var fieldSubtotal;
var fieldShippingCost;
var fieldTax;
var fieldTotal;

$(document).ready(function(){
	
	fieldProductCost = $("#productCost")
	fieldSubtotal = $("#subtotal")
	fieldShippingCost = $("#shippingCost")
	fieldTax = $("#tax")
	fieldTotal = $("#total")
	
	formatOrderAmounts();
	formatProductAmounts();
	
	$("#productList").on('change', ".quantity-input", function(e){
		updateSubtotalWhenQuantityChanged($(this))
		updateOrderAmounts()
	})
	
	$("#productList").on('change', ".price-input", function(e){
		updateSubtotalWhenPriceChanged($(this))
		updateOrderAmounts()
	})

	$("#productList").on('change', ".cost-input", function(e){
		updateOrderAmounts()
	})

	$("#productList").on('change', ".ship-input", function(e){
		updateOrderAmounts()
	})
})

function updateOrderAmounts(){
	totalCost = 0.0;
	
	$(".cost-input").each(function(idx, e){
		costInputField = $(this)
		rowNumber = costInputField.attr("rowNumber")
		quantityVal = $("#quantity" + rowNumber).val();
		
		totalCost += getNumberRemoveThousandSeperator(costInputField) * quantityVal
	})
	setAndFormat4Field("productCost", totalCost)
	
	subTotal = 0.0;
	$(".subtotal-output").each(function(idx, e){
		subTotal += getNumberRemoveThousandSeperator($(this))
	})
	setAndFormat4Field("subtotal", subTotal)
	
	shippingCostTotal = 0.0;
	$(".ship-input").each(function(idx, e){
		shippingCostTotal += getNumberRemoveThousandSeperator($(this))
	})
	setAndFormat4Field("shippingCost", shippingCostTotal)
	
	tax = getNumberRemoveThousandSeperator(fieldTax)
	orderTotal = subTotal + shippingCostTotal + tax;
	
	setAndFormat4Field("total", orderTotal)
}

function setAndFormat4Field(fieldId, fieldVal){
	$("#" + fieldId).val($.number(fieldVal, 2))
}

function getNumberRemoveThousandSeperator(number){
	return parseFloat(number.val().replaceAll(",", ""))
}

function updateSubtotalWhenPriceChanged(input){
	priceVal = getNumberRemoveThousandSeperator(input);
	rowNumber = input.attr("rowNumber")
	quantityVal = $("#quantity" + rowNumber).val()
		
	newSubtotal = parseFloat(quantityVal) * priceVal;
	setAndFormat4Field("subtotal" + rowNumber, newSubtotal)
}

function updateSubtotalWhenQuantityChanged(input){
	quantityVal = input.val();
	rowNumber = input.attr("rowNumber")
	priceVal = getNumberRemoveThousandSeperator($("#price" + rowNumber))
	
	newSubtotal = parseFloat(quantityVal) * priceVal;
	setAndFormat4Field("subtotal" + rowNumber, newSubtotal)
}

function formatProductAmounts(){
	$(".cost-input").each(function(idx, e){
		formatNumber4Field($(this));
	})
	
	$(".price-input").each(function(idx, e){
		formatNumber4Field($(this));
	})
	
	$(".subtotal-output").each(function(idx, e){
		formatNumber4Field($(this));
	})
	
	$(".ship-input").each(function(idx, e){
		formatNumber4Field($(this));
	})
}

function formatOrderAmounts(){
	formatNumber4Field(fieldProductCost);
	formatNumber4Field(fieldSubtotal);
	formatNumber4Field(fieldShippingCost);
	formatNumber4Field(fieldTax);
	formatNumber4Field(fieldTotal);
}

function formatNumber4Field(fieldRef){
	fieldRef.val($.number(fieldRef.val(), 2));
}

function processFormBeforeSubmit(){
	setCountryName()
	
	removeThousandSeperator4Field(fieldProductCost);
	removeThousandSeperator4Field(fieldShippingCost);
	removeThousandSeperator4Field(fieldSubtotal);
	removeThousandSeperator4Field(fieldTax);
	removeThousandSeperator4Field(fieldTotal);
	
	$(".cost-input").each(function(e){
		removeThousandSeperator4Field($(this))
	})
	
	$(".price-input").each(function(e){
		removeThousandSeperator4Field($(this))
	})
	
	$(".subtotal-output").each(function(e){
		removeThousandSeperator4Field($(this))
	})
	
	$(".ship-input").each(function(e){
		removeThousandSeperator4Field($(this))
	})
	
	return true;
}

function setCountryName(){
	countryName = $("#country option:selected").text()
	$("#countryName").val(countryName)
}

function removeThousandSeperator4Field(fieldRef){
	fieldRef.val(fieldRef.val().replaceAll(",", ""))
}