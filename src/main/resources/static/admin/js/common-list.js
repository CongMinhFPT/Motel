function clearFilter(){
	window.location = moduleURL;
}
function showDeleteConfirm(link, entityName, moduleURL) {
	entityId = link.attr("entityId");
	Swal.fire({
		title: 'Bạn muốn xóa ' + entityName + '?',
		showDenyButton: true,
		showCancelButton: true,
		confirmButtonText: 'Xóa',
		denyButtonText: `Hủy bỏ`,
		cancelButtonText: `Hủy`,
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				url: link.attr("href"),
				method: 'GET',
				success: function(data) {
					var message = "Đã xóa thành công " + entityName;
					Swal.fire('Thông báo!', message, 'success').then(() => {
						window.location.href = moduleURL;
					});
				},
				error: function() {
					Swal.fire('Lỗi', 'Đã xảy ra lỗi khi xóa ' + entityName, 'error');
				}
			});
		} else if (result.isDenied) {
			Swal.fire('Hủy bỏ', '', 'info');
		}
	});
}


$(document).ready(function() {
	var messageModal = document.getElementById('messageAlert');
	if (messageModal) {
		var message = messageModal.innerText;
		Swal.fire({
			icon: 'success',
			type: 'success',
			title: 'Thông báo!',
			text: message,
			confirmButtonText: 'OK',
			toast: true,
			position: 'top-end'
		}
		);
	}
	var errorImg = document.getElementById('errorImg');
	if (errorImg) {
		var message = errorImg.innerText;
		Swal.fire({
			icon: 'success',
			type: 'success',
			title: 'Thông báo!',
			text: message,
			confirmButtonText: 'OK',
			toast: true,
			position: 'top-end'
		}
		);
	}
});

