package org.devocative.onfood.iservice;

import org.devocative.onfood.dto.CalendarDTO;
import org.devocative.onfood.model.AuditedUser;
import org.devocative.onfood.service.CalendarService;

import java.util.Date;

public interface IBaseBeanMapper {

	default ICalendarService getCalendarService() {
		return CalendarService.get();
	}

	default CalendarDTO.DateTimeDTO toDateTimeDto(Date date) {
		return date != null ? getCalendarService().toDateTimeDTO(date) : null;
	}

	default Date toDate(CalendarDTO.DateTimeDTO dto) {
		return dto != null ? getCalendarService().toDate(dto) : null;
	}

	default CalendarDTO.DateDTO toDateDto(Date date) {
		return date != null ? getCalendarService().toDateDTO(date) : null;
	}

	default Date toDate(CalendarDTO.DateDTO dto) {
		return dto != null ? getCalendarService().toDate(dto) : null;
	}

	default String fromAuditedUser(AuditedUser user) {
		return user != null ? user.getUsername() : null;
	}
}
