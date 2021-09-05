package org.devocative.onfood.iservice;

import org.devocative.onfood.dto.CalendarDTO;
import org.devocative.onfood.service.CalendarService;

import java.util.Date;

public interface IBaseBeanMapper {

	default ICalendarService getCalendarService() {
		return CalendarService.get();
	}

	default CalendarDTO.DateTimeDTO toDateTimeDto(Date date) {
		return getCalendarService().toDateTimeDTO(date);
	}

	default Date toDate(CalendarDTO.DateTimeDTO dto) {
		return getCalendarService().toDate(dto);
	}

	default CalendarDTO.DateDTO toDateDto(Date date) {
		return getCalendarService().toDateDTO(date);
	}

	default Date toDate(CalendarDTO.DateDTO dto) {
		return getCalendarService().toDate(dto);
	}

}
