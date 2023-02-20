package host.luke.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"host.luke.file","host.luke.common"},exclude= {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class FileServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class,args);
    }
}
