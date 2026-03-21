package ivha.jpa.project2.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ivha.jpa.project2.DTO.productRequestDTO;
import ivha.jpa.project2.DTO.productResponseDTO;
import ivha.jpa.project2.Service.ProductService;


@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService service;

    // Punt 2 - Càrrega massiva de dades d’un fitxer en format .csv
    @PostMapping("products/batch")
    public ResponseEntity<String> importProducts(@RequestBody MultipartFile csv) {
        try {
            service.createProducts(csv);
            return ResponseEntity.ok("Productes creats");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No s'han pogut importar els productes");
        }
    }
    
    // Punt 3 - Endpoints simples

    // Consultar tots els productes
    @GetMapping("/products")
    public ResponseEntity<List<productResponseDTO>> findAllProducts() {
        try {
            List<productResponseDTO> products = service.findAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
    }

    // Afegir un producte
    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestBody productRequestDTO product) {
        try {
            service.createProduct(product);
            return ResponseEntity.ok("S'ha creat el producte");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No s'ha pogut crear el producte");
        }
        
    }
    
    // Modificar l’estoc de productes
    @PatchMapping("/products/{id}/stock/{stock}")
    public ResponseEntity<String> updateStock(@PathVariable long id, @PathVariable int stock){
        try{
            if (service.updateStock(id, stock)){
               return ResponseEntity.ok("Modificat l'estoc del producte"); 
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No s'ha pogut modificar l'estoc del producte");
  
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No s'ha pogut modificar l'estoc del producte");
        }
    }
    
    // Borrat físic d'un producte
    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){
        try {
            service.deleteProduct(id);
            return ResponseEntity.ok("Producte eliminat");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No s'ha pogut eliminar el producte");
        }
    }

    // Punt 4 - Consultes bàsiques amb Query Method
    
    @GetMapping("/products/search/nom")
    public ResponseEntity<List<productResponseDTO>> searchByNom(@RequestParam String prefix) {
        try {
            List<productResponseDTO> products = service.searchByNom(prefix);
            return ResponseEntity.ok(products);    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
    }

    @GetMapping("/products/search/order")
    public ResponseEntity<List<productResponseDTO>> searchByField(@RequestParam String camp, @RequestParam String order) {
        List<productResponseDTO> products = service.searchByField(camp, order);
        return ResponseEntity.ok(products);
    }

    // Punt 5 -  Consultes amb JPQL

    @GetMapping("/products/search/order2")
    public ResponseEntity<List<productResponseDTO>> searchByField(
        @RequestParam String camp, @RequestParam String order, @RequestParam float priceMin, @RequestParam float priceMax, @RequestParam int limit
    ) {
        List<productResponseDTO> products = service.searchByField(camp, order, priceMin, priceMax, limit);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/bestQP")
    public ResponseEntity<List<productResponseDTO>> getBestQP() {
        List<productResponseDTO> products = service.getBestQP();
        return ResponseEntity.ok(products);
    }

    
    
    
    
}
