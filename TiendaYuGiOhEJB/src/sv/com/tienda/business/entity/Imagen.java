package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.StringJoiner;

@Entity
@Table(schema = "teo", name = "imagenes")
@SequenceGenerator(schema = "teo", sequenceName = "imagenes_id_seq", name = "Imagen_Id_Seq", allocationSize = 1)
public class Imagen implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Imagen_Id_Seq")
    @NotNull
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "archivo")
    private String archivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Imagen)) return false;

        Imagen imagen = (Imagen) o;

        return id.equals(imagen.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Imagen.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("archivo='" + archivo + "'")
                .toString();
    }
}
