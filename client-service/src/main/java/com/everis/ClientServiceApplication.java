package com.everis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Configuracion del Servicio y Eureka.
 */
@SpringBootApplication
@EnableEurekaClient
public class ClientServiceApplication {

  /** Principal. */
  public static void main(String[] args) {

    SpringApplication.run(ClientServiceApplication.class, args);

  }

}
