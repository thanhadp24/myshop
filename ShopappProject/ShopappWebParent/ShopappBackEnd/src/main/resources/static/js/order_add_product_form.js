var productDetailCount;

$(document).ready(function(){
	productDetailCount = $(".hiddenProductId").length;
	$("#products").on("click", "#linkAddProduct", function(e){
		e.preventDefault();
		url = $(this).attr("href")
		
		$("#addProductModal").on("shown.bs.modal", function(){
			$(this).find("iframe").attr("src", url)
		})
		$("#addProductModal").modal("show")
	})
})

function getProductInfo(productId, shippingCost){
	url = contextPath + "products/get/" + productId;
	$.get(url, function(productJson){
		productName = productJson.name
		mainImgPath = contextPath.substring(0, contextPath.length - 1) + productJson.imagePath
		productPrice = $.number(productJson.price,2)
		productCost = $.number(productJson.cost, 2)
		
		html = generateProductCode(productId, productName, mainImgPath, 
			productPrice, productCost, shippingCost)
		
		$("#productList").append(html)
		updateOrderAmounts()
		
	}).fail(function(err){
		showWarningModal("Error: " + err.responseJSON.message)	
	})
}

function addProduct(productId, productName){
	
	getShippingCost(productId)
	
}

function generateProductCode(productId, productName, mainImagePath, 
	productPrice, productCost, shippingCost){
	nextCount = productDetailCount + 1;
	productDetailCount++;
	quantityId = 'quantity' + nextCount;
	priceId = 'price' + nextCount;
	subtotalId = 'subtotal' + nextCount;
	rowNumber = 'row' + nextCount;
	blankLineId = 'blankLine' + nextCount;
	
	var html = `
<div class="border rounded p-1" id='${rowNumber}'>
	<input type="hidden" name="detailId" value="0">
	<input type="hidden" name="productId" value="${productId}" class="hiddenProductId">
	<div class="row">
		<div class="col-1">
			<div class='divCount'>${nextCount}</div>
			<div><a class="fa-solid fa-trash text-dark linkRemove" href="" rowNumber="${nextCount}"></a></div>
		</div>
		<div class="col-3">
			<img src="${mainImagePath}" class="img-fluid">
		</div>
	</div>
	<div class="row m-2">
		<b>${productName}</b>
	</div>
	<div class="row m-2">
		<table>
			<tr>
				<td>Product cost: &nbsp;</td>
				<td><input type="text" class="form-control cost-input" required
					rowNumber="${nextCount}" name="productDetailCost"
					value="${productCost}" style="max-width: 140px"></td>
			</tr>
			<tr>
				<td>Quantity: &nbsp;</td>
				<td><input type="number" step="1" class="form-control quantity-input" min="1" max="5"
					rowNumber="${nextCount}" id="${quantityId}" name="quantity"
					value="1" style="max-width: 140px"></td>
			</tr>
			<tr>
				<td>Units Price: &nbsp;</td>
				<td><input type="text" step="1" class="form-control price-input" required
					id="${priceId}" rowNumber="${nextCount}" name="productPrice"
					value="${productPrice}" style="max-width: 140px"></td>
			</tr>
			<tr>
				<td>Subtotal: &nbsp;</td>
				<td><input type="text" class="form-control subtotal-output" readonly
					id="${subtotalId}" name="productSubtotal"
					value="${productPrice}" style="max-width: 140px"></td>
			</tr>
			<tr>
				<td>Shipping cost: &nbsp;</td>
				<td><input type="text" class="form-control ship-input" required
					name="productShippingCost"
					value="${shippingCost}" style="max-width: 140px"></td>
			</tr>
		</table>
	</div>
</div>
<div id='${blankLineId}' class="row">&nbsp;</div>`;

return html;
}

function getShippingCost(productId){
	countryId = $("#country option:selected").val();
	state = $("#state").val();
	
	if(state.length == 0){
		state = $("#city").val();
	}
	
	requestUrl = contextPath + "get_shipping_cost";
	params = {productId: productId, countryId: countryId, state:state}

	$.ajax({
		type: 'POST',
		url: requestUrl,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: params
	}).done(function(shippingCost){
		getProductInfo(productId, shippingCost)
	}).fail(function(err){
		shippingCost = 0.0
		getProductInfo(productId, shippingCost)
		showWarningModal("Error: " + err.responseJSON.message)
	}).always(function(){
		$("#addProductModal").modal("hide")
	})
}

function isProductAlreadyAdded(productId){
	productExists = false
	$(".hiddenProductId").each(function(idx, e){
		aProductId = $(this).val();
		if(aProductId == productId){
			productExists = true;
		}
	})
	return productExists
}