
$(document).ready(function(){
	
	$("#btnAdd2Cart").on("click", function(){
		addToCart();
	})
	
})

function addToCart(){
	quantity = $("#quantity" + pid).val();
	url = contextPath + "cart/add/" + pid + "/" + quantity;
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue)
		}
	}).done(function(response){
		$("#yes-btn").hide()
		showDialog("Shopping cart", response)
	}).fail(function(){
		showErrorModal("Error while adding product to cart")
	})
}