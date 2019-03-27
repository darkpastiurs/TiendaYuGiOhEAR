package sv.com.tienda.web.bean.monstruos.atributos;

import org.hibernate.validator.constraints.NotEmpty;
import sv.com.tienda.business.ejb.CartaBeanLocal;
import sv.com.tienda.business.entity.AtributoMonstruo;
import sv.com.tienda.business.utils.Constantes;
import sv.com.tienda.web.utils.FormateoDeCadenas;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

@Named(value = "gestionAtributoMonstruoWebBean")
@ViewScoped
public class GestionAtributoController implements Serializable {

    private static final long serialVersionUID = -3032499786965163859L;
    private static final Logger LOG = Logger.getLogger(GestionAtributoController.class.getName());

    //<editor-fold defaultstate="collapsed" desc="Componentes">
    @NotNull(message = "Es necesario escribir el nombre del atributo")
    @NotEmpty(message = "Es necesario escribir el nombre del atributo")
    private String nombre;
    private AtributoMonstruo atributoMonstruoSelected;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB(lookup = Constantes.JDNI_CARTA_BEAN)
    private CartaBeanLocal cartaBean;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private String nombreAtributoFormateado;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Eventos de carga y descarga de la pagina">
    @PostConstruct
    private void initial() {
        LOG.log(INFO, "[GestionAtributoController][initial]");
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            Map<String, Object> variablesSesion = fc.getExternalContext().getSessionMap();
            if (variablesSesion.containsKey("atributoMonstruoSeleccionado")) {
                atributoMonstruoSelected = (AtributoMonstruo) variablesSesion.get("atributoMonstruoSeleccionado");
                nombre = atributoMonstruoSelected.getNombre();
                nombreAtributoFormateado = FormateoDeCadenas.formatoURLEdicion(nombre);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionAtributoController][initial][Excepcion] -> ", e);
        }
    }

    //    @PreDestroy
    public void destroy(PhaseEvent evt) {
        LOG.log(INFO, "[GestionAtributoController][destroy]");
        try {
            if (evt.getPhaseId() == PhaseId.RENDER_RESPONSE) {
                FacesContext fc = FacesContext.getCurrentInstance();
                Map<String, Object> variablesSesion = fc.getExternalContext().getSessionMap();
                if (variablesSesion.containsKey("atributoMonstruoSeleccionado")) {
                    variablesSesion.remove("atributoMonstruoSeleccionado");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionAtributoController][initial][Excepcion] -> ", e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters y Setters">
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public AtributoMonstruo getAtributoMonstruoSelected() {
        return atributoMonstruoSelected;
    }

    public void setAtributoMonstruoSelected(AtributoMonstruo atributoMonstruoSelected) {
        this.atributoMonstruoSelected = atributoMonstruoSelected;
    }

    public String getNombreAtributoFormateado() {
        return nombreAtributoFormateado;
    }

    public void setNombreAtributoFormateado(String nombreAtributoFormateado) {
        this.nombreAtributoFormateado = nombreAtributoFormateado;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Acciones y/o Eventos">
    public String guardar() throws Exception {
        LOG.log(INFO, "[GestionAtributoController][guardar] -> {0}", new Object[]{nombre});
        try {
            AtributoMonstruo atributoMonstruo = new AtributoMonstruo();
            atributoMonstruo.setNombre(nombre);
            if (atributoMonstruoSelected != null) {
                atributoMonstruo.setId(atributoMonstruoSelected.getId());
            }
            cartaBean.guardarAtributoMonstruo(atributoMonstruo);
            return "atributos";
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionAtributoController][guardar][Excepcion] -> ", e);
            throw new Exception(e);
        }
    }
    //</editor-fold>
}
