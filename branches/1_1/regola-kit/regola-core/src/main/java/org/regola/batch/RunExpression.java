package org.regola.batch;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RunExpression implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(RunExpression.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"HH:mm:ss");

	private final String startHourExpr;
	private final String startMinuteExpr;
	private final Date periodStart;
	private final Date periodEnd;

	public RunExpression(final String startTime, final Date fromPeriod,
			final Date toPeriod) {
		if (startTime == null || startTime.trim().length() == 0) {
			startHourExpr = "*";
			startMinuteExpr = "*";
		} else {
			String[] parts = startTime.split("\\s+");
			if (parts.length != 2) {
				throw new IllegalArgumentException(
						"Espressione di startTime non riconosciuta: ["
								+ startTime + "]");
			}
			startHourExpr = parts[0];
			startMinuteExpr = parts[1];
		}

		this.periodStart = toDayStart(fromPeriod);
		this.periodEnd = toDayEnd(toPeriod);
	}

	@Override
	public String toString() {
		return "ore " + startHourExpr + " minuti " + startMinuteExpr + " dal "
				+ formatDate(periodStart) + " al " + formatDate(periodEnd);
	}

	private String formatDate(Date data) {
		return data == null ? "*" : DATE_FORMAT.format(data);
	}

	private String formatTime(Date data) {
		return data == null ? "*" : TIME_FORMAT.format(data);
	}

	private Date toDayStart(final Date fromPeriod) {
		if (fromPeriod == null) {
			return null;
		}
		final Calendar fromDate = Calendar.getInstance();
		fromDate.setTime(fromPeriod);
		fromDate.set(Calendar.HOUR_OF_DAY, 0);
		fromDate.set(Calendar.MINUTE, 0);
		fromDate.set(Calendar.SECOND, 0);
		fromDate.set(Calendar.MILLISECOND, 0);
		return fromDate.getTime();
	}

	private Date toDayEnd(final Date toPeriod) {
		if (toPeriod == null) {
			return null;
		}
		final Calendar toDate = Calendar.getInstance();
		toDate.setTime(toPeriod);
		toDate.set(Calendar.HOUR_OF_DAY, 0);
		toDate.set(Calendar.MINUTE, 0);
		toDate.set(Calendar.SECOND, 0);
		toDate.set(Calendar.MILLISECOND, 0);
		toDate.add(Calendar.DATE, 1);
		return toDate.getTime();
	}

	public static RunExpression anytime() {
		return new RunExpression("* *", null, null);
	}

	public boolean isSatifiedBy(final Date date) {
		if (!inEnabledPeriod(date)) {
			return false;
		}

		return aroundStartTime(date);
	}

	protected boolean inEnabledPeriod(final Date date) {
		if (periodStart != null && date.before(periodStart)) {
			LOG.debug("La data " + formatDate(date) + " è prima del periodo "
					+ toString());
			return false;
		}
		if (periodEnd != null && date.after(periodEnd)) {
			LOG.debug("La data " + formatDate(date) + " è dopo il periodo "
					+ toString());
			return false;
		}
		return true;
	}

	protected boolean aroundStartTime(final Date date) {
		if (!aroundStartHour(date)) {
			LOG.debug("Il tempo " + formatTime(date)
					+ " non è nelle ore ammesse " + toString());
			return false;
		}
		if (!aroundStartMinute(date)) {
			LOG.debug("Il tempo " + formatTime(date)
					+ " non è nei minuti ammessi " + toString());
			return false;
		}
		return true;
	}

	protected boolean aroundStartHour(final Date date) {
		final Calendar hourCal = Calendar.getInstance();
		hourCal.setTime(date);
		int hour = hourCal.get(Calendar.HOUR_OF_DAY);

		Set<Integer> hours = getNumbers(startHourExpr, 0, 23);
		LOG.debug("Ore ammesse: " + hours);
		return hours.contains(hour);
	}

	protected boolean aroundStartMinute(final Date date) {
		final Calendar hourCal = Calendar.getInstance();
		hourCal.setTime(date);
		int minute = hourCal.get(Calendar.MINUTE);

		Set<Integer> minutes = getNumbers(startMinuteExpr, 0, 59);
		LOG.debug("Minuti ammessi: " + minutes);
		return minutes.contains(minute);
	}

	protected static Set<Integer> getNumbers(final String cronExpr,
			final int min, final int max) {
		Set<Integer> numbers = new TreeSet<Integer>();
		String[] list = cronExpr.split(",");
		for (String rangePart : list) {
			Pattern rangePattern = Pattern
					.compile("(\\d+|\\*)(?:-(\\d+))?(?:/(\\d+))?");
			Matcher range = rangePattern.matcher(rangePart);
			if (!range.find()) {
				throw new IllegalArgumentException("Range non riconosciuto: ["
						+ rangePart + "]");
			}
			int from = min;
			int to = max;
			int step = 1;

			final String rangeFrom = range.group(1);
			final String rangeTo = range.group(2);
			if (!rangeFrom.equals("*")) {
				from = Integer.parseInt(rangeFrom);
				to = rangeTo == null ? from : Integer.parseInt(rangeTo);
			}
			final String rangeStep = range.group(3);
			if (rangeStep != null) {
				step = Integer.parseInt(rangeStep);
			}

			for (int i = from; i <= to; i += step) {
				numbers.add(i);
			}
		}
		return numbers;
	}

}