package sv.com.tienda.web.bean.cartas;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import javax.validation.constraints.NotEmpty;
import org.primefaces.model.file.UploadedFile;
import sv.com.tienda.business.ejb.CartaBeanLocal;
import sv.com.tienda.business.entity.*;
import sv.com.tienda.business.utils.Constantes;
import sv.com.tienda.web.utils.ArchivosUtils;
import sv.com.tienda.web.utils.FormateoDeCadenas;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.logging.Level.INFO;

@Named(value = "gestionCartaWebBean")
@ViewScoped
public class GestionCartaController implements Serializable {

    private static final long serialVersionUID = -9071571571677564544L;
    private static final Logger LOG = Logger.getLogger(GestionCartaController.class.getName());

    //<editor-fold defaultstate="collapsed" desc="Componentes">
    @NotNull(message = "Debes subir la ilustracion de la carta")
    private UploadedFile imagen;
    @NotNull(message = "Debes colocarle el nombre de la carta")
    @NotEmpty(message = "Debes colocarle el nombre de la carta")
    private String nombre;
    @NotNull(message = "Debes colocarle el efecto de la carta")
    @NotEmpty(message = "Debes colocarle el efecto de la carta")
    private String efecto;
    @NotNull(message = "Debes escoger la limitacion de la carta")
    private LimitacionCarta limitacionCartaSeleccionada;
    private List<LimitacionCarta> limitacionCartasModel;
    @Size(min = 1, message = "Debes agregar al menos una categoria de carta")
    private List<CategoriaCarta> categoriasCartaSeleccionadas;
    private CategoriaCarta categoriaCartaSeleccionada;
    private List<SelectItem> categoriaCartasModelComponente;
    @NotNull(message = "Debes elegir el atributo del monstruo")
    private AtributoMonstruo atributoMonstruoSeleccionado;
    private List<AtributoMonstruo> atributoMonstruosModel;
    @NotNull(message = "Debes elegir por lo menos un tipo de monstruo")
    @NotEmpty(message = "Debes elegir por lo menos un tipo de monstruo")
    private List<TipoMounstro> tipoMounstrosSeleccionados;
    private List<TipoMounstro> tipoMounstrosModel;
    @NotNull(message = "Debes establecer los materiales necesarios para la invocacion")
    @NotEmpty(message = "Debes establecer los materiales necesarios para la invocacion")
    private String materialesInvocacion;
    @NotNull(message = "Debes establecer el valor del ataque")
    @Min(value = 0, message = "El ataque debe ser un valor positivo")
    private Integer ataque;
    @Min(value = 0, message = "La defensa debe ser un valor positivo")
    private Integer defensa;
    @NotNull(message = "Debes de seleccionar una escala")
    private TipoEscala tipoEscalaSeleccionada;
    private List<TipoEscala> tipoEscalasModel;
    @NotNull(message = "Debes establece el valor de la escala")
    @Min(value = 0, message = "La escala solo puede ser positiva")
    private Short escala;
    @NotNull(message = "Debes colocarle el efecto pendulo de la carta")
    @NotEmpty(message = "Debes colocarle el efecto pendulo de la carta")
    private String efectoPendulo;
    @NotNull(message = "Debes establecer el valor de escala pendulo izquierda")
    @Min(value = 0, message = "La escala solo puede ser positiva")
    private Short escalaIzquierda;
    @NotNull(message = "Debes establecer el valor de escala pendulo derecha")
    @Min(value = 0, message = "La escala solo puede ser positiva")
    private Short escalaDerecha;
    @NotNull(message = "Debes elegir por lo menos una flecha")
    @NotEmpty(message = "Debes elegir por lo menos una flecha")
    private List<FlechaLink> flechaLinksSeleccionadas;
    private List<FlechaLink> flechaLinksModel;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB(lookup = Constantes.JDNI_CARTA_BEAN)
    private CartaBeanLocal cartaBean;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private String nombreImagen;
    private Carta cartaSeleccionada;
    private List<CategoriaCarta> categoriaCartasModel;
    private boolean tipoMonstruoSeleccionado;
    private boolean tipoMonstruoPenduloSeleccionado;
    private boolean tipoMonstruoLinkSeleccionado;
    private boolean tipoMonstruoExtraDeck;
    private boolean tipoMonstruoNormal;
    private boolean categoriaMultiple;
    private ArchivosUtils gestionArchivos;
    private String cartaNombreFormateado;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Carga y Descarga de la pagina">
    @PostConstruct
    private void init() {
        LOG.log(INFO, "[GestionCartaController][init]");
        try {
            limitacionCartasModel = cartaBean.obtenerListadoLimitacion();
            categoriaCartasModelComponente = new ArrayList<>();
            categoriaCartasModel = cartaBean.obtenerListadoDeCategorias(true).stream().sorted(Comparator.comparing(CategoriaCarta::getNombre)).collect(Collectors.toList());
            cargarCategoriasAgrupadas(categoriaCartasModel.stream().filter(cc -> cc.getCategoriaCartaSuperior() == null).collect(Collectors.toList()));
            atributoMonstruosModel = cartaBean.obtenerListadoAtributos(true).stream().sorted(Comparator.comparing(AtributoMonstruo::getNombre)).collect(Collectors.toList());
            tipoMounstrosModel = cartaBean.obtenerListadoTiposMonstruos(true).stream().sorted(Comparator.comparing(TipoMounstro::getNombre)).collect(Collectors.toList());
            tipoEscalasModel = cartaBean.obtenerListadoTipoEscala();
            flechaLinksModel = cartaBean.obtenerListadoFlechasLink();
            gestionArchivos = new ArchivosUtils();
            FacesContext fc = FacesContext.getCurrentInstance();
            Map<String, Object> variablesSesion = fc.getExternalContext().getSessionMap();
            if (variablesSesion.containsKey("cartaSeleccionada")) {
                cartaSeleccionada = (Carta) variablesSesion.get("cartaSeleccionada");
                if (cartaSeleccionada.getImagen() != null) {
                    nombreImagen = cartaSeleccionada.getImagen().getArchivo();
                }
                nombre = cartaSeleccionada.getNombre();
                efecto = cartaSeleccionada.getEfecto();
                limitacionCartaSeleccionada = cartaSeleccionada.getLimite();
                categoriasCartaSeleccionadas = cartaSeleccionada.getCategorias();
                evaluarCategorias();
                cartaNombreFormateado = FormateoDeCadenas.formatoURLEdicion(cartaSeleccionada.getNombre());
                if (cartaSeleccionada.getMonstruo() != null) {
                    atributoMonstruoSeleccionado = cartaSeleccionada.getMonstruo().getAtributo();
                    tipoMounstrosSeleccionados = cartaSeleccionada.getMonstruo().getTipos();
                    materialesInvocacion = cartaSeleccionada.getMonstruo().getMaterialInvocacion();
                    ataque = cartaSeleccionada.getMonstruo().getAtaque();
                    defensa = cartaSeleccionada.getMonstruo().getDefensa();
                    tipoEscalaSeleccionada = cartaSeleccionada.getMonstruo().getTipoEscala();
                    escala = cartaSeleccionada.getMonstruo().getEscala();
                    if (cartaSeleccionada.getMonstruo().getPenduloAtributos() != null) {
                        efectoPendulo = cartaSeleccionada.getMonstruo().getPenduloAtributos().getEfecto();
                        escalaIzquierda = cartaSeleccionada.getMonstruo().getPenduloAtributos().getIzquierda();
                        escalaDerecha = cartaSeleccionada.getMonstruo().getPenduloAtributos().getDerecha();
                    }
                    if (cartaSeleccionada.getMonstruo().getFlechasLinks() != null) {
                        flechaLinksSeleccionadas = cartaSeleccionada.getMonstruo().getFlechasLinks();
                    }
                }
            }
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionCartaController][init][Excepcion] -> ", e);
        }
    }

    public void destroy(PhaseEvent evt) {
//        LOG.log(Level.INFO, "[GestionCartaController][destroy]");
        if (evt.getPhaseId() == PhaseId.RENDER_RESPONSE) {
            FacesContext fc = FacesContext.getCurrentInstance();
            Map<String, Object> variablesSesion = fc.getExternalContext().getSessionMap();
            variablesSesion.remove("cartaSeleccionada");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters y Setters">
    public UploadedFile getImagen() {
        return imagen;
    }

    public void setImagen(UploadedFile imagen) {
        this.imagen = imagen;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEfecto() {
        return efecto;
    }

    public void setEfecto(String efecto) {
        this.efecto = efecto;
    }

    public LimitacionCarta getLimitacionCartaSeleccionada() {
        return limitacionCartaSeleccionada;
    }

    public void setLimitacionCartaSeleccionada(LimitacionCarta limitacionCartaSeleccionada) {
        this.limitacionCartaSeleccionada = limitacionCartaSeleccionada;
    }

    public List<LimitacionCarta> getLimitacionCartasModel() {
        return limitacionCartasModel;
    }

    public void setLimitacionCartasModel(List<LimitacionCarta> limitacionCartasModel) {
        this.limitacionCartasModel = limitacionCartasModel;
    }

    public List<CategoriaCarta> getCategoriasCartaSeleccionadas() {
        return categoriasCartaSeleccionadas;
    }

    public void setCategoriasCartaSeleccionadas(List<CategoriaCarta> categoriasCartaSeleccionadas) {
        this.categoriasCartaSeleccionadas = categoriasCartaSeleccionadas;
    }

    public CategoriaCarta getCategoriaCartaSeleccionada() {
        return categoriaCartaSeleccionada;
    }

    public void setCategoriaCartaSeleccionada(CategoriaCarta categoriaCartaSeleccionada) {
        this.categoriaCartaSeleccionada = categoriaCartaSeleccionada;
    }

    public List<SelectItem> getCategoriaCartasModelComponente() {
        return categoriaCartasModelComponente;
    }

    public void setCategoriaCartasModelComponente(List<SelectItem> categoriaCartasModelComponente) {
        this.categoriaCartasModelComponente = categoriaCartasModelComponente;
    }

    public AtributoMonstruo getAtributoMonstruoSeleccionado() {
        return atributoMonstruoSeleccionado;
    }

    public void setAtributoMonstruoSeleccionado(AtributoMonstruo atributoMonstruoSeleccionado) {
        this.atributoMonstruoSeleccionado = atributoMonstruoSeleccionado;
    }

    public List<AtributoMonstruo> getAtributoMonstruosModel() {
        return atributoMonstruosModel;
    }

    public void setAtributoMonstruosModel(List<AtributoMonstruo> atributoMonstruosModel) {
        this.atributoMonstruosModel = atributoMonstruosModel;
    }

    public List<TipoMounstro> getTipoMounstrosSeleccionados() {
        return tipoMounstrosSeleccionados;
    }

    public void setTipoMounstrosSeleccionados(List<TipoMounstro> tipoMounstrosSeleccionados) {
        this.tipoMounstrosSeleccionados = tipoMounstrosSeleccionados;
    }

    public List<TipoMounstro> getTipoMounstrosModel() {
        return tipoMounstrosModel;
    }

    public void setTipoMounstrosModel(List<TipoMounstro> tipoMounstrosModel) {
        this.tipoMounstrosModel = tipoMounstrosModel;
    }

    public String getMaterialesInvocacion() {
        return materialesInvocacion;
    }

    public void setMaterialesInvocacion(String materialesInvocacion) {
        this.materialesInvocacion = materialesInvocacion;
    }

    public Integer getAtaque() {
        return ataque;
    }

    public void setAtaque(Integer ataque) {
        this.ataque = ataque;
    }

    public Integer getDefensa() {
        return defensa;
    }

    public void setDefensa(Integer defensa) {
        this.defensa = defensa;
    }

    public TipoEscala getTipoEscalaSeleccionada() {
        return tipoEscalaSeleccionada;
    }

    public void setTipoEscalaSeleccionada(TipoEscala tipoEscalaSeleccionada) {
        this.tipoEscalaSeleccionada = tipoEscalaSeleccionada;
    }

    public List<TipoEscala> getTipoEscalasModel() {
        return tipoEscalasModel;
    }

    public void setTipoEscalasModel(List<TipoEscala> tipoEscalasModel) {
        this.tipoEscalasModel = tipoEscalasModel;
    }

    public Short getEscala() {
        return escala;
    }

    public void setEscala(Short escala) {
        this.escala = escala;
    }

    public String getEfectoPendulo() {
        return efectoPendulo;
    }

    public void setEfectoPendulo(String efectoPendulo) {
        this.efectoPendulo = efectoPendulo;
    }

    public Short getEscalaIzquierda() {
        return escalaIzquierda;
    }

    public void setEscalaIzquierda(Short escalaIzquierda) {
        this.escalaIzquierda = escalaIzquierda;
    }

    public Short getEscalaDerecha() {
        return escalaDerecha;
    }

    public void setEscalaDerecha(Short escalaDerecha) {
        this.escalaDerecha = escalaDerecha;
    }

    public List<FlechaLink> getFlechaLinksSeleccionadas() {
        return flechaLinksSeleccionadas;
    }

    public void setFlechaLinksSeleccionadas(List<FlechaLink> flechaLinksSeleccionadas) {
        this.flechaLinksSeleccionadas = flechaLinksSeleccionadas;
    }

    public List<FlechaLink> getFlechaLinksModel() {
        return flechaLinksModel;
    }

    public void setFlechaLinksModel(List<FlechaLink> flechaLinksModel) {
        this.flechaLinksModel = flechaLinksModel;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public Carta getCartaSeleccionada() {
        return cartaSeleccionada;
    }

    public void setCartaSeleccionada(Carta cartaSeleccionada) {
        this.cartaSeleccionada = cartaSeleccionada;
    }

    public boolean isTipoMonstruoSeleccionado() {
        return tipoMonstruoSeleccionado;
    }

    public void setTipoMonstruoSeleccionado(boolean tipoMonstruoSeleccionado) {
        this.tipoMonstruoSeleccionado = tipoMonstruoSeleccionado;
    }

    public boolean isTipoMonstruoPenduloSeleccionado() {
        return tipoMonstruoPenduloSeleccionado;
    }

    public void setTipoMonstruoPenduloSeleccionado(boolean tipoMonstruoPenduloSeleccionado) {
        this.tipoMonstruoPenduloSeleccionado = tipoMonstruoPenduloSeleccionado;
    }

    public boolean isTipoMonstruoLinkSeleccionado() {
        return tipoMonstruoLinkSeleccionado;
    }

    public void setTipoMonstruoLinkSeleccionado(boolean tipoMonstruoLinkSeleccionado) {
        this.tipoMonstruoLinkSeleccionado = tipoMonstruoLinkSeleccionado;
    }

    public boolean isTipoMonstruoExtraDeck() {
        return tipoMonstruoExtraDeck;
    }

    public void setTipoMonstruoExtraDeck(boolean tipoMonstruoExtraDeck) {
        this.tipoMonstruoExtraDeck = tipoMonstruoExtraDeck;
    }

    public boolean isTipoMonstruoNormal() {
        return tipoMonstruoNormal;
    }

    public void setTipoMonstruoNormal(boolean tipoMonstruoNormal) {
        this.tipoMonstruoNormal = tipoMonstruoNormal;
    }

    public String getCartaNombreFormateado() {
        return cartaNombreFormateado;
    }

    public void setCartaNombreFormateado(String cartaNombreFormateado) {
        this.cartaNombreFormateado = cartaNombreFormateado;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Acciones y/o Eventos">

    /**
     * Metodo para cargar las categorias de cartas en grupos
     */
    private void cargarCategoriasAgrupadas(List<CategoriaCarta> categoriasCartas) {
        LOG.log(INFO, "[GestionCartaController][cargarCategoriasAgrupadas] -> {0}", new Object[]{categoriasCartas.size()});
        try {
            categoriasCartas.forEach((CategoriaCarta cc) -> {
                if (cc.getSubcategorias() != null && !cc.getSubcategorias().isEmpty()) {
                    SelectItemGroup grupoitem = new SelectItemGroup(cc.getNombre());
                    List<SelectItem> subitems = new ArrayList<>();
                    cc.getSubcategorias().sort(Comparator.comparing(CategoriaCarta::getNombre));
                    cc.getSubcategorias().stream().forEach((CategoriaCarta scc) -> {
                        if (scc.isEstado()) {
                            SelectItem item = new SelectItem();
                            item.setLabel(scc.getNombre());
                            item.setValue(scc);
                            subitems.add(item);
                        }
                    });
                    grupoitem.setSelectItems(subitems.toArray(new SelectItem[subitems.size()]));
                    categoriaCartasModelComponente.add(grupoitem);
                }
            });
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionCartaController][cargarCategoriasAgrupadas][Excepcion] -> ", e);
        }
    }

    /**
     * Metodo encargado de realizar las verificaciones de las categorias de cartas
     */
    private void evaluarCategorias() {
        LOG.log(Level.INFO, "[GestionCartaController][evaluarCategorias]");
        try {
            if (categoriasCartaSeleccionadas != null && !categoriasCartaSeleccionadas.isEmpty()) {
                String normalizacionString = Normalizer.normalize(categoriasCartaSeleccionadas.stream().map(CategoriaCarta::getNombre).collect(Collectors.joining(",")), Normalizer.Form.NFD);
                Pattern patron = Pattern.compile("\\p{InCOMBINING_DIACRITICAL_MARKS}+");
                String nombreDatoFormateado = patron.matcher(normalizacionString).replaceAll("");
                ComponenteDeck componenteDeck = categoriasCartaSeleccionadas.stream().flatMap(ccs -> ccs.getComponentesDecks().stream()).filter(cd -> StringUtils.containsIgnoreCase(cd.getSeccion(), "Extra")).findAny().orElse(null);
                tipoMonstruoExtraDeck = componenteDeck != null;
                tipoMonstruoSeleccionado = StringUtils.containsIgnoreCase(nombreDatoFormateado, "monstruo");
                boolean tipoMagiaSeleccionada = StringUtils.containsIgnoreCase(nombreDatoFormateado, "magica");
                boolean tipoTrampaSeleccionada = StringUtils.containsIgnoreCase(nombreDatoFormateado, "trampa");

                if (tipoMagiaSeleccionada || tipoTrampaSeleccionada) {
                    if (categoriaCartaSeleccionada != null)
                        categoriasCartaSeleccionadas.removeIf(cc -> !cc.getCategoriaCartaSuperior().equals(categoriaCartaSeleccionada.getCategoriaCartaSuperior()));
//                        categoriasCartaSeleccionadas.add(categoriaCartaSeleccionada);
                    tipoMonstruoSeleccionado = false;
                    tipoMonstruoLinkSeleccionado = false;
                    tipoMonstruoPenduloSeleccionado = false;
                    tipoMonstruoNormal = false;
                }

                if (tipoMonstruoSeleccionado) {
                    if (categoriaCartaSeleccionada != null) {
//                        categoriasCartaSeleccionadas.add(categoriaCartaSeleccionada);
                        categoriasCartaSeleccionadas.removeIf(cc -> !cc.getCategoriaCartaSuperior().equals(categoriaCartaSeleccionada.getCategoriaCartaSuperior()));
                    }
                    tipoMonstruoLinkSeleccionado = StringUtils.containsIgnoreCase(nombreDatoFormateado, "enlace");
                    tipoMonstruoPenduloSeleccionado = StringUtils.containsIgnoreCase(nombreDatoFormateado, "pendulo");
                    boolean tipoMonstruoEfecto = StringUtils.containsIgnoreCase(nombreDatoFormateado, "efecto");
                    tipoMonstruoNormal = StringUtils.containsIgnoreCase(nombreDatoFormateado, "normal");
                    if (tipoMonstruoEfecto) {
                        categoriasCartaSeleccionadas.removeIf(cc -> tipoMonstruoSeleccionado && StringUtils.containsIgnoreCase(cc.getNombre(), "normal"));
                        tipoMonstruoNormal = false;
                    }

                    if (tipoMonstruoNormal) {
                        categoriasCartaSeleccionadas.removeIf(cc -> tipoMonstruoSeleccionado && StringUtils.containsIgnoreCase(cc.getNombre(), "efecto"));
                    }
                }
            } else {
                tipoMonstruoSeleccionado = false;
                tipoMonstruoLinkSeleccionado = false;
                tipoMonstruoPenduloSeleccionado = false;
                tipoMonstruoNormal = false;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionCartaController][evaluarCategorias][Excepcion] -> ", e);
        }
    }

    /**
     * Evento para boton de agregar categorias de cartas al listado
     */
    public void onClickBtnAgregarCategoria() {
        LOG.log(Level.INFO, "[GestionCartaController][onClickBtnAgregarCategoria]");
        if (categoriasCartaSeleccionadas == null)
            categoriasCartaSeleccionadas = new ArrayList<>();
        try {
            if (!categoriasCartaSeleccionadas.contains(categoriaCartaSeleccionada)) {
                categoriasCartaSeleccionadas.add(categoriaCartaSeleccionada);
                evaluarCategorias();
            }
            categoriaCartaSeleccionada = null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionCartaController][onClickBtnAgregarCategoria][Excepcion] -> ", e);
        }
    }

    /**
     * Evento de link para quitar elementos de la lista de categorias de cartas
     */
    public void onClickItemDtlstCategorias() {
        LOG.log(Level.INFO, "[GestionCartaController][onClickItemDtlstCategorias]");
        if (categoriaCartaSeleccionada != null) {
            categoriasCartaSeleccionadas.removeIf(ccs -> ccs.equals(categoriaCartaSeleccionada));
            evaluarCategorias();
            categoriaCartaSeleccionada = null;
        }
    }

    /**
     * Funcion encargada de subir los archivos y generar la referencia del archivo
     * para guardar en base de datos
     *
     * @return Ilustracion de la carta
     * @throws Exception
     */
    public Imagen subirIlustracion() throws Exception {
        LOG.log(INFO, "[GestionCartaController][subirIlustracion]");
        try (InputStream ins = imagen.getInputStream()) {
            Path carpetaSubida = gestionArchivos.getDirectorioSubida(Constantes.CARPETA_IMAGENES_ILUSTRACIONES);
            if (carpetaSubida != null) {
                StringJoiner nombreArchivo = new StringJoiner(".");
                nombreArchivo.add(UUID.randomUUID().toString());
                nombreArchivo.add(FilenameUtils.getExtension(imagen.getFileName()));
                File imagenASubir = new File(carpetaSubida.toFile(), nombreArchivo.toString());
                Files.copy(ins, imagenASubir.toPath());
                Imagen imagenCarta;
                if (imagenASubir.length() > 0) {
                    if (cartaSeleccionada != null && cartaSeleccionada.getImagen() != null) {
                        File imagenAnterior = Paths.get(carpetaSubida.toString(), cartaSeleccionada.getImagen().getArchivo()).toFile();
                        gestionArchivos.añadirArchivoPorSubir(imagenAnterior);
                        imagenCarta = cartaSeleccionada.getImagen();
                    } else {
                        imagenCarta = new Imagen();
                    }
                    imagenCarta.setArchivo(nombreArchivo.toString());
                    gestionArchivos.confirmarArchivoSubido(imagenASubir);
                    return imagenCarta;
                } else {
                    gestionArchivos.añadirArchivoPorSubir(imagenASubir);
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionCartaController][subirIlustracion][Excepcion] -> ", e);
            return null;
        }
    }

    public void validarCarta() throws Exception {
        LOG.log(Level.INFO, "[GestionCartaController][validarCarta]");
        if (categoriasCartaSeleccionadas == null || categoriasCartaSeleccionadas.isEmpty()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage mensaje = new FacesMessage();
            mensaje.setSeverity(FacesMessage.SEVERITY_ERROR);
            mensaje.setDetail("Debes ingresar por lo menos una categoria de la carta");
            fc.addMessage(null, mensaje);
            throw new Exception("No has selecciona ninguna cateogria");
        }
    }

    /**
     * Metodo encargado de guardar la carta
     *
     * @return Pagina de listado de cartas registradas
     * @throws Exception
     */
    public String guardarCarta() throws Exception {
        LOG.log(Level.INFO, "[GestionCartaController][guardarCarta]");
        try {
            validarCarta();
            Carta cartaGuardar = new Carta();
            if (cartaSeleccionada != null && cartaSeleccionada.getId() != null) {
                cartaGuardar.setId(cartaSeleccionada.getId());
            }
            cartaGuardar.setNombre(nombre);
            if (!tipoMonstruoNormal) {
                cartaGuardar.setEfecto(efecto);
            }
            cartaGuardar.setLimite(limitacionCartaSeleccionada);
            List<CategoriaCarta> categoriaCartasList = new ArrayList<>();
            categoriaCartasModel.forEach(cc -> {
                if (!categoriasCartaSeleccionadas.contains(cc)) {
                    cc.setRemover(true);
                }
                //Agregar validacion que sean el misma categoria de carta
                categoriaCartasList.add(cc);

            });
            cartaGuardar.setCategorias(categoriaCartasList);
            if (tipoMonstruoSeleccionado) {
                Monstruo monstruoGuardar = new Monstruo();
                if (cartaSeleccionada != null && cartaSeleccionada.getMonstruo() != null && cartaSeleccionada.getMonstruo().getId() != null) {
                    monstruoGuardar.setId(cartaSeleccionada.getMonstruo().getId());
                }
                monstruoGuardar.setAtributo(atributoMonstruoSeleccionado);
                monstruoGuardar.setTipos(tipoMounstrosSeleccionados);
                if (tipoMonstruoExtraDeck) {
                    monstruoGuardar.setMaterialInvocacion(materialesInvocacion);
                }
                monstruoGuardar.setAtaque(ataque);
                if (!tipoMonstruoLinkSeleccionado) {
                    monstruoGuardar.setDefensa(defensa);
                }
                monstruoGuardar.setTipoEscala(tipoEscalaSeleccionada);
                monstruoGuardar.setEscala(escala);
                if (tipoMonstruoPenduloSeleccionado) {
                    Pendulo penduloGuardar = new Pendulo();
                    if (cartaSeleccionada != null && cartaSeleccionada.getMonstruo().getPenduloAtributos() != null && cartaSeleccionada.getMonstruo().getPenduloAtributos().getId() != null) {
                        penduloGuardar.setId(cartaSeleccionada.getMonstruo().getPenduloAtributos().getId());
                    }
                    penduloGuardar.setEfecto(efectoPendulo);
                    penduloGuardar.setIzquierda(escalaIzquierda);
                    penduloGuardar.setDerecha(escalaDerecha);
                    penduloGuardar.setMonstruo(monstruoGuardar);
                    monstruoGuardar.setPenduloAtributos(penduloGuardar);
                }

                if (tipoMonstruoLinkSeleccionado) {
                    List<FlechaLink> flechasARemover = new ArrayList<>();
                    flechaLinksModel.stream().forEach(fl -> {
                        if (!flechaLinksSeleccionadas.contains(fl)) {
                            fl.setRemover(true);
                        }
                        flechasARemover.add(fl);
                    });
                    monstruoGuardar.setFlechasLinks(flechasARemover);
                }
                monstruoGuardar.setCarta(cartaGuardar);
                cartaGuardar.setMonstruo(monstruoGuardar);
            }
            Imagen ilustracion = subirIlustracion();
            if (ilustracion != null) {
                cartaGuardar.setImagen(ilustracion);
            }
            cartaBean.guardarCarta(cartaGuardar);
            return "cartas";
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[GestionCartaController][guardarCarta][Excepcion] -> ", e);
            throw new Exception(e);
        } finally {
            gestionArchivos.eliminarArchivos();
        }
    }
    //</editor-fold>
}
