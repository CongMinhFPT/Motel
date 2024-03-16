var app = angular.module('paymentApp', []);

app.controller('paymentCtrl', function ($scope, $http) {

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

