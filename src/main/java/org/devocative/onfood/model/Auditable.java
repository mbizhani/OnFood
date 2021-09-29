package org.devocative.onfood.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class Auditable {
	@CreatedDate
	@Column(name = "d_created_date", nullable = false, updatable = false)
	private Date createdDate;

	@CreatedBy
	@AttributeOverrides({
		@AttributeOverride(
			name = "userId",
			column = @Column(name = "f_created_by_user_id", updatable = false, nullable = false)),
		@AttributeOverride(
			name = "username",
			column = @Column(name = "c_created_by_username", updatable = false, nullable = false))
	})
	private AuditedUser createdBy;

	@LastModifiedDate
	@Column(name = "d_last_modified_date", insertable = false)
	private Date lastModifiedDate;

	@LastModifiedBy
	@AttributeOverrides({
		@AttributeOverride(
			name = "userId",
			column = @Column(name = "f_last_modified_by_user_id", insertable = false)),
		@AttributeOverride(
			name = "username",
			column = @Column(name = "c_last_modified_by_username", insertable = false))
	})
	private AuditedUser lastModifiedBy;

	@Version
	@Column(name = "n_version", nullable = false)
	private Integer version;
}
