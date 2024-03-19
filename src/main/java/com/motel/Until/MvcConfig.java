package com.motel.Until;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.motel.Until.page.PagingAndSortingArgurmentResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	
	@SuppressWarnings("null")
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		exposeDirectory("upload/blog-files", registry);
	}
	
	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();
		
		String logicalPath = pathPattern.replace("../", "") + "/**";
		
		registry.addResourceHandler(logicalPath)
			.addResourceLocations("file:/" + absolutePath + "/");
	}

	@Override
	public void addArgumentResolvers(@SuppressWarnings("null") List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new PagingAndSortingArgurmentResolver());
	}
	
	
}