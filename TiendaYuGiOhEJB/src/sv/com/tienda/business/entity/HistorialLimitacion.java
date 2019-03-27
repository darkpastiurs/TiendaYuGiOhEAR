package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(schema = "teo", name = "historial_limitacion")
@SequenceGenerator(schema = "teo", name = "HistorialLimitacion_seq_id", sequenceName = "historial_limitacion_id_seq", allocationSize = 1)
public class HistorialLimitacion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HistorialLimitacion_seq_id")
    @NotNull
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Carta.class)
    @JoinColumn(name = "idcarta", referencedColumnName = "id")
    private Carta carta;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = LimitacionCarta.class)
    @JoinColumn(name = "idlimitacion", referencedColumnName = "id")
    private LimitacionCarta limitacionCarta;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Usuario.class)
    @JoinColumn(name = "idusuario", referencedColumnName = "username")
    private Usuario usuarioModifico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }

    public LimitacionCarta getLimitacionCarta() {
        return limitacionCarta;
    }

    public void setLimitacionCarta(LimitacionCarta limitacionCarta) {
        this.limitacionCarta = limitacionCarta;
    }

    public Usuario getUsuarioModifico() {
        return usuarioModifico;
    }

    public void setUsuarioModifico(Usuario usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistorialLimitacion)) return false;

        HistorialLimitacion that = (HistorialLimitacion) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HistorialLimitacion{");
        sb.append("id=").append(id);
        sb.append(", fechaModificacion=").append(fechaModificacion);
        sb.append(", carta=").append(carta.getNombre());
        sb.append(", limitacionCarta=").append(limitacionCarta.getNombre());
        sb.append(", usuarioModifico=").append(usuarioModifico.getNickname());
        sb.append('}');
        return sb.toString();
    }
}
