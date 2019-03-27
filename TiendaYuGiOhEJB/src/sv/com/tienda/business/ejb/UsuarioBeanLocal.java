package sv.com.tienda.business.ejb;

import sv.com.tienda.business.entity.Usuario;

import javax.ejb.Local;

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

}
