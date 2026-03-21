package ivha.jpa.project2.DTO;

import java.security.Timestamp;

import ivha.jpa.project2.Model.Condition;

public class productRequestDTO {
    private Long id;
    private String nom;
    private String descripcio;
    private Integer stock;
    private Float preu;
    private Condition condition;
    private boolean active;

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getDescripcio() {
        return descripcio;
    }
    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Float getPreu() {
        return preu;
    }
    public void setPreu(Float preu) {
        this.preu = preu;
    }
    public Condition getCondition() {
        return condition;
    }
    public void setCondition(Condition condition) {
        this.condition = condition;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    
}

