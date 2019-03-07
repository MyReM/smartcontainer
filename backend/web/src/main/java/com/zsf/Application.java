package com.zsf;

import com.zsf.core.utils.LibLoader;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yyq
 */
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
@EnableTransactionManagement
@EnableAsync
@MapperScan(basePackages = {"com.zsf.**.mapper"}, basePackageClasses = {Mapper.class})
public class Application extends SpringBootServletInitializer {

	static {

//		LibLoader.loadLib("/lib/serialport/rxtx/rxtxParallel.dll");
//		LibLoader.loadLib("/lib/serialport/rxtx/rxtxSerial.dll");

		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
	}


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}
}
