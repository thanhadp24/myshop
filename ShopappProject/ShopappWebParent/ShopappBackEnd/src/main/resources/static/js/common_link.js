function clearFilter(){
	window.location = moduleURL;
}

function confirmDeleteModal(link, entity){
	entityId = link.attr('entityId');
	$('#yes-btn').attr("href", link.attr("href"));
	$('#confirmText').text('Are you sure to delete ' + entity +' ID: ' + entityId + "?");
	$('#confirmModal').modal();
}

	