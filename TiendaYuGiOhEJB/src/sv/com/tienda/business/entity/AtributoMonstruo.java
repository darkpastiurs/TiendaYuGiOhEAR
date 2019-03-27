package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(schema = "cat", name = "atributos_mounstros")
@SequenceGenerator(name = "AtributoMonstruo_seq_id", sequenceName = "atributos_mounstros_id_seq", schema = "cat", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "AtributoMonstruo.findAll", query = "SELECT am FROM AtributoMonstruo am ORDER BY am.nombre"),
        @NamedQuery(name = "AtributoMonstruo.findByEstado", query="SELECT am FROM AtributoMonstruo am WHERE am.estado = :estado ORDER BY am.nombre"),
        @NamedQuery(name = "AtributoMonstruo.findByIdActivo", query = "SELECT am FROM AtributoMonstruo am WHERE am.id = :id AND am.estado = true")
})
public class AtributoMonstruo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AtributoMonstruo_seq_id")
    @NotNull
    @Column(name = "id")
    private Integer id;
    @NotNull
    @Column(name = "nombre")
    @Size(min = 1, max = 50)
    private String nombre;
    @NotNull
    @Column(name = "estado", columnDefinition = "boolean default true")
    private boolean estado = true;

    @OneToMany(mappedBy = "atributo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Monstruo.class)
    private List<Monstruo> monstruos;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtributoMonstruo)) return false;

        AtributoMonstruo that = (AtributoMonstruo) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AtributoMonstruo{");
        sb.append("id=").append(id);
        sb.append(", nombre='").append(nombre);
        sb.append("', estado='").append(estado);
        sb.append("', mounstros='").append(monstruos == null ? 0 : monstruos.size());
        sb.append('}');
        return sb.toString();
    }
}
