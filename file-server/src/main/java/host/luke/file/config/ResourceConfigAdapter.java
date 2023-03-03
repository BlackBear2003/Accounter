package host.luke.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class ResourceConfigAdapter implements WebMvcConfigurer {


    @Value("${file.path.win}")
    String winPath ;
    @Value("${file.path.mac}")
    String macPath ;
    @Value("${file.path.linux}")
    String linuxPath ;


    public String judgeEnvPath(){
        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase();
        if(osName.startsWith("win")){
            return winPath;
        }
        if(osName.startsWith("mac")){
            return macPath;
        }
        if(osName.startsWith("linux")){
            return linuxPath;
        }
        return null;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取文件的真实路径

        String path = judgeEnvPath();



        registry.addResourceHandler("/images/**")
                        .addResourceLocations("file:" + path);

    }


}
