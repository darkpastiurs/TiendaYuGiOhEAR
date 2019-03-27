package sv.com.tienda.web.converter;

import sv.com.tienda.business.entity.FlechaLink;
import sv.com.tienda.web.utils.ConverterUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "FlechasLinkConverter")
public class FlechasLinkConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        try {
            return ConverterUtils.getAsObject(FlechaLink.class, s);
        } catch (NullPointerException | ConverterException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return ConverterUtils.getAsString(o);
    }
}
