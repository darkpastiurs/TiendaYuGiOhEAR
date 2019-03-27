package sv.com.tienda.web.bean.componentedeck;

import org.hibernate.validator.constraints.NotEmpty;
import sv.com.tienda.business.ejb.CartaBeanLocal;
import sv.com.tienda.business.entity.CategoriaCarta;
import sv.com.tienda.business.entity.ComponenteDeck;
import sv.com.tienda.business.utils.Constantes;
import sv.com.tienda.web.utils.FormateoDeCadenas;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.logging.Level.INFO;

@Named(value = "gestionComponenteDeckWebBean")
@ViewScoped
public class GestionComponenteDeckController implements Serializable {

    private static final long serialVersionUID = -1131389361799735585L;
    private static final Logger LOG = Logger.getLogger(GestionComponenteDeckController.class.getName());

    //<editor-fold defaultstate="collapsed" desc="Componentes">
    @NotNull(message = "Es necesario digitar la parte del deck")
    @NotEmpty(message = "Es necesario digitar la parte del deck")
    private String seccion;
    @NotNull(message = "Es necesario establecer la cantidad minima de cartas adminitidas")
    @Min(value = 0, message = "No se permiten valores negativos")
    private Short min;
    @NotNull(message = "Es necesario establecer la cantidad maxima de cartas adminitidas")
    @Min(value = 0, message = "No se permiten valores negativos")
    private Short max;
    @NotNull(message = "Es necesario seleccionar una categoria de carta por lo menos")
    private List<CategoriaCarta> categoriaCartasSeleccionadasModel;
    private List<SelectItem> categoriaCartaSelectModel;
    private boolean seleccionarTodos;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB(lookup = Constantes.JDNI_CARTA_BEAN)
    private CartaBeanLocal cartaBean;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ComponenteDeck componenteDeckSeleccionado;
    private String seccionFormateada;
    private List<CategoriaCarta> categoriaCartaListado;
    private List<CategoriaCarta> categoriaCartasSeleccionadas;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Eventos de Carga y Descarga de la Pagina">
    @PostConstruct
    private void initial() {
        LOG.log(INFO, "[GestionComponenteDeckController][initial]");
        try {
            categoriaCartaListado = cartaBean.obtenerListadoDeCategorias(true);
            categoriaCartaSelectModel = new ArrayList<>();
            categoriaCartaListado.stream().forEach((CategoriaCarta categoria) -> {
                if (categoria.getSubcategorias() != null && !categoria.getSubcategorias().isEmpty()) {
                    SelectItemGroup grupo = new SelectItemGroup(categoria.getNombre());
                    List<SelectItem> subItems = new ArrayList<>();
                    categoria.getSubcategorias().stream().forEach((CategoriaCarta subcategoria) -> {
                        SelectItem item = new SelectItem();
                        item.setLabel(subcategoria.getNombre());
                        item.setValue(subcategoria);
                        subItems.add(item);
                    });
                    grupo.setSelectItems(subItems.toArray(new SelectItem[subItems.size()]));
                    categoriaCartaSelectModel.add(grupo);
                }
            });
            FacesContext fc = FacesContext.getCurrentInstance();
            Map<String, Object> variablesSesion = fc.getExternalContext().getSessionMap();
            if (variablesSesion.containsKey("componenteDeckSeleccionado")) {
                componenteDeckSeleccionado = (ComponenteDeck) variablesSesion.get("componenteDeckSeleccionado");
                List<SelectItem> itemsSelected = new ArrayList<>();
                if (componenteDeckSeleccionado != null) {
                    seccion = componenteDeckSeleccionado.getSeccion();
                    seccionFormateada = FormateoDeCadenas.formatoURLEdicion(seccion);
                    min = componenteDeckSeleccionado.getNumeroMinimo();
                    max = componenteDeckSeleccionado.getNumeroMaximo();
                    categoriaCartasSeleccionadasModel = componenteDeckSeleccionado.getCategoriasCarta();
                    categoriaCartasSeleccionadas = componenteDeckSeleccionado.getCategoriasCarta();
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionComponenteDeckController][initial][Excepcion] -> ", e);
        }
    }

    //@PreDestroy
    public void destroy(PhaseEvent evt) {
        try {
            if (evt.getPhaseId() == PhaseId.RENDER_RESPONSE) {
                FacesContext fc = FacesContext.getCurrentInstance();
                Map<String, Object> variablesSesion = fc.getExternalContext().getSessionMap();
                if (variablesSesion.containsKey("componenteDeckSeleccionado")) {
                    variablesSesion.remove("componenteDeckSeleccionado");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionComponenteDeckController][destroy][Excepcion] -> ", e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters y Setters">
    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public Short getMin() {
        return min;
    }

    public void setMin(Short min) {
        this.min = min;
    }

    public Short getMax() {
        return max;
    }

    public void setMax(Short max) {
        this.max = max;
    }

    public List<CategoriaCarta> getCategoriaCartasSeleccionadasModel() {
        return categoriaCartasSeleccionadasModel;
    }

    public void setCategoriaCartasSeleccionadasModel(List<CategoriaCarta> categoriaCartasSeleccionadasModel) {
        this.categoriaCartasSeleccionadasModel = categoriaCartasSeleccionadasModel;
    }

    public List<SelectItem> getCategoriaCartaSelectModel() {
        return categoriaCartaSelectModel;
    }

    public void setCategoriaCartaSelectModel(List<SelectItem> categoriaCartaSelectModel) {
        this.categoriaCartaSelectModel = categoriaCartaSelectModel;
    }

    public String getSeccionFormateada() {
        return seccionFormateada;
    }

    public void setSeccionFormateada(String seccionFormateada) {
        this.seccionFormateada = seccionFormateada;
    }

    public boolean isSeleccionarTodos() {
        return seleccionarTodos;
    }

    public void setSeleccionarTodos(boolean seleccionarTodos) {
        this.seleccionarTodos = seleccionarTodos;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Acciones y/o Eventos">
    public void onSelectAll() {
        LOG.log(Level.INFO, "[GestionComponenteDeckController][onSelectAll]");
        if (componenteDeckSeleccionado == null) {
            categoriaCartasSeleccionadasModel = seleccionarTodos ? new ArrayList<>(categoriaCartaListado) : null;
        } else {
            categoriaCartasSeleccionadasModel = seleccionarTodos ? new ArrayList<>(categoriaCartaListado) : categoriaCartasSeleccionadas;
        }
    }

    public void onChangeSelect(ValueChangeEvent evt) {
        LOG.log(INFO, "[GestionComponenteDeckController][onChangeSelect] -> {0}", new Object[]{evt.getComponent()});
        try {
            if (categoriaCartasSeleccionadas != null) {
                List<CategoriaCarta> categoriaCartasOld = (List<CategoriaCarta>) evt.getOldValue();
                List<CategoriaCarta> categoriaCartasNew = (List<CategoriaCarta>) evt.getNewValue();
                List<CategoriaCarta> categoriaCartasRemoved = null;
                if (categoriaCartasOld != null && categoriaCartasNew != null) {
                    categoriaCartasRemoved = categoriaCartasOld.stream().filter(cc -> !categoriaCartasNew.contains(cc)).collect(Collectors.toList());
                }

                if (categoriaCartasRemoved != null && !categoriaCartasRemoved.isEmpty()) {
                    List<CategoriaCarta> finalCategoriaCartasRemoved = categoriaCartasRemoved;
                    categoriaCartasSeleccionadas.stream().forEach(cc -> {
                        if (finalCategoriaCartasRemoved.contains(cc)) {
                            cc.setRemover(true);
                        }
                    });
                }

                if (categoriaCartasNew != null && !categoriaCartasNew.isEmpty()) {
                    categoriaCartasNew.stream().forEach(cc -> {
                        if (!categoriaCartasSeleccionadas.contains(cc)) {
                            categoriaCartasSeleccionadas.add(cc);
                        }
                    });
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionComponenteDeckController][onChangeSelect][Excepcion] -> ", e);
        }
    }

    public String guardarComponente() throws Exception {
        LOG.log(INFO, "[GestionComponenteDeckController][guardarComponente] -> {0}, {1}, {2}, {3}", new Object[]{seccion, min, max, categoriaCartasSeleccionadasModel.size()});
        try {
            ComponenteDeck componenteDeckGuardar = new ComponenteDeck();
            componenteDeckGuardar.setSeccion(seccion);
            componenteDeckGuardar.setNumeroMinimo(min);
            componenteDeckGuardar.setNumeroMaximo(max);
            if (componenteDeckSeleccionado != null) {
                componenteDeckGuardar.setId(componenteDeckSeleccionado.getId());
                componenteDeckGuardar.setCategoriasCarta(categoriaCartasSeleccionadas);
            } else {
                componenteDeckGuardar.setCategoriasCarta(categoriaCartasSeleccionadasModel);
            }
            cartaBean.guardarComponenteDeck(componenteDeckGuardar);
            return "estructuradeck";
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionComponenteDeckController][guardarComponente][Excepcion] -> ", e);
            throw new Exception(e);
        }
    }
    //</editor-fold>
}
