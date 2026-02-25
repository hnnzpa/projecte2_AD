package ivha.jpa.project2.Model;

import java.security.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity 
public class Product {
    @Id
    @GeneratedValue 
    private Long id;
    private String nom;
    private String descripcio;
    private Integer stock;
    private Float price;
    private Float rating;
    private Condition condition;
    private boolean active; 
    private Timestamp dateCreated;
    private Timestamp dateUpdated;
    
    
    public Product() {
    }


    public Product(Long id, String nom, String descripcio, Integer stock, Float price, Float rating,
            Condition condition, boolean active, Timestamp dateCreated, Timestamp dateUpdated) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.stock = stock;
        this.price = price;
        this.rating = rating;
        this.condition = condition;
        this.active = active;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
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


    public Timestamp getDateCreated() {
        return dateCreated;
    }


    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }


    public Timestamp getDateUpdated() {
        return dateUpdated;
    }


    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    
}





