package sv.com.tienda.web.bean.categoriacartas;

import sv.com.tienda.business.ejb.CartaBeanLocal;
import sv.com.tienda.business.entity.CategoriaCarta;
import sv.com.tienda.business.utils.Constantes;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

@Named("listaCategoriaWebBean")
@ViewScoped
public class ListadoCategoriaController implements Serializable {

    private static final long serialVersionUID = -8869367849371465482L;
    private static final Logger LOG = Logger.getLogger(ListadoCategoriaController.class.getName());

    //<editor-fold defaultstate="collapsed" desc="Componentes">
    private DataModel<CategoriaCarta> categoriaCartaDataModel;
    private DataModel<CategoriaCarta> categoriaCartaDataModelFilter;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB(lookup = Constantes.JDNI_CARTA_BEAN)
    private CartaBeanLocal cartaBean;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private List<CategoriaCarta> categoriaCartaList;
    //</editor-fold>

    @PostConstruct
    private void initial() {
        LOG.log(INFO, "[ListadoCategoriaController][initial]");
        try {
            categoriaCartaList = cartaBean.obtenerListadoDeCategorias(true);
            if (categoriaCartaList != null && !categoriaCartaList.isEmpty()) {
                categoriaCartaDataModel = new ListDataModel<>(categoriaCartaList);
            } else {
                categoriaCartaDataModel = new ListDataModel<>(Collections.emptyList());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoCategoriaController][initial][Excepcion] -> ", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters y Setters">
    public DataModel<CategoriaCarta> getCategoriaCartaDataModel() {
        return categoriaCartaDataModel;
    }

    public void setCategoriaCartaDataModel(DataModel<CategoriaCarta> categoriaCartaDataModel) {
        this.categoriaCartaDataModel = categoriaCartaDataModel;
    }

    public DataModel<CategoriaCarta> getCategoriaCartaDataModelFilter() {
        return categoriaCartaDataModelFilter;
    }

    public void setCategoriaCartaDataModelFilter(DataModel<CategoriaCarta> categoriaCartaDataModelFilter) {
        this.categoriaCartaDataModelFilter = categoriaCartaDataModelFilter;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Acciones y/o eventos">
    public String enviarCategoria() throws Exception {
        LOG.log(Level.INFO, "[ListadoCategoriaController][enviarCategoria] -> {0}", new Object[]{categoriaCartaDataModel.getRowData()});
        try {
            CategoriaCarta categoriaCartaSeleccionada = categoriaCartaDataModel.getRowData();
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().getFlash().setKeepMessages(true);
            if (categoriaCartaSeleccionada != null) {
                HttpSession sesion = (HttpSession) fc.getExternalContext().getSession(false);
                sesion.setAttribute("categoriaCartaSeleccionada", categoriaCartaSeleccionada);
                return "categoriascartasgestion";
            } else {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Categoria Cartas", "No se ha seleccionado ninguna categoria"));
                return "categoriascartas";
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoCategoriaController][enviarCategoria][Excepcion] -> ", e);
            throw e;
        }
    }

    public String eliminarCategoria() throws Exception {
        LOG.log(Level.INFO, "[ListadoCategoriaController][eliminarCategoria] -> {0}", categoriaCartaDataModel.getRowData());
        try {
            CategoriaCarta categoriaCartaSelected = categoriaCartaDataModel.getRowData();
            if (categoriaCartaSelected != null) {
                cartaBean.eliminarCategoriasCartas(categoriaCartaSelected);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Categoria Cartas", "No se ha seleccionado ninguna categoria"));
            }
            return "categoriascartas";
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ListadoCategoriaController][eliminarCategoria][Excepcion] -> ", e);
            throw e;
        }
    }
    //</editor-fold>

}
