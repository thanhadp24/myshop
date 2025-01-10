$(document).ready(function(){
	$("#productList").on("click", ".linkRemove", function(e){
		e.preventDefault()
		if(doesOrderHaveOnly1Product()){
			showWarningModal("Could not remove product.The order must have at least 1 product!")
		}else {
			removeProduct($(this))
			updateOrderAmounts()
		}
	})
})

function removeProduct(link){
	rowNumber = link.attr("rowNumber")
	$("#row" + rowNumber).remove()
	$("#blankLine" + rowNumber).remove()
	updateCountNumbers()
}

function updateCountNumbers(){
	$(".divCount").each(function(idx, e){
		e.innerHTML = "" + (idx+1)
	})
}

function doesOrderHaveOnly1Product(){
	productCount = $(".hiddenProductId").length;
	return productCount == 1;
}