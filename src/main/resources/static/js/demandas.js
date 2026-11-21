async function concluir(botao) {

    if (!confirm("Marcar esta demanda como concluída? Ela só será finalizada quando o cliente também confirmar.")) return;

    try {
        const resposta = await fetch(`/demandas/${botao.dataset.id}/finalizar`, {
            method: "PUT"
        });

        if (resposta.ok) {
            window.location.reload();
        } else if (resposta.status === 400) {
            const erro = await resposta.json();
            alert(erro.erro);
            window.location.reload();
        } else {
            alert("Não foi possível finalizar a demanda!")
        }

    } catch (e) {
        alert("Não foi possível conectar ao servidor!");
    }

}

async function candidatar(botao) {
    if (!confirm("Enviar sua candidatura para esta demanda? O cliente desta demanda poderá escolher você para o serviço")) return;

    try {
        const resposta = await fetch(`/demandas/${botao.dataset.id}/candidatar`, {method: "POST"});

        if (resposta.ok)
            window.location.reload();
        else if (resposta.status === 400) {
            const erro = await resposta.json();
            alert(erro.erro);
            window.location.reload();
        } else
            alert("Não foi possível enviar a candidatura!");
    } catch (e) {
        alert("Não foi possível conectar ao servidor!");
    }
}

async function escolherPrestador(botao) {
    if (!confirm("Escolher este prestador para a demanda? As outras candidaturas serão encerradas.")) return;

    try {
        const resposta = await fetch(`/demandas/${botao.dataset.demanda}/escolher/${botao.dataset.trabalhador}`, {method: "POST"});

        if (resposta.ok)
            window.location.reload();
        else if (resposta.status === 400) {
            const erro = await resposta.json();
            alert(erro.erro);
            window.location.reload();
        } else
            alert("Não foi possível escolher o prestador.");
    } catch (e) {
        alert("Não foi possível conectar ao servidor");
    }
}



