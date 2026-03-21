package ivha.jpa.project2.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ivha.jpa.project2.DTO.productRequestDTO;
import ivha.jpa.project2.DTO.productResponseDTO;
import ivha.jpa.project2.Mapper.ProductMapper;
import ivha.jpa.project2.Model.Product;
import ivha.jpa.project2.Model.Condition;
import ivha.jpa.project2.Repository.ProductRepository;
import ivha.jpa.project2.logs.ProductLogs;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

    @Autowired
    ProductLogs log;

    @Autowired
    ProductRepository repo;

    @Autowired
    ProductMapper mapper;


    // Punt 2 - Càrrega massiva de dades d’un fitxer en format .csv

    @Transactional
    public int createProducts(MultipartFile csv) throws IOException{
        String msg = log.info("ProductService", "createProducts", "Carregant la informació del fitxer " + csv.getName());
        log.writeToFile(msg);

        int comptador = 0;
        int erronis = 0;

        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // Llegim amb buffered reader
        try(BufferedReader br = new BufferedReader(new InputStreamReader(csv.getInputStream()))){
            String linia;
            int nLinia = 1;
            while((linia = br.readLine())!= null){
                String[] c = linia.split(",");
                try{
                    repo.save(new Product(
                        c[0],
                        c[1],
                        Integer.valueOf(c[2]),
                        Float.valueOf(c[3]),
                        Float.valueOf(c[4]),
                        Condition.valueOf(c[5]),
                        Boolean.parseBoolean(c[6]),
                        now,
                        now));
                    comptador++;
                } catch(Exception e){
                    msg = log.error("ProductService", "createProducts",
                        String.format("Error en la línia %d del fitxer. Missatge d'error: %s",nLinia,e));
                    log.writeToFile(msg);
                    erronis++;
                }
                nLinia++;
            }

        } catch (IOException e){
            System.err.println("Error d'accès al fitxer: " + e.getMessage());
           msg = log.error("ProductService", "createProducts", "Error de lectura de l'arxiu");
           log.writeToFile(msg);
           return -1;
        }
        String dir = "src/main/resources/private/csv_processed";
        Path directory = Paths.get(dir);
        Path filePath = Paths.get(dir + "/" + csv.getOriginalFilename());
        
        // Guardem el csv
        try{
            Files.createDirectories(directory);
            Files.copy(csv.getInputStream(),filePath);
            
        }
        catch (Exception e){
            System.err.println("No s'ha pogut guardar el csv");
            msg = log.error("ProductService", "createProducts", "No s'ha pogut guardar l'arxiu");
            log.writeToFile(msg);
        }
        msg = log.info("ProductService", "createProducts",
            String.format("S'han guardat correctament %d registres i han donat error %d registres",comptador, erronis));
        log.writeToFile(msg);
        // Retornem registres creats
        return comptador;







        //obtenir un producte per id
        public Product getProductById(Long id){
            try{
                Product p = repo.findById(id);
                if(p != null){
                    return p;
                } else {
                    return null;
                }
            } catch (Exception e){ // en cas d'un error inesperat, retornem null i guardem l'error al log
                log.writeToFile(log.error("ProductService", "getProductById", "Error al consultar el producte amb id " + id + ". Missatge d'error: " + e));
                return null;
            }
        }

        // modificar tots els camps del producte
        public boolean updateProduct(Long id, productRequestDTO productRequest){
            try{
                Optional<Product> optionalProduct = repo.findById(id);
                if(optionalProduct.isPresent()){
                    Product p = optionalProduct.get();
                    p.setNom(productRequest.getNom());
                    p.setPreu(productRequest.getPreu());
                    p.setDateUpdated(new Timestamp(System.currentTimeMillis()));
                    p.setDescripcio(productRequest.getDescripcio());
                    p.setStock(productRequest.getStock());
                    p.setCondition(productRequest.getCondition());
                    p.setActive(productRequest.isActive());
                    // guardem el producte amb les modificacions
                    repo.save(p);
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e){ // en cas d'un error inesperat, retornem false i guardem l'error al log
                log.writeToFile(log.error("ProductService", "updateProduct", "Error al modificar el producte amb id " + id + ". Missatge d'error: " + e));
                return false;
            }
        }

        // modificar preu producte
        public boolean updatePreuProducte(Long id, double preu){
            try{
                Optional<Product> optionalProduct = repo.findById(id);
                if(optionalProduct.isPresent()){
                    Product p = optionalProduct.get();
                    p.setPreu((float) preu);
                    p.setDateUpdated(new Timestamp(System.currentTimeMillis()));
                    // guardem el producte amb el nou preu
                    repo.save(p);
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e){ // en cas d'un error inesperat, retornem false i guardem l'error al log
                log.writeToFile(log.error("ProductService", "updatePreuProducte", "Error al modificar el preu del producte amb id " + id + ". Missatge d'error: " + e));
                return false;
            }
        }

        // borrat lògic d'un producte
        public boolean deleteProductLogic(Long id){
            try{
                Optional<Product> optionalProduct = repo.findById(id);
                if(optionalProduct.isPresent()){
                    Product p = optionalProduct.get();
                    p.setActive(false);
                    p.setDateUpdated(new Timestamp(System.currentTimeMillis()));
                    
                    // guardem el producte amb active a false
                    repo.save(p);
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e){ // en cas d'un error inesperat, retornem false i guardem l'error al log
                log.writeToFile(log.error("ProductService", "deleteProductLogic", "Error al eliminar lògicament el producte amb id " + id + ". Missatge d'error: " + e));
                return false;
            }
        }
}
