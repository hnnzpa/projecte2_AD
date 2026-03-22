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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ivha.jpa.project2.DTO.ErrorDTO;
import ivha.jpa.project2.DTO.productRequestDTO;
import ivha.jpa.project2.DTO.productResponseDTO;
import ivha.jpa.project2.Model.Condition;
import ivha.jpa.project2.Service.ProductService;


@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService service;


    // Punt 2 - Càrrega massiva de dades d’un fitxer en format .csv
    @PostMapping("products/batch")
    public ResponseEntity<?> importProducts(@RequestBody MultipartFile csv) {
        try {
            service.createProducts(csv);
            return ResponseEntity.ok("Productes creats");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }

     // Punt 3 - Endpoints simples

    // Consultar tots els productes
    @GetMapping("/products")
    public ResponseEntity<?> findAllProducts() {
        try {
            List<productResponseDTO> products = service.findAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
        
    }

    // Afegir un producte
    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody productRequestDTO product) {
        try {
            service.createProduct(product);
            return ResponseEntity.ok("S'ha creat el producte");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
        
    }
    
    // Modificar l’estoc de productes
    @PatchMapping("/products/{id}/stock/{stock}")
    public ResponseEntity<?> updateStock(@PathVariable long id, @PathVariable int stock){
        try{    
            if (service.updateStock(id, stock)){
               return ResponseEntity.ok("Modificat l'estoc del producte"); 
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(HttpStatus.NOT_FOUND.value(),"No s'ha trobat el producte"));
  
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }
    
    // Borrat físic d'un producte
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id){
        try {
            service.deleteProduct(id);
            return ResponseEntity.ok("Producte eliminat");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }



    // consultar un producte x id
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
        try{
            productResponseDTO product = service.getProductById(id);
            if(product != null){
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(HttpStatus.NOT_FOUND.value(),"No s'ha trobat cap producte amb aquest id")); // retornem un 404 si no trobem el producte
            }
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage())); // retornem un 500 en cas d'un error inesperat 
        }
    }


    // modificar tots els camps del producte
    @PutMapping("/product/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody productRequestDTO productRequest){
        try{
            boolean updated = service.updateProduct(id, productRequest);
            if(updated){
                return ResponseEntity.ok("Producte actualitzat");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(HttpStatus.NOT_FOUND.value(),"No s'ha trobat cap producte amb aquest id"));
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }

    // modificar preu producte
    @PatchMapping("/product/update/preu/{id}")
    public ResponseEntity<?> updatePreuProducte(@PathVariable Long id, @RequestBody double preu){
        try{
            boolean updated = service.updatePreuProducte(id, preu);
            if(updated){
                return ResponseEntity.ok("Preu del producte actualitzat");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(HttpStatus.NOT_FOUND.value(),"No s'ha trobat cap producte amb aquest id"));
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }

    // borrat lògic d'un producte
    @DeleteMapping("/product/logic/delete/{id}")
    public ResponseEntity<?> deleteProductLogic(@PathVariable Long id){
        try{
            boolean deleted = service.deleteProductLogic(id);
            if(deleted){
                return ResponseEntity.ok("Producte eliminat lògicament");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(HttpStatus.NOT_FOUND.value(),"No s'ha trobat cap producte amb aquest id"));
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }


    // Punt 4 - Consultes bàsiques amb Query Method

    // Busca un producte per prefix, busca coincidencia amb l'inici del nom
    @GetMapping("/products/search/nom")
    public ResponseEntity<?> searchByNom(@RequestParam String prefix) {
        try {
            List<productResponseDTO> products = service.searchByNom(prefix);
            return ResponseEntity.ok(products);    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
        
    }


    // Ordena els productes per preu o rating ascendent o descendent
    @GetMapping("/products/search/order")
    public ResponseEntity<?> searchByField(@RequestParam String camp, @RequestParam String order) {
        try {
            List<productResponseDTO> products = service.searchByField(camp, order);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
        
    }


    // Busca productes per condició
    @GetMapping("/products/search/condition")
    public ResponseEntity<?> findByCondition(@RequestParam("condition") Condition condition) {
        System.out.println("He recibido la peticion con: " + condition);
        try {
            List<productResponseDTO> products = service.findByCondition(condition);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }


    // Punt 5 -  Consultes amb JPQL

    // Ordena els productes per preu o rating ascendent o descendent amb limits de preu mínim i màxim
    @GetMapping("/products/search/order2")
    public ResponseEntity<?> searchByField(
        @RequestParam String camp, @RequestParam String order, @RequestParam float priceMin, @RequestParam float priceMax, @RequestParam int limit
    ) {
        try {
            List<productResponseDTO> products = service.searchByField(camp, order, priceMin, priceMax, limit);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }

    // Retorna els 5 productes amb millor relació rating/preu
    @GetMapping("/products/bestQP")
    public ResponseEntity<?> getBestQP() {
        try {
            List<productResponseDTO> products = service.getBestQP();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }


    // Retorna productes amb preu superior al indicat i estigui active true
    @GetMapping("/products/search/order3")
    public ResponseEntity<?> findByPriceMin( 
        @RequestParam(defaultValue = "preuMinim") String camp, @RequestParam(defaultValue = "desc") String order, @RequestParam float priceMin, @RequestParam int limit
    ) {
        try {
            List<productResponseDTO> products = service.findByPriceMin(camp, order, priceMin, limit);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }

    //Obtenir els productes més nous i millor valorats (menor preu major rating)
    @GetMapping("/products/bestNew")
    public ResponseEntity<?> getBestNew() {
        try {
            List<productResponseDTO> products = service.getBestNew();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }

    //Retorna productes en blocs de 5
    @GetMapping("/products/get5")
    public ResponseEntity<?> get5Products(
        @RequestParam(defaultValue = "1") int pag,
        @RequestParam(defaultValue = "5") int size
    ) {
        try {
            List<productResponseDTO> products = service.get5Products(pag, size);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }

}
