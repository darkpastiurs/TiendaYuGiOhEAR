package sv.com.tienda.web.utils;

import org.eclipse.persistence.internal.descriptors.PersistenceEntity;
import sv.com.tienda.business.ejb.ConsultasGenericasBeanLocal;
import sv.com.tienda.business.utils.Constantes;

import javax.faces.convert.ConverterException;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public final class ConverterUtils {
    private static final Logger LOG = Logger.getLogger(ConverterUtils.class.getName());

    public ConverterUtils() {
        LOG.log(INFO, "[ConverterUtils][INIT]");
    }

    public static <T> T getAsObject(Class<T> returnEntity, String value) throws NullPointerException, ConverterException {
        if (returnEntity == null) {
            LOG.log(Level.SEVERE, "[ConverterUtils][getAsObject][NullPointerException] -> No se puede convertir debido a que no se ha definido el tipo de retorno");
            throw new NullPointerException("No se puede convertir debido a que no se ha definido el tipo de retorno");
        }
        if (value == null) {
            LOG.log(Level.SEVERE, "[ConverterUtils][getAsObject][NullPointerException] -> No se puede convertir debido a que no hay un valor de entrada");
            throw new NullPointerException("No se puede convertir debido a que no hay un valor de entrada");
        }
        if(!value.matches("[0-9]+")){
            LOG.log(Level.SEVERE, "[ConverterUtils][getAsObject][ExcepcionNoNumero] -> No se puede convertir debido a que no hay un valor de entrada");
            throw new ConverterException("No se puede convertir debido a que no hay un valor de entrada");
        }
        try {
            Context initialContext = new InitialContext();
            ConsultasGenericasBeanLocal consultasGenericasBean = (ConsultasGenericasBeanLocal) initialContext.lookup(Constantes.JDNI_CONSULTAS_GENERICAS_BEAN);
            T result = consultasGenericasBean.find(returnEntity, value);
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[ConverterUtils][getAsObject][Excepcion] -> ", e);
            throw new ConverterException("No se pudo cargar datos del EJB");
        }
    }

    public static String getAsString(Object value) {
        if (value instanceof PersistenceEntity) {
            PersistenceEntity result = (PersistenceEntity) value;
            return String.valueOf(result._persistence_getId());
        }
        return null;
    }
}
