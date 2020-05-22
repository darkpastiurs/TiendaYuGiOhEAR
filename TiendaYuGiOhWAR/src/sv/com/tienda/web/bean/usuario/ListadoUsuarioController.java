package sv.com.tienda.web.bean.usuario;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import sv.com.tienda.business.ejb.UsuarioBeanLocal;
import sv.com.tienda.business.entity.Usuario;
import sv.com.tienda.business.utils.Constantes;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ListadoUsuarioController implements Serializable {

    private static final long serialVersionUID = -45153165L;
    private static final Logger LOG = Logger.getLogger(ListadoUsuarioController.class.getName());

    //<editor-fold desc="Componentes" defaultstate="collapsed">
    private List<Usuario> listaUsuarios;
    //</editor-fold>

    //<editor-fold desc="EJB" defaultstate="collapsed">
    @EJB(lookup = Constantes.JDNI_USUARIO_BEAN)
    private UsuarioBeanLocal usuarioBean;
    //</editor-fold>

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private Usuario usuarioSeleccionado;
    //</editor-fold>

    @PostConstruct
    private void init() {
        LOG.info("[ListadoUsuarioController][init]");
        try {
            listaUsuarios = usuarioBean.obtenerUsuarios();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "[ListadoUsuarioController][init][Exception] -> ", ex);
        }
    }

    //<editor-fold desc="Getters y Setters" defaultstate="collapsed">
    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }
    //</editor-fold>

    //<editor-fold desc="Acciones y/o Eventos" defaultstate="collapsed">
    public void resetearPassword() throws Exception {
        LOG.info("[ListadoUsuarioController][resetearPassword]");
        FacesMessage facesMessage = new FacesMessage();
        facesMessage.setSummary(Constantes.SUMMARY_TITLE_USUARIO_RESETEO_PASSWORD);
        try {
            if (usuarioSeleccionado != null) {
                String passwordTemporal = RandomStringUtils.random(6, true, true);
                String passwordEncrypTemporal = DigestUtils.md5Hex(passwordTemporal);
                usuarioBean.cambiarUsuarioPassword(usuarioSeleccionado, passwordEncrypTemporal);
                facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
                facesMessage.setDetail("La nueva contraseña ha sido cambiada y se ha enviado un correo al usuario");
            }
        } catch (Exception ex) {
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesMessage.setDetail("Ha ocurrido un error en el cambio de contraseña. Consulte al desarrollador.");
            LOG.log(Level.SEVERE, "[ListadoUsuarioController][resetearPassword][Exception] -> ", ex);
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }
    //</editor-fold>
}
