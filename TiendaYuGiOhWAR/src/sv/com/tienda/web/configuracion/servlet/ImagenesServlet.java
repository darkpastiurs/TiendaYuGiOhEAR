package sv.com.tienda.web.configuracion.servlet;

import org.apache.commons.lang.StringUtils;
import sv.com.tienda.business.utils.Constantes;
import sv.com.tienda.web.utils.ArchivosUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet(value = "/imagenes/*", name = "ImageServlet")
public class ImagenesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getPathInfo();
        File imagen = null;
        String nombreImagen = null;
        Path imagenOSDir = null;
        try {
            if(StringUtils.contains(url, "ilustraciones")){
                nombreImagen = StringUtils.remove(url, "/ilustraciones/");
                imagenOSDir = new ArchivosUtils().getDirectorioSubida(Constantes.CARPETA_IMAGENES_ILUSTRACIONES);
                imagen = new File(imagenOSDir.toString(), nombreImagen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(imagen != null){
            resp.setHeader("Content-type", getServletContext().getMimeType(nombreImagen));
            resp.setHeader("Content-Length", String.valueOf(imagen.length()));
            resp.setHeader("Content-Disposition", "inline; filename=\"" + nombreImagen + "\"");
            Files.copy(imagen.toPath(), resp.getOutputStream());
        }
    }
}
