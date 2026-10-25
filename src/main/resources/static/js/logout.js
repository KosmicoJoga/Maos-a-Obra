async function logout() {
    const resposta = await fetch("/login/logout", {method: "POST"});

    if (resposta.ok) {
        window.location.href = "/";
    } else {
        alert("Erro ao encerrar a sessão");
    }
}