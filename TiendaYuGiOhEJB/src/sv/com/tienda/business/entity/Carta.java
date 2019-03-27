package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "teo", name = "cartas")
@NamedQueries({
        @NamedQuery(name = "Cartas.findAll", query = "SELECT c FROM Carta c"),
        @NamedQuery(name = "Cartas.findAllEstado", query = "SELECT c FROM Carta c WHERE c.estado = :estado ORDER BY c.nombre"),
        @NamedQuery(name = "Cartas.findByIdActivo", query = "SELECT c FROM Carta c WHERE c.id = :id AND c.estado = true")
})
@SequenceGenerator(schema = "teo", name = "Carta_seq_id", sequenceName = "cartas_id_seq", allocationSize = 1)
public class Carta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Carta_seq_id")
    @Column(name = "id")
    private Long id;
    @NotNull
    @Size(min = 1, max = 200)
    private String nombre;
    @NotNull
    @Lob
    @Column(name = "efecto")
    private String efecto;
    @NotNull
    @Column(name = "estado", columnDefinition = "boolean default true")
    private boolean estado = true;

    @OneToOne(mappedBy = "carta", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Monstruo.class)
    @PrimaryKeyJoinColumn
    private Monstruo monstruo;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Imagen.class)
    @JoinColumn(name = "idimagen", referencedColumnName = "id")
    private Imagen imagen;

    @OneToMany(mappedBy = "carta", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = HistorialLimitacion.class)
    private List<HistorialLimitacion> historialLimitaciones;
    @OneToMany(mappedBy = "carta", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Inventario.class)
    private List<Inventario> inventarios;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = CategoriaCarta.class)
    @JoinTable(name = "cartas_categorias", schema = "teo",
            joinColumns = @JoinColumn(name = "idcarta", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "idcategoria", referencedColumnName = "id")
    )
    private List<CategoriaCarta> categorias;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = LimitacionCarta.class, optional = false)
    @JoinColumn(name = "idlimitacion", referencedColumnName = "id")
    private LimitacionCarta limite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEfecto() {
        return efecto;
    }

    public void setEfecto(String efecto) {
        this.efecto = efecto;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<CategoriaCarta> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaCarta> categoria) {
        this.categorias = categoria;
    }

    public LimitacionCarta getLimite() {
        return limite;
    }

    public void setLimite(LimitacionCarta limite) {
        this.limite = limite;
    }

    public List<HistorialLimitacion> getHistorialLimitaciones() {
        return historialLimitaciones;
    }

    public void setHistorialLimitaciones(List<HistorialLimitacion> historialLimitaciones) {
        this.historialLimitaciones = historialLimitaciones;
    }

    public List<Inventario> getInventarios() {
        return inventarios;
    }

    public void setInventarios(List<Inventario> inventarios) {
        this.inventarios = inventarios;
    }

    public Monstruo getMonstruo() {
        return monstruo;
    }

    public void setMonstruo(Monstruo monstruo) {
        this.monstruo = monstruo;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }

    public void addCategoria(CategoriaCarta categoria){
        if(categorias == null)
            categorias = new ArrayList<>();

        if(!categorias.contains(categoria)){
            categoria.getCartas().add(this);
            categorias.add(categoria);
        }
    }

    public void removeCategoria(CategoriaCarta categoria){
        if(categorias != null && !categorias.isEmpty()) {
            categoria.getCartas().removeIf(c -> c.equals(this));
            categorias.removeIf(cc -> cc.equals(categoria));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carta)) return false;

        Carta carta = (Carta) o;

        return id.equals(carta.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Carta{");
        sb.append("id=").append(id);
        sb.append(", nombre='").append(nombre);
        sb.append(", efecto='").append(efecto);
        sb.append(", estado=").append(estado);
        sb.append(", categoria=").append(categorias);
        sb.append(", limite=").append(limite);
        sb.append('}');
        return sb.toString();
    }
}
