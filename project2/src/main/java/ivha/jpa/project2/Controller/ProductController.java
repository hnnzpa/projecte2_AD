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




    // consultar un producte x id
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        try{
            Product product = service.getProductById(id);
            if(product != null){
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producte no trobat");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al consultar el producte");
        }
    }


    // modificar tots els camps del producte
    @PutMapping("/product/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody productRequestDTO productRequest){
        try{
            boolean updated = service.updateProduct(id, productRequest);
            if(updated){
                return ResponseEntity.ok("Producte actualitzat");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producte no trobat");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al modificar el producte");
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

    // Busca un producte per prefix, busca coincidencia amb l'inici del nom
    @GetMapping("/products/search/nom")
    public ResponseEntity<List<productResponseDTO>> searchByNom(@RequestParam String prefix) {
        try {
            List<productResponseDTO> products = service.searchByNom(prefix);
            return ResponseEntity.ok(products);    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
    }

    // Ordena els productes per preu o rating ascendent o descendent
    @GetMapping("/products/search/order")
    public ResponseEntity<List<productResponseDTO>> searchByField(@RequestParam String camp, @RequestParam String order) {
        try {
            List<productResponseDTO> products = service.searchByField(camp, order);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
    }

    // Punt 5 -  Consultes amb JPQL

    // Ordena els productes per preu o rating ascendent o descendent amb limits de preu mínim i màxim
    @GetMapping("/products/search/order2")
    public ResponseEntity<List<productResponseDTO>> searchByField(
        @RequestParam String camp, @RequestParam String order, @RequestParam float priceMin, @RequestParam float priceMax, @RequestParam int limit
    ) {
        try {
            List<productResponseDTO> products = service.searchByField(camp, order, priceMin, priceMax, limit);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Retorna els 5 productes amb millor relació rating/preu
    @GetMapping("/products/bestQP")
    public ResponseEntity<List<productResponseDTO>> getBestQP() {
        try {
            List<productResponseDTO> products = service.getBestQP();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    
    
    
    


    // modificar preu producte
    @PatchMapping("/product/update/preu/{id}")
    public ResponseEntity<String> updatePreuProducte(@PathVariable Long id, @RequestBody double preu){
        try{
            boolean updated = service.updatePreuProducte(id, preu);
            if(updated){
                return ResponseEntity.ok("Preu del producte actualitzat");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producte no trobat");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al modificar el preu del producte");
        }
    }

    // borrat lògic d'un producte
    @DeleteMapping("/product/logic/delete/{id}")
    public ResponseEntity<String> deleteProductLogic(@PathVariable Long id){
        try{
            boolean deleted = service.deleteProductLogic(id);
            if(deleted){
                return ResponseEntity.ok("Producte eliminat lògicament");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producte no trobat");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el producte");
        }
    }



}
