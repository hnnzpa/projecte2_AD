CREATE TABLE productes(
    id BIGINT AUTO_INCREMENT PRIMARY KEY, 
    nom VARCHAR(50) NOT NULL, 
    descriptio VARCHAR(255),
    stock INT, 
    price FLOAT, 
    rating FLOAT,  
    condition VARCHAR(20), 
    active BOOLEAN,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
