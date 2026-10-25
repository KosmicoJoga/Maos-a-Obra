async function concluir(botao) {

    if (!confirm("Marcar esta demanda como concluída? Ela só será finalizada quando o cliente também confirmar.")) return;

    try {
        const resposta = await fetch(`/demandas/${botao.dataset.id}/finalizar`, {
            method: "PUT"
        });

        if(resposta.ok) {
            window.location.reload();
        } else if(resposta.status === 400) {
            const erro = await resposta.json();
            alert (erro.erro);
            window.location.reload();
        } else {
            alert("Não foi possível finalizar a demanda!")
        }

    } catch (e) {
        alert("Não foi possível conectar ao servidor!");
    }

}