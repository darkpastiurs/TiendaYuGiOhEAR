package sv.com.tienda.web.configuracion;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.Redirect;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

import javax.servlet.ServletContext;

@RewriteConfiguration
public class ReWriteConfiguration extends HttpConfigurationProvider {

    /**
     * Return the additional configuration.
     *
     * @param context
     */
    @Override
    public Configuration getConfiguration(ServletContext context) {
        ConfigurationBuilder config = ConfigurationBuilder.begin();
        //Configuracion de slash final de las direcciones
        config.addRule()
                .when(Path.matches("/{path}/"))
                .perform(Redirect.permanent(context.getContextPath() + "/{path}"))
                .where("path").matches("[\\/]$");
        //Configuracion de paginas publicos
        config.addRule(Join.path("/inicio").to("/public/index.xhtml"));
        config.addRule(Join.path("/login").to("/public/login.xhtml"));
        //Configuracion de paginas de error
        config.addRule(Join.path("/denegado").to("/WEB-INF/errorpages/error403.xhtml"));
        config.addRule(Join.path("/noencontrado").to("/WEB-INF/errorpages/error404.xhtml"));
        //<editor-fold defaultstate="collapsed" desc="Configuracion para categoria de cartas">
        config.addRule(
                Join.path("/administracion/categorias")
                        .to("/administracion/cartas-gestion/categorias/listado.xhtml")
        );
        config.addRule(
                Join.path("/administracion/categoria/nueva")
                        .to("/administracion/cartas-gestion/categorias/gestion.xhtml")
        );
        config.addRule(
                Join.path("/administracion/categoria/editar/{categoriaSelected}")
                        .to("/administracion/cartas-gestion/categorias/gestion.xhtml")
        )
                .where("categoriaSelected");
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Configuracion para atributos de monstruos">
        config.addRule(
                Join.path("/administracion/monstruos/atributos")
                        .to("/administracion/cartas-gestion/monstruos/atributos/listado.xhtml")
        );
        config.addRule(
                Join.path("/administracion/monstruos/atributo/nuevo")
                        .to("/administracion/cartas-gestion/monstruos/atributos/gestion.xhtml")
        );
        config.addRule(
                Join.path("/administracion/monstruos/atributo/editar/{atributoSelected}")
                        .to("/administracion/cartas-gestion/monstruos/atributos/gestion.xhtml")
        ).where("atributoSelected");
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Configuracion para tipos de monstruos">
        config.addRule(
                Join.path("/administracion/monstruos/tipos")
                        .to("/administracion/cartas-gestion/monstruos/tipos/listado.xhtml")
        );
        config.addRule(
                Join.path("/administracion/monstruos/tipo/nuevo")
                        .to("/administracion/cartas-gestion/monstruos/tipos/gestion.xhtml")
        );
        config.addRule(
                Join.path("/administracion/monstruos/tipo/editar/{tipo}")
                        .to("/administracion/cartas-gestion/monstruos/tipos/gestion.xhtml")
        );
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Configuracion para estructura del deck">
        config.addRule(
                Join.path("/administracion/estructura-deck")
                        .to("/administracion/componentedeck/listado.xhtml")
        );
        config.addRule(
                Join.path("/administracion/estructura-deck/nueva-seccion")
                        .to("/administracion/componentedeck/gestion.xhtml")
        );
        config.addRule(
                Join.path("/administracion/estructura-deck/editar-seccion/{componenteDeck}")
                        .to("/administracion/componentedeck/gestion.xhtml")
        );
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Configuracion de cartas">
        config.addRule(
                Join.path("/administracion/cartas")
                        .to("/administracion/cartas-gestion/listado.xhtml"));
        config.addRule(
                Join.path("/administracion/carta/nueva")
                        .to("/administracion/cartas-gestion/gestion.xhtml"));
        config.addRule(
                Join.path("/administracion/carta/editar/{cartaNombre}")
                        .to("/administracion/cartas-gestion/gestion.xhtml"));
        //</editor-fold>
        return config;
    }

    @Override
    public int priority() {
        return 10;
    }
}
