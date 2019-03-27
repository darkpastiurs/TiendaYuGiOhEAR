package sv.com.tienda.web.configuracion.filter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"*.xhtml"})
public class AuthenticationFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(AuthenticationFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        LOG.log(Level.INFO, "[AuthenticationFilter][doFilter]");
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            HttpSession session = request.getSession();
            String requestURL = request.getRequestURI();
            if(StringUtils.containsIgnoreCase(requestURL, "/login.xhtml") || StringUtils.containsIgnoreCase(requestURL, "/inicio.xhtml") || (session != null && session.getAttribute("usuarioEnSesion") != null) || StringUtils.containsIgnoreCase(requestURL, "/public/") || StringUtils.containsIgnoreCase(requestURL, "/javax.faces.resource/")){
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "[AuthenticationFilter][doFilter][IOException] -> ", e);
        } catch (ServletException e) {
            LOG.log(Level.SEVERE, "[AuthenticationFilter][doFilter][ServletException] -> ", e);
        } catch (ClassCastException e){
            LOG.log(Level.SEVERE, "[AuthenticationFilter][doFilter][ClassCastException] -> ", e);
        }
    }

    @Override
    public void destroy() {
    }
}
