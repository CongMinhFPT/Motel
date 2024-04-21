var app = angular.module("favoriteRoomApp", []);
let hostFavoriteRoom = "http://localhost:8080/api/deleteFavoriteRoom";

app.controller('favoriteRoomCtrl', function ($scope, $http, $timeout, $rootScope) {

    // $scope.deleteFavoriteRoom = (favoriteRoomId) => {
    //     Swal.fire({
    //         icon: 'question',
    //         title: 'Question!',
    //         text: 'Bạn muốn xóa khỏi danh sách yêu thích?',
    //         showCancelButton: true,
    //         confirmButtonText: 'Yes',
    //         cancelButtonText: 'No'
    //     }).then((result) => {
    //         if (result.isConfirmed) {
    //             const url = `${hostFavoriteRoom}/${favoriteRoomId}`;
    //             $http.delete(url).then(resp => {
    //                 console.log(resp.data);
    //             }).catch(error => {
    //                 console.log(error);
    //             })
    //         } else if (result.dismiss === Swal.DismissReason.cancel) {
    //             Swal.fire('Cancelled', 'Bạn đã hủy!', 'error');
    //         }
    //     });

    // }

    $scope.deleteFavoriteRoom = (favoriteRoomId) => {
        Swal.fire({
            icon: 'question',
            title: 'Xác Nhận!',
            text: 'Bạn muốn xóa khỏi danh sách yêu thích?',
            showCancelButton: true,
            confirmButtonText: 'Yes',
            cancelButtonText: 'No',
            toast: true,
            position: 'top-end'
        }).then((result) => {
            if (result.isConfirmed) {
                const url = `${hostFavoriteRoom}/${favoriteRoomId}`;
                $http.delete(url).then(resp => {
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành Công!',
                        toast: true,
                        position: 'top-end',
                        timer: 1000
                    }).then(() => {
                        window.location.reload();
                    });
                }).catch(error => {
                    console.log(error);
                    Swal.fire('Error', 'Đã có lỗi xảy ra khi xóa phòng!', 'error');
                });
            }
        });
    }




    $scope.favoriteRooms = [];

    $scope.getFavoriteRoom = function () {
        var url = '/api/listFavoriteRoom';
        $http.get(url)
            .then(function (response) {
                $scope.favoriteRooms = response.data;
                console.log(response.data);
            }).catch(error => {
                console.log(error);
            })
    };

    $scope.getFavoriteRoom();
})