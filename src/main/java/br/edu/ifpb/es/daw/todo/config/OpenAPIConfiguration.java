package br.edu.ifpb.es.daw.todo.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import java.util.Arrays;

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
//	),
//	tags = {
//			@io.swagger.v3.oas.annotations.tags.Tag(
//					name = "todo",
//					description = "API Tarefa"
//			),
//			@io.swagger.v3.oas.annotations.tags.Tag(
//					name = "auth",
//					description = "API Autenticação"
//			),
//			@io.swagger.v3.oas.annotations.tags.Tag(
//					name = "usuario",
//					description = "API Usuário"
//			),
//	},
//	security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearer-jwt")
////	externalDocs = @io.swagger.v3.oas.annotations.ExternalDocumentation(
////			description = "Full Documentation",
////			url = "http://example.com/docs"
////	),
//)
//@io.swagger.v3.oas.annotations.security.SecurityScheme(
//		name = "bearer-jwt", // A unique name for your scheme
//		type = SecuritySchemeType.HTTP,
//		bearerFormat = "JWT",
//		scheme = "bearer",
//		in = SecuritySchemeIn.HEADER,
//		paramName = "Authorization"
//)
public class OpenAPIConfiguration {
	
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
	                            .url("http://www.apache.org/licenses/LICENSE-2.0")))
				.tags(Arrays.asList(new Tag().name("todo").description("API Tarefa"),
									new Tag().name("auth").description("API Autenticação"),
									new Tag().name("usuario").description("API Usuário")))
				.components(new Components().addSecuritySchemes("bearer-jwt",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
								.in(SecurityScheme.In.HEADER).name("Authorization")))
				.addSecurityItem(
						new SecurityRequirement().addList("bearer-jwt"));
//	            .externalDocs(new ExternalDocumentation()
//	                    .description("Full Documentation")
//	                    .url("http://example.com/docs"));
    }
}
