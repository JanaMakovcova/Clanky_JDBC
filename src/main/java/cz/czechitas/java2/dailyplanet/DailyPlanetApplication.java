package cz.czechitas.java2.dailyplanet;

import org.slf4j.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.*;
import org.springframework.boot.context.embedded.*;
import org.springframework.boot.web.support.*;
import org.springframework.context.annotation.*;
import org.springframework.context.event.*;
import org.springframework.core.env.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.view.*;

@SpringBootApplication
public class DailyPlanetApplication extends SpringBootServletInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(DailyPlanetApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(DailyPlanetApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure (SpringApplicationBuilder builder){
		return builder.sources(DailyPlanetApplication.class);
	}

	@Bean
	public RequestToViewNameTranslator viewNameTranslator(Environment environment) {
		DefaultRequestToViewNameTranslator viewNameTranslator = new DefaultRequestToViewNameTranslator();
		String thymeleafSuffix = environment.getProperty("spring.thymeleaf.suffix");
		if (thymeleafSuffix != null && thymeleafSuffix.isEmpty()) {
			viewNameTranslator.setStripExtension(false);
		}
		return viewNameTranslator;
	}

	@EventListener
	public void onAppEvent(EmbeddedServletContainerInitializedEvent evt) {
		int port = evt.getEmbeddedServletContainer().getPort();
		logger.info("Your web app address: http://localhost:" + port +
				evt.getApplicationContext().getServletContext().getContextPath());
	}
}
