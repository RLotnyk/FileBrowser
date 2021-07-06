function file_upload_server(path, file) {
    const xhr = new XMLHttpRequest();
    const form = new FormData();
    form.append("path", path);
    form.append("file", file);
    xhr.open("POST", "upload", false);
    xhr.send(form);
}

window.onload = function () {
        const btn = document.getElementById('button');
        btn.addEventListener('click', function () {
                const path = $('#jstree').jstree('get_selected')[0];
                const file = document.getElementById("file").files[0];
                file_upload_server(path, file);
        }, false);
}

