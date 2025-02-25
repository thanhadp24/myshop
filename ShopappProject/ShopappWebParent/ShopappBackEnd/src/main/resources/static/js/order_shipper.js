var confirmText;
var confirmModal;
var yesBtn;
var noBtn;
var iconNames = {
	'PICKED': 'fa-people-carry-box',
	'SHIPPING': 'fa-truck-fast',
	'DELIVERED': 'fa-box-open',
	'RETURNED': 'fa-rotate-left'
}

$(document).ready(function(){
	confirmText = $("#confirmText");
	confirmModal = $("#confirmModal");
	yesBtn = $("#yes-btn");
	noBtn = $("#no-btn");
	
	$(".linkUpdateStatus").on('click', function(e){
		e.preventDefault();
		link = $(this);
		showUpdateConfirmModal(link);
	})
	
	addEventHandlerForYesBtn()
})

function addEventHandlerForYesBtn(){
	yesBtn.on("click", function(e){
		e.preventDefault();
		sendRequestToUpdateStatus($(this))
	})
}

function sendRequestToUpdateStatus(button){
	url = button.attr("href")
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response){
		showMessageModal("Order updated successfully");
		updateIconColor(response.orderId, response.status);
	}).fail(function(err){
		showMessageModal("Error updating order status");
	})
}

function updateIconColor(orderId, status){
	$("#link" + status + orderId)
	.replaceWith("<i class='fa-solid " + iconNames[status] + " fa-2xl icon-green'></i>")
}

function showUpdateConfirmModal(link){
	noBtn.text("No");
	yesBtn.show();
	orderId = link.attr("orderId");
	status = link.attr("status");
	confirmText.text("Are you sure to update status of the order #" + orderId + " to " + status + "?");
	yesBtn.attr("href", link.attr("href"))

	confirmModal.modal();
}

function showMessageModal(message){
	noBtn.text("Close");
	yesBtn.hide();
	confirmText.text(message);
}
