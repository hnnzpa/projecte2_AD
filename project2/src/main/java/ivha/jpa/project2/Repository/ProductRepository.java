package ivha.jpa.project2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ivha.jpa.project2.Model.Product;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    

}
