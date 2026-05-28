package com.hotelmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelmanagerApplication.class, args);
    }//Essa linha liga o Spring Boot. Ela inicia o servidor web, carrega as configurações, conecta no banco e prepara as rotas do sistema.

}
