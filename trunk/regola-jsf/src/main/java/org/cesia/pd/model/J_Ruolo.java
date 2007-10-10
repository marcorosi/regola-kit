package org.cesia.pd.model;
// Generated 16-feb-2007 12.58.55 by Hibernate Tools 3.2.0.beta8


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DiRuolo generated by hbm2java
 */
@Entity
@Table(name="DI_RUOLO"
    , uniqueConstraints = {  }
)
public class J_Ruolo  implements java.io.Serializable {

    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = -7747203814388794928L;
	private Integer ruolo;
     private String descrizione;
     private String nome;
     private Date dataora;
     private BigDecimal versione;
     //private Set<RuoliUtente> diUtentiruolis = new HashSet<RuoliUtente>(0);

     // Constructors

    /** default constructor */
    public J_Ruolo() {
    }

	/** minimal constructor */
    public J_Ruolo(Integer ruolo) {
        this.ruolo = ruolo;
    }
    /** full constructor */
    public J_Ruolo(Integer ruolo, String descrizione, String nome, Date dataora, BigDecimal versione) {//, Set<RuoliUtente> diUtentiruolis) {
       this.ruolo = ruolo;
       this.descrizione = descrizione;
       this.nome = nome;
       this.dataora = dataora;
       this.versione = versione;
       //this.diUtentiruolis = diUtentiruolis;
    }
   
    // Property accessors
     @Id
    
    @Column(name="RUOLO", unique=true, nullable=false, insertable=true, updatable=true, precision=22, scale=0)
    public Integer getRuolo() {
        return this.ruolo;
    }
    
    public void setRuolo(Integer ruolo) {
        this.ruolo = ruolo;
    }
    
    @Column(name="DESCRIZIONE", unique=false, nullable=true, insertable=true, updatable=true, length=100)
    public String getDescrizione() {
        return this.descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    @Column(name="NOME", unique=false, nullable=true, insertable=true, updatable=true, length=15)
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    @Column(name="DATAORA", unique=false, nullable=true, insertable=true, updatable=true, length=11)
    public Date getDataora() {
        return this.dataora;
    }
    
    public void setDataora(Date dataora) {
        this.dataora = dataora;
    }
    
    @Column(name="VERSIONE", unique=false, nullable=true, insertable=true, updatable=true, precision=22, scale=0)
    public BigDecimal getVersione() {
        return this.versione;
    }
    
    public void setVersione(BigDecimal versione) {
        this.versione = versione;
    }
    
    /*
    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="Ruolo")
    public Set<RuoliUtente> getDiUtentiruolis() {
        return this.diUtentiruolis;
    }
    
    public void setDiUtentiruolis(Set<RuoliUtente> diUtentiruolis) {
        this.diUtentiruolis = diUtentiruolis;
    }
     */
}


