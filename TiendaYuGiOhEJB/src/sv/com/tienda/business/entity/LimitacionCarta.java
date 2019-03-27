package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(schema = "cat", name = "limitaciones_cartas")
@SequenceGenerator(schema = "cat", name = "LimitacionCarta_seq_id", sequenceName = "limitaciones_cartas_id_seq", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "LimitacionesCarta.findAll", query="SELECT lc FROM LimitacionCarta lc"),
        @NamedQuery(name = "LimitacionesCarta.findById", query = "SELECT lc FROM LimitacionCarta lc WHERE lc.id = :id")
})
public class LimitacionCarta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LimitacionCarta_seq_id")
    @NotNull
    @Column(name = "id")
    private Integer id;
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @NotNull
    @Min(1)
    @Max(3)
    @Column(name = "cantidad")
    private Short cantidad;
    @Column(name = "estado", columnDefinition = "boolean default true")
    private boolean estado = true;

    @OneToMany(mappedBy = "limite", fetch = FetchType.LAZY, targetEntity = Carta.class)
    private List<Carta> cartas;

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

    public Short getCantidad() {
        return cantidad;
    }

    public void setCantidad(Short cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LimitacionCarta)) return false;

        LimitacionCarta that = (LimitacionCarta) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LimitacionCarta{");
        sb.append("id=").append(id);
        sb.append(", nombre='").append(nombre);
        sb.append(", cantidad=").append(cantidad);
        sb.append(", estado=").append(estado);
        sb.append('}');
        return sb.toString();
    }
}
