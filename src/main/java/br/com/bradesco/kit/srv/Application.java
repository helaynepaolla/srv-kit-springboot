package br.com.bradesco.kit.srv;

import br.com.bradesco.enge.logcloud.spring.EnableLoggingAutoConfiguration;
import br.com.bradesco.enge.mbtoken.api.EnableMobileBearerToken;
import br.com.bradesco.frwk.connector.EnableCwsOnWebMvc;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.util.Arrays;

@EnableLoggingAutoConfiguration
@SpringBootApplication
@EnableFeignClients
@EnableCaching
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(
        security = {@SecurityRequirement(name = "Bearer Authentication")}
)
@EnableMobileBearerToken
@EnableCwsOnWebMvc
public class Application implements CommandLineRunner {

    private static final Logger LOGGER_TECNICO = LoggerFactory.getLogger(Application.class);

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String activeProfile = Arrays.toString(environment.getActiveProfiles());
        String profiles = defaultIfBlank(activeProfile.replace("[]", ""), "[DEFAULT]");
        LOGGER_TECNICO.info("ACTIVE PROFILES: {}", profiles);
    }

}
