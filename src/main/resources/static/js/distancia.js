// fórmula haversine para calcular a distância de 2 pontos numa esfera (Terra)
function haversinekm(lat1, lon1, lat2, lon2) {
    const R = 6371; // raio da Terra em km
    const toRad = g => g * Math.PI / 180;
    const dLat = toRad(lat2 - lat1);
    const dLon = toRad(lon2 - lon1);

    const a = Math.sin(dLat / 2) ** 2 + Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * Math.sin(dLon / 2) ** 2;

    return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
}

// calculo de distância da demanda com a localização atual do computador
function mostrarDistancias() {
    if (!navigator.geolocation) return;

    navigator.geolocation.getCurrentPosition(pos => {
        const minhaLat = pos.coords.latitude;
        const minhaLon = pos.coords.longitude;

        document.querySelectorAll(".card-demanda").forEach(card => {
            if (!card.dataset.lat || !card.dataset.lng) return; // demanda sem coordenada

            const km = haversinekm(minhaLat, minhaLon, Number(card.dataset.lat), Number(card.dataset.lng));

            card.querySelector(".distancia").textContent = "Aproximadamente " + km.toFixed(1).replace(".", ",") + " km de você";
        });
    });
}

mostrarDistancias();