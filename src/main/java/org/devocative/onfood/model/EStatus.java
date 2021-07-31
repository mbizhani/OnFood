package org.devocative.onfood.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Getter
@RequiredArgsConstructor
public enum EStatus {
	Closed(0),
	Open(1),
	Opening(2);

	// ------------------------------

	private final Integer id;

	// ------------------------------

	@Converter(autoApply = true)
	public static class EStatusConverter implements AttributeConverter<EStatus, Integer> {
		@Override
		public Integer convertToDatabaseColumn(EStatus literal) {
			return literal != null ? literal.getId() : null;
		}

		@Override
		public EStatus convertToEntityAttribute(Integer integer) {
			for (EStatus literal : EStatus.values()) {
				if (literal.getId().equals(integer)) {
					return literal;
				}
			}
			return null;
		}
	}
}
