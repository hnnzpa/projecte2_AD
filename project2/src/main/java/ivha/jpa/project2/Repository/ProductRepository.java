package ivha.jpa.project2.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ivha.jpa.project2.Model.Product;
import java.util.List;



public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product>  findByNomStartingWithAndActiveTrue(String nom);
    public List<Product> findByActiveTrueOrderByPriceAsc();
    public List<Product> findByActiveTrueOrderByPriceDesc();
    

    

}
