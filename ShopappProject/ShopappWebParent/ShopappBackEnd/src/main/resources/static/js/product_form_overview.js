dropdownBrand = $("#brand");
dropdownCategory = $("#category");

$(document).ready(function() {
	
	$("#shortDescription").richText();
	$("#fullDescription").richText();

	dropdownBrand.change(function() {
		dropdownCategory.empty();
		getCategories();
	})
	
	getCategoriesForNewForm();
	
});

function getCategoriesForNewForm(){
	catIdField = $("#categoryId");
	editMode = false;
	
	if(catIdField.length){
		editMode = true;
	}
	
	if(!editMode){
		getCategories();
	}
}

function getCategories(){
	brandId = dropdownBrand.val();
	brandUrl = brandURL + "/" + brandId + "/categories";
	$.get(brandUrl, function(response) {
		$.each(response, function(idx, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategory);
		})
	});		
}

function checkUniqueProductName(form){
		
		productId = $("#id").val();
		productName = $("#name").val();
		csrfVal = $("input[name='_csrf']").val();
		params = {id: productId, name: productName, _csrf: csrfVal};
		
		$.post(url, params, function(response) {
			if(response === "ok") {
				form.submit();
			}else if(response === "duplicate") {
				showWarningModal("The product with name " + productName + " is duplicated!!");
			} 
		}).fail(function() {
				showErrorModal('Could not connect to the server');
			})
		
		return false;
}