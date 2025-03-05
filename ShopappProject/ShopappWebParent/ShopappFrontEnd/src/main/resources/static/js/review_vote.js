
$(document).ready(function(){
	$(".linkVoteReview").on("click", function(e){
		e.preventDefault();
		voteReview($(this));
	})
})

function voteReview(link){
	url = link.attr("href");
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue)
		}
	}).done(function(voteResult){
		console.log(voteResult)
		
		if(voteResult.successful){
			$("#modalDialog").on("hide.bs.modal", function(e){
				updateVoteCountAndIcon(link, voteResult);
			})
		}
		
		showDialog("review", voteResult.message)
	}).fail(function(){
		showErrorModal("Error voting message")
	})
}

function updateVoteCountAndIcon(link, voteResult){
	reviewId = link.attr("reviewId")
	voteUpLink= $("#linkVoteUp-" + reviewId);
	voteDownLink= $("#linkVoteDown-" + reviewId);
	
	$("#voteCount-" + reviewId).text(voteResult.voteCount + " votes");
	
	message = voteResult.message;
	
	if(message.includes("successfully voted up")){
		hightLightVoteUpLink(link, voteDownLink)
	}
	else if(message.includes("successfully voted down")){
		hightLightVoteDownLink(link, voteUpLink)
	}else if(message.includes("unvoted up")){
		unHightLightVoteUpLink(link)
	}else if(message.includes("unvoted down")){
		unHightLightVoteDownLink(link)
	}
	
}

function unHightLightVoteDownLink(link){
	link.removeClass("fas").addClass("far");
	link.attr("title", "vote down this review");
}

function unHightLightVoteUpLink(link){
	link.removeClass("fas").addClass("far");
	link.attr("title", "vote up this review");
}

function hightLightVoteUpLink(link, voteDownLink){
	link.removeClass("far").addClass("fas");
	link.attr("title", "Undo vote up this review");
	voteDownLink.removeClass("fas").addClass("far");
}


function hightLightVoteDownLink(link, voteUpLink){
	link.removeClass("far").addClass("fas");
	link.attr("title", "Undo vote down this review");
	voteUpLink.removeClass("fas").addClass("far");
}