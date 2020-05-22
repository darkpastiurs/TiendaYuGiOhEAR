package sv.com.tienda.business.entity;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(schema = "sis", name = "tokens")
@SequenceGenerator(name = "Token_seq_id", schema = "sis", sequenceName = "tokens_id_seq", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "Token.findByReferencia", query = "SELECT t FROM Token t WHERE t.referencia = :referencia")
})
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Token_seq_id")
    @Column(name = "id")
    private Long id;
    @Column(name = "referencia")
    private String referencia;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fechacaducidad")
    private LocalDateTime fechaCaducidad;
    @Column(name = "activo", columnDefinition = "boolean default true")
    private boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Usuario.class)
    @JoinColumn(name = "idusuario", referencedColumnName = "username")
    private Usuario usuario;

    public Token() {
    }

    public Token(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public LocalDateTime getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDateTime fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (!Objects.equals(id, token.id)) return false;
        return Objects.equals(usuario, token.usuario);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (usuario != null ? usuario.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Token.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("referencia='" + referencia + "'")
                .add("fechaCaducidad=" + fechaCaducidad)
                .add("usuario=" + usuario)
                .toString();
    }
}
