package ivha.jpa.project2.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ivha.jpa.project2.Model.Product;
import java.util.List;



public interface ProductRepository extends JpaRepository<Product, Long> {

    // Punt 4 - Consultes bàsiques amb Query Method
    public List<Product>  findByNomStartingWithAndActiveTrue(String nom);
    public List<Product> findByActiveTrueOrderByPriceAsc();
    public List<Product> findByActiveTrueOrderByPriceDesc();
    
    // Punt 5 -  Consultes amb JPQL

    // Diferents queries en funció de l'ordre y el camp per a l'endpoint /api/products/search/order amb preu min i max
    @Query("select p from Product p where p.price >= :priceMin and p.price <= :priceMax and p.active = true order by p.price asc")
    List<Product> findByPriceAsc(float priceMin, float priceMax);

    @Query("select p from Product p where p.price >= :priceMin and p.price <= :priceMax and p.active = true order by p.price desc")
    List<Product> findByPriceDesc(float priceMin, float priceMax);

    @Query("select p from Product p where p.price >= :priceMin and p.price <= :priceMax and p.active = true order by p.rating asc")
    List<Product> findByRatingAsc(float priceMin, float priceMax);

    @Query("select p from Product p where p.price >= :priceMin and p.price <= :priceMax and p.active = true order by p.rating desc")
    List<Product> findByRatingDesc(float priceMin, float priceMax);

    // Retorna tots el productes ordenats per qualitat/preu descendent
    @Query("select p from Product p where p.active = true order by (p.rating / p.price) desc")
    List<Product> findBestQp();

    

}
