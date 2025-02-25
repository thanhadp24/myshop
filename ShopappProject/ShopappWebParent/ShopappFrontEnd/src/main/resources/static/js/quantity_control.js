$(document).ready(function(){
	$(".linkMinus").on('click', function(e) {
		e.preventDefault();
		productId = $(this).attr("pid");
		productQuantity = $("#quantity" + productId);
		newQuantity = parseInt(productQuantity.val()) - 1;
		if(newQuantity > 0){
			productQuantity.val(newQuantity);
		}else {
			showWarningModal("The minimum is 1")
		}
	})
	
	$(".linkPlus").on('click', function(e) {
		e.preventDefault();
		productId = $(this).attr("pid");
		productQuantity = $("#quantity" + productId);
		newQuantity = parseInt(productQuantity.val()) + 1;
		if(newQuantity < 6){
			productQuantity.val(newQuantity);
		}else {
			showWarningModal("The maximum is 5")
		}
	})
})