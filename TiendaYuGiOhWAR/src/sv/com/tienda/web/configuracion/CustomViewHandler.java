package sv.com.tienda.web.configuracion;

import org.apache.commons.lang.StringUtils;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

public class CustomViewHandler  extends ViewHandlerWrapper {

    private String removeContextPath(FacesContext fc, String url){
        ServletContext servletContext = (ServletContext) fc.getExternalContext().getContext();
        String contextPath = servletContext.getContextPath();
        if(StringUtils.isBlank(contextPath)){
            return url;
        } else {
            return StringUtils.contains(url, "javax.faces.resource")? StringUtils.remove(url, "javax.faces.resource/") : url;
        }
    }

    public CustomViewHandler(ViewHandler wrapped) {
        super(wrapped);
    }

    @Override
    public ViewHandler getWrapped() {
        return super.getWrapped();
    }

    @Override
    public String getActionURL(FacesContext context, String viewId) {
        String url = super.getActionURL(context, viewId);
        return removeContextPath(context, url);
    }

    @Override
    public String getRedirectURL(FacesContext context, String viewId, Map<String, List<String>> parameters, boolean includeViewParams) {
        String url = super.getRedirectURL(context, viewId, parameters, includeViewParams);
        return removeContextPath(context, url);
    }

    @Override
    public String getResourceURL(FacesContext context, String path) {
        String url = super.getResourceURL(context, path);
        return removeContextPath(context, url);
    }
}
