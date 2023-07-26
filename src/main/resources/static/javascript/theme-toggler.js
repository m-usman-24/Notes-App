
    // Define the getCookie function
    function getCookie(name) {
    let nameEQ = name + "=";
    let ca = document.cookie.split(';');
    for(let i=0; i < ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0)===' ') c = c.substring(1);
    if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length);
}
    return null;
}

    $(document).ready(function() {
    let theme = getCookie('theme');
        console.log('Theme from cookie:', theme);
    if (theme) {
    document.documentElement.setAttribute("data-theme", theme);
}
});

    document.addEventListener('DOMContentLoaded', function() {
    document.getElementById("theme-toggle").addEventListener("click", function (event) {
        event.preventDefault(); // prevent default action
        let currentTheme = document.documentElement.getAttribute("data-theme");
        const newTheme = currentTheme === "light" ? "dark" : "light";
        document.documentElement.setAttribute("data-theme", newTheme);
        currentTheme = newTheme; // update current theme after toggling

        // here we update the theme cookie with the new theme
        let date = new Date();
        date.setTime(date.getTime() + (365 * 24 * 60 * 60 * 1000)); // 1 year
        document.cookie = "theme=" + currentTheme + ";expires=" + date.toUTCString() + ";path=/";
    });
});
