package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(schema = "cat", name = "estados_cartas")
@NamedQueries({
        @NamedQuery(name = "EstadosCartas.findAll", query = "SELECT ec FROM EstadoCarta ec"),
        @NamedQuery(name = "EstadosCartas.findById", query = "SELECT ec FROM EstadoCarta ec WHERE ec.id = :id")
})
@SequenceGenerator(name = "EstadoCarta_seq_id", schema = "cat", sequenceName = "estados_cartas_id_seq", allocationSize = 1)
public class EstadoCarta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EstadoCarta_seq_id")
    @Column(name = "id")
    private Short id;
    @NotNull
    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "estadoCarta", fetch = FetchType.LAZY, targetEntity = Inventario.class)
    private List<Inventario> inventarios;

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

    public List<Inventario> getInventarios() {
        return inventarios;
    }

    public void setInventarios(List<Inventario> inventarios) {
        this.inventarios = inventarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstadoCarta)) return false;

        EstadoCarta that = (EstadoCarta) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EstadoCarta{");
        sb.append("id=").append(id);
        sb.append(", nombre='").append(nombre).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
