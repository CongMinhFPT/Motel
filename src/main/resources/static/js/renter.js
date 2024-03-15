var app = angular.module('renterApp', []);

app.controller('renterCtrl', function ($scope, $http, $location) {
    $scope.results = [];
    $scope.motelRooms = [];

    $scope.search = function () {
        let keyword = $scope.keyword;
        $http.get('/api/search?phone=' + keyword)
            .then(function (response) {
                $scope.results = response.data;
            });
    };

    $scope.showFullName = function (fullname, id) {
        $scope.fullname = fullname;
        $scope.accountId = id;
    };

    $scope.getAllMotelRoom = function () {
        let url = 'http://localhost:8080/api/motelRoom';
        $http.get(url)
            .then(function (response) {
                $scope.motelRooms = response.data;
                console.log(response.data);
            })
            .catch(function (error) {
                console.log(error);
            })
    }

    $scope.getAllMotelRoom();

    $scope.addRenter = function (accountId, renterDate, motelRoomId) {
        let urlAdd = "http://localhost:8080/api/addRenter";
        let formData = {
            accountId: accountId,
            renterDate: renterDate,
            motelRoomId: motelRoomId
        };
        $http.post(urlAdd, formData)
            .then(function (response) {
                console.log(response.data);
                Swal.fire({
                    icon: 'success',
                    title: 'Thành Công  !',
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 3000
                }).then(() => {
                    window.location.href = '/admin/addRenter';
                });
            })
            .catch(function (error) {
                if (error.status === 400) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi!',
                        text: error.data.message, // Hiển thị thông báo cụ thể từ máy chủ
                        toast: true,
                        position: 'top-end',
                        showConfirmButton: false,
                        timer: 3000
                    })
                } else if (error.status === 500) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi!',
                        text: error.data.message,
                        toast: true,
                        position: 'top-end',
                        showConfirmButton: false,
                        timer: 3000
                    })
                }
                else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi!',
                        text: 'Đã xảy ra lỗi khi thêm người thuê!',
                        toast: true,
                        position: 'top-end',
                        showConfirmButton: false,
                        timer: 3000
                    })
                }
            })
    }

    // $scope.getAllRenter = function () {
    //     let url = 'http://localhost:8080/api/renter';
    //     $http.get(url)
    //         .then(function (response) {
    //             $scope.renters = response.data;
    //             console.log(response.data);
    //         })
    //         .catch(function (error) {
    //             console.log(error);
    //         });
    // }

    // $scope.getAllRenter();
});