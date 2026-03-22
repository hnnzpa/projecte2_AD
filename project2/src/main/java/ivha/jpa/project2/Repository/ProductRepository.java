package ivha.jpa.project2.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ivha.jpa.project2.Model.Product;
import ivha.jpa.project2.Model.Condition;



public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findAllAndActiveTrue();
    public Product findByIdAndActiveTrue(Long id);

    // Punt 4 - Consultes bàsiques amb Query Method
    public List<Product>  findByNomStartingWithAndActiveTrue(String nom);
    public List<Product> findByActiveTrueOrderByPriceAsc();
    public List<Product> findByActiveTrueOrderByPriceDesc();

    public List<Product> findByConditionAndActiveTrue(Condition condition);
    public List<Product> findByActiveTrueOrderByRatingAsc();
    public List<Product> findByActiveTrueOrderByRatingDesc();
    
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

    //Retorna els mes nous i millor valorats (rating)
    @Query("SELECT p FROM Product p " +
        "WHERE p.condition = :cond " +
        "AND p.active = true " +
        "AND p.rating = (SELECT MAX(p2.rating) FROM Product p2 WHERE p2.condition = :cond AND p2.active = true)")    
    List<Product> getBN(@Param("cond") Condition cond);

    // Retorna els productes amb el preu minim indicat ascendentment
    @Query("select p from Product p where p.price >= :priceMin and p.active = true order by (p.rating) asc")
    List<Product> findByPriceMinAsc(float priceMin);

    // Retorna els productes amb el preu minim indicat descendentment
    @Query("select p from Product p where p.price >= :priceMin and p.active = true order by (p.rating) desc")
    List<Product> findByPriceMinDesc(float priceMin);


}
