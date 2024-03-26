var app = angular.module("myAppUpdateMotelRoom", ["ngMessages"]);
app.controller(
  "myCtrlUpdateMotelRoom",
  function ($scope, $http, $rootScope, $sce) {
    $scope.host = "http://localhost:8080";
    $scope.Checkbill = {};
    $scope.title = "dầ";
    $scope.idmotel = 0;
    $scope.idmotelroom = 0;
    $scope.namebill = "";
    $scope.numberbill = "";
    $scope.error = "";
    $scope.roomStatuses;
    $scope.selectedStatus = "";
    $scope.number = 0;
    $scope.dateroomstatus = "";
    $scope.getallbill = function (idmotel, idmotelroom) {
      var url =
        $scope.host +
        "/api/all/Motel/" +
        idmotel +
        "/BilliMotelRoom/" +
        idmotelroom;
      console.log(url);
      $http.get(url).then(
        function (response) {
          $scope.Checkbill = response.data;
          $scope.idmotel = idmotel;
          $scope.idmotelroom = idmotelroom;
          console.log($scope.Checkbill);
        },
        function (error) {
          console.log(error);
          $scope.Checkbill = "";
        }
      );
    };
    $scope.Addbil = function (key) {
      $scope.addorupdata = "Thêm";
      $scope.namebill = key;
      $scope.numberbill = "";
      $scope.error = "";
      if (key == "dien") {
        $scope.title = "Điện";
      }
      if (key == "nuoc") {
        $scope.title = "Nước";
      }
      if (key == "wifi") {
        $scope.title = "Wifi";
      }
      if (key == "giaphong") {
        $scope.title = "Giá phòng";
      }
    };
    $scope.Updatabil = function (key) {
      $scope.addorupdata = "Cập nhật";
      $scope.namebill = key;
      $scope.error = "";
      if (key == "dien") {
        $scope.title = "Điện";
        $scope.numberbill = $scope.Checkbill.ElectricityCash[0].electricityBill;
      }
      if (key == "nuoc") {
        $scope.title = "Nước";
        $scope.numberbill = $scope.Checkbill.WaterCash[0].waterBill;
      }
      if (key == "wifi") {
        $scope.title = "Wifi";
        $scope.numberbill = $scope.Checkbill.WifiCash[0].wifiBill;
      }
      if (key == "giaphong") {
        $scope.title = "Giá phòng";
        $scope.numberbill = $scope.Checkbill.RoomCash[0].roomBill;
      }
    };
    $scope.PostBill = function () {
      console.log($scope.idmotelroom);
      if (!$scope.numberbill) {
        $scope.error = "không được để trống";
      } else if (
        typeof $scope.numberbill === "string" &&
        !$scope.numberbill.match(/^\d+$/)
      ) {
        $scope.error = "phải là số";
      } else if (Number($scope.numberbill) <= 0) {
        $scope.error = "phải lớn hơn 0";
      } else {
        $scope.error = "";
        let url =
          $scope.host +
          "/api/bill/" +
          $scope.namebill +
          "/motel/" +
          $scope.idmotel +
          "/motelroom/" +
          $scope.idmotelroom;
        var data = $scope.numberbill;
        $http.post(url, data).then(
          function (response) {
            if ($scope.addorupdata == "Cập nhật") {
              Swal.fire({
                title: "Bạn có muốn cập nhật?",
                showDenyButton: true,
                showCancelButton: false,
                confirmButtonText: "Có",
                denyButtonText: "Không",
              }).then((result) => {
                if (result.isConfirmed) {
                  document.getElementById("butclose").click();
                  $scope.getallbill($scope.idmotel, $scope.idmotelroom);
                  toastr.success(
                    $scope.addorupdata + " tiền " + $scope.title,
                    $scope.addorupdata + " Thành công",
                    { positionClass: "toast-top-right" }
                  );
                }
              });
            } else if ($scope.addorupdata == "Thêm") {
              document.getElementById("butclose").click();
              $scope.getallbill($scope.idmotel, $scope.idmotelroom);
              toastr.success(
                $scope.addorupdata + " tiền " + $scope.title,
                $scope.addorupdata + " Thành công",
                { positionClass: "toast-top-right" }
              );
            }
          },
          function (error) {
            toastr.error("Đã có lỗi xảy ra thử lại sau ít phút", "Thất bại", {
              positionClass: "toast-top-right",
            });
            document.getElementById("butclose").click();
          }
        );
      }
    };

    $scope.getallroomstatus = function (idmote) {
      let url = $scope.host + "/api/all/motel/getallroomstatus/" + idmote;
      $http.get(url).then(
        function (response) {
          console.log(response.data);
          $scope.roomStatuses = response.data;
          $scope.selectedStatus = response.data.roomStatus.roomStatusId + "";
          $scope.checke = response.data.roomStatus.roomStatusId;
          $scope.checkesttus=null;
          if ($scope.checke === 3) {
            $scope.checkesttus=response.data.datestatus;
            var today = new Date(response.data.datestatus);
            var day = ("0" + today.getUTCDate()).slice(-2);
            var month = ("0" + (today.getUTCMonth() + 1)).slice(-2);
            var todayAsString = today.getUTCFullYear() + "-" + month + "-" + day;
            $scope.dateroomstatus = new Date(todayAsString);
          }
          $scope.number = response.data.number;
          console.log(response.data);
        },
        function (error) {
          console.log(error);
        }
      );
    };
    $scope.checkandupdateroomstatus = function () {
      Swal.fire({
        title: "Bạn có muốn cập nhật loại phòng",
        showDenyButton: true,
        showCancelButton: false,
        confirmButtonText: "Có",
        denyButtonText: "Không",
      }).then((result) => {
        if (result.isConfirmed) {
          if ($scope.selectedStatus == 3) {
            var myModal = new bootstrap.Modal(
              document.getElementById("staticBackdropf"),
              {
                keyboard: false,
              }
            );
            myModal.show();
          } else if ($scope.selectedStatus == 1) {
            if ($scope.number == 0) {
              let url =
                $scope.host +
                "/api/motel/roomstatus/" +
                $scope.idmotelroom +
                "/" +
                $scope.selectedStatus;
              $http.get(url).then(
                function (response) {
                  $scope.getallroomstatus($scope.idmotelroom);
                  toastr.success(
                    "Thông Báo",
                    "lưu trạng thái thành công",
                    { positionClass: "toast-top-right" }
                  );
                },
                function (error) {
                  toastr.error("Đã có lỗi xảy ra thử lại sau ít phút", "Thất bại", {
                    positionClass: "toast-top-right",
                  });
                  console.log(error);
                }
              );
            }else{
              toastr.success(
                "Thông Báo",
                "Phòng đang có người không thể lưu trạng thái chưa có người",
                { positionClass: "toast-top-right" }
              );
            }
          }else {
            let url =
            $scope.host +
            "/api/motel/roomstatus2/" +
            $scope.idmotelroom +
            "/" +
            $scope.selectedStatus;
            $http.get(url).then(
              function (response) {
                $scope.getallroomstatus($scope.idmotelroom);
                toastr.success(
                  "Thông Báo",
                  "lưu trạng thái thành công",
                  { positionClass: "toast-top-right" }
                );
              },
              function (error) {
                toastr.error("Đã có lỗi xảy ra thử lại sau ít phút", "Thất bại", {
                  positionClass: "toast-top-right",
                });
                console.log(error);
              }
            );
          }
        } else {
          $scope.getallroomstatus($scope.idmotelroom);
        }
      });
    };
    $scope.setroomstatus = function () {
      $scope.getallroomstatus($scope.idmotelroom);
    };
    $scope.updateroomstatus = function () {
      let today = new Date();
      let selected = new Date($scope.dateroomstatus);
      if ($scope.dateroomstatus === undefined) {
        $scope.errorstatus = "Phải chọn ngày";
      } else if (selected <= today) {
        $scope.errorstatus = "Ngày trả phòng phải lớn hơn ngày hiện tại";
      } else {
        $scope.errorstatus = '';
        let url =
          $scope.host +
          "/api/motel/roomstatus/" +
          $scope.idmotelroom +
          "/" +
          $scope.selectedStatus;
        $http.post(url, selected).then(
          function (response) {
            toastr.success(
              "Lưu ",
              " Thành công",
              { positionClass: "toast-top-right" }
            );
            document.getElementById("butclosee").click();
          },
          function (error) {
            toastr.error("Đã có lỗi xảy ra thử lại sau ít phút", "Thất bại", {
              positionClass: "toast-top-right",
            });
            console.log(error);
          }
        );
      }
    };
  }
);
