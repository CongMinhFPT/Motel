var citis = document.getElementById("city");
var districts = document.getElementById("district");
var wards = document.getElementById("ward");
let MAX_FILE_SIZE = 1048576;
var Parameter = {
  url: "https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json",
  method: "GET",
  responseType: "application/json",
};
var promise = axios(Parameter);
promise.then(function (result) {
  renderCity(result.data);
});

function renderCity(data) {
  for (const x of data) {
    var opt = document.createElement("option");
    opt.value = x.Name;
    opt.text = x.Name;
    opt.setAttribute("data-id", x.Id);
    citis.options.add(opt);
  }
  citis.onchange = function () {
    district.length = 2;
    ward.length = 2;
    if (this.options[this.selectedIndex].dataset.id != "") {
      const result = data.filter(
        (n) => n.Id === this.options[this.selectedIndex].dataset.id
      );
      for (const k of result[0].Districts) {
        var opt = document.createElement("option");
        opt.value = k.Name;
        opt.text = k.Name;
        opt.setAttribute("data-id", k.Id);
        district.options.add(opt);
      }
    }
    var huyenOption = document.getElementById("huyen");
    var nextOption = huyenOption.nextElementSibling;
    if (nextOption != null) {
      nextOption.remove();
    }
    var huyenOption = document.getElementById("phuong");
    var nextOption = huyenOption.nextElementSibling;
    if (nextOption != null) {
      nextOption.style.display = "none";
    }
    var selectedOption = this.options[this.selectedIndex];
    var dataId = selectedOption.getAttribute("data-id");
    document.getElementById("cityIdInput").value = dataId;
  };
  district.onchange = function () {
    ward.length = 2;
    const dataCity = data.filter(
      (n) => n.Id === citis.options[citis.selectedIndex].dataset.id
    );
    if (this.options[this.selectedIndex].dataset.id != "") {
      const dataWards = dataCity[0].Districts.filter(
        (n) => n.Id === this.options[this.selectedIndex].dataset.id
      )[0].Wards;

      for (const w of dataWards) {
        var opt = document.createElement("option");
        opt.value = w.Name;
        opt.text = w.Name;
        opt.setAttribute("data-id", w.Id);
        wards.options.add(opt);
      }
    }
    var huyenOption = document.getElementById("phuong");
    var nextOption = huyenOption.nextElementSibling;
    nextOption.remove();
    var selectedOption = this.options[this.selectedIndex];
    var dataId = selectedOption.getAttribute("data-id");
    document.getElementById("districtIdnput").value = dataId;
  };
}
document.addEventListener("DOMContentLoaded", function () {
  setTimeout(function () {
    citis.dispatchEvent(new Event("change"));
    districts.dispatchEvent(new Event("change"));
    citis.addEventListener("change", function () {
      let optionToRemove = document.getElementById("city1");
      let cityOpdistrict = document.getElementById("district1");
      let cityward1 = document.getElementById("ward1");
      if (optionToRemove != null) {
        optionToRemove.remove();
        cityOpdistrict.remove();
        cityward1.remove();
      }
    });
    districts.addEventListener("change", function () {
      let optionToRemove = document.getElementById("city1");
      let cityward1 = document.getElementById("ward1");
      let cityOpdistrict = document.getElementById("district1");
      if (optionToRemove != null) {
        cityward1.remove();
        cityOpdistrict.remove();
      }
    });
  }, 500);
  imageInput.setCustomValidity('');
});

const imageInput = document.getElementById("imageInput");
const previewImage = document.getElementById("previewImage");
const imageInputerror =document.getElementById('imageInput-error');
const submitmotel = document.getElementById('submitmotel');
let inlu =previewImage.src;
imageInput.addEventListener("change", function () {
  const file = this.files[0];
  if(file===undefined){
    previewImage.src =inlu;
    this.setCustomValidity('');
    this.required = false;
  }else{
    if(file.size > MAX_FILE_SIZE) {
      this.setCustomValidity('Ảnh đã vượt quá 1MB');
      this.required = true;
    }else{
      this.setCustomValidity('');
      this.required = false;
    }
  }
  if (file) {
    const reader = new FileReader();
    reader.onload = function (e) {
      previewImage.src = e.target.result;
    };
    reader.readAsDataURL(file);
  }
});
function setCustomMessage(inputElem, message) {
  inputElem.oninvalid = function (event) {
    event.target.setCustomValidity(message);
  }
  inputElem.oninput = function (event) {
    event.target.setCustomValidity('');

  }
}
const mota = document.getElementById('descriptions');
const city = document.getElementById('city');
const district = document.getElementById('district');
const ward = document.getElementById('ward');
const detailAddress = document.getElementById('detailAddress');
setCustomMessage(mota, 'Vui lòng nhập mô tả');
setCustomMessage(city, 'Vui lòng chọn thành phố');
setCustomMessage(district, 'Vui lòng chọn thành huyện');
setCustomMessage(ward, 'Vui lòng chọn thành phường');
setCustomMessage(detailAddress, 'Vui lòng nhập số nhà');