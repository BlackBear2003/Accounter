package host.luke.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class ResourceConfigAdapter implements WebMvcConfigurer {

    @Value("${file.path}")
    String path ;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取文件的真实路径



        registry.addResourceHandler("/images/**")
                        .addResourceLocations("file:" + path);

    }
}
