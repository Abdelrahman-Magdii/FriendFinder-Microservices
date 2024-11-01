package com.spring.usermanagement.config.swagger;

import org.springframework.context.annotation.Configuration;

@Configuration
//@SecurityScheme(name = "BearerAuth", description = "JWT authentication",
//                scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT",
//                in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {

    //    @Bean
    //    public GroupedOpenApi userManagementApi() {
    //        String packagesToscan[] = { "com.spring.usermanagement" };
    //        return GroupedOpenApi.builder().group("User Management API")
    //                        .packagesToScan(packagesToscan)
    //                        .addOperationCustomizer(appTokenHeaderParam()).build();
    //    }

    //    @Bean
    //    public OperationCustomizer appTokenHeaderParam() {
    //        return (Operation operation, HandlerMethod handlerMethod) -> {
    //            Parameter headerParameter = new Parameter().in(ParameterIn.HEADER.toString())
    //                            .required(false)
    //                            .schema(new StringSchema()
    //                                            ._default("app_token_header_default_value"))
    //                            .name("app_token_header").description("App Token Header");
    //            operation.addParametersItem(headerParameter);
    //            return operation;
    //        };
    //    }

}
