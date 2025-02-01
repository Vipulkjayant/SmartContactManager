package smartcontact.manager.Config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class AppConfig {
    

    @Value("${cloudinary.cloud.name}")
    private String cloudname;

    @Value("${cloudinary.api.key}")
    private String api_key;

    @Value("${cloudinary.api.secret}")
    private String api_secret;


    @Bean
    public Cloudinary cloudinary()
    {

        return new Cloudinary(

        ObjectUtils.asMap(
            "cloud_name",cloudname,
             "api_key",api_key,
             "api_secret",api_secret
        )

        );
    }

}
