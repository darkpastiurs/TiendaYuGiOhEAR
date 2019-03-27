package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Entity
@Table(schema = "cat", name = "componentes_deck")
@SequenceGenerator(schema = "cat", name = "ComponenteDeck_seq_id", sequenceName = "componentes_deck_id_seq", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "ComponenteDeck.findAll", query = "SELECT cd FROM ComponenteDeck cd"),
        @NamedQuery(name = "ComponenteDeck.findAllByEstado", query = "SELECT cd FROM ComponenteDeck cd WHERE cd.estado = :estado"),
        @NamedQuery(name = "ComponenteDeck.findByIdActivo", query = "SELECT cd FROM ComponenteDeck cd WHERE cd.id = :id AND cd.estado = true")
})
public class ComponenteDeck implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ComponenteDeck_seq_id")
    @NotNull
    @Column(name = "id")
    private Integer id;
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "seccion")
    private String seccion;
    @NotNull
    @Column(name = "numero_minimo_carta")
    private Short numeroMinimo;
    @NotNull
    @Column(name = "numero_maximo_carta")
    private Short numeroMaximo;
    @Column(name = "estado", columnDefinition = "boolean default true")
    private boolean estado = true;

    @ManyToMany(mappedBy = "componentesDecks", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = CategoriaCarta.class)
    private List<CategoriaCarta> categoriasCarta;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public Short getNumeroMinimo() {
        return numeroMinimo;
    }

    public void setNumeroMinimo(Short numero_minimo) {
        this.numeroMinimo = numero_minimo;
    }

    public Short getNumeroMaximo() {
        return numeroMaximo;
    }

    public void setNumeroMaximo(Short numero_maximo) {
        this.numeroMaximo = numero_maximo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<CategoriaCarta> getCategoriasCarta() {
        return categoriasCarta;
    }

    public void setCategoriasCarta(List<CategoriaCarta> categoriasCarta) {
        this.categoriasCarta = categoriasCarta;
    }

    public void addCategoriaCarta(CategoriaCarta categoriaCarta) {
        if (categoriasCarta == null) {
            categoriasCarta = new ArrayList<CategoriaCarta>();
        }
        if (!this.getCategoriasCarta().contains(categoriaCarta)) {
            categoriaCarta.getComponentesDecks().add(this);
            categoriasCarta.add(categoriaCarta);
        }
    }

    public void removeCategoriaCarta(CategoriaCarta categoriaCarta) {
        for (ListIterator<CategoriaCarta> iterador = categoriasCarta.listIterator(); iterador.hasNext(); ) {
            CategoriaCarta categoriaCartaActual = iterador.next();
            if (categoriaCartaActual.equals(categoriaCarta)) {
                categoriaCartaActual.setComponentesDecks(null);
                iterador.remove();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponenteDeck)) return false;

        ComponenteDeck that = (ComponenteDeck) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ComponenteDeck{");
        sb.append("id=").append(id);
        sb.append(", seccion='").append(seccion);
        sb.append(", numero_minimo=").append(numeroMinimo);
        sb.append(", numero_maximo=").append(numeroMaximo);
        sb.append(", categoriasCarta=").append(categoriasCarta != null ? categoriasCarta.size() : 0);
        sb.append(", estado=").append(estado);
        sb.append('}');
        return sb.toString();
    }
}
