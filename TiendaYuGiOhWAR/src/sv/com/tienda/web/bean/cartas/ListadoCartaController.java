package sv.com.tienda.web.bean.cartas;

import sv.com.tienda.business.ejb.CartaBeanLocal;
import sv.com.tienda.business.entity.Carta;
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

@Named(value = "listadoCartasWebBean")
@ViewScoped
public class ListadoCartaController implements Serializable {

    private static final long serialVersionUID = 7414595969706466481L;
    private static final Logger LOG = Logger.getLogger(ListadoCartaController.class.getName());

    //<editor-fold defaultstate="collapsed" desc="Componentes">
    private List<Carta> cartasModelo;
    private Carta cartaSeleccionada;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB(lookup = Constantes.JDNI_CARTA_BEAN)
    private CartaBeanLocal cartaBean;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Evento de carga de la pagina">
    @PostConstruct
    public void init(){
        LOG.log(INFO, "[ListadoCartaController][init]");
        try {
            cartasModelo = cartaBean.obtenerListadoCartas(true);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoCartaController][init][Excepcion] -> ", e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters y Setters">
    public List<Carta> getCartasModelo() {
        return cartasModelo;
    }

    public void setCartasModelo(List<Carta> cartasModelo) {
        this.cartasModelo = cartasModelo;
    }

    public Carta getCartaSeleccionada() {
        return cartaSeleccionada;
    }

    public void setCartaSeleccionada(Carta cartaSeleccionada) {
        this.cartaSeleccionada = cartaSeleccionada;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Eventos y/o Acciones">
    public String enviarCarta() throws Exception{
        LOG.log(INFO, "[ListadoCartaController][enviarCarta] -> {0}", cartaSeleccionada);
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if(cartaSeleccionada != null){
                Map<String, Object> variablesSesion = fc.getExternalContext().getSessionMap();
                variablesSesion.put("cartaSeleccionada", cartaSeleccionada);
                return "cartasgestion";
            } else {
                fc.getExternalContext().getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Administracion de Cartas", "No se ha seleccionado nunguna carta"));
                return "cartas";
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoCartaController][enviarCarta][Excepcion] -> ", e);
            throw new Exception(e);
        }
    }

    public String eliminarCarta() throws Exception {
        LOG.log(INFO, "[ListadoCartaController][eliminarCarta] -> {0}", new Object[]{cartaSeleccionada});
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if(cartaSeleccionada != null){
                cartaBean.eliminarCarta(cartaSeleccionada);
            } else {
                fc.getExternalContext().getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Administracion de Cartas", "No se ha seleccionado nunguna carta"));
            }
            return "cartas";
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoCartaController][eliminarCarta][Excepcion] -> ", e);
            throw new Exception(e);
        }
    }
    //</editor-fold>
}
