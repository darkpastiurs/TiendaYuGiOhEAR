package sv.com.tienda.web.bean.componentedeck;

import sv.com.tienda.business.ejb.CartaBeanLocal;
import sv.com.tienda.business.entity.CategoriaCarta;
import sv.com.tienda.business.entity.ComponenteDeck;
import sv.com.tienda.business.utils.Constantes;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.logging.Level.INFO;

@Named(value = "listadoComponenteWebBean")
@ViewScoped
public class ListadoComponenteDeckController implements Serializable {

    private static final long serialVersionUID = -2814642681149594640L;
    private static final Logger LOG = Logger.getLogger(ListadoComponenteDeckController.class.getName());

    //<editor-fold defaultstate="collapsed" desc="Componentes">
    private List<ComponenteDeck> componenteDeckModel;
    private ComponenteDeck componenteDeckSeleccionado;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB(lookup = Constantes.JDNI_CARTA_BEAN)
    private CartaBeanLocal cartaBean;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Eventos de la pagina">
    @PostConstruct
    private void initial(){
        LOG.log(INFO, "[ListadoComponenteDeckController][initial]");
        try {
            componenteDeckModel = cartaBean.obtenerListadoComponentesDeck(true);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoComponenteDeckController][initial][Excepcion] -> ", e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters y Setters">
    public List<ComponenteDeck> getComponenteDeckModel() {
        return componenteDeckModel;
    }

    public void setComponenteDeckModel(List<ComponenteDeck> componenteDeckModel) {
        this.componenteDeckModel = componenteDeckModel;
    }

    public ComponenteDeck getComponenteDeckSeleccionado() {
        return componenteDeckSeleccionado;
    }

    public void setComponenteDeckSeleccionado(ComponenteDeck componenteDeckSeleccionado) {
        this.componenteDeckSeleccionado = componenteDeckSeleccionado;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Acciones y/o eventos">
    public String listadoCategoriasCartas(ComponenteDeck componenteDeck) {
        return componenteDeck.getCategoriasCarta().stream().map((CategoriaCarta::getNombre)).collect(Collectors.joining(", "));
    }

    public String enviarComponenteDeck() throws Exception {
        LOG.log(INFO, "[ListadoComponenteDeckController][enviarComponenteDeck] -> {0}", new Object[]{componenteDeckSeleccionado});
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if(componenteDeckSeleccionado != null){
                Map<String, Object> variablesSesion = fc.getExternalContext().getSessionMap();
                variablesSesion.put("componenteDeckSeleccionado", componenteDeckSeleccionado);
                return "estructuradeckgestion";
            } else {
                fc.getExternalContext().getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Estructura del Deck", "No se ha seleccionado ninguna parte del deck"));
                return "estructuradeck";
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoComponenteDeckController][enviarComponenteDeck][Excepcion] -> ", e);
            throw new Exception(e);
        }
    }

    public String eliminarComponenteDeck() throws Exception{
        LOG.log(INFO, "[ListadoComponenteDeckController][eliminarComponenteDeck] -> {0}", new Object[]{componenteDeckSeleccionado});
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if(componenteDeckSeleccionado != null){
                cartaBean.eliminarComponenteDeck(componenteDeckSeleccionado);
            } else {
                fc.getExternalContext().getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Estructura del Deck", "No se ha seleccionado ninguna parte del deck"));
            }
            return "estructuradeck";
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoComponenteDeckController][eliminarComponenteDeck][Excepcion] -> ", e);
            throw new Exception(e);
        }
    }
    //</editor-fold>
}
