package sv.com.tienda.web.bean.usuario;

import org.apache.commons.collections.EnumerationUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.menu.*;
import sv.com.tienda.business.ejb.UsuarioBeanLocal;
import sv.com.tienda.business.entity.Menu;
import sv.com.tienda.business.entity.Nivel;
import sv.com.tienda.business.entity.Token;
import sv.com.tienda.business.entity.Usuario;
import sv.com.tienda.business.utils.Constantes;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

@Named("sessionWebBean")
@SessionScoped
public class SessionController implements Serializable {

    private static final long serialVersionUID = 1382165441560764139L;
    private static final Logger LOG = Logger.getLogger(SessionController.class.getName());

    @EJB(lookup = Constantes.JDNI_USUARIO_BEAN)
    private UsuarioBeanLocal usuarioBean;

    private Usuario usuarioEnSession;

    private List<Nivel> nivelesUsuario;

    private MenuModel menuModel;

    @PostConstruct
    private void init() {
        LOG.log(Level.INFO, "[SessionController][init]");
        if (usuarioEnSession == null) {
            loadSession();
        }
    }

    public void loadSession() {
        LOG.info("[SessionController][loadSession]");
        try {
//            if (usuarioEnSession == null) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) ec.getRequest();
            Usuario usuarioLogged;
            if (request.getCookies() != null && request.getUserPrincipal() == null) {
                Cookie cookie = Arrays.stream(request.getCookies())
                        .filter(cookie1 -> StringUtils.equals("tid", cookie1.getName()))
                        .findFirst()
                        .orElse(null);
                if (cookie != null) {
                    Token token = usuarioBean.obtenerTokenPorReferencia(cookie.getValue());
                    if (token != null && token.getFechaCaducidad().isAfter(LocalDateTime.now())) {
                        usuarioLogged = token.getUsuario();
                        request.login(usuarioLogged.getNickname(), usuarioLogged.getContraseña());
                        Principal logged = request.getUserPrincipal();
                        if (logged != null) {
                            ec.getSessionMap().put("usuarioEnSesion", usuarioLogged);
                        }
                    }
                }
            }
//            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "[SessionController][loadSession][Exception] -> ", ex);
        }
    }

    public Usuario getUsuarioEnSession() {
        if (usuarioEnSession == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession sesionActiva = (HttpSession) fc.getExternalContext().getSession(false);
            if (EnumerationUtils.toList(sesionActiva.getAttributeNames()).contains("usuarioEnSesion")) {
                usuarioEnSession = (Usuario) sesionActiva.getAttribute("usuarioEnSesion");
            }
        }
        return usuarioEnSession;
    }

    public void setUsuarioEnSession(Usuario usuarioEnSession) {
        this.usuarioEnSession = usuarioEnSession;
    }

    public List<Nivel> getNivelesUsuario() {
        if (nivelesUsuario == null) {
            nivelesUsuario = usuarioEnSession.getNiveles();
        }
        return nivelesUsuario;
    }

    public void setNivelesUsuario(List<Nivel> nivelesUsuario) {
        this.nivelesUsuario = nivelesUsuario;
    }

    private void construirMenu() {
        LOG.log(Level.INFO, "[SessionController][construirMenu]");
        menuModel = new DefaultMenuModel();
        DefaultMenuItem inicioItem = DefaultMenuItem.builder()
                .value("Inicio")
                .icon("home")
                .command("index")
                .ajax(false)
                .build();
        menuModel.getElements().add(inicioItem);
        nivelesUsuario = usuarioEnSession != null ? usuarioEnSession.getNiveles() : null;
        if (nivelesUsuario != null && !nivelesUsuario.isEmpty()) {
            nivelesUsuario.stream().forEach((Nivel lvl) -> {
                Collections.sort(lvl.getMenus(), Comparator.comparing(Menu::getId));
                lvl.getMenus().stream().forEach((Menu menu) -> {
                    try {
                        menuModel.getElements().add(construirMenuItem(menu));
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "[SessionController][construirMenu][Menu " + menu.getNombre() + "][Exception] -> ", e);
                    }
                });
            });
        }
        menuModel.generateUniqueIds();
    }

    public MenuElement construirMenuItem(Menu menu) throws Exception {
        LOG.log(INFO, "[SessionController][construirMenuItem] -> {0}", usuarioEnSession);
        try {
            if (menu.getSubmenus() != null && !menu.getSubmenus().isEmpty()) {
                DefaultSubMenu subMenuItem = DefaultSubMenu.builder()
                        .label(menu.getNombre())
                        .icon(menu.getIcono())
                        .build();
                menu.getSubmenus().forEach(submenu -> {
                    try {
                        subMenuItem.getElements().add(construirMenuItem(submenu));
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "[SessionController][construirMenuItem][Menu " + submenu.getNombre() + "][Exception]", e);
                    }
                });
                return subMenuItem;
            } else {
                DefaultMenuItem menuItem = DefaultMenuItem.builder()
                        .value(menu.getNombre())
                        .icon(menu.getIcono())
                        .ajax(false)
                        .build();
                ConfigurableNavigationHandler configNav = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
                Map<String, Set<NavigationCase>> navCases = configNav.getNavigationCases();

                if (navCases.entrySet().stream().flatMap(entry -> entry.getValue().stream()).anyMatch(navCase -> StringUtils.equals(navCase.getFromOutcome(), menu.getDireccion()))) {
                    menuItem.setOutcome(menu.getDireccion());
                }
                return menuItem;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[SessionController][construirMenuItem][Excepcion] -> ", e);
            throw e;
        }
    }

    public MenuModel getMenuModel() {
        if (menuModel == null || menuModel.getElements().size() < 2) {
            construirMenu();
        }
        return menuModel;
    }

    public String cerrarSession() {
        LOG.log(INFO, "[SessionController][cerrarSession] -> {0}", usuarioEnSession);
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.logout();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        } catch (ServletException e) {
            LOG.log(Level.SEVERE, "[SessionController][cerrarSession][Excepcion] -> ", e);
        }
        return "index";
    }

    public boolean isNivel(String nivelNombre) {
        if (nivelesUsuario != null && !nivelesUsuario.isEmpty()) {
            return nivelesUsuario.stream().filter((Nivel lvl) -> StringUtils.equals(lvl.getNombre(), nivelNombre)).count() > 0;
        }
        return false;
    }
}
