package sv.com.tienda.business.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "teo", name = "monstruos")
public class Monstruo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    @NotNull
    @Min(0)
    @Column(name = "ataque")
    private Integer ataque;
    @Min(0)
    @Column(name = "defensa")
    private Integer defensa;
    @Column(name = "escala")
    private Short escala;
    @Lob
    @Column(name = "materialinvocacion")
    private String materialInvocacion;
    @Column(name = "estado", columnDefinition = "boolean default true")
    private boolean estado = true;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Carta.class)
    @MapsId("id")
    @JoinColumn(name = "idcarta")
    private Carta carta;

    @OneToOne(mappedBy = "monstruo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Pendulo.class, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Pendulo penduloAtributos;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = AtributoMonstruo.class)
    @JoinColumn(name = "idatributo", referencedColumnName = "id")
    private AtributoMonstruo atributo;
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = TipoMounstro.class)
    @JoinTable(schema = "teo", name = "monstruos_tipos",
            joinColumns = @JoinColumn(name = "idmonstruo", referencedColumnName = "idcarta"),
            inverseJoinColumns = @JoinColumn(name = "idtipomonstruo", referencedColumnName = "id"))
    private List<TipoMounstro> tipos;
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = FlechaLink.class)
    @JoinTable(name = "links", schema = "teo",
            joinColumns = @JoinColumn(name = "idmonstruo", referencedColumnName = "idcarta"),
            inverseJoinColumns = @JoinColumn(name = "idflecha", referencedColumnName = "id"))
    private List<FlechaLink> flechasLinks;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TipoEscala.class)
    @JoinColumn(name = "idtipoescala", referencedColumnName = "id")
    private TipoEscala tipoEscala;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAtaque() {
        return ataque;
    }

    public void setAtaque(Integer ataque) {
        this.ataque = ataque;
    }

    public Integer getDefensa() {
        return defensa;
    }

    public void setDefensa(Integer defensa) {
        this.defensa = defensa;
    }

    public Short getEscala() {
        return escala;
    }

    public void setEscala(Short escala) {
        this.escala = escala;
    }

    public String getMaterialInvocacion() {
        return materialInvocacion;
    }

    public void setMaterialInvocacion(String materialInvocacion) {
        this.materialInvocacion = materialInvocacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public AtributoMonstruo getAtributo() {
        return atributo;
    }

    public void setAtributo(AtributoMonstruo atributo) {
        this.atributo = atributo;
    }

    public List<TipoMounstro> getTipos() {
        return tipos;
    }

    public void setTipos(List<TipoMounstro> tipo) {
        this.tipos = tipo;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }

    public Pendulo getPenduloAtributos() {
        return penduloAtributos;
    }

    public void setPenduloAtributos(Pendulo penduloAtributos) {
        this.penduloAtributos = penduloAtributos;
    }

    public List<FlechaLink> getFlechasLinks() {
        return flechasLinks;
    }

    public void setFlechasLinks(List<FlechaLink> flechasLinks) {
        this.flechasLinks = flechasLinks;
    }

    public TipoEscala getTipoEscala() {
        return tipoEscala;
    }

    public void setTipoEscala(TipoEscala tipoEscala) {
        this.tipoEscala = tipoEscala;
    }

    public void addTipoMonstruo(TipoMounstro tipoMounstro){
        if(tipos == null){
            tipos = new ArrayList<>();
        }
        if(!tipos.contains(tipoMounstro)){
            tipoMounstro.getMonstruos().add(this);
            tipos.add(tipoMounstro);
        }
    }

    public void removeTipoMonstruo(TipoMounstro tipoMounstro){
        tipoMounstro.getMonstruos().removeIf(m -> m.equals(this));
        tipos.removeIf(tm -> tm.equals(tipoMounstro));
    }

    public void addFlechaLink(FlechaLink flechaLink){
        if(flechasLinks == null){
            flechasLinks = new ArrayList<>();
        }
        if(!flechasLinks.contains(flechaLink)){
            flechasLinks.add(flechaLink);
        }
    }

    public void removerFlechaLink(FlechaLink flechaLink){

        flechasLinks.removeIf(fl -> fl.equals(flechaLink));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Monstruo)) return false;

        Monstruo monstruo = (Monstruo) o;

        return id.equals(monstruo.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Monstruo{");
        sb.append("id=").append(id);
        sb.append(", carta=").append(carta);
        sb.append(", atributo=").append(atributo);
        sb.append(", ataque=").append(ataque);
        sb.append(", defensa=").append(defensa);
        sb.append(", tipoescala=").append(tipoEscala);
        sb.append(", escala=").append(escala);
        sb.append(", materialInvocacion='").append(materialInvocacion);
        sb.append(", estado=").append(estado);
        sb.append('}');
        return sb.toString();
    }
}
