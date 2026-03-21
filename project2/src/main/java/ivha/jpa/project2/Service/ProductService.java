package ivha.jpa.project2.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import ivha.jpa.project2.Model.Condition;
import ivha.jpa.project2.Model.Product;
import ivha.jpa.project2.Repository.ProductRepository;
import jakarta.transaction.Transactional;

@Service
public class ProductService {


    @Autowired
    ProductRepository repo;

    @Autowired
    ProductMapper mapper;


    // Punt 2 - Càrrega massiva de dades d’un fitxer en format .csv amb transactional
    // Carrega tots els registres o cap

    @Transactional
    public void createProducts(MultipartFile csv) throws IOException{

        Timestamp now = new Timestamp(System.currentTimeMillis());
        // Llegim amb buffered reader
        try(BufferedReader br = new BufferedReader(new InputStreamReader(csv.getInputStream()))){
            String linia;
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
                } catch(Exception e){
                    System.err.println("Error en el guardat d'un registre: " + e.getMessage());
                }
            }

        } catch (IOException e){
            System.err.println("Error d'accès al fitxer: " + e.getMessage());
            throw e;
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
            
        }
    }

    // Punt 3 - Endpoints simples

    // Consultar tots els productes
    public List<productResponseDTO> findAllProducts() {
        List<Product> productes =  repo.findAll();
        List<productResponseDTO> response = new ArrayList<>();

        for (Product p: productes){
            response.add(mapper.toProductResponseDTO(p));
        }
        return response;
    }

    // Afegir un producte
    public void createProduct(productRequestDTO product) {
        Product p = mapper.toProduct(product);
        repo.save(p);
    }

    // Modificar l’estoc de productes
    public boolean updateStock(long id, int stock){
        Optional<Product> p = repo.findById(id);
        if (p.isEmpty()){
            return false;
        }
        Product producte = p.get();
        producte.setStock(stock);
        return true;
    }

    // Borrat físic d'un producte
    public void deleteProduct(long id) {
        repo.deleteById(id);
    }

    // Punt 4 - Consultes bàsiques amb Query Method

    // Búsqueda per prefix
    public List<productResponseDTO> searchByNom(String prefix) {
        List<Product> productes = repo.findByNomStartingWithAndActiveTrue(prefix);
        List<productResponseDTO> response = new ArrayList<>();

        // Mapejem a productResponseDTO amb la capa mapper
        for (Product p: productes){
            response.add(mapper.toProductResponseDTO(p));
        }

        return response;
    }

    // Ordena els productes ascendent o descendent en funció del camp passat per paràmetre
    public List<productResponseDTO> searchByField(String camp, String order) {
        if (!(order.equals("asc") || order.equals("desc"))){
            throw new UnsupportedOperationException ("order ha der ser 'asc' o 'desc'");
        }

        List<Product> products;
        List<productResponseDTO> response = new ArrayList<>();

        if (camp.equals("preu")){
            if (order.equals("asc")){
                products = repo.findByActiveTrueOrderByPriceAsc();
            } else {
                products = repo.findByActiveTrueOrderByPriceDesc();
            }
            for (Product p: products){
                response.add(mapper.toProductResponseDTO(p));
            }
            return response;
            
        }
        return null;
    }

    // Punt 5 - Consultes amb JPQL

    // Cerca amb un límit de preu mínim i màxim i ordena per camp asc o desc
    public List<productResponseDTO> searchByField(String camp, String order, float priceMin, float priceMax, int limit) {
        if (!(order.equals("asc") || order.equals("desc"))){
            throw new UnsupportedOperationException ("order ha der ser 'asc' o 'desc'");
        }

        List<Product> products;
        List<productResponseDTO> response = new ArrayList<>();

        if (camp.equals("preu")){
            if (order.equals("asc")){
                products = repo.findByPriceAsc(priceMin, priceMax);
            } else {
                products = repo.findByPriceDesc(priceMin, priceMax);
            }

            if (limit > products.size()) limit = products.size();
            
            for (int i = 0; i < limit; i++){
                response.add(mapper.toProductResponseDTO(products.get(i)));
            }
            return response;
            
        }

        if (camp.equals("rating")){
            if (order.equals("asc")){
                products = repo.findByPriceAsc(priceMin, priceMax);
            } else {
                products = repo.findByPriceDesc(priceMin, priceMax);
            }

            if (limit > products.size()) limit = products.size();
            
            for (int i = 0; i < limit; i++){
                response.add(mapper.toProductResponseDTO(products.get(i)));
            }
            return response;
            
        }
        return null;
    }

    // Retorna els productes amb millor relació rating/preu
    public List<productResponseDTO> getBestQP() {

        List<Product> productes = repo.findBestQp();
        List<productResponseDTO> response = new ArrayList<>();
        List<Product> top5;

        if (productes.size() > 5){
            top5 = productes.subList(0, 5);
        } else {
            top5 = productes;
        }

        for (Product p: top5){
            response.add(mapper.toProductResponseDTO(p));
        }

        return response;
    }

    







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
