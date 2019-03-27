package sv.com.tienda.business.ejb;

import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Local(ConsultasGenericasBeanLocal.class)
@Stateless
@PermitAll
public class ConsultasGenericasBean implements ConsultasGenericasBeanLocal {

    private static final Logger LOG = Logger.getLogger(ConsultasGenericasBean.class.getName());
    @PersistenceContext(unitName = "TiendaPU")
    private EntityManager em;

    /**
     * Metodo para consultas generales del sistema
     * principalmente para los conversores
     *
     * @param entity
     * @param id
     */
    @Override
    public <T> T find(Class<T> entity, Object id) {
//        LOG.log(Level.INFO, "[ConsultasGenericasBean][find] -> {0}, {1}", new Object[]{entity, id});
        Object idValue = null;
        try {
            Class idType = null;
            for (Field f : entity.getDeclaredFields()) {
                if (idType == null) {
                    for (Annotation a : f.getAnnotations()) {
                        if (a instanceof Id) {
                            idType = f.getType();
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            idValue = idType.getMethod("valueOf", String.class).invoke(idType, id);
        } catch (NumberFormatException e) {
            LOG.log(Level.SEVERE, "[ConsultasGenericasBean][find][NumberFormatException] -> ", e);
        } catch (IllegalAccessException e) {
            LOG.log(Level.SEVERE, "[ConsultasGenericasBean][find][IllegalAccessException] -> ", e);
        } catch (InvocationTargetException e) {
            LOG.log(Level.SEVERE, "[ConsultasGenericasBean][find][InvocationTargetException] -> ", e);
        } catch (NoSuchMethodException e) {
            LOG.log(Level.SEVERE, "[ConsultasGenericasBean][find][NoSuchMethodException] -> ", e);
        }
        return em.find(entity, idValue);
    }

}
