package org.devocative.onfood.iservice;

import org.devocative.onfood.dto.CalendarDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface ICalendarService {
	CalendarDTO.DateTimeDTO toDateTimeDTO(Date date);

	CalendarDTO.DateTimeDTO toDateTimeDTO(Instant instant);

	CalendarDTO.DateTimeDTO toDateTimeDTO(LocalDateTime localDateTime);

	// -----

	Date toDate(CalendarDTO.DateTimeDTO dto);

	LocalDateTime toLocalDateTime(CalendarDTO.DateTimeDTO dto);

	Instant toInstant(CalendarDTO.DateTimeDTO dto);

	// -----

	CalendarDTO.DateDTO toDateDTO(Date date);

	CalendarDTO.DateDTO toDateDTO(LocalDate date);

	CalendarDTO.DateDTO toDateDTO(Instant instant);

	// -----

	Date toDate(CalendarDTO.DateDTO dto);

	LocalDate toLocalDate(CalendarDTO.DateDTO dto);

	Instant toInstant(CalendarDTO.DateDTO dto);
}
