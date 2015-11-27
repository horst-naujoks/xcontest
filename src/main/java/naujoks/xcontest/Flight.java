package naujoks.xcontest;

import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Flight
{
	private float averageSpeed;
	private float contestPoints;
	private float contestTrackLength;
	private FAIType faiType;
	private String glider;
	private String igcFileUrl;
	private String launchCountry;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm")
	private LocalDateTime launchDate;
	private String launchLocation;
	private int maxHeight;
	private Duration trackDuration;
	private String xContestUrl;

	public float getAverageSpeed()
	{
		return averageSpeed;
	}

	public float getContestPoints()
	{
		return contestPoints;
	}

	public float getContestTrackLength()
	{
		return contestTrackLength;
	}

	public FAIType getFaiType()
	{
		return faiType;
	}

	public String getGlider()
	{
		return glider;
	}

	public String getIgcFileUrl()
	{
		return igcFileUrl;
	}

	public String getLaunchCountry()
	{
		return launchCountry;
	}

	public LocalDateTime getLaunchDate()
	{
		return launchDate;
	}

	public String getLaunchLocation()
	{
		return launchLocation;
	}

	public int getMaxHeight()
	{
		return maxHeight;
	}

	public Duration getTrackDuration()
	{
		return trackDuration;
	}

	public String getXContestUrl()
	{
		return xContestUrl;
	}

	public void setAverageSpeed(float averageSpeed)
	{
		this.averageSpeed = averageSpeed;
	}

	public void setContestPoints(float contestPoints)
	{
		this.contestPoints = contestPoints;
	}

	public void setContestTrackLength(float trackLength)
	{
		this.contestTrackLength = trackLength;
	}

	public void setFaiType(FAIType faiType)
	{
		this.faiType = faiType;
	}

	public void setGlider(String glider)
	{
		this.glider = glider;
	}

	public void setIgcFileUrl(String igcFileUrl)
	{
		this.igcFileUrl = igcFileUrl;
	}

	public void setLaunchCountry(String launchCountry)
	{
		this.launchCountry = launchCountry;
	}

	public void setLaunchDate(LocalDateTime launchDate)
	{
		this.launchDate = launchDate;
	}

	public void setLaunchLocation(String launchLocation)
	{
		this.launchLocation = launchLocation;
	}

	public void setMaxHeight(int maxHeight)
	{
		this.maxHeight = maxHeight;
	}

	public void setTrackDuration(Duration trackDuration)
	{
		this.trackDuration = trackDuration;
	}

	public void setXContestUrl(String xContestUrl)
	{
		this.xContestUrl = xContestUrl;
	}
}
