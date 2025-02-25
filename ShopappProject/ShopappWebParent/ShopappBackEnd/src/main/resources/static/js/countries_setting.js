var btnLoad;
var dropdownCountry;
var btnAdd;
var btnUpdate;
var btnDelete;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function(){
	btnLoad = $("#btnLoad");
	dropdownCountry = $("#dropdownCountry");
	btnAdd = $("#btnAdd");
	btnUpdate = $("#btnUpdate");
	btnDelete = $("#btnDelete");
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");
	
	btnLoad.on('click', function(){
		loadCountries();
	})
	
	dropdownCountry.on('change', function(){
		changeFormStateToBeSelectedCountry();
	})
	
	btnAdd.on('click', function(){
		if(btnAdd.val() === "Add"){
			addCountry();
		}else {
			changeFormStateToNewCountry();
		}
	})
	
	btnUpdate.on('click', function(){
		updateCountry();
	});
	
	btnDelete.on('click', function(){
		deleteCountry();
	})
})

function deleteCountry(){
	optionValue = dropdownCountry.val();
	countryId = optionValue.split("-")[0];
	
	url = contextPath + "countries/delete/" + countryId;
	
	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
	}).done(function(){
		$("#dropdownCountry option[value='" + optionValue + "']").remove();
		changeFormStateToNewCountry();
		showToastMessage("The country has been deleted");
	}).fail(function(){
		showToastMessage("ERROR: Server has been encountered error!!!");
	})
}

function updateCountry(){
	if(!validateFormCountry()) return;
	
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	
	countryId = dropdownCountry.val().split("-")[0];
	
	jsonData = {id: countryId, name: countryName, code: countryCode}
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId){
		$("#dropdownCountry option:selected").val(countryId + "-" + countryCode);
		$("#dropdownCountry option:selected").text(countryName);
		showToastMessage("The new country has been updated");
		
		changeFormStateToNewCountry();
	}).fail(function(){
		showToastMessage("ERROR: Server has been encountered error!!!");
	})
}

function validateFormCountry(){
	formCountry =document.getElementById("formCountry");
	if(!formCountry.checkValidity()){
		formCountry.reportValidity();
		return false;
	}
	return true;
}

function addCountry(){
	if(!validateFormCountry()) return;
	
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	
	jsonData = {name: countryName, code: countryCode}
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId){
		selectNewlyCountry(countryId, countryCode, countryName);
		showToastMessage("The new country has been added");
	}).fail(function(){
		showToastMessage("ERROR: Server has been encountered error!!!");
	})
}

function selectNewlyCountry(countryId, countryCode, countryName){
	optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropdownCountry);
	
	$("#dropdownCountry option[value='" + optionValue + "']").prop("selected", true);
	
	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
}

function changeFormStateToNewCountry(){
	labelCountryName.text("Country Name:");
	btnAdd.val("Add")
	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
	
	btnUpdate.prop('disabled', true)
	btnDelete.prop('disabled', true)
}

function changeFormStateToBeSelectedCountry(){
	btnAdd.prop('value', 'New')
	btnUpdate.prop('disabled', false)
	btnDelete.prop('disabled', false)
	
	labelCountryName.text("Selected Country:");
	selectedText = $("#dropdownCountry option:selected").text();
	fieldCountryName.val(selectedText);
	
	countryCode = dropdownCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode);
}

function loadCountries(){
	url = contextPath + "countries/list";
	$.get(url, function(responseJson){
		dropdownCountry.empty();
		$.each(responseJson, function(idx, country){
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropdownCountry);
		})
	}).done(function(){
		btnLoad.val("Refresh Coutry List");
		showToastMessage("All coutries have been loaded");
	}).fail(function(){
		showToastMessage("ERROR: Server has been encountered error!!!");
	})
}

function showToastMessage(message){
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}