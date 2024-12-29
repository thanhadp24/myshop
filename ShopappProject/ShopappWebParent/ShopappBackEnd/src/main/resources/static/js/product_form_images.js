var extraImgCounter = 0;

$(document).ready(function() {
	$("input[name='extraImage']").each(function(idx) {
		extraImgCounter++;
		$(this).change(function(){
			if(!checkFileSize(this)){
				return;
			}	
			showExtraImageThumnail(this, idx);
		});
	});	
});

function showExtraImageThumnail(fileInput, idx){
	var file = fileInput.files[0];
	
	fileName = file.name;
	
	imageNameHiddenField = $("#imageName" + idx);
	if(imageNameHiddenField.length){
		imageNameHiddenField.val(fileName);
	} 
	
	var reader = new FileReader();
	reader.onload = function(e){
		$("#extraThumbnail" + idx).attr("src", e.target.result);
	}
	reader.readAsDataURL(file);
	if(idx >= extraImgCounter - 1){
		addExtraImageSession(idx + 1);
	}
}

function addExtraImageSession(idx){
	var html = `<div class="col border m-3 p-2" id="divExtraImg${idx}">
				<div id='extraImgHeader${idx}'><label>Extra Image #${idx+1}:</label></div>
				<div class="m-2">
					<img id="extraThumbnail${idx}" class="img-fluid" 
					src="${defaultImgThumbnailSrc}">
				</div>
				<div>
					<input type="file" name="extraImage"
					 accept="image/png, image/jpg, image/jpeg"
					 onchange="showExtraImageThumnail(this, ${idx})">
				</div>
			</div>`;
	var htmlRemove = `<a class='btn fa-regular fa-circle-xmark fa-2xl float-right' 
		href="javascript:removeExtraImage(${idx - 1})"
		title='Remove this image'></a>`;
	$("#divProductImages").append(html);
	$("#extraImgHeader" + (idx-1)).append(htmlRemove);
	extraImgCounter++;
}

function removeExtraImage(idx){
	$("#divExtraImg" + idx).remove();
}
