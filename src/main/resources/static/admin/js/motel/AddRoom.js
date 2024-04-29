const input = document.getElementById("input");
const imageBox = document.getElementById("image-box");
const selectedImage = document.getElementById("selected-image");
const Seleclinkyoutubr = document.getElementById("idtext-youtube");
const linkyoutube = document.getElementById("link-youtube");
const linkclose = document.getElementById("exampleModal");
const texterrorfile = document.getElementById("text-error-file");
const submitluu = document.getElementById('submitluu');
input.addEventListener("change", function () {
  var filesechck = Array.from(input.files).slice(0, 6);
  var filesechckc=false;
  var indexerro = [];
  var filess = [];
  for (var i = 0; i < filesechck.length; i++) {
    var file = filesechck[i];
    if(file.size > 1048576) {
      filesechckc = true;
      indexerro.push(" vị trí "+(i+1)+" ");
      filess.push(file);
    }else{
      filess.push(file);
    }
  }
  imageBox.innerHTML = "";
  const files = filess;
  const fileserror = Array.from(input.files);
  if (fileserror.length >= 7) {
    texterrorfile.innerText = "Thông báo chỉ nhận 6 ảnh đầu ";
  }else{
    texterrorfile.innerText ='';
  }
  if(filesechckc){
    let Stringerror = indexerro.join('và');
    texterrorfile.innerText ='Ảnh tại '+Stringerror+' đã quá kích cỡ cho phép';
    submitluu.disabled = true;
  }else{
    submitluu.disabled = false;
    texterrorfile.innerText = " ";
  }
  const reader = new FileReader();
  if (files[0] != null) {
    reader.readAsDataURL(files[0]);
    reader.onload = function (event) {
      selectedImage.src = event.target.result;
    };
  } else {
    selectedImage.src = "";
  }
  async function displayImages(files, imageBox) {
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      const dataUrl = await readAsDataURL(file); 
  
      const img = document.createElement("img");
      img.src = dataUrl;
      img.classList.add("col-2");
      img.addEventListener("click", function () {
        document.querySelectorAll(".image-box img").forEach(function (item) {
          item.classList.remove("enlarged");
        });
        selectedImage.src = dataUrl;  
        img.classList.add("enlarged");
      });
      imageBox.appendChild(img);
    }
  }
  
  displayImages(files, imageBox);
function readAsDataURL(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onloadend = () => resolve(reader.result);
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });
}
});
document.getElementById('Idbutton-linkyoutube').addEventListener('click', function () {
  var inputValue = document.getElementById('Idinput-linkyoutube').value;
  let url = inputValue;
  let pattern = "https://youtu.be/";
  let result = url.replace(pattern, "");
  let linkreplace = "https://www.youtube.com/embed/";
  let urlfull = linkreplace + result;
  linkyoutube.src = urlfull;

});
linkclose.addEventListener('click', function () {
  document.getElementById('Idbutton-linkyoutube').click();
})
function validateForm(event) {
  var check = true;
  let descriptionsroom = document.getElementById('descriptionsroom');
  let descriptionsroomeroor = document.getElementById('descriptionsroom-error');
  let descriptionsroomeroor1 = document.getElementById('descriptionsroom-error1');
  //mô tả
  let length = document.getElementById('length');
  let lengtherror = document.getElementById('length-error');
  let lengtherror1 = document.getElementById('length-error1');
  // dai length
  let width = document.getElementById('width');
  let widtherror = document.getElementById('width-error');
  let widtherror1 = document.getElementById('width-error1');
  // rong width
  let categoryRoom = document.getElementById('categoryRoom');
  let categoryRoomerror = document.getElementById('categoryRoomerror');
  let categoryRoomerror1 = document.getElementById('categoryRoomerror1');
  // danh muc categoryRoom
  let video = document.getElementById('Idinput-linkyoutube');
  let videoerro = document.getElementById('video-error');
  let videoerro1 = document.getElementById('video-error1');
  // kiem tra Mô tả
  descriptionsroomeroor1.classList.remove('d-block')
  lengtherror1.classList.remove('d-block')
  widtherror1.classList.remove('d-block')
  categoryRoomerror1.classList.remove('d-block')
  videoerro1.classList.remove('d-block')
  if (descriptionsroom.value == '') {
    if (descriptionsroomeroor != null) {
      descriptionsroomeroor.classList.add('d-none');
    }
    descriptionsroomeroor1.innerText = 'không được để rỗng phần mô tả';
    descriptionsroomeroor1.classList.add('d-block');
    check = false;
  }
  if (length.value == '') {
    if (lengtherror != null) {
      lengtherror.classList.add('d-none');
    }
    lengtherror1.innerText = 'Vui lòng nhập chiều dài của phòng';
    lengtherror1.classList.add('d-block');
    check = false;
  } else if (length.value <= 0) {
    if (lengtherror != null) {
      lengtherror.classList.add('d-none');
    }
    lengtherror1.innerText = 'Vui lòng nhập chiều dài của phòng là số dương';
    lengtherror1.classList.add('d-block');
    check = false;
  }
  if (width.value == '') {
    if (widtherror != null) {
      widtherror.classList.add('d-none');
    }
    widtherror1.innerText = 'Vui lòng nhập chiều rộng của phòng';
    widtherror1.classList.add('d-block');
    check = false;
  } else if (width.value <= 0) {
    if (widtherror != null) {
      widtherror.classList.add('d-none');
    }
    widtherror1.innerText = 'Vui lòng nhập chiều rộng của phòng là số dương';
    widtherror1.classList.add('d-block');
    check = false;
  }
  if (categoryRoom.value == '') {
    if (categoryRoomerror != null) {
      categoryRoomerror.classList.add('d-none');
    }
    categoryRoomerror1.innerText = 'Vui lòng chọn loại phòng';
    categoryRoomerror1.classList.add('d-block');
    check = false;
  }  
  if(!check){
    event.preventDefault(); 
  }
  
}
document.getElementById('your-form-id').addEventListener('submit', validateForm);
  var clickimg = function(img){
     document.querySelectorAll(".image-box img").forEach(function (item) {
          item.classList.remove("enlarged");
        });
        img.classList.add("enlarged");
        selectedImage.src = img.src;
  }
  let imgElements = document.querySelectorAll('img#imgthyme');
  if(imgElements.length!==0){
    selectedImage.src = imgElements[0].src;
  
  }