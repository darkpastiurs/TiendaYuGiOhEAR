package sv.com.tienda.web.configuracion;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;

@ApplicationScoped
public class ApplicationController {
    private static final Logger LOG = Logger.getLogger(ApplicationController.class.getName());

    
    public void init(@Observes @Initialized(ApplicationScoped.class)Object init) {
        LOG.info("[ApplicationController][init]");
    }
    
    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init){
        LOG.info("[ApplicationController][destroy]");
    }
}
