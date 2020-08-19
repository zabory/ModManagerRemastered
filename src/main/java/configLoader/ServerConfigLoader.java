package configLoader;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Config loader for the the Server program
 * @author Ben Shabowski
 * @version 0.1
 * @since 0.1
 *
 */
@Configuration
@PropertySource("classpath:configs//Server.properties")
public class ServerConfigLoader {

}
