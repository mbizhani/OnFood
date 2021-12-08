package org.devocative.onfood.service;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import org.devocative.onfood.dto.CalendarDTO;
import org.devocative.onfood.iservice.ICalendarService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
	public CalendarDTO.DateTimeDTO toDateTimeDTO(Instant instant) {
		return toDateTimeDTO(Date.from(instant));
	}

	@Override
	public CalendarDTO.DateTimeDTO toDateTimeDTO(LocalDateTime localDateTime) {
		return toDateTimeDTO(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
	}

	// ---------------

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
	public LocalDateTime toLocalDateTime(CalendarDTO.DateTimeDTO dto) {
		final Date date = toDate(dto);
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	@Override
	public Instant toInstant(CalendarDTO.DateTimeDTO dto) {
		throw new RuntimeException("Invalid Conversion!");
	}

	// ---------------

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
	public CalendarDTO.DateDTO toDateDTO(LocalDate date) {
		return toDateDTO(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

	@Override
	public CalendarDTO.DateDTO toDateDTO(Instant instant) {
		throw new RuntimeException("Invalid Conversion");
	}

	// ---------------

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

	@Override
	public LocalDate toLocalDate(CalendarDTO.DateDTO dto) {
		return LocalDate.ofInstant(toDate(dto).toInstant(), ZoneId.systemDefault());
	}

	@Override
	public Instant toInstant(CalendarDTO.DateDTO dto) {
		throw new RuntimeException("Invalid Conversion");
	}

	// ------------------------------

	private Calendar createCalendar() {
		//new ULocale("en_US@calendar=gregorian");
		return Calendar.getInstance(TimeZone.getDefault(), PERSIAN_LOCALE);
	}

}
