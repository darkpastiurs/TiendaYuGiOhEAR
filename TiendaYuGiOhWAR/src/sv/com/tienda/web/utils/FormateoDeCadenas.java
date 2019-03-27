package sv.com.tienda.web.utils;

import org.apache.commons.lang.StringUtils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class FormateoDeCadenas {

    public static final String formatoURLEdicion(String datoSeleccionado){
        String nombreDatoFormateado = "";
        String normalizacionString = Normalizer.normalize(datoSeleccionado, Normalizer.Form.NFD);
        Pattern patron = Pattern.compile("\\p{InCOMBINING_DIACRITICAL_MARKS}+");
        nombreDatoFormateado = patron.matcher(normalizacionString).replaceAll("");
        nombreDatoFormateado = StringUtils.lowerCase(nombreDatoFormateado);
        nombreDatoFormateado = StringUtils.replaceChars(nombreDatoFormateado, " ", "-");
        return nombreDatoFormateado;
    }

}
