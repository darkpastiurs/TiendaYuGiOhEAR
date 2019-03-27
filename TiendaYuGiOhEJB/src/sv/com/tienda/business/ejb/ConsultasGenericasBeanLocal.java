package sv.com.tienda.business.ejb;

import javax.ejb.Local;

@Local
public interface ConsultasGenericasBeanLocal {
    /**
     * Metodo para consultas generales del sistema
     * principalmente para los conversores
     *  @param entity
     * @param id*/
    <T> T find(Class<T> entity, Object id);
}
