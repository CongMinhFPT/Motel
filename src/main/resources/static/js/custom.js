const loadingOverlays = document.getElementsByClassName('loading');

function showLoadingSpinner() {
    for (let i = 0; i < loadingOverlays.length; i++) {
        loadingOverlays[i].style.display = 'block';
    }
}

function hideLoadingSpinner() {
    for (let i = 0; i < loadingOverlays.length; i++) {
        loadingOverlays[i].style.display = 'none';
    }
}
hideLoadingSpinner();

const latitude = [[${latitude}]];
const longitude = [[${longitude}]];
const map = L.map('gg-map').setView([latitude, longitude], 17);
L.tileLayer(`https://api.maptiler.com/maps/streets-v2/256/{z}/{x}/{y}.png?key=TMaCmxPUt0XNH7bb7Wbt`, {
    attribution: '<a href="https://www.maptiler.com/copyright/" target="_blank"></a> <a href="https://www.openstreetmap.org/copyright" target="_blank"></a>'
}).addTo(map);

var marker = L.marker([latitude, longitude]).addTo(map);

const apiUrl = `https://nominatim.openstreetmap.org/search`;
let routingControl;

function displayRouteToAddress(address) {
    showLoadingSpinner();
    const currentLatLng = marker.getLatLng();
    fetch(`${apiUrl}?q=${address}&format=json`)
        .then(response => response.json())
        .then(data => {
            const newLatitude = parseFloat(data[0].lat);
            const newLongitude = parseFloat(data[0].lon);
            const waypoints = [
                L.latLng(currentLatLng.lat, currentLatLng.lng),
                L.latLng(newLatitude, newLongitude)
            ];
            if (routingControl) {
                map.removeControl(routingControl);
            }
            routingControl = L.Routing.control({
                waypoints: waypoints,
                routeWhileDragging: true
            }).addTo(map);
            hideLoadingSpinner();
        })
        .catch(error => console.error('Error:', error));
}

const searchButton = document.querySelector('.btn-location-search');
searchButton.addEventListener('click', function () {
    const address = document.getElementById('inp-location').value;
    displayRouteToAddress(address);
});
