var citis = document.getElementById("city");
var districts = document.getElementById("district");
var wards = document.getElementById("ward");
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
  }, 1000);
});

const imageInput = document.getElementById("imageInput");
const previewImage = document.getElementById("previewImage");

imageInput.addEventListener("change", function () {
  const file = this.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = function (e) {
      previewImage.src = e.target.result;
    };
    reader.readAsDataURL(file);
  }
});
