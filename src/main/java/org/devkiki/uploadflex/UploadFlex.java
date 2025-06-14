package org.devkiki.uploadflex;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Nishimwe Rukundo Prosper",
                        email = "prosper.rk1@gmail.com"
                ),
                description = "A flexible and extensible image upload solution for Spring Boot applications. Easily switch between providers like MinIO, AWS S3, and Cloudinary without modifying your business logic.",
                title = "UploadFlex API Documentation",
                version = "v.1.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                )
        }
)
@SpringBootApplication
public class UploadFlex {

    public static void main(String[] args) {
        SpringApplication.run(UploadFlex.class, args);
    }

}
