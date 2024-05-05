package com.ecom.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    /**
     * WebMvcConfigurer es una interfaz de Spring que proporciona métodos para personalizar la configuración de Spring MVC, 
     * incluida la configuración de CORS.corsConfigurer(): 
     * Este método crea y devuelve un objeto WebMvcConfigurer personalizado que configura la política de CORS.
     * 
     * 
     * */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            /**
             * addCorsMappings(CorsRegistry registry): Este método se implementa para agregar mapeos CORS específicos. 
             * Toma un objeto CorsRegistry como argumento, que se utiliza para configurar la política de CORS.
             * 
             * */
            public void addCorsMappings(CorsRegistry registry) {
            	// Este método especifica a qué URL o patrones de URL se aplicará la configuración de CORS. 
            	//En este caso, "/**" indica que la configuración de CORS se aplicará a todas las URL.
                registry.addMapping("/**")
                //Este método especifica qué métodos HTTP están permitidos en las solicitudes CORS. 
                        .allowedMethods(GET, POST, PUT, DELETE)
                        /*Este método especifica qué encabezados de solicitud se permiten en las solicitudes CORS.
                           El asterisco (*) indica que se permiten todos los encabezados*/
                        .allowedHeaders("*")
                        /* Este método especifica desde qué orígenes se permiten las solicitudes CORS. 
                         * El asterisco (*) indica que se permiten todas las origenes.*/
                        .allowedOriginPatterns("*")
                        /*Este método especifica si el servidor permitirá el envío de credenciales (como cookies o encabezados de autorización) 
                         * en las solicitudes CORS. En este caso, se permite el envío de credenciales.*/
                        .allowCredentials(true);
            }
        };
    }
}
