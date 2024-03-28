document.addEventListener("DOMContentLoaded",()=>{
    let name =document.getElementById("name")
    let phone =document.getElementById("phone")
    let content =document.getElementById("content")
    let submit =document.getElementById("submit")
    submit.addEventListener("click", (e)=>{
        e.preventDefault();
      if(check()){
        const data ={
            Name: name.value ,
            Phone: phone.value ,
            Content: content.value,
          };
        postgg(data)
        Swal.fire({
            title: 'Đã gửi Feedback',
            icon: 'success',
            timer: 500,
            showConfirmButton: false
        });
        name.value='';
       phone.value='';
       content.value='';
      }
   
  
    });
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
    var check = function(){
     var checktrue = true;
        if(name.value ===''){
           let errorname = document.getElementById('errorname');
           errorname.innerText='Vui lòng nhập họ tên';
           checktrue=false;
        }else{
            errorname.innerText='';
        }
        if(phone.value === '') {
            let errorphone = document.getElementById('errorphone');
            errorphone.innerText = 'Vui lòng nhập số điện thoại';
            checktrue =false;
       } else {
            let phonePattern = /((09|03|07|08|05)+([0-9]{8})\b)/g;
       
            if (!phonePattern.test(phone.value)) {
                 let errorphone = document.getElementById('errorphone');
                 errorphone.innerText = 'Số điện thoại không hợp lệ';
                 checktrue = false;
            }else{
                errorphone.innerText = '';
            }
       }
       if(content.value===''){
        let errorcontent = document.getElementById('errorcontent');
        errorcontent.innerText = 'Vui lòng nhập Nội dung Feedback';
        checktrue =false;
       }else{
        errorcontent.innerText = '';
       }
       return checktrue;
    }
})