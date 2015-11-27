package naujoks.utils;

import static org.junit.Assert.assertEquals;
import org.joda.time.Duration;
import org.junit.Test;

public class DurationUtilsTest
{

	@Test
	public void parseHourAndMinutes()
	{
		{
			Duration duration = DurationUtils.parseHH_MM_h("1:01 h");
			assertEquals(61, duration.getStandardMinutes());
			assertEquals("1:01", DurationUtils.asHH_MM(duration));
		}

		{
			Duration duration = DurationUtils.parseHH_MM_h("9:99 h");
			assertEquals(9 * 3600 + 99 * 60, duration.getStandardSeconds());
			assertEquals("10:39", DurationUtils.asHH_MM(duration));
		}

		{
			Duration duration = DurationUtils.parseHH_MM_h("0:01 h");
			assertEquals(60, duration.getStandardSeconds());
			assertEquals("0:01", DurationUtils.asHH_MM(duration));
		}
	}
}
