package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(schema = "sis", name = "menus")
@SequenceGenerator(name = "Menu_seq_id", sequenceName = "menus_id_seq")
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Menu_seq_id")
    @NotNull
    @Column(name = "id")
    private Integer id;
    @NotNull
    @Column(name = "nombre")
    private String nombre;
    @NotNull
    @Column(name = "icono")
    private String icono;
    @NotNull
    @Column(name = "direccion")
    private String direccion;
    @NotNull
    @Column(name = "estado", columnDefinition = "boolean default true")
    private boolean estado = true;

    @ManyToMany(mappedBy = "menus", fetch = FetchType.LAZY, targetEntity = Nivel.class)
    private List<Nivel> niveles;

    @OneToMany(mappedBy = "menuSuperior", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Menu.class)
    private List<Menu> submenus;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Menu.class)
    @JoinColumn(name = "idmenusuperior", referencedColumnName = "id")
    private Menu menuSuperior;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

    public List<Menu> getSubmenus() {
        return submenus;
    }

    public void setSubmenus(List<Menu> menusInferiores) {
        this.submenus = menusInferiores;
    }

    public Menu getMenuSuperior() {
        return menuSuperior;
    }

    public void setMenuSuperior(Menu menuSuperior) {
        this.menuSuperior = menuSuperior;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;

        Menu menu = (Menu) o;

        return id.equals(menu.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Menu{");
        sb.append("id=").append(id);
        sb.append(", nombre='").append(nombre).append('\'');
        sb.append(", icono='").append(icono).append('\'');
        sb.append(", direccion='").append(direccion).append('\'');
        sb.append(", estado=").append(estado);
        sb.append('}');
        return sb.toString();
    }
}
