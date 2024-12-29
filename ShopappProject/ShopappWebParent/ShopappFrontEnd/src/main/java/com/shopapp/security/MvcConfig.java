package com.shopapp.security;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		exposeDirectory("../categories-images", registry);
		exposeDirectory("../brand-logos", registry);
		exposeDirectory("../product-images", registry);
		exposeDirectory("../site-logo", registry);
	}
	
	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		String absPath = path.toFile().getAbsolutePath();
		
		String logicalPath = pathPattern.replace("../", "") + "/**";
		
		registry.addResourceHandler(logicalPath)
				.addResourceLocations("file:/" + absPath + "/");
	}
}
