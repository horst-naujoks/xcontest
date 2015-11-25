package naujoks.xcontest;

import java.time.LocalDateTime;

public class Flight {

	private String xcontextUrl;
	private LocalDateTime launchDate;

	public LocalDateTime getLaunchDate() {
		return launchDate;
	}

	public void setLaunchDate(LocalDateTime launchDate) {
		this.launchDate = launchDate;
	}

	public String getLaunchLocation() {
		return launchLocation;
	}

	public void setLaunchLocation(String launchLocation) {
		this.launchLocation = launchLocation;
	}

	public String getLaunchCountry() {
		return launchCountry;
	}

	public void setLaunchCountry(String launchCountry) {
		this.launchCountry = launchCountry;
	}

	private String launchLocation;
	private String launchCountry;

	public String getXcontextUrl() {
		return xcontextUrl;
	}

	public void setXcontextUrl(String xcontextUrl) {
		this.xcontextUrl = xcontextUrl;
	}

}
