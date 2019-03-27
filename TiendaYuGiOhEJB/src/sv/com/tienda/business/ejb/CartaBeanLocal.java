package sv.com.tienda.business.ejb;

import sv.com.tienda.business.entity.*;

import javax.ejb.Local;
import java.util.List;

@Local
public interface CartaBeanLocal {


    /**
     * Retorna el listado de los tipos de cartas
     * <p>
     * Ejemplo: Mounstros, Magias, Trampas
     *
     * @param estado
     * @return Listado de Categorias
     */
    List<CategoriaCarta> obtenerListadoDeCategorias(boolean estado);

    /**
     * Metodo encargado de guardar las categorias de las cartas
     *
     * @param categoriaCartaGuardar
     */
    void guardarCategoriaCarta(CategoriaCarta categoriaCartaGuardar);

    /**
     * Obtiene la categoria de carta de acuerdo a su id
     * @param id
     * @return Categoria de Carta
     */
    CategoriaCarta obtenerCategoria(Integer id);

    /**
     * Obtiene el lista de categorias de cartas superiores (en este caso solo se
     * consideran a Magicas, Trampas y Mounstros como categoria superior)
     * @return Listado Categoria Superior
     */
    List<CategoriaCarta> obtenerListadoDeCategoriasSuperiores();

    /**
     * Metodo encargado de eliminar (ocultar una categoria de cartas
     *
     * @param categoriaCartaEliminar
     */
    void eliminarCategoriasCartas(CategoriaCarta categoriaCartaEliminar);

    /**
     * Obtiene el listado de atributos de mounstros
     *
     * @param estado
     * @return Listado de Atributos de Monstruos
     */
    List<AtributoMonstruo> obtenerListadoAtributos(boolean estado);

    /**
     * Metodo encargado de guardar ya sea un nuevo atributo a actualizar sus valores
     *
     * @param atributoMonstruoGuardar
     */
    void guardarAtributoMonstruo(AtributoMonstruo atributoMonstruoGuardar);

    /**
     * Metodo encargado de eliminar (ocultar) los registros de atributos de monstruos
     *
     * @param atributoMonstruoEliminar
     */
    void eliminarAtributoMonstruo(AtributoMonstruo atributoMonstruoEliminar);

    /**
     * Metodo que obtiene un atributo de monstruo
     *
     * @param id de atributo
     */
    AtributoMonstruo obtenerAtributoMonstruo(Integer id);

    /**
     * Metodo encargado de traer el listado de todos los tipos de monstruos
     *
     * @param estado
     * @return Listado de tipos de monstruos
     */
    List<TipoMounstro> obtenerListadoTiposMonstruos(boolean estado);

    /**
     * Metodo encargado de obtener un tipo de monstruo
     *
     * @param id del tipo de monstruo
     * @return Tipo de Monstruo
     */
    TipoMounstro obtenerTipoMonstruo(Integer id);

    /**
     * Metodo encargado de ingresar y actualizar los tipos de monstruos
     *
     * @param tipoMonstruoGuardar
     */
    void guardarTipoMonstruo(TipoMounstro tipoMonstruoGuardar);

    /**
     * Metodo encargado de eliminar (ocultar) un tipo de monstruo
     *
     * @param  tipoMounstroEliminar
     */
    void eliminarTipoMonstruo(TipoMounstro tipoMounstroEliminar);

    /**
     * Metodo encargado de retornar una lista de las partes de un deck
     * @param estado
     * @return Listado de Componentes
     */
    List<ComponenteDeck> obtenerListadoComponentesDeck(boolean estado);

    /**
     * Obtiene un componente o parte de la estructura de un deck
     * por medio de su codigo
     * @param id
     * @return Componente del Deck
     */
    ComponenteDeck obtenerComponenteDeck(Integer id);

    /**
     * Metodo encargado de ingresar y actualizar los datos de un compoenente de deck
     * @param componenteDeckGuardar
     */
    void guardarComponenteDeck(ComponenteDeck componenteDeckGuardar);

    /**
     * Metodo encargado de eliminar (ocultar) una parte de la estructura de un deck
     * @param componenteDeckEliminar
     */
    void eliminarComponenteDeck(ComponenteDeck componenteDeckEliminar);

    /**
     * Metodo que obtiene el listado de los tipos de limitaciones
     * de cartas en la banlist
     *
     * @return Listado de la limitaciones de cartas
     */
    List<LimitacionCarta> obtenerListadoLimitacion();

    /**
     * Metodo que obtiene el limite de cartas permitido en la banlist
     *
     * @param id
     * @return Limitacion de Carta
     */
    LimitacionCarta obtenerLimitacion(Integer id);

    /**
     * Metodo encargado de obtener el listado de cartas registradas
     *
     * @param estado
     * @return Listado de Cartas
     */
    List<Carta> obtenerListadoCartas(boolean estado);

    /**
     * Metodo que obtiene una carta en particular por medio del id y que su estado
     * es activo
     *
     * @param id
     * @return Carta
     */
    Carta obtenerCarta(Long id);

    /**
     * Metodo encargado de registrar o actualizar una carta
     *
     * @param cartaGuardar
     */
    void guardarCarta(Carta cartaGuardar);

    /**
     * Metodo encargado de eliminar(ocultar) una carta
     *
     *
     */
    void eliminarCarta(Carta cartaEliminar);

    /**
     * Metodo que obtiene el listado de los tipos de escala de
     * monstruos
     *
     * @return Listado de la limitaciones de cartas
     */
    List<TipoEscala> obtenerListadoTipoEscala();

    /**
     * Metodo que obtiene el tipo de escala de un monstruo
     *
     * @param id
     * @return Limitacion de Carta
     */
    TipoEscala obtenerTipoEscala(Integer id);

    /**
     * Metodo encargado de obtener el listado de las flechas de los monstruos link
     *
     * @return Listado de Flechas de Links
     */
    List<FlechaLink> obtenerListadoFlechasLink();

    /**
     * Metodo encargado de obtener la flecha de monstruos link
     *
     * @param id
     * @return Flecha de Links
     */
    FlechaLink obtenerFlechaLink(Integer id);
}
