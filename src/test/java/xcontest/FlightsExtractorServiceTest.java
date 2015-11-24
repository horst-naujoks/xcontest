package xcontest;

import naujoks.xcontest.FlightsExtractorApplication;
import naujoks.xcontest.FlightsExtractorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(FlightsExtractorApplication.class)
public class FlightsExtractorServiceTest
{
	@Autowired
	FlightsExtractorService flightsExtractorService;

	@Test
	public void testGetFlights()
	{
		flightsExtractorService.getFlights("horstnaujoks", "nivea1");
	}

}
