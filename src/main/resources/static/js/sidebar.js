const notificacoes = document.getElementById("sidebarNotificacoes");
const barraLateralDireita = document.getElementById("sidebar");
const conversas = document.getElementById("sidebarConversas");

async function carregarNotificacoes() {
    notificacoes.style.display = "flex";
    barraLateralDireita.style.display = "none";
    conversas.style.display = "none";
}

async function sideBar() {
    notificacoes.style.display = "none";
    barraLateralDireita.style.display = "flex";
    conversas.style.display = "none";
}

async function carregarConversas() {
    notificacoes.style.display = "none";
    barraLateralDireita.style.display = "none";
    conversas.style.display = "flex";
}