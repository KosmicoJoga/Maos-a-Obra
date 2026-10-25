async function iniciarConversa(botao) {
    const resposta = await fetch("/api/conversas", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({idOutro: Number(botao.dataset.outro)})
    });

    if (resposta.ok) {
        const conversa = await resposta.json();
        window.location.href = "/chat/" + conversa.id;
    } else {
        alert("Não foi possível iniciar a conversa.");
    }
}