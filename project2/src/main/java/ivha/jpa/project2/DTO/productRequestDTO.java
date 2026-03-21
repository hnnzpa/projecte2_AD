package ivha.jpa.project2.DTO;

import ivha.jpa.project2.Model.Condition;

public class productRequestDTO {
    private String nom;
    private String descripcio;
    private Integer stock;
    private Float price;
    private Float rating;
    private Condition condition;
    private boolean active;

    public productRequestDTO(String nom, String descripcio, Integer stock, Float price, Float rating,
            Condition condition, boolean active) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.stock = stock;
        this.price = price;
        this.rating = rating;
        this.condition = condition;
        this.active = active;
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
    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    public Float getRating() {
        return rating;
    }
    public void setRating(Float rating) {
        this.rating = rating;
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
