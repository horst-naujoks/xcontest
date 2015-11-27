package naujoks.xcontest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import naujoks.utils.DurationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlEmphasis;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

@Component
public class FlightsExtractorService
{

	private static final String WWW_XCONTEST_ORG = "http://www.xcontest.org";

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm");

	private static Logger LOG = LogManager.getLogger(FlightsExtractorService.class);

	@Autowired
	private Environment environment;

	public List<Flight> getFlights(String username, String password)
	{

		WebClient webClient = new WebClient(BrowserVersion.CHROME, "proxy.corproot.net", 8079);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(true);

		HtmlPage rootPage = getRootPage(webClient);

		doLogin(rootPage, username, password);

		List<String> flightsPerYearPaths = getFlightsPerYearPaths(webClient);
		List<String> flightsUrls = new ArrayList<String>();
		List<Flight> flights = new ArrayList<Flight>();

		for (String flightsPerYearPath : flightsPerYearPaths)
		{
			flightsUrls.addAll(getFlightUrlsPerYear(webClient, flightsPerYearPath));
		}

		for (String flightUrl : flightsUrls)
		{
			flights.add(getFlight(webClient, flightUrl));
		}

		return flights;
	}

	private HtmlPage getRootPage(WebClient webClient)
	{

		try
		{
			return webClient.getPage(WWW_XCONTEST_ORG);
		}
		catch (Exception e)
		{
			LOG.error("Error while loading: " + WWW_XCONTEST_ORG, e);
			return null;
		}
	}

	private void doLogin(HtmlPage rootPage, String username, String password)
	{

		HtmlForm loginForm = rootPage.getForms().get(0);
		loginForm.getInputByName("login[username]").setValueAttribute(username);
		loginForm.getInputByName("login[password]").setValueAttribute(password);

		HtmlSubmitInput submitButton = (HtmlSubmitInput) loginForm.getInputByValue("::LogIN::");
		try
		{
			submitButton.click();
		}
		catch (IOException e)
		{
			LOG.error("Error while login", e);
		}
	}

	private List<String> getFlightsPerYearPaths(WebClient webClient)
	{

		List<String> urlPaths = new ArrayList<String>();
		HtmlPage page = null;
		try
		{
			page = webClient.getPage(WWW_XCONTEST_ORG + "/world/en/my-flights/");
		}
		catch (Exception e)
		{
			LOG.error("Error while loading: " + WWW_XCONTEST_ORG + "/world/en/my-flights/", e);
			return null;
		}

		@SuppressWarnings("unchecked")
		List<HtmlOption> myFlightsOptions = (List<HtmlOption>) page.getByXPath("//select/*");

		for (HtmlOption option : myFlightsOptions)
		{
			String optionValue = option.getAttribute("value");
			if (isTestEnvironment() && !optionValue.contains("2013"))
				continue;
			LOG.debug("Year: " + optionValue);
			urlPaths.add(optionValue);
		}

		return urlPaths;

	}

	private List<String> getFlightUrlsPerYear(WebClient webClient, String flightsPerYearPath)
	{

		List<String> flightUrlsPerYear = new ArrayList<String>();

		HtmlPage page = null;
		try
		{
			page = webClient.getPage(WWW_XCONTEST_ORG + flightsPerYearPath);
		}
		catch (Exception e)
		{
			LOG.error("Error while loading: " + WWW_XCONTEST_ORG + flightsPerYearPath, e);
			return null;
		}

		@SuppressWarnings("unchecked")
		List<DomAttr> flightsPerYear = (List<DomAttr>) page.getByXPath("//tbody/tr/td/div/a[@class='detail']/@href");

		LOG.debug("Number of Flights in " + flightsPerYearPath + " : " + flightsPerYear.size());

		for (DomAttr flight : flightsPerYear)
		{
			LOG.debug(flight.getValue());
			flightUrlsPerYear.add(flight.getValue());
		}

		return flightUrlsPerYear;

	}

