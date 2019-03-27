function readImage(fileupload, graphicimage) {
    if (fileupload.files && fileupload.files[0]) {
        console.log("Imagen: " + graphicimage)
        var reader = new FileReader();
        reader.onload = function (ev) { graphicimage.attr('src', ev.target.result); }
        reader.readAsDataURL(fileupload.files[0]);
    }
}