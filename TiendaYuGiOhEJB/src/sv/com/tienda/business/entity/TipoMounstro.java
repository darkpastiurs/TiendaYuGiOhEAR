package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(schema = "cat", name = "tipos_monstruos")
@SequenceGenerator(schema = "cat", name = "TipoMonstruo_seq_id", sequenceName = "tipos_monstruos_id_seq", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "TipoMonstruo.findAll", query = "SELECT tm FROM TipoMounstro tm ORDER BY tm.nombre "),
        @NamedQuery(name = "TipoMonstruo.findByEstado", query = "SELECT tm FROM TipoMounstro tm WHERE tm.estado = :estado"),
        @NamedQuery(name = "TipoMonstruo.findById", query = "SELECT tm FROM TipoMounstro tm WHERE tm.id = :id AND tm.estado = true")
})
public class TipoMounstro implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TipoMonstruo_seq_id")
    @NotNull
    @Column(name = "id")
    private Integer id;
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "estado", columnDefinition = "boolean default true")
    private boolean estado = true;

    @ManyToMany(mappedBy = "tipos", fetch = FetchType.LAZY, targetEntity = Monstruo.class)
    private List<Monstruo> monstruos;

    @Transient
    private boolean remover = false;

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

    public List<Monstruo> getMonstruos() {
        return monstruos;
    }

    public void setMonstruos(List<Monstruo> monstruos) {
        this.monstruos = monstruos;
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
        if (!(o instanceof TipoMounstro)) return false;

        TipoMounstro that = (TipoMounstro) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TipoMounstro{");
        sb.append("id=").append(id);
        sb.append(", nombre='").append(nombre);
        sb.append("', monstruos='").append(monstruos != null ? monstruos.size() : 0);
        sb.append("', estado='").append(estado);
        sb.append("'}");
        return sb.toString();
    }
}
