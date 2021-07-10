$(document).ready(function () {
    $("#search").click(function () {
       const text = document.getElementById("search-input").value;
       $('#jstree').jstree(true).search(text, true, true, '#');
    });
});