package org.devocative.onfood.iservice;

import org.devocative.onfood.dto.CalendarDTO;

import java.util.Date;

public interface ICalendarService {
	CalendarDTO.DateTimeDTO toDateTimeDTO(Date date);

	Date toDate(CalendarDTO.DateTimeDTO dto);

	CalendarDTO.DateDTO toDateDTO(Date date);

	Date toDate(CalendarDTO.DateDTO dto);
}