	private Flight getFlight(WebClient webClient, String flightUrl)
	{

		HtmlPage page = null;
		try
		{
			page = webClient.getPage(WWW_XCONTEST_ORG + flightUrl);
			Thread.sleep(2000);

		}
		catch (Exception e)
		{
			LOG.error("Error while loading: " + WWW_XCONTEST_ORG + flightUrl, e);
			return null;
		}

		Flight flight = null;

		HtmlTable table = (HtmlTable) page.getByXPath("//div[@class='XCbaseInfo']/table[@class='XCinfo']").get(0);
		Map<String, List<HtmlTableCell>> baseInfo = getBaseInfo(table);

		flight = new Flight();
		flight.setXContestUrl(flightUrl);

		HtmlTableCell launchDate = baseInfo.get("date").get(1);
		String date = ((HtmlAnchor) launchDate.getFirstChild().getNextSibling()).getFirstChild().getTextContent();
		String time = ((HtmlEmphasis) launchDate.getFirstChild().getNextSibling().getNextSibling().getNextSibling()).getFirstChild().getTextContent();

		flight.setLaunchDate(LocalDateTime.parse(date + " " + time, DATE_TIME_FORMATTER));

		HtmlTableCell launch = baseInfo.get("launch").get(1);

		flight.setLaunchLocation(((HtmlAnchor) launch.getFirstChild().getNextSibling().getNextSibling().getNextSibling()).getAttribute("title").split(":")[1].trim());
		flight.setLaunchCountry(((HtmlAnchor) launch.getFirstChild().getNextSibling()).getFirstChild().getFirstChild().getTextContent());

		flight.setGlider(baseInfo.get("glider").get(1).getAttribute("title"));

		List<HtmlTableCell> route = baseInfo.get("route");

		flight.setContestTrackLength(Float.parseFloat(route.get(1).getFirstChild().getNextSibling().getTextContent()));
		flight.setFaiType(FAIType.from(route.get(2).getFirstChild().getNextSibling().getTextContent()));
		flight.setContestPoints(Float.parseFloat(route.get(3).getFirstChild().getNextSibling().getTextContent()));

		List<HtmlTableCell> flightDuration = baseInfo.get("flight duration");

		flight.setTrackDuration(DurationUtils.parseHH_MM_h(flightDuration.get(1).getTextContent().trim()));
		flight.setAverageSpeed(Float.parseFloat((flightDuration.get(2).getFirstChild().getNextSibling().getTextContent())));

		flight.setIgcFileUrl(((HtmlAnchor) baseInfo.get("igc").get(0).getFirstChild().getNextSibling().getNextSibling()).getAttribute("href"));

		table = (HtmlTable) page.getByXPath("//div[@class='innerDoc']/table[@class='XCinfo']").get(0);
		Map<String, List<HtmlTableCell>> additionalBaseInfo = getBaseInfo(table);

		flight.setMaxHeight(Integer.parseInt(additionalBaseInfo.get("max. altitude").get(1).getTextContent().replaceAll("[^-?.0-9]+", "")));

		return flight;
	}

	private Map<String, List<HtmlTableCell>> getBaseInfo(HtmlTable table)
	{
		Map<String, List<HtmlTableCell>> data = new HashMap<String, List<HtmlTableCell>>();

		List<HtmlTableRow> rows = table.getRows();
		for (HtmlTableRow row : rows)
		{
			List<HtmlTableCell> cells = row.getCells();
			String key = null;
			for (HtmlTableCell cell : cells)
			{
				if (cell instanceof HtmlTableHeaderCell)
				{
					key = key(cell);
					data.put(key, new ArrayList<HtmlTableCell>());
				}
				data.get(key).add(cell);
			}
		}
		return data;
	}

	private String key(HtmlTableCell cell)
	{

		String text = cell.asText();
		if (text.contains(":"))
			return text.split(":")[0].trim();
		return cell.getAttribute("class");
	}

	private boolean isTestEnvironment()
	{
		return environment.acceptsProfiles("test");
	}

}