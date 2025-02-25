var btnLoadForStates;
var dropdownCountryForStates;
var dropdownStates;
var btnAddState;
var btnUpdateState;
var btnDeleteState;
var fieldStateName;
var labelStateName;

$(document).ready(function(){
	btnLoadForStates = $("#btnLoadForStates");
	dropdownCountryForStates = $("#dropdownCountryForStates");
	dropdownStates = $("#dropdownStates");
	btnAddState = $("#btnAddState");
	btnUpdateState = $("#btnUpdateState");
	btnDeleteState = $("#btnDeleteState");
	fieldStateName = $("#fieldStateName");
	labelStateName = $("#labelStateName");
	
	btnLoadForStates.on('click', function(){
		loadCountriesForStates();
	})
	
	dropdownCountryForStates.on('change', function(){
		loadStatesForCountry();
	})
	
	dropdownStates.on('change', function(){
		changeFormStateToSelectedState();
	})
	
	btnAddState.on('click', function(){
		if(btnAddState.val() === "Add"){
			addState();
		}else {
			changeFormStateToNew();
		}
	})
	
	btnUpdateState.on('click', function(){
		updateState();
	})
	
	btnDeleteState.on('click', function(){
		deleteState();
	})

})

function deleteState(){
	stateId = dropdownStates.val();
	
	url = contextPath + "states/delete/" + stateId;
		
	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
	})
	.done(function(){
		$("#dropdownStates option[value='" + stateId + "']").remove();
		changeFormStateToNew();
		showToastMessage("The state has been deleted");
	}).fail(function(){
		showToastMessage("ERROR: Server has been encountered error!!!");
	})
}

function updateState(){
	if(!validateFormState()) return;
	
	url = contextPath + "states/save";
	stateName = fieldStateName.val();
	stateId = dropdownStates.val();

	selectedCountry = $("#dropdownCountryForStates option:selected");
	countryId =  selectedCountry.val();
	countryName = selectedCountry.text();
	
	jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}}
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId){
		$("#dropdownStates option:selected").text(stateName);
		showToastMessage("The new country has been added");
	}).fail(function(){
		showToastMessage("ERROR: Server has been encountered error!!!");
	})
}

function validateFormState(){
	formState = document.getElementById("formState");
	if(!formState.checkValidity()){
		formState.reportValidity();
		return false;
	}
	return true;
}

function addState(){
	if(!validateFormState()) return;
	
	url = contextPath + "states/save";
	stateName = fieldStateName.val();
	
	selectedCountry = $("#dropdownCountryForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	
	jsonData = {name: stateName, country: {id: countryId, name: countryName}}
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId){
		selectNewlyAddedState(stateId, stateName);
		showToastMessage("The new country has been added");
	}).fail(function(){
		showToastMessage("ERROR: Server has been encountered error!!!");
	})
}

function selectNewlyAddedState(stateId, stateName){
	$("<option>").val(stateId).text(stateName).appendTo(dropdownStates);
	
	$("#dropdownStates option[value='" + stateId + "']").prop("selected", true);
	
	fieldStateName.val("").focus();
}

function changeFormStateToNew(){
	labelStateName.text("State/Province Name:");
	btnAddState.val("Add")
	fieldStateName.val("").focus();
	
	btnUpdateState.prop('disabled', true)
	btnDeleteState.prop('disabled', true)
}

function changeFormStateToSelectedState(){
	btnAddState.prop('value', 'New')
	btnUpdateState.prop('disabled', false)
	btnDeleteState.prop('disabled', false)
	
	labelStateName.text("Selected State:");
	selectedText = $("#dropdownStates option:selected").text();
	fieldStateName.val(selectedText);
}

function loadCountriesForStates(){
	url = contextPath + "countries/list";
	$.get(url, function(responseJson){
		dropdownCountryForStates.empty();
		$.each(responseJson, function(idx, country){
			$("<option>").val(country.id).text(country.name).appendTo(dropdownCountryForStates);
		})
	}).done(function(){
		btnLoadForStates.val("Refresh Coutry List");
		showToastMessage("All coutries have been loaded");
	}).fail(function(){
		showToastMessage("ERROR: Server has been encountered error!!!");
	})
}

function loadStatesForCountry(){
	countryId = $("#dropdownCountryForStates option:selected").val();
	url = contextPath + "states/countries/" + countryId;
	$.get(url, function(responseJson){
		dropdownStates.empty();
		$.each(responseJson, function(idx, state){
			$("<option>").val(state.id).text(state.name).appendTo(dropdownStates);
		})
	}).done(function(){
		btnLoad.val("Refresh Coutry List");
		showToastMessage("All coutries have been loaded");
	}).fail(function(){
		showToastMessage("ERROR: Server has been encountered error!!!");
	})
}
