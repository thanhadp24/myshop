var counter = 0;
function addMoreDetail(){
	html = `
		<div class="form-inline" id="divDetail${counter+1}">
			<input type="hidden" name="detailIds" value="0">
			<label class="m-3">Name:</label>
			<input type="text" class="form-control w-25" name="detailNames" maxlength="255">
			<label class="m-3">Value:</label>
			<input type="text" class="form-control w-25" name="detailValues" maxlength="255">
		</div>
	`;
	$("#divProductDetails").append(html);

	var htmlRemove = `<a class='m-2 icon-gray fa-regular fa-circle-xmark fa-xl float-right' 
			href="javascript:removeDetail(${counter})"
			title='Remove this detail'></a>`;
			
	$("#divDetail" + counter).append(htmlRemove);
	$("input[name='detailNames']").last().focus();
	counter++;
}

function removeDetail(idx){
	$("#divDetail" + idx).remove();
}

