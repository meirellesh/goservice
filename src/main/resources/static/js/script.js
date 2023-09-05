document.addEventListener("DOMContentLoaded", function () {
    const filtroNomeInput = document.getElementById("filtroNome");
    const servicosTable = document.getElementById("servicosTable").getElementsByTagName("tbody")[0];

    filtroNomeInput.addEventListener("input", function () {
        const filtro = filtroNomeInput.value.toLowerCase();
        const rows = servicosTable.getElementsByTagName("tr");

        for (let i = 0; i < rows.length; i++) {
            const nomeServico = rows[i].getElementsByTagName("td")[0].textContent.toLowerCase();
            if (nomeServico.includes(filtro)) {
                rows[i].style.display = "";
            } else {
                rows[i].style.display = "none";
            }
        }
    });
});
