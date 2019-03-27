package sv.com.tienda.web.bean.categoriacartas;

import org.apache.commons.collections.EnumerationUtils;
import org.hibernate.validator.constraints.NotEmpty;
import sv.com.tienda.business.ejb.CartaBeanLocal;
import sv.com.tienda.business.entity.CategoriaCarta;
import sv.com.tienda.business.utils.Constantes;
import sv.com.tienda.web.utils.FormateoDeCadenas;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

@Named(value = "gestionCategoriaBean")
@ViewScoped
public class GestionCategoriaController implements Serializable {

    private static final long serialVersionUID = -7615971252304880835L;
    private static final Logger LOG = Logger.getLogger(GestionCategoriaController.class.getName());

    //<editor-fold defaultstate="collapsed" desc="Componentes">
    @NotNull(message = "El nombre es obligatorio")
    @NotEmpty(message = "El nombre es obligatorio")
    private String nombre;
    private boolean subcategoria;
    @NotNull(message = "Al ser subcategoria es obligatorio seleccionar una categoria")
    private CategoriaCarta categoriaCartaSuperior;
    private List<CategoriaCarta> categoriaCartasSuperiorModel;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB(lookup = Constantes.JDNI_CARTA_BEAN)
    private CartaBeanLocal cartaBean;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private CategoriaCarta categoriaCartaSelected;
    private String nombreCategoriaFormateado;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Eventos de carga y descarga de la pagina">
    @PostConstruct
    public void initial() {
        LOG.log(INFO, "[GestionCategoriaController][initial]");
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession sesion = (HttpSession) fc.getExternalContext().getSession(false);
            if (EnumerationUtils.toList(sesion.getAttributeNames()).contains("categoriaCartaSeleccionada")) {
                categoriaCartaSelected = (CategoriaCarta) sesion.getAttribute("categoriaCartaSeleccionada");
                nombre = categoriaCartaSelected.getNombre();
                nombreCategoriaFormateado = FormateoDeCadenas.formatoURLEdicion(nombre);
                subcategoria = categoriaCartaSelected.getCategoriaCartaSuperior() != null;
                if (subcategoria) {
                    categoriaCartaSuperior = categoriaCartaSelected.getCategoriaCartaSuperior();
                }
            }
            categoriaCartasSuperiorModel = cartaBean.obtenerListadoDeCategoriasSuperiores();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionCategoriaController][initial][Excepcion] -> ", e);
        }
    }

    //@PreDestroy
    public void destroy(PhaseEvent evt) {
        LOG.log(Level.INFO, "[GestionCategoriaController][destroy]");
        try {
            if (evt.getPhaseId() == PhaseId.RENDER_RESPONSE) {
                FacesContext fc = FacesContext.getCurrentInstance();
                HttpSession sesion = (HttpSession) fc.getExternalContext().getSession(false);
                if (EnumerationUtils.toList(sesion.getAttributeNames()).contains("categoriaCartaSeleccionada")) {
                    sesion.removeAttribute("categoriaCartaSeleccionada");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionCategoriaController][destroy][Excepcion] -> ", e);
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

    public boolean isSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(boolean subcategoria) {
        this.subcategoria = subcategoria;
    }

    public CategoriaCarta getCategoriaCartaSuperior() {
        return categoriaCartaSuperior;
    }

    public void setCategoriaCartaSuperior(CategoriaCarta categoriaCartaSuperior) {
        this.categoriaCartaSuperior = categoriaCartaSuperior;
    }

    public List<CategoriaCarta> getCategoriaCartasSuperiorModel() {
        return categoriaCartasSuperiorModel;
    }

    public void setCategoriaCartasSuperiorModel(List<CategoriaCarta> categoriaCartasSuperiorModel) {
        this.categoriaCartasSuperiorModel = categoriaCartasSuperiorModel;
    }

    public String getNombreCategoriaFormateado() {
        return nombreCategoriaFormateado;
    }

    public void setNombreCategoriaFormateado(String nombreCategoriaFormateado) {
        this.nombreCategoriaFormateado = nombreCategoriaFormateado;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Eventos y/o acciones">

    /**
     * Metodo encargado de realizar la operacion de adicion/edicion de categorias de cartas
     *
     * @return
     * @throws Exception
     */
    public String guardar() throws Exception {
        LOG.log(INFO, "[GestionCategoriaController][guardar] -> {0},{1},{2}", new Object[]{nombre, subcategoria, categoriaCartaSuperior});
        FacesContext fc = FacesContext.getCurrentInstance();
        FacesMessage mensaje = new FacesMessage();
        mensaje.setSummary("Cartegoria de Cartas");
        try {
            CategoriaCarta categoriaCartaGuardar = new CategoriaCarta();
            categoriaCartaGuardar.setNombre(nombre);
            if (subcategoria) {
                categoriaCartaGuardar.setCategoriaCartaSuperior(categoriaCartaSuperior);
            }
            if (categoriaCartaSelected != null) {
                categoriaCartaGuardar.setId(categoriaCartaSelected.getId());
            }
            cartaBean.guardarCategoriaCarta(categoriaCartaGuardar);
            mensaje.setSeverity(FacesMessage.SEVERITY_INFO);
            mensaje.setDetail("Se ha registrado correctamente");
            fc.addMessage(null, mensaje);
            return "categoriascartas";
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionCategoriaController][guardar][Excepcion] -> ", e);
            mensaje.setSeverity(FacesMessage.SEVERITY_ERROR);
            mensaje.setDetail("Ha ocurrido un error con el registro, contacte con el desarrollador");
            fc.addMessage(null, mensaje);
            throw new Exception(e);
        }
    }
    //</editor-fold>
}
