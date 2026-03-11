package ivha.jpa.project2.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ivha.jpa.project2.DTO.productResponseDTO;
import ivha.jpa.project2.Model.Product;
import ivha.jpa.project2.Repository.ProductRepository;
import ivha.jpa.project2.logs.ProductLogs;

@Service
public class ProductService {

    @Autowired
    ProductLogs log;

    @Autowired
    ProductRepository repo;

    public int createProducts(MultipartFile csv) throws IOException{
        String msg = log.info("TaskService", "createTasks", "Carregant la informació del fitxer " + csv.getName());
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
                    //repo.createProduct(new Product(c[0],Integer.parseInt(c[1]),Timestamp.valueOf(c[2]), now, now));
                    comptador++;
                } catch(Exception e){
                    msg = log.error("TaskService", "createTasks",
                        String.format("Error en la línia %d del fitxer. Missatge d'error: %s",nLinia,e));
                    log.writeToFile(msg);
                    erronis++;
                }
                nLinia++;
            }

        } catch (IOException e){
            System.err.println("Error d'accès al fitxer: " + e.getMessage());
           msg = log.error("TasksService", "createTask", "Error de lectura de l'arxiu");
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
            msg = log.error("TaskService", "createTasks", "No s'ha pogut guardar l'arxiu");
            log.writeToFile(msg);
        }
        msg = log.info("TaskService", "createTasks",
            String.format("S'han guardat correctament %d registres i han donat error %d registres",comptador, erronis));
        log.writeToFile(msg);
        // Retornem registres creats
        return comptador;
    }

    public List<productResponseDTO> searchByNom(String prefix) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchByField'");
    }

    public List<productResponseDTO> searchByField(String camp, boolean asc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchByField'");
    }

    public List<productResponseDTO> getBestQP() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBestQP'");
    }
}
