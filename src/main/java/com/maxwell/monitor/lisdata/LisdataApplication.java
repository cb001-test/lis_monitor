package com.maxwell.monitor.lisdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(value = "com.maxwell.monitor.lisdata.dao")
public class LisdataApplication {

    public static void main(String[] args) {
        SpringApplication.run(LisdataApplication.class, args);
    }

}
