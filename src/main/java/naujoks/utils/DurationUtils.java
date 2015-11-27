package naujoks.utils;

import org.joda.time.Duration;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class DurationUtils
{
	private static PeriodType HH_MM_TYPE;
	private final static PeriodFormatter HH_MM_FORMAT;
	private final static PeriodFormatter HH_MM_h_FORMAT;

	static
	{
		HH_MM_TYPE = PeriodType.time().withSecondsRemoved().withMillisRemoved();
		HH_MM_FORMAT = new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(1).appendHours().appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
		HH_MM_h_FORMAT = new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(1).appendHours().appendSeparator(":").minimumPrintedDigits(2).appendMinutes().appendSuffix(" h").toFormatter();
	}

	public static Duration parseHH_MM_h(String duration)
	{
		return HH_MM_h_FORMAT.parsePeriod(duration).toStandardDuration();
	}

	public static String asHH_MM(Duration duration)
	{
		return HH_MM_FORMAT.print(duration.toPeriod(HH_MM_TYPE));
	}

}
