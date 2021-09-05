package org.devocative.onfood.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public abstract class CalendarDTO {
	@NotNull
	protected Integer year;
	@NotNull
	protected Integer month;
	@NotNull
	protected Integer day;

	// ------------------------------

	public CalendarDTO() {
	}

	public CalendarDTO(Integer year, Integer month, Integer day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	// ------------------------------

	@Getter
	@Setter
	public static class DateDTO extends CalendarDTO {
		public DateDTO() {
		}

		public DateDTO(Integer year, Integer month, Integer day) {
			super(year, month, day);
		}

		@Override
		public String toString() {
			return String.format("%02d/%02d/%02d", year, month, day);
		}
	}

	// ------------------------------

	@Getter
	@Setter
	public static class DateTimeDTO extends CalendarDTO {
		@NotNull
		private Integer hour;
		@NotNull
		private Integer minute;
		@NotNull
		private Integer second;

		public DateTimeDTO() {
		}

		public DateTimeDTO(Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second) {
			super(year, month, day);
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		}

		@Override
		public String toString() {
			return String.format("%02d/%02d/%02d - %02d:%02d:%02d", year, month, day, hour, minute, second);
		}
	}
}
