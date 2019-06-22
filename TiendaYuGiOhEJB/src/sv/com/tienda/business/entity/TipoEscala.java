package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(schema = "cat", name = "tipos_escalas")
@SequenceGenerator(schema = "cat", name = "TipoEscala_seq_id", sequenceName = "tipos_escalas_id_seq", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "TipoEscalas.findAll", query = "SELECT te FROM TipoEscala te"),
        @NamedQuery(name = "TipoEscalas.findById", query = "SELECT te FROM TipoEscala te WHERE te.id = :id")
})
public class TipoEscala implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TipoEscala_seq_id")
    @NotNull
    @Column(name = "id")
    private Short id;
    @NotNull
    @Column(name = "nombre")
    private String nombre;


    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipoEscala)) return false;

        TipoEscala that = (TipoEscala) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TipoEscala{");
        sb.append("id=").append(id);
        sb.append(", nombre='").append(nombre);
        sb.append('}');
        return sb.toString();
    }
}
