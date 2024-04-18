function readURL(input, thumbimage) {
	if (input.files && input.files[0]) { //Sử dụng  cho Firefox - chrome
		var reader = new FileReader();
		reader.onload = function(e) {
			$("#thumbimage").attr('src', e.target.result);
		}
		reader.readAsDataURL(input.files[0]);
	}
	else { // Sử dụng cho IE
		$("#thumbimage").attr('src', input.value);

	}
	$("#thumbimage").show();
	$('.filename').text($("#uploadfile").val());
	$('.Choicefile').css('background', '#14142B');
	$('.Choicefile').css('cursor', 'default');
	$(".removeimg").show();
	$(".Choicefile").unbind('click');

}
$(document).ready(function() {
	$(".Choicefile").bind('click', function() {
		$("#uploadfile").click();

	});
	$(".removeimg").click(function() {
		$("#thumbimage").attr('src', '').hide();
		$("#myfileupload").html('<input type="file" id="uploadfile"  onchange="readURL(this);" />');
		$(".removeimg").hide();
		$(".Choicefile").bind('click', function() {
			$("#uploadfile").click();
		});
		$('.Choicefile').css('background', '#14142B');
		$('.Choicefile').css('cursor', 'pointer');
		$(".filename").text("");
	});
});


function showModalDialog(title, message) {
	$('#modalTitle').text(title);
	$('#modalBody').text(message);
	$('#modalDialog').modal('show');

}
function showErrorModal(message) {
	showModalDialog("Thất bại", message);
}
function showWarningModal(message) {
	showModalDialog("Cảnh báo", message);
}

// load ảnh sản phẩm
$(document).ready(function() {
	$("#buttonCancel").on("click", function(){
		window.location = moduleURL;
	});
	
	$("#fileImage").change(function(){
		if(!checkFileSize(this)){
			return;
		}

			showImageThubnail(this);
		
		
	});
});
function showImageThubnail(fileInput){
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e){
		$("#thumbnail").attr("src", e.target.result);
		
	};
	reader.readAsDataURL(file);
}

function checkFileSize(fileInput){
	fileSize = fileInput.files[0].size;

	if (fileSize > MAX_FILE_SIZE) {
		fileInput.setCustomValidity("Bạn vừa chọn ảnh vượt quá kích thước cho phép " + MAX_FILE_SIZE + "bytes!");
		fileInput.reportValidity();
		
		return false;
	} else {
		fileInput.setCustomValidity("");
		return true;
	}	
}
// end load image














