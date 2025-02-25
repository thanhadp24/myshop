var returnModal;
var returnTitle;
var fieldNote;
var sendBtn;
var cancelBtn;
var orderId;
var divReason;
var divMessage;

$(document).ready(function(){
	returnModal = $("#returnModal");
	returnTitle = $("#returnOrderTitleModal");
	fieldNote = $("#returnNotes");	
	sendBtn = $("#send-btn");
	cancelBtn = $("#cancel-btn");
	divReason = $("#divReason");
	divMessage = $("#divMessage");
		
	handleReturnOrderLink();
})

function showReturnModal(link){
	divMessage.hide();
	divReason.show();
	
	orderId = link.attr("orderId");
	returnTitle.text("Return order id #" + orderId);
	sendBtn.attr("href", link.attr("href"));
	returnModal.modal();
}

function showMessgeReturnModal(){
	divMessage.show();
	divReason.hide();
	sendBtn.hide();
	cancelBtn.text("Close");
	
}

function handleReturnOrderLink(){
	$(".linkReturn").on("click", function(e){
		e.preventDefault();
		showReturnModal($(this));
	})
}

function submitReturnOrderForm(){
	reason = $("input[name='returnReason']:checked").val();
	notes = fieldNote.val();
	sendReturnOrderRequest(reason, notes);
	return false;
}

function sendReturnOrderRequest(reason, notes){
	url = contextPath + "orders/return";
	params = {
		'orderId': orderId,
		'reason': reason,
		'note': notes
	}
	$.ajax({
		type: "POST",
		url: url,
		data: JSON.stringify(params),
		contentType: 'application/json',
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue)
		}
	}).done(function(response){
		showMessgeReturnModal();
		updateStatusAndHideReturnBtn(orderId);
	}).fail(function(){
		showErrorModal("Error while removing product quantity")
	})
}

function updateStatusAndHideReturnBtn(orderId){
	$(".textStatus" + orderId).each(function(idx, e){
		$(this).text("RETURN_REQUESTED");
	})
	
	$(".linkReturn" + orderId).each(function(idx, e){
		$(this).hide()
	})
}