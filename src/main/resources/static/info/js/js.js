document.addEventListener("DOMContentLoaded", () => {
    const boton = document.getElementById("boton");
    const guardarColor = localStorage.getItem("theme") || "light";

    document.documentElement.setAttribute("data-theme", guardarColor);


    boton.addEventListener("click", () => {
        const colorActual = document.documentElement.getAttribute("data-theme");
        const nuevoColor = colorActual === "light" ? "dark" : "light";

        document.documentElement.setAttribute("data-theme", nuevoColor);
        localStorage.setItem("theme", nuevoColor);
    });
});