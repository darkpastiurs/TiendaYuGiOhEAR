package sv.com.tienda.business.ejb;

import sv.com.tienda.business.entity.Token;
import sv.com.tienda.business.entity.Usuario;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import java.util.List;

@Local
public interface UsuarioBeanLocal {

    /**
     * Validacion del usuario a la hora de loguearse
     *
     * @param usernameOrEmail
     * @param password
     * @return Usuario
     */
    Usuario validarUsuarios(String usernameOrEmail, String password);

    /**
     * Metodo para obtener usuario por el nickname
     * @param username
     * @return
     * @throws NoResultException
     * @throws Exception
     */
    Usuario obtenerUsuarioPorNickname(String username) throws Exception;

    /**
     * Metodo encargado de obtener el listado de usuarios registrados en a¿la web
     *
     * @return Listado de usuarios
     * @throws Exception
     */
    List<Usuario> obtenerUsuarios() throws Exception;

    /**
     * Metodo para resetear la contraseña del usuario
     *
     * @param usuario
     * @param passwordTemporal
     * @throws Exception
     */
    void cambiarUsuarioPassword(Usuario usuario, String passwordTemporal) throws Exception;

    /**
     * Metodo que obtiene el token de la cookie del navegador
     *
     * @param referencia
     * @return
     * @throws Exception
     */
    Token obtenerTokenPorReferencia(String referencia) throws Exception;

    /**
     * Metodo encargado de actualizar el token de la cookie del navegador
     *
     * @param usuario @throws Exception
     * @return
     */
    Token generarTokenUsuario(Usuario usuario) throws Exception;

}
