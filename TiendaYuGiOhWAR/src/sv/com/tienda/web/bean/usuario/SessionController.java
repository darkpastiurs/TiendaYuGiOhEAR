package sv.com.tienda.web.bean.usuario;

import org.apache.commons.collections.EnumerationUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import sv.com.tienda.business.entity.Menu;
import sv.com.tienda.business.entity.Nivel;
import sv.com.tienda.business.entity.Usuario;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

@Named("sessionWebBean")
@SessionScoped
public class SessionController implements Serializable {

    private static final long serialVersionUID = 1382165441560764139L;
    private static final Logger LOG = Logger.getLogger(SessionController.class.getName());

    private Usuario usuarioEnSession;

    private List<Nivel> nivelesUsuario;

    private MenuModel menuModel;

    @PostConstruct
    private void init() {
        LOG.log(INFO, "[SessionController][init]");
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
        DefaultMenuItem inicioItem = new DefaultMenuItem("Inicio");
        inicioItem.setIcon("home");
        inicioItem.setCommand("index");
        inicioItem.setAjax(false);
        menuModel.addElement(inicioItem);
        nivelesUsuario = usuarioEnSession != null ? usuarioEnSession.getNiveles() : null;
        if (nivelesUsuario != null && !nivelesUsuario.isEmpty()) {
            nivelesUsuario.stream().forEach((Nivel lvl) -> {
                Collections.sort(lvl.getMenus(), Comparator.comparing(Menu::getId));
                lvl.getMenus().stream().forEach((Menu menu) -> {
                    if (menu.getSubmenus() != null && !menu.getSubmenus().isEmpty()) {
                        Collections.sort(menu.getSubmenus(), Comparator.comparing(Menu::getId));
                        DefaultSubMenu menuSuperior = new DefaultSubMenu(menu.getNombre());
                        menuSuperior.setIcon(menu.getIcono());
                        menu.getSubmenus().stream().forEach((Menu submenu) -> {
                            if (submenu.isEstado()) {
                                DefaultMenuItem menuItem = new DefaultMenuItem(submenu.getNombre());
                                menuItem.setIcon(submenu.getIcono());
                                menuItem.setOutcome(submenu.getDireccion());
                                //menuItem.setCommand(submenu.getDireccion());
                                //menuItem.setAjax(false);
                                menuSuperior.addElement(menuItem);
                            }
                        });
                        menuModel.addElement(menuSuperior);
                    } else if (menu.getMenuSuperior() == null) {
                        if (menu.isEstado()) {
                            DefaultMenuItem menuItem = new DefaultMenuItem(menu.getNombre());
                            menuItem.setIcon(menu.getIcono());
                            if (menu.getNombre().contains("Cart")) {
                                menuItem.setOutcome(menu.getDireccion());
                            }
                            menuModel.addElement(menuItem);
                        }
                    }
                });
            });
        }
        menuModel.generateUniqueIds();
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
