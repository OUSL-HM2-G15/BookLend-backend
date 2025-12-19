package com.booklend.backend.config;

import io.imagekit.sdk.ImageKit;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value; // for injecting environment variables
import jakarta.annotation.PostConstruct; // for initialization after construction of bean

@Configuration
public class ImageKitConfig {
    // Inject sensitive values from environment variables
    @Value("${imagekit.publicKey}")
    private String publicKey;
    
    @Value("${imagekit.privateKey}")
    private String privateKey;
    
    @Value("${imagekit.urlEndpoint}")
    private String urlEndpoint;

    @PostConstruct
    public void initImageKit() {
        ImageKit imageKit = ImageKit.getInstance();
        io.imagekit.sdk.config.Configuration config = new io.imagekit.sdk.config.Configuration(
        // environment variables
          publicKey,
          privateKey,
          urlEndpoint
        );

    imageKit.setConfig(config); // set the configuration for ImageKit SDK
    }    
}
