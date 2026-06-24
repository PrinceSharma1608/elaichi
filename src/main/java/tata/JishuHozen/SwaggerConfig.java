package tata.JishuHozen;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI machineManagementAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Jishu Hozen Management System")
                                .description("Tata Machine Maintenance API")
                                .version("1.0")
                                .contact(
                                        new Contact()
                                                .name("Backend Developer")
                                                .email("1608.prince@gmail.com")
                                )
                );
    }
}