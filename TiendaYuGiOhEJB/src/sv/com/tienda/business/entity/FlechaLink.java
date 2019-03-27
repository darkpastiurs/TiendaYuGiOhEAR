package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(schema = "cat", name = "flechas_links")
@SequenceGenerator(schema = "cat", name = "FlechaLink_seq_id", sequenceName = "flechas_links_id_seq")
@NamedQueries({
        @NamedQuery(name = "FlechasLink.findAll", query = "SELECT fl FROM FlechaLink fl"),
        @NamedQuery(name = "FlechasLink.findById", query = "SELECT fl FROM FlechaLink fl WHERE fl.id = :id")
})
public class FlechaLink implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FlechaLink_seq_id")
    @NotNull
    @Column(name = "id")
    private Short id;
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "estado", columnDefinition = "boolean default true")
    private boolean estado = true;

    @Transient
    private boolean remover = false;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
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

    public boolean isRemover() {
        return remover;
    }

    public void setRemover(boolean remover) {
        this.remover = remover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlechaLink)) return false;

        FlechaLink that = (FlechaLink) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FlechaLink{");
        sb.append("id=").append(id);
        sb.append(", direccion='").append(direccion);
        sb.append(", estado='").append(estado);
        sb.append('}');
        return sb.toString();
    }
}
