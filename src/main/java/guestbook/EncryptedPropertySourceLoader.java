package guestbook;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.spring31.properties.EncryptablePropertiesPropertySource;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;


/**
 * This class is a replacement for the default Spring PropertySourceLoader. It has the capability of detecting
 * and decrypting encrypted properties via Jasypt Encryption Library.
 * The decryption password must be provided via an environment variable or via a System property. The name of the property can be {@code PROPERTY_ENCRYPTION_PASSWORD} or {@code property.encryption.password}.
 * For more information see http://www.jasypt.org/ and http://www.jasypt.org/spring31.html
 * For Spring Boot integration the default {@link PropertySourceLoader} configuration was overriden by
 * META-INF/spring.factories file.
 *
 * @see org.springframework.boot.env.PropertySourceLoader
 */

public class EncryptedPropertySourceLoader implements PropertySourceLoader, PriorityOrdered {

    private static final String ENCRYPTION_PASSWORD_ENVIRONMENT_VAR_NAME_UNDERSCORE = "PROPERTY_ENCRYPTION_PASSWORD";
    private static final String ENCRYPTION_PASSWORD_ENVIRONMENT_VAR_NAME_DOT = "property.encryption.password";
    private static final String ENCRYPTION_PASSWORD_NOT_SET = "ENCRYPTION_PASSWORD_NOT_SET";

    private final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    public EncryptedPropertySourceLoader() {
        this.encryptor.setPassword(getPasswordFromEnvAndSystemProperties());
    }

    private String getPasswordFromEnvAndSystemProperties() {
        System.out.println("HEY LOOK OVER HERE SFASDFAS!" + System.getenv(ENCRYPTION_PASSWORD_ENVIRONMENT_VAR_NAME_UNDERSCORE));
        String password = System.getenv(ENCRYPTION_PASSWORD_ENVIRONMENT_VAR_NAME_UNDERSCORE);
        if (password == null) {
            password = System.getenv(ENCRYPTION_PASSWORD_ENVIRONMENT_VAR_NAME_DOT);
            if (password == null) {
                password = System.getProperty(ENCRYPTION_PASSWORD_ENVIRONMENT_VAR_NAME_UNDERSCORE);
                if (password == null) {
                    password = System.getProperty(ENCRYPTION_PASSWORD_ENVIRONMENT_VAR_NAME_DOT);
                    if (password == null) {
                        password = ENCRYPTION_PASSWORD_NOT_SET;
                    }
                }
            }
        }
        return password;
    }

    @Override
    public String[] getFileExtensions() {
        return new String[]{"properties"};
    }

    @Override
    public PropertySource load(final String name, final Resource resource, final String profile) throws
            IOException {
        if (profile == null) {
            //load the properties
            final Properties props = PropertiesLoaderUtils.loadProperties(resource);

            if (!props.isEmpty()) {
                //create the encryptable properties property source
                return new EncryptablePropertiesPropertySource(name, props, this.encryptor);
            }
        }

        return null;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
