package sv.com.tienda.web.bean.usuario;

import org.apache.commons.codec.digest.DigestUtils;
import sv.com.tienda.business.ejb.UsuarioBeanLocal;
import sv.com.tienda.business.entity.Token;
import sv.com.tienda.business.entity.Usuario;
import sv.com.tienda.business.utils.Constantes;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("loginBean")
@ViewScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = -8503079483789177638L;
    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());

    private String nickname;
    private String contraseña;
    private boolean recuerdame = false;

    @EJB(lookup = Constantes.JDNI_USUARIO_BEAN)
    private UsuarioBeanLocal usuarioBean;

    //@PostConstruct
    public void init() {
        LOG.log(Level.INFO, "[LoginController][init]");
        try {
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "[LoginController][init][Exception] -> ", ex);
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public boolean isRecuerdame() {
        return recuerdame;
    }

    public void setRecuerdame(boolean recuerdame) {
        this.recuerdame = recuerdame;
    }

    public String iniciarSesion() throws ServletException {
        LOG.log(Level.INFO, "[LoginController][iniciarSesion] -> {0}", nickname);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getExternalContext().getFlash().setKeepMessages(true);
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        String urlToRedirect = "login";
        try {
            String encryptPassword = DigestUtils.md5Hex(contraseña);
            Usuario usuario = usuarioBean.validarUsuarios(nickname, encryptPassword);
            if (usuario != null) {
                if (usuario.isEstado()) {
                    request.login(usuario.getNickname(), encryptPassword);
                    nickname = null;
                    encryptPassword = null;
                    contraseña = null;
                    if (request.getUserPrincipal() != null) {
                        Map<String, Object> variablesSesion = fc.getExternalContext().getSessionMap();
                        if (recuerdame) {
                            Token token = usuarioBean.generarTokenUsuario(usuario);
                            Cookie cookie = new Cookie("tid", token.getReferencia());
                            cookie.setMaxAge(Integer.parseInt(String.valueOf(ChronoUnit.SECONDS.between(LocalDateTime.now(), token.getFechaCaducidad()))));
                            cookie.setPath(request.getContextPath());
                            HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
                            response.addCookie(cookie);
                        }
                        variablesSesion.put("usuarioEnSesion", usuario);
                        urlToRedirect = "index";
                    }
                } else {
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de inicio de Sesion", "La cuenta de usuario ha sido desactivada"));
                }
            } else {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de inicio de sesion", "Usuario y/o Contraseña invalido"));
            }
        } catch (ServletException se) {
            LOG.log(Level.SEVERE, "[LoginController][iniciarSesion][ServletException] -> ", se);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "[LoginController][iniciarSesion][Exception] -> ", ex);
        }

        return urlToRedirect;
    }
}
