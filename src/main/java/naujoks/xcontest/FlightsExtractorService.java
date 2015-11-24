package naujoks.xcontest;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.springframework.stereotype.Component;

@Component
public class FlightsExtractorService {

	private static final String WWW_XCONTEST_ORG = "http://www.xcontest.org";

	private static Logger LOG = LogManager.getLogger(FlightsExtractorService.class);

	private CloseableHttpClient httpclient;
	private HtmlCleaner cleaner;

	private RequestConfig requestConfig;

	public FlightsExtractorService() {
		
        HttpHost proxy = new HttpHost("proxy.corproot.net", 8079, "http");

        requestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .build();
		
		httpclient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy())
				.setDefaultCookieStore(new BasicCookieStore()).build();

		cleaner = new HtmlCleaner();
	}

	public List<Flight> getFlights(String username, String password) {

		getRootPage();

		doLogin(username, password);

		List<String> flightsPerYearPaths = getFlightsPerYearPaths();

		for (String flightsPerYearPath : flightsPerYearPaths) {
			getFlightUrlsPerYear(flightsPerYearPath);
		}

		return null;
	}

	private void getRootPage() {

		HttpUriRequest getRootPage = RequestBuilder.get(WWW_XCONTEST_ORG).setConfig(requestConfig).build();
		try (CloseableHttpResponse response = httpclient.execute(getRootPage)) {

			HttpEntity entity = response.getEntity();

			LOG.debug("getRootPage: " + response.getStatusLine().getStatusCode());
			EntityUtils.consume(entity);

		} catch (Exception e) {
			LOG.error("Problem getting getRootPage", e);
		}
	}

	private void doLogin(String username, String password) {

		HttpUriRequest login = RequestBuilder.post(WWW_XCONTEST_ORG + "/world/en/").setConfig(requestConfig)
				.addParameter("login[username]", username).addParameter("login[password]", password).build();

		try (CloseableHttpResponse response = httpclient.execute(login)) {
			HttpEntity entity = response.getEntity();

			LOG.debug("login: " + response.getStatusLine().getStatusCode());
			EntityUtils.consume(entity);

		} catch (Exception e) {
			LOG.error("Problem while login", e);
		}
	}

	private List<String> getFlightsPerYearPaths() {

		List<String> urlPaths = new ArrayList<String>();

		HttpUriRequest getFlightPerYearPaths = RequestBuilder.get(WWW_XCONTEST_ORG + "/world/en/my-flights/").setConfig(requestConfig).build();

		try (CloseableHttpResponse response = httpclient.execute(getFlightPerYearPaths)) {

			HttpEntity entity = response.getEntity();

			LOG.debug("getFlightPerYearPaths: " + response.getStatusLine().getStatusCode());

			TagNode rootNode = cleaner.clean(entity.getContent());

			Object[] myFlightsOptions = rootNode.evaluateXPath("//select/*");

			LOG.debug("Number of Contest Years");

			for (Object option : myFlightsOptions) {
				String optionValue = ((TagNode) option).getAttributeByName("value");
				LOG.debug("Year: " + optionValue);
				urlPaths.add(optionValue);
			}

		} catch (Exception e) {
			LOG.error("Problem getting getFlightPerYearPaths", e);
		}

		return urlPaths;
	}

	private List<String> getFlightUrlsPerYear(String flightsPerYearPath) {

		List<String> flightUrlsPerYear = new ArrayList<String>();

		HttpUriRequest getFlightsPerYear = RequestBuilder.get(WWW_XCONTEST_ORG + flightsPerYearPath).setConfig(requestConfig).build();

		try (CloseableHttpResponse response = httpclient.execute(getFlightsPerYear)) {

			HttpEntity entity = response.getEntity();

			LOG.debug("getFlightsPerYear: " + response.getStatusLine().getStatusCode());

			TagNode rootNode = cleaner.clean(entity.getContent());

			Object[] flightsPerYear = rootNode.evaluateXPath("//tbody/tr/td/div/a[@class='detail']/@href");

			LOG.debug("Number of Flights in " + flightsPerYearPath + " : " + flightsPerYear.length);

			for (Object flight : flightsPerYear) {
				LOG.debug(flight.toString());
				flightUrlsPerYear.add(flight.toString());
			}

		} catch (Exception e) {
			LOG.error("Problem getting getFlightPerYearPaths", e);
		}

		return flightUrlsPerYear;
	}

//	public static void main(String[] args) {
//		
//		FlightsExtractor extractor = new FlightsExtractor("horstnaujoks", "nivea1");
//		extractor.getFlights();
//	}

}