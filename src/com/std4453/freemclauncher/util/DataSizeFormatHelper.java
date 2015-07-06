package com.std4453.freemclauncher.util;

public class DataSizeFormatHelper {
	private static final String[] UNIT_NAMES = { "B", "KB", "MB", "GB", "TB",
			"PB", "PB" };

	/**
	 * returns a formatted string of the given bytes. The format is as follows:<br>
	 * XXXXYY <br>
	 * XXXX is the number of unit YY, where XXXX >=0 and XXXX <1024 (2^10). YY
	 * is the name of the unit, which comes in B, KB, MB and up to PB (2^50)
	 * 
	 * @param bytes
	 * @return
	 */
	public static String formatDataSizeLong(long bytes) {
		int digits = (int) (Math.log(Math.abs(bytes)) / Math.log(2));
		if (digits < 10)
			return String.format("%s%dB", bytes >= 0 ? "" : "-",
					(int) Math.abs(bytes));
		if (digits < 20)
			return String.format("%s%dKB", bytes >= 0 ? "" : "-",
					(int) Math.abs(bytes >>> 10));
		if (digits < 30)
			return String.format("%s%dMB", bytes >= 0 ? "" : "-",
					(int) Math.abs(bytes >>> 20));
		if (digits < 40)
			return String.format("%s%dGB", bytes >= 0 ? "" : "-",
					(int) Math.abs(bytes >>> 30));
		if (digits < 50)
			return String.format("%s%dTB", bytes >= 0 ? "" : "-",
					(int) Math.abs(bytes >>> 40));
		return String.format("%s%dPB", bytes >= 0 ? "" : "-",
				(int) Math.abs(bytes >>> 50));
	}

	/**
	 * Mostly same as {@code formatDataSizeFloat()}, though it displays also the
	 * fraction digits. And if the integral number of units is more than 512
	 * (for example, 513KB), it displays as 0.XXXX and with a larger unit. For
	 * example, 513KB is displayed as 0.50MB.
	 * <p>
	 * 
	 * See {@link #formatDataSizeLong(long)}
	 * 
	 * @param bytes
	 * @param percision
	 *            number of fraction digits
	 * @return
	 */
	public static String formatDataSizeDouble(double bytes, int percision) {
		if (percision < 0)
			percision = 2;
		if (percision == 0)
			return formatDataSizeLong((long) bytes);

		if (bytes == 0)
			bytes = .0001f;

		int digits = (int) (Math.log(Math.abs(bytes)) / Math.log(2));
		int baseDigits = digits / 10 * 10;
		if (baseDigits == 60)
			baseDigits = 50;
		long baseLong = 1L << baseDigits;
		double ratio = bytes / baseLong;
		if (ratio > 512 && baseDigits < 50) {
			baseDigits += 10;
			baseLong <<= 10;
			ratio = bytes / baseLong;
		}
		if (baseDigits < 0)
			baseDigits = 0;
		String unitName = UNIT_NAMES[baseDigits / 10];

		return String.format("%s%s", formatStringBase10(ratio, percision),
				unitName);
	}

	private static String formatStringBase10(double number, int percision) {
		String integerPart = String.valueOf((long) number);

		if (percision <= 0)
			return integerPart;

		String fracPart = null;

		number = Math.abs(number) - Math.abs((long) number);
		for (int i = 0; i < percision; ++i, number *= 10)
			;
		if (percision > 0)
			fracPart = String.valueOf(Math.round(number));

		return integerPart + "." + fracPart;
	}
}
