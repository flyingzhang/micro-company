package com.idugalic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * An application for my company - monolithic
 * 
 * @author idugalic
 *
 */
@SpringBootApplication
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String... args) throws UnknownHostException {
//        String key = "java.protocol.handler.pkgs";
//        String was = System.getProperty(key, "");
//        String pkgName = Handler.class.getPackage().getName();
//        pkgName = pkgName.substring(0, pkgName.lastIndexOf("."));
//        System.setProperty(key, was.isEmpty() ? pkgName : was + "|" + pkgName);
//        try {
//            URLConnection urlConnection = new URL("classpath://abc.jks").openConnection();
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        SpringApplication app = new SpringApplication(Application.class);
         Environment env = app.run(args).getEnvironment();
         LOG.info("\n----------------------------------------------------------\n\t" +
                 "Application '{}' is running! Access URLs:\n\t" +
                 "Local: \t\thttp://127.0.0.1:{}\n\t" +
                 "External: \thttp://{}:{}\n----------------------------------------------------------",
             env.getProperty("spring.application.name"),
             env.getProperty("server.port"),
             InetAddress.getLocalHost().getHostAddress(),
             env.getProperty("server.port"));
    }
    
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
