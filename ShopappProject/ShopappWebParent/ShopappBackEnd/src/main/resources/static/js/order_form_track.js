var trackCount;

$(document).ready(function(){
	trackCount = $(".hiddenTrackId").length;
	$("#trackList").on("click", ".linkRemoveTrack", function(e){
		e.preventDefault();
		deleteTrack($(this))
		updateTrackCountNumbers()
	})
	
	$("#track").on("click", "#linkAddTrack", function(e){
		e.preventDefault()
		addNewTrackRecord()
	})
	
	$("#trackList").on("change", ".dropdownStatus", function(e){
		dropdownList = $(this)
		rowNumber = dropdownList.attr("rowNumber")
		selectedOption = $("option:selected", dropdownList)
		
		defaultNote = selectedOption.attr("defaultDescription")
		$("#trackNote" + rowNumber).text(defaultNote)
	})
})

function deleteTrack(link){
	rowNumber = link.attr("rowNumber")
	$("#rowTrack" + rowNumber).remove()
	$("#lineTrackBlank" + rowNumber).remove()
}

function updateTrackCountNumbers(){
	$(".divCountTrack").each(function(idx, e){
		e.innerHTML = "" + (idx + 1)
	})
}

function addNewTrackRecord(){
	html = generatedTrackCode()
	$("#trackList").append(html)
	updateTrackCountNumbers()
}

function generatedTrackCode(){
	nextCount = trackCount + 1;
	trackCount++;
	rowId = 'rowTrack' + nextCount;
	currentDateTime = formatCurrentDateTime()
	trackId = 'trackNote' + nextCount;
	lineTrackBlankId = 'lineTrackBlank' + nextCount;
	
var html = `
<div class="row border rounded p-2" id='${rowId}'>
	<input type="hidden" name="trackId" value="0" class="hiddenTrackId">
	
	<div class="col-2">
		<div class="divCountTrack">${nextCount}</div>
		<div class="mt-1">
			<a class="fa-solid fa-trash text-dark linkRemoveTrack"
				href="" rowNumber=${nextCount}></a>
		</div>
	</div>
	
	<div class="col-10">
		<div class="form-group row">
			<label class="col-form-label">Time:</label>
			<div class="col">
				<input type="datetime-local" name="trackDate" 
				value="${currentDateTime}" class="form-control"
				style="max-width: 300px">
			</div>
		</div>
		<div class="form-group row">
			<label class="col-form-label">Status:</label>
			<div class="col">
				<select name="trackStatus" class="form-control dropdownStatus"
					required style="max-width: 150px" rowNumber="${nextCount}">`;
					
	html += $("#trackStatusOption").clone().html();
		
	html +=	`</select>
			</div>
		</div>
		<div class="form-group row">
			<label class="col-form-label">Notes:</label>
			<div class="col">
				<textarea rows="2" cols="10" class="form-control" name="trackNote"
					style="max-width: 300px" id="${trackId}" required>
					</textarea>
			</div>
		</div>
	</div>
	<div id="${lineTrackBlankId}" class="row">&nbsp;</div>
</div>`

return html;
}

function formatCurrentDateTime(){
	date = new Date()
	year = date.getFullYear()
	month = date.getMonth() + 1
	day = date.getDate()
	hour = date.getHours()
	minute = date.getMinutes()
	second = date.getSeconds()
	
	if(month < 10) month = "0" + month
	if(day < 10) day = "0" + day
	if(hour < 10) hour = "0" + hour
	if(minute < 10) minute = "0" + minute
	if(second < 10) second = "0" + second
	
	return year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second;
}