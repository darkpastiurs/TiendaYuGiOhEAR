package sv.com.tienda.web.bean.monstruos.tipos;

import sv.com.tienda.business.ejb.CartaBeanLocal;
import sv.com.tienda.business.entity.TipoMounstro;
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

@Named(value = "listadoTiposMonstruosWebBean")
@ViewScoped
public class ListadoTiposController implements Serializable {

    private static final long serialVersionUID = -8339693145422221577L;
    private static final Logger LOG = Logger.getLogger(ListadoTiposController.class.getName());

    //<editor-fold defaultstate="collapsed" desc="Componentes">
    private List<TipoMounstro> tipoMounstrosModel;
    private List<TipoMounstro> tipoMounstrosFiltradoModel;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB(lookup = Constantes.JDNI_CARTA_BEAN)
    private CartaBeanLocal cartaBean;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private TipoMounstro tipoMounstroSelected;
    //</editor-fold>

    @PostConstruct
    private void initial() {
        LOG.log(INFO, "[ListadoTiposController][initial]");
        try {
            tipoMounstrosModel = cartaBean.obtenerListadoTiposMonstruos(true);
            tipoMounstrosFiltradoModel = tipoMounstrosModel;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoTiposController][initial][Excepcion] -> ", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters y Setters">
    public List<TipoMounstro> getTipoMounstrosModel() {
        return tipoMounstrosModel;
    }

    public void setTipoMounstrosModel(List<TipoMounstro> tipoMounstrosModel) {
        this.tipoMounstrosModel = tipoMounstrosModel;
    }

    public List<TipoMounstro> getTipoMounstrosFiltradoModel() {
        return tipoMounstrosFiltradoModel;
    }

    public void setTipoMounstrosFiltradoModel(List<TipoMounstro> tipoMounstrosFiltradoModel) {
        this.tipoMounstrosFiltradoModel = tipoMounstrosFiltradoModel;
    }

    public TipoMounstro getTipoMounstroSelected() {
        return tipoMounstroSelected;
    }

    public void setTipoMounstroSelected(TipoMounstro tipoMounstroSelected) {
        this.tipoMounstroSelected = tipoMounstroSelected;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Acciones y/o Eventos">
    public String enviarTipoMonstruo() throws Exception{
        LOG.log(INFO, "[ListadoTiposController][enviarTipoMonstruo] -> {0}", new Object[]{tipoMounstroSelected});
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if(tipoMounstroSelected != null){
                Map<String, Object> variablesSesion = fc.getExternalContext().getSessionMap();
                variablesSesion.put("tipoMonstruoSeleccionado", tipoMounstroSelected);
                return "tiposmonstruosgestion";
            } else {
                fc.getExternalContext().getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Tipos de Monstruos", "No hay un tipo de monstruo seleccionado"));
                return "tiposmonstruos";
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoTiposController][enviarTipoMonstruo][Excepcion] -> ", e);
            throw new Exception(e);
        }
    }

    public String eliminarTiposMonstruo() throws Exception{
        LOG.log(INFO, "[ListadoTiposController][eliminarTiposMonstruo] -> {0}", new Object[]{tipoMounstroSelected});
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if(tipoMounstroSelected != null){
                cartaBean.eliminarTipoMonstruo(tipoMounstroSelected);
            } else {
                fc.getExternalContext().getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Tipos de Monstruos", "No hay un tipo de monstruo seleccionado"));
            }
            return "tiposmonstruos";
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoTiposController][eliminarTiposMonstruo][Excepcion] -> ", e);
            throw new Exception(e);
        }
    }
    //</editor-fold>
}
