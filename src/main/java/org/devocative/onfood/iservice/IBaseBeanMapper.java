package org.devocative.onfood.iservice;

import org.devocative.onfood.dto.CalendarDTO;
import org.devocative.onfood.model.AuditedUser;
import org.devocative.onfood.service.CalendarService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface IBaseBeanMapper {

	default ICalendarService getCalendarService() {
		return CalendarService.get();
	}

	// -----

	default CalendarDTO.DateTimeDTO toDateTimeDto(Date date) {
		return date != null ? getCalendarService().toDateTimeDTO(date) : null;
	}

	default CalendarDTO.DateTimeDTO toDateTimeDTO(Instant instant) {
		return instant != null ? getCalendarService().toDateTimeDTO(instant) : null;
	}

	default CalendarDTO.DateTimeDTO toDateTimeDTO(LocalDateTime localDateTime) {
		return localDateTime != null ? getCalendarService().toDateTimeDTO(localDateTime) : null;
	}

	// -----

	default Date toDate(CalendarDTO.DateTimeDTO dto) {
		return dto != null ? getCalendarService().toDate(dto) : null;
	}

	default LocalDateTime toLocalDateTime(CalendarDTO.DateTimeDTO dto) {
		return dto != null ? getCalendarService().toLocalDateTime(dto) : null;
	}

	default Instant toInstant(CalendarDTO.DateTimeDTO dto) {
		return dto != null ? getCalendarService().toInstant(dto) : null;
	}

	// -----

	default CalendarDTO.DateDTO toDateDto(Date date) {
		return date != null ? getCalendarService().toDateDTO(date) : null;
	}

	default CalendarDTO.DateDTO toDateDTO(LocalDate date) {
		return date != null ? getCalendarService().toDateDTO(date) : null;
	}

	default CalendarDTO.DateDTO toDateDTO(Instant instant) {
		return instant != null ? getCalendarService().toDateDTO(instant) : null;
	}

	// -----

	default Date toDate(CalendarDTO.DateDTO dto) {
		return dto != null ? getCalendarService().toDate(dto) : null;
	}

	default LocalDate toLocalDate(CalendarDTO.DateDTO dto) {
		return dto != null ? getCalendarService().toLocalDate(dto) : null;
	}

	default Instant toInstant(CalendarDTO.DateDTO dto) {
		return dto != null ? getCalendarService().toInstant(dto) : null;
	}

	// -----

	default String fromAuditedUser(AuditedUser user) {
		return user != null ? user.getUsername() : null;
	}
}
