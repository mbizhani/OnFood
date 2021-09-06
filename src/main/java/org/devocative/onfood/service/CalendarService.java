package org.devocative.onfood.service;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import org.devocative.onfood.dto.CalendarDTO;
import org.devocative.onfood.iservice.ICalendarService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CalendarService implements ICalendarService {
	private static final ULocale PERSIAN_LOCALE = new ULocale("@calendar=persian");
	private static CalendarService INSTANCE;

	public static CalendarService get() {
		return INSTANCE;
	}

	// ------------------------------

	public CalendarService() {
		INSTANCE = this;
	}

	// ------------------------------

	@Override
	public CalendarDTO.DateTimeDTO toDateTimeDTO(Date date) {
		final Calendar c = createCalendar();
		c.setTime(date);

		return new CalendarDTO.DateTimeDTO(
			c.get(Calendar.YEAR),
			c.get(Calendar.MONTH) + 1,
			c.get(Calendar.DAY_OF_MONTH),
			c.get(Calendar.HOUR_OF_DAY),
			c.get(Calendar.MINUTE),
			c.get(Calendar.SECOND));
	}

	@Override
	public Date toDate(CalendarDTO.DateTimeDTO dto) {
		final Calendar c = createCalendar();
		c.set(Calendar.YEAR, dto.getYear());
		c.set(Calendar.MONTH, dto.getMonth() - 1);
		c.set(Calendar.DAY_OF_MONTH, dto.getDay());
		c.set(Calendar.HOUR_OF_DAY, dto.getHour());
		c.set(Calendar.MINUTE, dto.getMinute());
		c.set(Calendar.SECOND, dto.getSecond());
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	@Override
	public CalendarDTO.DateDTO toDateDTO(Date date) {
		final Calendar c = createCalendar();
		c.setTime(date);

		return new CalendarDTO.DateDTO(
			c.get(Calendar.YEAR),
			c.get(Calendar.MONTH) + 1,
			c.get(Calendar.DAY_OF_MONTH));
	}

	@Override
	public Date toDate(CalendarDTO.DateDTO dto) {
		final Calendar c = createCalendar();
		c.set(Calendar.YEAR, dto.getYear());
		c.set(Calendar.MONTH, dto.getMonth() - 1);
		c.set(Calendar.DAY_OF_MONTH, dto.getDay());
		c.set(Calendar.HOUR_OF_DAY, 12);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	// ------------------------------

	private Calendar createCalendar() {
		//new ULocale("en_US@calendar=gregorian");
		return Calendar.getInstance(TimeZone.getDefault(), PERSIAN_LOCALE);
	}

}
