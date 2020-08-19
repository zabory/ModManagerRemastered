package configLoader;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Config loader for the the Admin program
 * @author Ben Shabowski
 * @version 0.1
 * @since 0.1
 *
 */
@Configuration
@PropertySource("classpath:configs//Admin.properties")
public class AdminConfigLoader {

}
