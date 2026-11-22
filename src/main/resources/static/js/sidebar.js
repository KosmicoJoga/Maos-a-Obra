const notificacoes = document.getElementById("sidebarNotificacoes");
const barraLateralDireita = document.getElementById("sidebar");
const conversas = document.getElementById("sidebarConversas");

async function carregarNotificacoes() {
    notificacoes.style.display = "flex";
    barraLateralDireita.style.display = "none";
    conversas.style.display = "none";
    buscarNotificacoes();
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

// ---------- NOTIFICAÇÕES ----------
let listaNotificacoes = null;
let badgeNotificacoes = null;

function prepararNotificacoes() {
    if (!notificacoes) return;            // página sem a sidebar

    const botao = document.createElement("button");
    botao.type = "button";
    botao.className = "btn-marcar-lidas";
    botao.textContent = "Marcar todas como lidas";
    botao.onclick = marcarTodasComoLidas;

    listaNotificacoes = document.createElement("section");
    listaNotificacoes.className = "lista-notificacoes";

    notificacoes.append(botao, listaNotificacoes);

    // contador vermelho no sino
    const sino = document.querySelector('a[onclick*="carregarNotificacoes"]');
    if (sino) {
        sino.style.position = "relative";
        badgeNotificacoes = document.createElement("span");
        badgeNotificacoes.className = "badge-notificacoes";
        badgeNotificacoes.style.display = "none";
        sino.appendChild(badgeNotificacoes);
    }
}

async function buscarNotificacoes() {
    if (!listaNotificacoes) return;

    try {
        const resposta = await fetch("/api/notificacoes");
        if (!resposta.ok) return;
        renderizarNotificacoes(await resposta.json());
    } catch (e) {
        // servidor fora do ar: tenta de novo no próximo ciclo
    }
}

function renderizarNotificacoes(itens) {
    listaNotificacoes.replaceChildren();

    if (itens.length === 0) {
        const vazio = document.createElement("p");
        vazio.textContent = "Você não tem notificações.";
        listaNotificacoes.appendChild(vazio);
    }

    itens.forEach(n => {
        const item = document.createElement("a");
        item.className = "item-notificacao" + (n.lida ? "" : " nao-lida");
        item.href = n.link;
        item.onclick = (evento) => {
            evento.preventDefault();
            abrirNotificacao(n);
        };

        const texto = document.createElement("span");
        texto.textContent = n.mensagem;     // textContent: o texto traz nomes digitados por usuários

        const data = document.createElement("small");
        data.textContent = new Date(n.dataCriacao)
            .toLocaleString("pt-BR", {dateStyle: "short", timeStyle: "short"});

        item.append(texto, data);
        listaNotificacoes.appendChild(item);
    });

    const naoLidas = itens.filter(n => !n.lida).length;
    if (badgeNotificacoes) {
        badgeNotificacoes.textContent = naoLidas > 9 ? "9+" : naoLidas;
        badgeNotificacoes.style.display = naoLidas > 0 ? "flex" : "none";
    }
}

async function abrirNotificacao(n) {
    if (!n.lida) await fetch(`/api/notificacoes/${n.id}/lida`, {method: "POST"});
    window.location.href = n.link;
}

async function marcarTodasComoLidas() {
    await fetch("/api/notificacoes/lidas", {method: "POST"});
    buscarNotificacoes();
}

prepararNotificacoes();
buscarNotificacoes();
setInterval(() => { if (!document.hidden) buscarNotificacoes(); }, 10000);   // polling, como o chat