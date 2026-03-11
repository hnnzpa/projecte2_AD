package ivha.jpa.project2.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ivha.jpa.project2.Service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService service;

    @PostMapping("task/batch")
    public ResponseEntity<String> importTasks(@RequestBody MultipartFile csv) {
        try {
            service.createProducts(csv);
            return ResponseEntity.ok("Productes creats");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No s'han pogut importar els productes");
        }
    }
}
