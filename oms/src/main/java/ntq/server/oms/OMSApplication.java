package vn.com.mbbank.steady.core;

import io.hypersistence.utils.spring.repository.BaseJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import vn.com.mbbank.steady.core.util.EnvironmentInitializer;

@SpringBootApplication
@ComponentScan({"ntq.server.common", "ntq.server.core"})
@EnableJpaRepositories(value = {"ntq.server.common", "ntq.server.oms"}, repositoryBaseClass = BaseJpaRepositoryImpl.class)
@EntityScan({"ntq.server.common", "ntq.server.oms"})
public class OMSApplication {
  public static void main(String[] args) {
    System.setProperty("jasypt.encryptor.password", "std@aczz!$^6");
    System.setProperty("server.response.prefix-code", "CODE-");
    EnvironmentInitializer.initialize();
    SpringApplication.run(SteadyCoreApplication.class, args);
  }
}