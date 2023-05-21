package deu.cse.spring_webmail;

import java.io.IOException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;

@ComponentScan
@SpringBootApplication
@Slf4j
public class SpringWebmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebmailApplication.class, args);
    }

    @Bean(name="systemProperties")
    public PropertiesFactoryBean systemProperties() {
        
        log.debug("runProperties() called...");
        StandardPBEStringEncryptor spe = new StandardPBEStringEncryptor();
        spe.setAlgorithm("PBEWithMD5AndDES");
        spe.setPassword("gulio");
        System.out.println("admin = " + spe.encrypt("admin"));      
        System.out.println("ip = " + spe.encrypt("113.198.236.222"));      
        System.out.println("url = " + spe.encrypt("jdbc:mysql://113.198.236.222:9090/mail?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul"));      
        System.out.println("id = " + spe.encrypt("root"));      
        System.out.println("db pw = " + spe.encrypt("1q2w3e4r"));
        
        
        log.debug("systemProperties() called...");
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("/system.properties"));
        try {
            Properties props = bean.getObject();
            // log.debug("props = {}", props.keySet());
        } catch (IOException ex) {
            log.error("configProperties: 예외 = {}", ex.getMessage());
        }
        
        return bean;
    }

}
