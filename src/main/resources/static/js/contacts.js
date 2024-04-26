document.addEventListener("DOMContentLoaded",()=>{
    let name =document.getElementById("name")
    let phone =document.getElementById("phone")
    let content =document.getElementById("content")
    let form = document.getElementById('yourFormId'); // Thay 'yourFormId' bằng Id của form của bạn
    form.onsubmit = function(event) {
           event.preventDefault();
        const data = {
            Name: name.value,
            Phone: phone.value,
            Content: content.value,
        };
        postgg(data);
        Swal.fire({
			title: "Thành công!",
			text: "Đã gửi Feedback!",
			icon: "success",
			toast: true,
			position: 'top-end',
			showConfirmButton: false,
			timer: 3000
		})
        setTimeout(function() {
            window.location.href = '/index';
        }, 1500);
        name.value='';
        phone.value='';
        content.value='';
    }
    async function postgg(data){
        const formURL ="https://docs.google.com/forms/d/e/1FAIpQLSftK8c2jDNMpfog5qLCvJ1IUXrhcRnJs7hGDuBvlCwncsu0Rg/formResponse";
        const formData = new FormData();
        formData.append("entry.1490147000",data.Name);
        formData.append("entry.1714183618",data.Phone);
        formData.append("entry.1763820817",data.Content);
        await fetch (formURL ,{
            method:"POST",
            body:formData,
        });
    };
    let phonePattern = /((09|03|07|08|05)+([0-9]{8})\b)/g;
    function setCustomMessage(inputElem, message) {
        inputElem.oninvalid = function(event) {
            event.target.setCustomValidity(message);
        }
        inputElem.oninput = function(event) {
            event.target.setCustomValidity('');   
        }
      }
      function setCustomMessagephone(inputElem, message) {
        inputElem.oninvalid = function(event) {
            event.target.setCustomValidity(message);
        }
        inputElem.oninput = function(event) {
            event.target.setCustomValidity('');
           if(!phonePattern.test(inputElem.value)){
            event.target.setCustomValidity('Số điện thoại chưa đúng');
           } 
        }
      }

    setCustomMessage(name ,'Vui lòng nhập họ tên');
    setCustomMessagephone(phone ,'Vui lòng nhập số điện thoại');
    setCustomMessage(content ,'Vui lòng nhập nội dung Feedback');
})