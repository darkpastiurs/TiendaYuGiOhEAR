package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Table(schema = "teo", name = "tiendas")
@SequenceGenerator(name = "Tienda_seq_id", schema = "teo", sequenceName = "tiendas_id_seq")
public class Tienda implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Tienda_seq_id")
    @NotNull
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre")
    private String nombre;
    @NotNull
    @Pattern(message = "El correo no cumple con el formato adecuado", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    @Column(name = "correo")
    private String correo;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Usuario.class, optional = false)
    @JoinColumn(name = "idusuarioduenio", referencedColumnName = "username")
    private Usuario usuarioDueño;

    public Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NotNull String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(@NotNull String correo) {
        this.correo = correo;
    }

    public Usuario getUsuarioDueño() {
        return usuarioDueño;
    }

    public void setUsuarioDueño(Usuario usuarioDueño) {
        this.usuarioDueño = usuarioDueño;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tienda)) return false;

        Tienda tienda = (Tienda) o;

        return id.equals(tienda.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tienda{");
        sb.append("id=").append(id);
        sb.append(", nombre='").append(nombre).append('\'');
        sb.append(", correo='").append(correo).append('\'');
        sb.append(", usuarioDueño=").append(usuarioDueño);
        sb.append('}');
        return sb.toString();
    }
}
