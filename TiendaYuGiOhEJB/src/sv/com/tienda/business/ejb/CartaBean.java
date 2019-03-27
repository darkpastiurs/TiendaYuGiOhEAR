package sv.com.tienda.business.ejb;

import org.apache.commons.lang.StringUtils;
import sv.com.tienda.business.entity.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

@Stateless
public class CartaBean implements CartaBeanLocal {

    private static final Logger LOG = Logger.getLogger(CartaBean.class.getName());
    @PersistenceContext(unitName = "TiendaPU")
    private EntityManager em;

    //<editor-fold defaultstate="collapsed" desc="Metodos de Categorias de Cartas">

    /**
     * Obtiene el listado de todas las categorias registradas dependiendo de su estado
     *
     * @param estado
     * @return Listado de Categoria de  Cartas
     */
    @Override
    public List<CategoriaCarta> obtenerListadoDeCategorias(boolean estado) {
        LOG.log(INFO, "[CartaBean][obtenerListadoDeCategorias]");
        List<CategoriaCarta> categoriaCartas = null;
        Query query;
        try {
            query = em.createNamedQuery("CategoriaCartas.findByEstado");
            query.setParameter("estado", estado);
            categoriaCartas = query.getResultList();

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerListadoDeCategorias][Excepcion] -> ", e);
        }
        return categoriaCartas;
    }

    /**
     * Obtiene el lista de categorias de cartas superiores (en este caso solo se
     * consideran a Magicas, Trampas y Mounstros como categoria superior)
     *
     * @return Listado Categoria Superior
     */
    @Override
    public List<CategoriaCarta> obtenerListadoDeCategoriasSuperiores() {
        LOG.log(INFO, "[CartaBean][obtenerListadoDeCategorias]");
        List<CategoriaCarta> categoriaCartas = null;
        Query query;
        try {
            query = em.createNamedQuery("CategoriaCartas.onlyCategoriaSuperior");
            categoriaCartas = query.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerListadoDeCategorias][Excepcion] -> ", e);
        }
        return categoriaCartas;
    }


    /**
     * Obtiene la categoria de carta de acuerdo a su id
     *
     * @param id
     * @return Categoria de Carta
     */
    @Override
    public CategoriaCarta obtenerCategoria(Integer id) {
        LOG.log(INFO, "[CartaBean][obtenerCategoria] -> {0}", new Object[]{id});
        CategoriaCarta categoriaCarta = null;
        Query query;
        try {
            query = em.createNamedQuery("CategoriaCartas.findByIdActivo");
            query.setParameter("id", id);
            categoriaCarta = (CategoriaCarta) query.getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerCategoria][Excepcion] -> ", e);
        }
        return categoriaCarta;
    }

    /**
     * Metodo encargado de guardar las categorias de las cartas
     *
     * @param categoriaCartaGuardar
     */
    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public void guardarCategoriaCarta(CategoriaCarta categoriaCartaGuardar) {
        LOG.log(INFO, "[CartaBean][guardarCategoriaCarta] -> {0}", new Object[]{categoriaCartaGuardar});
        CategoriaCarta categoriaCarta;
        try {
            if (categoriaCartaGuardar.getId() == null) {
                categoriaCarta = new CategoriaCarta();
            } else {
                categoriaCarta = em.find(CategoriaCarta.class, categoriaCartaGuardar.getId());
            }
            categoriaCarta.setNombre(categoriaCartaGuardar.getNombre());
            if (categoriaCartaGuardar.getCategoriaCartaSuperior() != null) {
                CategoriaCarta categoriaCartaSuperior = em.find(CategoriaCarta.class, categoriaCartaGuardar.getCategoriaCartaSuperior().getId());
                categoriaCarta.setCategoriaCartaSuperior(categoriaCartaSuperior);
            }

            if (categoriaCartaGuardar.getId() == null) {
                em.persist(categoriaCarta);
            } else {
                em.merge(categoriaCarta);
            }
            em.flush();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][guardarCategoriaCarta][Excepcion] -> ", e);
        }
    }

    /**
     * Metodo encargado de eliminar (ocultar una categoria de cartas
     *
     * @param categoriaCartaEliminar
     */
    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public void eliminarCategoriasCartas(CategoriaCarta categoriaCartaEliminar) {
        LOG.log(INFO, "[CartaBean][eliminarCategoriasCartas] -> {0}", new Object[]{categoriaCartaEliminar});
        try {
            CategoriaCarta categoriaCarta = em.find(CategoriaCarta.class, categoriaCartaEliminar.getId());
            categoriaCarta.setEstado(false);
            if (categoriaCarta.getSubcategorias() != null && !categoriaCarta.getSubcategorias().isEmpty()) {
                categoriaCarta.getSubcategorias().stream().forEach((CategoriaCarta subcategoria) -> {
                    subcategoria.setEstado(false);
                });
            }
            em.merge(categoriaCarta);
            em.flush();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][eliminarCategoriasCartas][Excepcion] -> ", e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Metodos de Atributos de Monstruos">

    /**
     * Obtiene el listado de atributos de mounstros
     *
     * @param estado
     * @return Listado de Atributos de Monstruos
     */
    @Override
    public List<AtributoMonstruo> obtenerListadoAtributos(boolean estado) {
        LOG.log(INFO, "[CartaBean][obtenerListadoAtributos] -> {0}", new Object[]{estado});
        List<AtributoMonstruo> listado = null;
        Query query;
        try {
            query = em.createNamedQuery("AtributoMonstruo.findByEstado");
            query.setParameter("estado", estado);
            listado = query.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerListadoAtributos][Excepcion] -> ", e);
        }
        return listado;
    }

    /**
     * Metodo que obtiene un atributo de monstruo
     *
     * @param id de atributo
     */
    public AtributoMonstruo obtenerAtributoMonstruo(Integer id) {
        LOG.log(INFO, "[CartaBean][obtenerAtributoMonstruo] -> {0}", new Object[]{id});
        AtributoMonstruo resultado = null;
        Query query;
        try {
            query = em.createNamedQuery("AtributoMonstruo.findByIdActivo");
            query.setParameter("id", id);
            resultado = (AtributoMonstruo) query.getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerAtributoMonstruo][Excepcion] -> ", e);
        }
        return resultado;
    }

    /**
     * Metodo encargado de guardar ya sea un nuevo atributo a actualizar sus valores
     *
     * @param atributoMonstruoGuardar
     */
    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public void guardarAtributoMonstruo(AtributoMonstruo atributoMonstruoGuardar) {
        LOG.log(INFO, "[CartaBean][guardarAtributoMonstruo] -> {0}", new Object[]{atributoMonstruoGuardar});
        try {
            AtributoMonstruo atributoMonstruo = null;
            if (atributoMonstruoGuardar.getId() == null) {
                atributoMonstruo = new AtributoMonstruo();
            } else {
                atributoMonstruo = em.find(AtributoMonstruo.class, atributoMonstruoGuardar.getId());
            }

            atributoMonstruo.setNombre(atributoMonstruoGuardar.getNombre());

            if (atributoMonstruoGuardar.getId() == null) {
                em.persist(atributoMonstruo);
            } else {
                em.merge(atributoMonstruo);
            }
            em.flush();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][guardarAtributoMonstruo][Excepcion] -> ", e);
        }
    }

    /**
     * Metodo encargado de eliminar (ocultar) los registros de atributos de monstruos
     *
     * @param atributoMonstruoEliminar
     */
    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public void eliminarAtributoMonstruo(AtributoMonstruo atributoMonstruoEliminar) {
        LOG.log(INFO, "[CartaBean][eliminarAtributoMonstro] -> {0}", new Object[]{atributoMonstruoEliminar});
        try {
            AtributoMonstruo atributoMonstruo = em.find(AtributoMonstruo.class, atributoMonstruoEliminar.getId());
            atributoMonstruo.setEstado(false);
            em.merge(atributoMonstruo);
            em.flush();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][eliminarAtributoMonstro][Excepcion] -> ", e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Metodos de Tipos de Monstruos">

    /**
     * Metodo encargado de traer el listado de todos los tipos de monstruos
     *
     * @param estado
     * @return Listado de tipos de monstruos
     */
    @Override
    public List<TipoMounstro> obtenerListadoTiposMonstruos(boolean estado) {
        LOG.log(INFO, "[CartaBean][obtenerListadoTiposMonstruos] -> {0}", new Object[]{estado});
        List<TipoMounstro> listado = null;
        try {
            listado = null;
            Query query;
            query = em.createNamedQuery("TipoMonstruo.findByEstado");
            query.setParameter("estado", estado);
            listado = query.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerListadoTiposMonstruos][Excepcion] -> ", e);
        }
        return listado;
    }

    /**
     * Metodo encargado de obtener un tipo de monstruo
     *
     * @param id del tipo de monstruo
     * @return Tipo de Monstruo
     */
    @Override
    public TipoMounstro obtenerTipoMonstruo(Integer id) {
        LOG.log(INFO, "[CartaBean][obtenerTipoMonstruo] -> {0}", new Object[]{id});
        TipoMounstro resultado = null;
        Query query;
        try {
            query = em.createNamedQuery("TipoMonstruo.findById");
            query.setParameter("id", id);
            resultado = (TipoMounstro) query.getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerTipoMonstruo][Excepcion] -> ", e);
        }
        return resultado;
    }

    /**
     * Metodo encargado de ingresar y actualizar los tipos de monstruos
     *
     * @param tipoMonstruoGuardar
     */
    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public void guardarTipoMonstruo(TipoMounstro tipoMonstruoGuardar) {
        LOG.log(INFO, "[CartaBean][guardarTipoMonstruo] -> {0}", new Object[]{tipoMonstruoGuardar});
        TipoMounstro tipoMounstro = null;
        try {
            if (tipoMonstruoGuardar.getId() == null) {
                tipoMounstro = new TipoMounstro();
            } else {
                tipoMounstro = em.find(TipoMounstro.class, tipoMonstruoGuardar.getId());
            }

            tipoMounstro.setNombre(tipoMonstruoGuardar.getNombre());

            if (tipoMonstruoGuardar.getId() == null) {
                em.persist(tipoMounstro);
            } else {
                em.merge(tipoMounstro);
            }
            em.flush();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][guardarTipoMonstruo][Excepcion] -> ", e);
        }
    }

    /**
     * Metodo encargado de eliminar (ocultar) un tipo de monstruo
     *
     * @param tipoMounstroEliminar
     */
    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public void eliminarTipoMonstruo(TipoMounstro tipoMounstroEliminar) {
        LOG.log(INFO, "[CartaBean][eliminarTipoMonstruo] -> {0}", new Object[]{tipoMounstroEliminar});
        try {
            TipoMounstro tipoMounstro = em.find(TipoMounstro.class, tipoMounstroEliminar.getId());
            tipoMounstro.setEstado(false);
            em.merge(tipoMounstro);
            em.flush();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][eliminarTipoMonstruo][Excepcion] -> ", e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Metodos de Estructura de Deck">

    /**
     * Metodo encargado de retornar una lista de las partes de un deck
     *
     * @param estado
     * @return Listado de Componentes
     */
    @Override
    public List<ComponenteDeck> obtenerListadoComponentesDeck(boolean estado) {
        LOG.log(INFO, "[CartaBean][obtenerListadoComponentesDeck] -> {0}", new Object[]{estado});
        List<ComponenteDeck> listado = null;
        Query query;
        try {
            query = em.createNamedQuery("ComponenteDeck.findAllByEstado");
            query.setParameter("estado", estado);
            listado = query.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerListadoComponentesDeck][Excepcion] -> ", e);
        }
        return listado;
    }

    /**
     * Obtiene un componente o parte de la estructura de un deck
     * por medio de su codigo
     *
     * @param id
     * @return Componente del Deck
     */
    @Override
    public ComponenteDeck obtenerComponenteDeck(Integer id) {
        LOG.log(INFO, "[CartaBean][obtenerComponenteDeck] -> {0}", new Object[]{id});
        ComponenteDeck resultado = null;
        Query query;
        try {
            query = em.createNamedQuery("ComponenteDeck.findByIdActivo");
            query.setParameter("id", id);
            resultado = (ComponenteDeck) query.getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerComponenteDeck][Excepcion] -> ", e);
        }
        return resultado;
    }

    /**
     * Metodo encargado de ingresar y actualizar los datos de un compoenente de deck
     *
     * @param componenteDeckGuardar
     */
    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public void guardarComponenteDeck(ComponenteDeck componenteDeckGuardar) {
        LOG.log(INFO, "[CartaBean][guardarComponenteDeck] -> {0}", new Object[]{componenteDeckGuardar});
        try {
            ComponenteDeck componenteDeck;
            if (componenteDeckGuardar.getId() == null) {
                componenteDeck = new ComponenteDeck();
            } else {
                componenteDeck = em.find(ComponenteDeck.class, componenteDeckGuardar.getId());
            }
            componenteDeck.setSeccion(componenteDeckGuardar.getSeccion());
            componenteDeck.setNumeroMinimo(componenteDeckGuardar.getNumeroMinimo());
            componenteDeck.setNumeroMaximo(componenteDeckGuardar.getNumeroMaximo());
            if (componenteDeckGuardar.getCategoriasCarta() != null) {
                componenteDeckGuardar.getCategoriasCarta().stream().forEach((CategoriaCarta cc) -> {
                    if (cc.isRemover()) {
                        componenteDeck.removeCategoriaCarta(cc);
                    } else {
                        componenteDeck.addCategoriaCarta(cc);
                    }
                });
            }
            if (componenteDeckGuardar.getId() == null) {
                em.persist(componenteDeck);
            } else {
                em.merge(componenteDeck);
            }
            em.flush();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][guardarComponenteDeck][Excepcion] -> ", e);
        }
    }

    /**
     * Metodo encargado de eliminar (ocultar) una parte de la estructura de un deck
     *
     * @param componenteDeckEliminar
     */
    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public void eliminarComponenteDeck(ComponenteDeck componenteDeckEliminar) {
        LOG.log(INFO, "[CartaBean][eliminarComponenteDeck] -> {0}", new Object[]{componenteDeckEliminar});
        try {
            ComponenteDeck componenteDeck = em.find(ComponenteDeck.class, componenteDeckEliminar.getId());
            componenteDeck.setEstado(false);
            em.merge(componenteDeck);
            em.flush();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][eliminarComponenteDeck][Excepcion] -> ", e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Metodos de Cartas">

    //<editor-fold defaultstate="collapsed" desc="Limitaciones de Cartas">

    /**
     * Metodo que obtiene el listado de los tipos de limitaciones
     * de cartas en la banlist
     *
     * @return Listado de la limitaciones de cartas
     */
    @Override
    public List<LimitacionCarta> obtenerListadoLimitacion() {
        LOG.log(INFO, "[CartaBean][obtenerListadoLimitacion]");
        List<LimitacionCarta> limitacionCartas = null;
        Query query;
        try {
            query = em.createNamedQuery("LimitacionesCarta.findAll");
            limitacionCartas = query.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerListadoLimitacion][Excepcion] -> ", e);
        }
        return limitacionCartas;
    }

    /**
     * Metodo que obtiene el limite de cartas permitido en la banlist
     *
     * @param id
     * @return Limitacion de Carta
     */
    @Override
    public LimitacionCarta obtenerLimitacion(Integer id) {
        LOG.log(INFO, "[CartaBean][obtenerLimitacion] -> {0}", new Object[]{id});
        LimitacionCarta limitacionCarta = null;
        Query query;
        try {
            query = em.createNamedQuery("LimitacionesCarta.findById");
            query.setParameter("id", id);
            limitacionCarta = (LimitacionCarta) query.getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerLimitacion][Excepcion] -> ", e);
        }
        return limitacionCarta;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tipos de Escala">

    /**
     * Metodo que obtiene el listado de los tipos de escala de
     * monstruos
     *
     * @return Listado de la limitaciones de cartas
     */
    @Override
    public List<TipoEscala> obtenerListadoTipoEscala() {
        LOG.log(INFO, "[CartaBean][obtenerListadoTipoEscala]");
        List<TipoEscala> tipoEscalas = null;
        Query query;
        try {
            query = em.createNamedQuery("TipoEscalas.findAll");
            tipoEscalas = query.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerListadoTipoEscala][Excepcion] -> ", e);
        }
        return tipoEscalas;
    }

    /**
     * Metodo que obtiene el tipo de escala de un monstruo
     *
     * @param id
     * @return Limitacion de Carta
     */
    @Override
    public TipoEscala obtenerTipoEscala(Integer id) {
        LOG.log(INFO, "[CartaBean][obtenerTipoEscala] -> {0}", new Object[]{id});
        TipoEscala tipoEscala = null;
        Query query;
        try {
            query = em.createNamedQuery("TipoEscalas.findById");
            query.setParameter("id", id);
            tipoEscala = (TipoEscala) query.getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerTipoEscala][Excepcion] -> ", e);
        }
        return tipoEscala;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Flechas Link">

    /**
     * Metodo encargado de obtener el listado de las flechas de los monstruos link
     *
     * @return Listado de Flechas de Links
     */
    @Override
    public List<FlechaLink> obtenerListadoFlechasLink() {
        LOG.log(Level.INFO, "[CartaBean][obtenerListadoFlechasLink]");
        List<FlechaLink> flechaLinks = null;
        Query query;
        try {
            query = em.createNamedQuery("FlechasLink.findAll");
            flechaLinks = query.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerListadoFlechasLink][Excepcion] -> ", e);
        }
        return flechaLinks;
    }

    /**
     * Metodo encargado de obtener la flecha de monstruos link
     *
     * @param id
     * @return Flecha de Links
     */
    @Override
    public FlechaLink obtenerFlechaLink(Integer id) {
        LOG.log(Level.INFO, "[CartaBean][obtenerFlechaLink]");
        FlechaLink flechaLink = null;
        Query query;
        try {
            query = em.createNamedQuery("FlechasLink.findById");
            query.setParameter("id", id);
            flechaLink = (FlechaLink) query.getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerFlechaLink][Excepcion] -> ", e);
        }
        return flechaLink;
    }
    //</editor-fold>

    /**
     * Metodo encargado de obtener el listado de cartas registradas
     *
     * @param estado
     * @return Listado de Cartas
     */
    @Override
    public List<Carta> obtenerListadoCartas(boolean estado) {
        LOG.log(INFO, "[CartaBean][obtenerListadoCarga] -> {0}", new Object[]{estado});
        List<Carta> cartas = null;
        Query query;
        try {
            query = em.createNamedQuery("Cartas.findAllEstado");
            query.setParameter("estado", estado);
            cartas = query.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerListadoCarga][Excepcion] -> ", e);
        }
        return cartas;
    }

    /**
     * Metodo que obtiene una carta en particular por medio del id y que su estado
     * es activo
     *
     * @param id
     * @return Carta
     */
    @Override
    public Carta obtenerCarta(Long id) {
        LOG.log(INFO, "[CartaBean][obtenerCarta] -> {0}", new Object[]{id});
        Carta carta = null;
        Query query;
        try {
            query = em.createNamedQuery("Cartas.findByIdActivo");
            query.setParameter("id", id);
            carta = (Carta) query.getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][obtenerCarta][Excepcion] -> ", e);
        }
        return carta;
    }

    /**
     * Metodo encargado de registrar o actualizar una carta
     *
     * @param cartaGuardar
     */
    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public void guardarCarta(Carta cartaGuardar) {
        LOG.log(INFO, "[CartaBean][guardarCarta] -> {0}", new Object[]{cartaGuardar});
        try {
            Carta carta;
            if (cartaGuardar.getId() == null) {
                carta = new Carta();
            } else {
                carta = em.find(Carta.class, cartaGuardar.getId());
            }
            carta.setNombre(cartaGuardar.getNombre());
            carta.setEfecto(cartaGuardar.getEfecto());
            List<CategoriaCarta> categoriaCartas = cartaGuardar.getCategorias();
            categoriaCartas.forEach(cc -> {
                if(cc.isRemover()){
                    carta.removeCategoria(cc);
                }else{
                    carta.addCategoria(cc);
                }
            });
            carta.setLimite(cartaGuardar.getLimite());
            if (cartaGuardar.getMonstruo() != null) {
                Monstruo monstruoGuardar = cartaGuardar.getMonstruo();
                Monstruo monstruo;
                if (monstruoGuardar.getId() != null) {
                    monstruo = em.find(Monstruo.class, monstruoGuardar.getId());
                } else {
                    monstruo = new Monstruo();
                }
                monstruo.setAtaque(monstruoGuardar.getAtaque());
                monstruo.setDefensa(monstruoGuardar.getDefensa());
                monstruo.setAtributo(monstruoGuardar.getAtributo());
                List<TipoMounstro> tipoMounstros = monstruoGuardar.getTipos();
                tipoMounstros.stream().forEach((TipoMounstro tm) -> {
                    if (tm.isRemover()) {
                        monstruo.removeTipoMonstruo(tm);
                    } else {
                        monstruo.addTipoMonstruo(tm);
                    }
                });
                if (StringUtils.isNotBlank(monstruoGuardar.getMaterialInvocacion())) {
                    monstruo.setMaterialInvocacion(cartaGuardar.getMonstruo().getMaterialInvocacion());
                }
                monstruo.setTipoEscala(monstruoGuardar.getTipoEscala());
                monstruo.setEscala(monstruoGuardar.getEscala());
                if (monstruoGuardar.getPenduloAtributos() != null) {
                    Pendulo penduloGuardar = monstruoGuardar.getPenduloAtributos();
                    Pendulo pendulo;
                    if (penduloGuardar.getId() != null) {
                        pendulo = em.find(Pendulo.class, carta.getId());
                    } else {
                        pendulo = new Pendulo();
                    }
                    pendulo.setEfecto(penduloGuardar.getEfecto());
                    pendulo.setIzquierda(penduloGuardar.getIzquierda());
                    pendulo.setDerecha(penduloGuardar.getDerecha());
                    pendulo.setMonstruo(monstruo);
                    monstruo.setPenduloAtributos(pendulo);
                } else {
                    monstruo.setPenduloAtributos(null);
                }
                if (monstruoGuardar.getFlechasLinks() != null && !monstruoGuardar.getFlechasLinks().isEmpty()) {
                    List<FlechaLink> flechaLinks = monstruoGuardar.getFlechasLinks();
                    flechaLinks.stream().forEach(flechaLink -> {
                        if (flechaLink.isRemover()) {
                            monstruo.removerFlechaLink(flechaLink);
                        } else {
                            monstruo.addFlechaLink(flechaLink);
                        }
                    });
                } else {
                    monstruo.setFlechasLinks(null);
                }
                monstruo.setCarta(carta);
                carta.setMonstruo(monstruo);
            }

            if (cartaGuardar.getImagen() != null) {
                Imagen ilustracionGuardar = cartaGuardar.getImagen();
                Imagen ilustracion = null;
                if (ilustracionGuardar.getId() != null) {
                    ilustracion = em.find(Imagen.class, ilustracionGuardar.getId());
                } else {
                    ilustracion = new Imagen();
                }
                ilustracion.setArchivo(ilustracionGuardar.getArchivo());
                carta.setImagen(ilustracion);
            }
            if (carta.getId() == null) {
                em.persist(carta);
            } else {
                em.merge(carta);
            }
            em.flush();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][guardarCarta][Excepcion] -> ", e);
        }
    }

    /**
     * Metodo encargado de eliminar(ocultar) una carta
     */
    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public void eliminarCarta(Carta cartaEliminar) {
        LOG.log(INFO, "[CartaBean][eliminarCarta] -> {0}", new Object[]{cartaEliminar});
        try {
            Carta carta = em.find(Carta.class, cartaEliminar.getId());
            carta.setEstado(false);
            carta.getMonstruo().setEstado(false);
            em.merge(carta);
            em.flush();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[CartaBean][eliminarCarta][Excepcion] -> ", e);
        }
    }
    //</editor-fold>
}