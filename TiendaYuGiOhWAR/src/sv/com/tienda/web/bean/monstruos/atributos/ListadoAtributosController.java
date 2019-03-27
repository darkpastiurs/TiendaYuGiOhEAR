package sv.com.tienda.web.bean.monstruos.atributos;

import sv.com.tienda.business.ejb.CartaBeanLocal;
import sv.com.tienda.business.entity.AtributoMonstruo;
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

import static java.util.logging.Level.INFO;

@Named(value = "listadoAtributosMonstruosWebBean")
@ViewScoped
public class ListadoAtributosController implements Serializable {


    private static final long serialVersionUID = 2922524982084098722L;
    private static final Logger LOG = Logger.getLogger(ListadoAtributosController.class.getName());

    //<editor-fold defaultstate="collapsed" desc="Componentes">
    private List<AtributoMonstruo> atributosMonstruosModel;
    private List<AtributoMonstruo> atributosMounstrosFilterModel;
    private AtributoMonstruo atributoMonstruoSeleccionado;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB(lookup = Constantes.JDNI_CARTA_BEAN)
    private CartaBeanLocal cartaBean;
    //</editor-fold>

    @PostConstruct
    private void initial() {
        LOG.log(INFO, "[ListadoAtributosController][initial]");
        try {
            atributosMonstruosModel = cartaBean.obtenerListadoAtributos(true);
            atributosMounstrosFilterModel = atributosMonstruosModel;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoAtributosController][initial][Excepcion] -> ", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters y Setters">
    public List<AtributoMonstruo> getAtributosMonstruosModel() {
        return atributosMonstruosModel;
    }

    public void setAtributosMonstruosModel(List<AtributoMonstruo> atributosMonstruosModel) {
        this.atributosMonstruosModel = atributosMonstruosModel;
    }

    public List<AtributoMonstruo> getAtributosMounstrosFilterModel() {
        return atributosMounstrosFilterModel;
    }

    public void setAtributosMounstrosFilterModel(List<AtributoMonstruo> atributosMounstrosFilterModel) {
        this.atributosMounstrosFilterModel = atributosMounstrosFilterModel;
    }

    public AtributoMonstruo getAtributoMonstruoSeleccionado() {
        return atributoMonstruoSeleccionado;
    }

    public void setAtributoMonstruoSeleccionado(AtributoMonstruo atributoMonstruoSeleccionado) {
        this.atributoMonstruoSeleccionado = atributoMonstruoSeleccionado;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Acciones y/o Eventos">
    public String enviarAtributo() throws Exception {
        LOG.log(INFO, "[ListadoAtributosController][enviarAtributo] -> {0}", new Object[]{atributoMonstruoSeleccionado});
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if(atributoMonstruoSeleccionado != null){
                Map<String, Object> variablesSesion = fc.getExternalContext().getSessionMap();
                variablesSesion.put("atributoMonstruoSeleccionado", atributoMonstruoSeleccionado);
                return "atributosmonstruosgestion";
            } else {
                fc.getExternalContext().getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atributos de Monstruos", "No se ha seleciconado ningun registro"));
                return "atributos";
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoAtributosController][enviarAtributo][Excepcion] -> ", e);
            throw new Exception(e);
        }
    }

    public String eliminarAtributo() {
        LOG.log(Level.INFO, "[ListadoAtributosController][eliminarAtributo] -> {0}", atributoMonstruoSeleccionado);
        FacesContext fc = FacesContext.getCurrentInstance();
        if(atributoMonstruoSeleccionado != null){
            cartaBean.eliminarAtributoMonstruo(atributoMonstruoSeleccionado);
        } else {
            fc.getExternalContext().getFlash().setKeepMessages(true);
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atributo Monstruo", "No se ha seleccionado ningun registro"));
        }
        return "atributos";

    }
    //</editor-fold>
}
