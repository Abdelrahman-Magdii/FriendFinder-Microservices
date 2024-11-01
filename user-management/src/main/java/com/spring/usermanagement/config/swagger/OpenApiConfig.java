package com.spring.usermanagement.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "User Management", version = "1.0",
                description = "Microservices to management security"))
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Auth Token";

    //    @Bean
    //    public OpenAPI customOpenAPI(
    //                    @Value("${application-description}") String appDescription,
    //                    @Value("${application-version}") String appVersion) {
    //        return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info()
    //                        .title("User Management").version(appVersion)
    //                        .contact(getContact()).description(appDescription)
    //                        .termsOfService("http://swagger.io/terms/").license(getLicense()))
    //                        .addSecurityItem(new SecurityRequirement().addList(
    //                                        SECURITY_SCHEME_NAME,
    //                                        Arrays.asList("read", "write")))
    //                        .components(new Components().addSecuritySchemes(
    //                                        SECURITY_SCHEME_NAME,
    //                                        new SecurityScheme().name(SECURITY_SCHEME_NAME)
    //                                                        .type(SecurityScheme.Type.HTTP)
    //                                                        .scheme("bearer")
    //                                                        .bearerFormat("JWT")));
    //    }
    //
    //    private Contact getContact() {
    //        Contact contact = new Contact();
    //        contact.setEmail("info@gmail.com");
    //        contact.setName("Book Service");
    //        contact.setUrl("https://www.book.com");
    //        contact.setExtensions(Collections.emptyMap());
    //        return contact;
    //    }
    //
    //    private License getLicense() {
    //        License license = new License();
    //        license.setName("Apache License, Version 2.0");
    //        license.setUrl("http://www.apache.org/licenses/LICENSE-2.0");
    //        license.setExtensions(Collections.emptyMap());
    //        return license;
    //    }

}
