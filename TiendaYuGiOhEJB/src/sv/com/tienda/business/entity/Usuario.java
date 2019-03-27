package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "usuarios", schema = "sis")
@NamedQueries({
        @NamedQuery(name = "Usuarios.findAllActivos", query = "SELECT us FROM Usuario us WHERE us.estado = true"),
        @NamedQuery(name = "Usuarios.findByValidacion", query = "SELECT us FROM Usuario us WHERE (us.nickname = :nickOrEmail OR us.correo = :nickOrEmail) AND us.contraseña = :pass")
})
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "username")
    private String nickname;
    @NotNull
    @Column(name = "contrasena")
    private String contraseña;
    @NotNull
    @Pattern(message = "El correo no cumple con el formato adecuado", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    @Column(name = "correo")
    private String correo;
    @NotNull
    @Column(name = "estado", columnDefinition = "boolean default true")
    private boolean estado = true;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Nivel.class)
    @JoinTable(name = "niveles_usuarios", schema = "sis",
            joinColumns = @JoinColumn(name = "idusuario", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "idnivel", referencedColumnName = "id"))
    private List<Nivel> niveles;

    @OneToMany(mappedBy = "usuarioDueño", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Tienda.class)
    private List<Tienda> tiendas;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

    public List<Tienda> getTiendas() {
        return tiendas;
    }

    public void setTiendas(List<Tienda> tiendas) {
        this.tiendas = tiendas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(nickname, usuario.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Usuario{");
        sb.append("nickname='").append(nickname).append('\'');
        sb.append(", contraseña='").append(contraseña).append('\'');
        sb.append(", correo='").append(correo).append('\'');
        sb.append(", estado=").append(estado);
        sb.append('}');
        return sb.toString();
    }
}
