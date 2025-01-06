decimalSeperator = decimalPointType === "COMMA" ? ",": "."
thousandsSeperator = thousandsPointType === "COMMA" ? ",": "."

$(document).ready(function(){
	$(".linkMinus").on('click', function(e) {
		e.preventDefault();
		descreaseQuantity($(this))
	})
	
	$(".linkPlus").on('click', function(e) {
		e.preventDefault();
		increaseQuantity($(this))
	})
	
	$(".linkRemove").on('click', function(e) {
		e.preventDefault();
		removeProduct($(this));
	})
})

function removeProduct(link){
	url = link.attr("href");
	rowNumber = link.attr("id")
	$.ajax({
		type: "DELETE",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue)
		}
	}).done(function(response){
		removeProductHTML(rowNumber)
		updateTotal()
		updateCountNumbers()
		$("#yes-btn").hide()
		showDialog("Shopping cart", response)
	}).fail(function(){
		showErrorModal("Error while removing product quantity")
	})
}

function updateCountNumbers(){
	$(".divCount").each(function(idx, element){
		element.innerHTML = (idx + 1) + "";
	})
}

function removeProductHTML(rowNumber){
	$("#row" + rowNumber).remove();
	$("blankId" + rowNumber).remove();
}

function descreaseQuantity(link){
	productId = link.attr("pid");
	productQuantity = $("#quantity" + productId);
	newQuantity = parseInt(productQuantity.val()) - 1;
	if(newQuantity > 0){
		productQuantity.val(newQuantity);
		updateQuantity(productId, newQuantity)
	}else {
		showWarningModal("The minimum is 1")
	}
}

function increaseQuantity(link){
	productId = link.attr("pid");
	productQuantity = $("#quantity" + productId);
	newQuantity = parseInt(productQuantity.val()) + 1;
	if(newQuantity < 6){
		productQuantity.val(newQuantity);
		updateQuantity(productId, newQuantity)
	}else {
		showWarningModal("The maximum is 5")
	}
}

function updateQuantity(productId, quantity){
	url = contextPath + "cart/update/" + productId + "/" + quantity;
	
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue)
		}
	}).done(function(newSubTotal){
		updateSubTotal(productId, newSubTotal)
		updateTotal()
	}).fail(function(){
		showErrorModal("Error while updating product quantity")
	})
}

function updateSubTotal(productId, newSubTotal){
	$("#subTotal" + productId).text(formatCurrency(newSubTotal))
}

function updateTotal(){
	total = 0.0;
	productCount = 0
	
	$(".subTotal").each(function(idx, element){
		total += parseFloat(clearCurrencyFormat(element.innerHTML));
		productCount++;
	})
	
	if(productCount < 1){
		$("#sectionTotal").hide();
		$("#sectionEmptyProducts").removeClass("d-none")
	}else {
		$("#total").text(formatCurrency(total));
	}
}

function formatCurrency(amount){
	return $.number(amount, decimalDigits, decimalSeperator, thousandsSeperator);
}

function clearCurrencyFormat(numberAsString){
	result = numberAsString.replaceAll(thousandsSeperator, "");
	return result.replaceAll(decimalSeperator, ".")
}