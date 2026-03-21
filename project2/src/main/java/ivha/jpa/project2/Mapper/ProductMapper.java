package ivha.jpa.project2.Mapper;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import ivha.jpa.project2.DTO.productRequestDTO;
import ivha.jpa.project2.DTO.productResponseDTO;
import ivha.jpa.project2.Model.Product;

@Component
public class ProductMapper {

    public Product toProduct(productRequestDTO p){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return new Product(p.getNom(), p.getDescripcio(), p.getStock(), p.getPrice(), p.getRating(), p.getCondition(), true, now, now);
    }

    public productResponseDTO toProductResponseDTO (Product p){
        return new productResponseDTO(p.getNom(), p.getDescripcio(), p.getStock(), p.getPrice(), p.getRating(), p.getCondition());
    }
}
