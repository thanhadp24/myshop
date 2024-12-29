
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