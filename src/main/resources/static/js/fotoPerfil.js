document.addEventListener("DOMContentLoaded", () => {

    const input = document.getElementById("inputFotoPerfil");
    const imagem = document.getElementById("imagemPerfil");

    if (!input || !imagem) return;

    input.addEventListener("change", async () => {

        const arquivo = input.files[0];

        if (!arquivo) return;

        const formData = new FormData();
        formData.append("foto", arquivo);

        try {

            const resposta = await fetch("/perfil/foto", {
                method: "POST",
                body: formData,
            });

            if (!resposta.ok) {
                const erro = await resposta.json();
                alert(erro.erro || "Não foi possível atualizar a foto!");
                return;
            }

            imagem.src = `/perfil/foto/${imagem.dataset.id}?t=${Date.now()}`;

        } catch (e) {
            alert("Erro ao enviar a imagem!");
        }

    });

});