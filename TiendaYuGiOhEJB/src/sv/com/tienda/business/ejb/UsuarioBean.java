package sv.com.tienda.business.ejb;

import sv.com.tienda.business.entity.Usuario;

import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.logging.Level;
import java.util.logging.Logger;

@Local(UsuarioBeanLocal.class)
@Stateless
@PermitAll
public class UsuarioBean implements UsuarioBeanLocal{
    private static final Logger LOG = Logger.getLogger(UsuarioBean.class.getName());
    @PersistenceContext(unitName = "TiendaPU")
    private EntityManager entityManager;

    public UsuarioBean() {
    }


    /**
     * Validacion del usuario a la hora de loguearse
     *
     * @param usernameOrEmail
     * @param password
     * @return Usuario
     */
    @Override
    public Usuario validarUsuarios(String usernameOrEmail, String password) {
        LOG.log(Level.INFO, "[UsuarioBean][validarUsuarios] -> {0}, {1}", new Object[]{usernameOrEmail, password});
        Usuario usuario = null;
        Query query;
        try {
            query = entityManager.createNamedQuery("Usuarios.findByValidacion");
            query.setParameter("nickOrEmail", usernameOrEmail);
            query.setParameter("pass", password);
            usuario = (Usuario) query.getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[UsuarioBean][validarUsuarios][Excepcion] -> ", e);
        }
        return usuario;
    }
}
