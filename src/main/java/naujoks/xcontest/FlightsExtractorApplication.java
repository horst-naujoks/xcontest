package naujoks.xcontest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class FlightsExtractorApplication extends SpringBootServletInitializer 
{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		return application.sources(FlightsExtractorApplication.class);
	}

	public static void main(String[] args)
	{
		SpringApplication.run(FlightsExtractorApplication.class, args);
	}

}