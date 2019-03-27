package sv.com.tienda.web.utils;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.text.StrSubstitutor;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArchivosUtils {
    private final List<File> archivosPorSubir;
    private static final Logger LOG = Logger.getLogger(ArchivosUtils.class.getName());

    public ArchivosUtils() {
        archivosPorSubir = new ArrayList<>();
    }

    public void añadirArchivoPorSubir(File archivo){
        archivosPorSubir.add(archivo);
    }

    public void confirmarArchivoSubido(File archivo){
        archivosPorSubir.remove(archivo);
    }

    public void eliminarArchivos(){
        try {
            archivosPorSubir.stream().forEach(archivo -> {
                archivo.delete();
            });
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ArchivosUtils][eliminarArchivos][Excepcion] -> ", e);
        }
    }

    public Path getDirectorioSubida(String rutaTipoArchivo) throws Exception{
        try {
            Map directorioOS = new HashMap();
            if(SystemUtils.IS_OS_WINDOWS){
                directorioOS.put("dirOS", "C:/Tienda");
            } else if (SystemUtils.IS_OS_LINUX){
                directorioOS.put("dirOS", "/opt/tienda/uploads");
            }
            StrSubstitutor substitutor = new StrSubstitutor(directorioOS);
            String subidaDir = substitutor.replace(rutaTipoArchivo);
            File directorioSubida = new File(subidaDir);
            if(!directorioSubida.exists()){
                if(directorioSubida.mkdirs()){
                    return directorioSubida.toPath();
                }
            } else {
                return directorioSubida.toPath();
            }
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ArchivosUtils][getDirectorioSubida][Excepcion] -> ", e);
            throw new Exception(e);
        }
    }
}
