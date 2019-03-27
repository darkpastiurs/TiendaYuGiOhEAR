package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(schema = "teo", name = "inventarios")
@SequenceGenerator(name = "Inventario_seq_id", schema = "teo", sequenceName = "inventarios_id_seq", allocationSize = 1)
public class Inventario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Inventario_seq_id")
    @NotNull
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "existencia")
    private Integer existencia;
    @NotNull
    @Column(name = "precioventa")
    private BigDecimal precioVenta;
    @NotNull
    @Min(1)
    @Column(name = "edicion")
    private Integer edicion;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Carta.class, optional = false)
    @JoinColumn(name = "idcarta", referencedColumnName = "id")
    private Carta carta;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = EstadoCarta.class)
    @JoinColumn(name = "idestadocarta", referencedColumnName = "id")
    private EstadoCarta estadoCarta;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Tienda.class)
    @JoinColumn(name = "iditienda", referencedColumnName = "id")
    private Tienda tienda;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExistencia() {
        return existencia;
    }

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getEdicion() {
        return edicion;
    }

    public void setEdicion(Integer edicion) {
        this.edicion = edicion;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }

    public EstadoCarta getEstadoCarta() {
        return estadoCarta;
    }

    public void setEstadoCarta(EstadoCarta estadoCarta) {
        this.estadoCarta = estadoCarta;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventario)) return false;

        Inventario that = (Inventario) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Inventario{");
        sb.append("id=").append(id);
        sb.append(", existencia=").append(existencia);
        sb.append(", precioVenta=").append(precioVenta);
        sb.append(", edicion=").append(edicion);
        sb.append(", carta=").append(carta);
        sb.append(", estadoCarta=").append(estadoCarta);
        sb.append(", tienda=").append(tienda);
        sb.append('}');
        return sb.toString();
    }
}
