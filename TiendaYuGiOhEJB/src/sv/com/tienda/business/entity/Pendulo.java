package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(schema = "teo", name = "pendulos")
public class Pendulo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    @NotNull
    @Lob
    @Column(name = "efecto")
    private String efecto;
    @NotNull
    @Min(0)
    @Max(13)
    @Column(name = "escalaizquierda")
    private Short izquierda;
    @NotNull
    @Min(0)
    @Max(13)
    @Column(name = "escaladerecha")
    private Short derecha;
    @Column(name = "estado", columnDefinition = "boolean default true")
    private boolean estado = true;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Monstruo.class)
    @MapsId("id")
    @JoinColumn(name = "idmonstruo")
    private Monstruo monstruo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEfecto() {
        return efecto;
    }

    public void setEfecto(String efecto) {
        this.efecto = efecto;
    }

    public Short getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(Short izquierda) {
        this.izquierda = izquierda;
    }

    public Short getDerecha() {
        return derecha;
    }

    public void setDerecha(Short derecha) {
        this.derecha = derecha;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Monstruo getMonstruo() {
        return monstruo;
    }

    public void setMonstruo(Monstruo monstruo) {
        this.monstruo = monstruo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pendulo)) return false;

        Pendulo pendulo = (Pendulo) o;

        return id.equals(pendulo.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pendulo{");
        sb.append("id=").append(id);
        sb.append(", monstruo=").append(monstruo.getCarta().getNombre());
        sb.append(", efecto='").append(efecto).append('\'');
        sb.append(", izquierda=").append(izquierda);
        sb.append(", derecha=").append(derecha);
        sb.append(", estado=").append(estado);
        sb.append('}');
        return sb.toString();
    }
}
