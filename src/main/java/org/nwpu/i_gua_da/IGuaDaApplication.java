package org.nwpu.i_gua_da;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class IGuaDaApplication {

    public static void main(String[] args) {
        SpringApplication.run(IGuaDaApplication.class, args);
    }

}
