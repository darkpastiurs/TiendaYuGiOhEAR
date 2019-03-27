package sv.com.tienda.web.converter;

import sv.com.tienda.business.entity.LimitacionCarta;
import sv.com.tienda.web.utils.ConverterUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.util.logging.Logger;

@FacesConverter(value = "LimitacionConverter")
public class LimitacionConverter implements Converter {
    private static final Logger LOG = Logger.getLogger(LimitacionConverter.class.getName());

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        try {
            return ConverterUtils.getAsObject(LimitacionCarta.class, s);
        } catch (NullPointerException | ConverterException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return ConverterUtils.getAsString(o);
    }
}
