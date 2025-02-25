var dropdownCountry;
var listStates;
var fieldState;

$(document).ready(function(){
	dropdownCountry = $("#country");
	listStates = $("#listStates");
	fieldState = $("#state");
	
	dropdownCountry.on('change', function(){
		showSuggestionStates();
		fieldState.val('').focus();
	});
}); 

function showSuggestionStates() {
	countryId = dropdownCountry.val();
	url = contextPath + "settings/list_states_by_country/" + countryId;
	
	$.get(url, function(response) {
		listStates.empty();
		$.each(response, function(idx, state){
			$("<option>").val(state.name).text(state.name).appendTo(listStates);	
		})
	}).fail(function(e){
		alert(e);
	});
}

function checkPasswordMatch(confirmPassword){
	if(confirmPassword.value != $("#password").val()) {
		confirmPassword.setCustomValidity('Password do not match!');
	}else {
		confirmPassword.setCustomValidity('');
	}
}

function showDialog(title, message){
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}
	
function showWarningModal(message){
	showDialog('Warning', message);
}

function showErrorModal(message){
	showDialog('Error', message);
}