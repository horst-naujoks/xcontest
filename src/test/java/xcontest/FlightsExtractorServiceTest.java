package xcontest;

import static org.junit.Assert.*;

import java.util.List;
import naujoks.xcontest.Flight;
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
		List<Flight> flights = flightsExtractorService.getFlights("horstnaujoks", "nivea1");
		assertEquals(2, flights.size());
		
		Flight flight = flights.get(0);
		assertEquals("2013-06-07", flight.getLaunchDate().toLocalDate().toString());
		assertEquals("10:46", flight.getLaunchDate().toLocalTime().toString());
		assertEquals("CH", flight.getLaunchCountry());
		assertEquals("Niederwiler Stierenberg", flight.getLaunchLocation());
	}

}
