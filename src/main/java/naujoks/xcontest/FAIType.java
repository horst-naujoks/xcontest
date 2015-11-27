package naujoks.xcontest;

public enum FAIType
{
	FREE_FLIGHT("VP"), FLAT_TRIANGLE("PT"), FAI_TRIANGLE("FT");

	private String code;

	private FAIType(String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

	public static FAIType from(String code)
	{
		if (code != null)
		{
			for (FAIType type : FAIType.values())
			{
				if (code.equalsIgnoreCase(type.code))
				{
					return type;
				}
			}
		}
		return null;
	}
}
