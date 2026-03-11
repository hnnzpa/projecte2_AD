package ivha.jpa.project2.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ivha.jpa.project2.DTO.productResponseDTO;
import ivha.jpa.project2.Service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService service;

    // Integrant 1 - Ivan
    
    @PostMapping("products/batch")
    public ResponseEntity<String> importTasks(@RequestBody MultipartFile csv) {
        try {
            service.createProducts(csv);
            return ResponseEntity.ok("Productes creats");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No s'han pogut importar els productes");
        }
    }

    @GetMapping("/products/search/nom")
    public ResponseEntity<List<productResponseDTO>> searchByNom(@RequestParam String prefix) {
        List<productResponseDTO> products = service.searchByNom(prefix);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/search/order")
    public ResponseEntity<List<productResponseDTO>> searchByField(@RequestParam String camp, @RequestParam boolean asc) {
        List<productResponseDTO> products = service.searchByField(camp, asc);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/bestQP")
    public ResponseEntity<List<productResponseDTO>> getBestQP() {
        List<productResponseDTO> products = service.getBestQP();
        return ResponseEntity.ok(products);
    }
    
    
    
}
