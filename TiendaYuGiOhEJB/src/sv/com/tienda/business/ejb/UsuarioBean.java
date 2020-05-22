package sv.com.tienda.business.ejb;

import org.apache.commons.lang.RandomStringUtils;
import sv.com.tienda.business.entity.Token;
import sv.com.tienda.business.entity.Usuario;

import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Local(UsuarioBeanLocal.class)
@Stateless
@PermitAll
public class UsuarioBean implements UsuarioBeanLocal {
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

    /**
     * Metodo para obtener usuario por el nickname
     * @param username
     * @return
     * @throws NoResultException
     * @throws Exception
     */
    @Override
    public Usuario obtenerUsuarioPorNickname(String username) throws NoResultException, Exception {
        LOG.info("[UsuarioBean][obtenerUsuarioPorNickname]");
        try {
            return entityManager.find(Usuario.class, username);
        } catch (NoResultException nre) {
            throw new NoResultException("No se encontro al usuario " + username + " " + nre.getMessage());
        } catch (Exception ex) {
            throw new Exception("Error al obtener al usuario " + username + " " + ex.getMessage());
        }
    }

    /**
     * Metodo encargado de obtener el listado de usuarios registrados en a¿la web
     *
     * @return Listado de usuarios
     * @throws Exception
     */
    public List<Usuario> obtenerUsuarios() throws Exception {
        LOG.info("[UsuarioBean][obtenerUsuarios]");
        try {
            TypedQuery<Usuario> query = entityManager.createNamedQuery("Usuarios.findAll", Usuario.class);
            return query.getResultList();
        } catch (NoResultException nre) {
            throw new NoResultException("No se han encontrado datos");
        } catch (Exception ex) {
            throw new Exception("Ha ocurrido en la consulta de datos. Detalle: " + ex.getMessage());
        }
    }

    /**
     * Metodo para resetear la contraseña del usuario
     *
     * @param usuario
     * @param passwordTemporal
     * @throws Exception
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void cambiarUsuarioPassword(Usuario usuario, String passwordTemporal) throws Exception {
        LOG.info("[UsuarioBean][reseteoPassword]");
        try {
            Usuario us = entityManager.find(Usuario.class, usuario.getNickname());
            us.setContraseña(passwordTemporal);
            entityManager.merge(us);
            entityManager.flush();
        } catch (Exception ex) {
            //LOG.log(Level.SEVERE,"[UsuarioBean][reseteoPassword][Exception] -> ", ex);
            throw new Exception("Ha ocurrido un error al resetear la contraseña " + ex.getMessage());
        }
    }

    /**
     * Metodo que obtiene el token de la cookie del navegador
     *
     * @param referencia
     * @return
     * @throws Exception
     */
    @Override
    public Token obtenerTokenPorReferencia(String referencia) throws Exception {
        LOG.info("[UsuarioBean][obtenerTokenPorReferencia]");
        try {
            Query query = entityManager
                    .createNamedQuery("Token.findByReferencia")
                    .setParameter("referencia", referencia);
            return (Token) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new NoResultException("No se encontro dicho token activo " + ex.getMessage());
        }
    }

    /**
     * Metodo encargado de actualizar el token de la cookie del navegador
     *
     * @param usuario
     * @return
     * @throws Exception
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Token generarTokenUsuario(Usuario usuario) throws Exception {
        LOG.info("[UsuarioBean][guardarToken]");
        try {
            Token tokenGuardar = new Token();
            tokenGuardar.setReferencia(RandomStringUtils.randomAlphanumeric(32));
            tokenGuardar.setFechaCaducidad(LocalDateTime.now().plusDays(30L));
            tokenGuardar.setUsuario(entityManager.find(Usuario.class, usuario.getNickname()));
            entityManager.persist(tokenGuardar);
            entityManager.flush();
            entityManager.refresh(tokenGuardar);
            return tokenGuardar;
        } catch (Exception ex) {
            throw new Exception("Error al guardar token para el usuario " + usuario.getNickname() + " " + ex.getMessage());
        }
    }


}
