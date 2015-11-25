package naujoks.xcontest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/getMyXContestFlights")
public class FlightsExtractorController {
	
	@Autowired
	private FlightsExtractorService flightsExtractorService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<Flight> getMyXContestFlights(@RequestParam(value = "name", required = false) String name) {
		return flightsExtractorService.getFlights("horstnaujoks", "nivea1");
	}

}