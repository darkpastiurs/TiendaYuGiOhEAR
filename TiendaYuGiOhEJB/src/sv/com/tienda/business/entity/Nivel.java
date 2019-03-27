package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "niveles", schema = "sis")
@SequenceGenerator(name = "Nivel_seq_id", sequenceName = "niveles_id_seq", schema = "sis", allocationSize = 1)
public class Nivel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Nivel_seq_id")
    @NotNull
    @Column(name = "id")
    private Integer id;
    @NotNull
    @Column(name = "nombre")
    private String nombre;
    @NotNull
    @Column(name = "estado", columnDefinition = "boolean default true")
    private boolean estado = true;

    @ManyToMany(mappedBy = "niveles", fetch = FetchType.LAZY, targetEntity = Usuario.class)
    private List<Usuario> usuarios;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Menu.class)
    @JoinTable(name = "niveles_menus", schema = "sis",
            joinColumns = @JoinColumn(name = "idnivel", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "idmenu", referencedColumnName = "id"))
    private List<Menu> menus;

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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nivel)) return false;
        Nivel nivel = (Nivel) o;
        return Objects.equals(id, nivel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Nivel{");
        sb.append("id=").append(id);
        sb.append(", nombre='").append(nombre).append('\'');
        sb.append(", estado=").append(estado);
        sb.append('}');
        return sb.toString();
    }
}
