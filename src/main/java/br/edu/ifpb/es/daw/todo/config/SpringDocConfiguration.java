package br.edu.ifpb.es.daw.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
//@io.swagger.v3.oas.annotations.OpenAPIDefinition(
//	info = @io.swagger.v3.oas.annotations.info.Info(
//			title = "CRUD de Tarefa",
//			description = "Exemplo de API REST com um CRUD de Tarefas",
//			version = "1.0.0",
//	        contact = @io.swagger.v3.oas.annotations.info.Contact(
//	                name = "Código no github",
//	                url = "https://github.com/ifpb-es/spring-boot-todo"
//	                //,email = "support@example.com"
//	        ),
//	        license = @io.swagger.v3.oas.annotations.info.License(
//	        			name = "Apache 2.0",
//	        			url = "http://www.apache.org/licenses/LICENSE-2.0"
//	        		)
//	)
//)
public class SpringDocConfiguration {
	
	@Bean
	OpenAPI customOpenAPI() {
	    return new OpenAPI()
	            .info(new Info()
	                    .title("CRUD de Tarefa")
	                    .description("Exemplo de API REST com um CRUD de Tarefas")
	                    .version("1.0.0")
	                    .contact(new Contact()
	                            .name("Código no github")
	                            .url("https://github.com/ifpb-es/spring-boot-todo"))
	                            //.email("support@example.com"))
	                    .license(new License()
	                            .name("Apache 2.0")
	                            .url("http://www.apache.org/licenses/LICENSE-2.0")));
//	            .externalDocs(new ExternalDocumentation()
//	                    .description("Full Documentation")
//	                    .url("http://example.com/docs"));
    }
}
