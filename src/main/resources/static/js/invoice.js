var app = angular.module('invoiceApp', []);

app.controller('invoiceCtrl', function ($scope, $http) {

    $scope.invoiceId = $('#invoiceIdInput').val();

    $scope.getAllInvoice = function (invoiceId) {
        let url = "http://localhost:8080/api/invoice-list/" + invoiceId;
        $http.get(url)
            .then(function (response) {
                $scope.invoices = response.data;
                console.log(response.data);
            });
    };

    $scope.getAllInvoice($scope.invoiceId);


    // $scope.makePayment = function () {
    //     var amount = $('#amountVNPay').val();
    //     var invoiceId = $('#invoiceIdVNPay').val();
    //     var url = '/pay?price=' + amount + '&invoiceId=' + invoiceId;

    //     $http.get(url)
    //         .then(function (response) {
    //             window.location.href = response.data;
    //         })
    //         .catch(function (error) {
    //             console.error('Error making payment: ', error);
    //         });
    // };

    $scope.createPayment = function (totalPrice, invoiceId) {
        $http.get('/api/payment/create_payment/' + totalPrice+'/'+invoiceId)
            .then(function (response) {
                window.location.href = response.data.url;
            })
            .catch(function (error) {
                $scope.message = 'An error occurred while processing your request.';
                console.error('Error:', error);
            })

    };
});

