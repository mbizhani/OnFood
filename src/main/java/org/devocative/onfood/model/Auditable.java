package org.devocative.onfood.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class Auditable {
	@CreatedDate
	@Column(name = "d_created_date", nullable = false, updatable = false)
	private Date createdDate;

	@CreatedBy
	@Column(name = "c_created_by", nullable = false, updatable = false)
	private String createdBy;

	@LastModifiedDate
	@Column(name = "d_last_modified_date")
	private Date lastModifiedDate;

	@LastModifiedBy
	@Column(name = "c_last_modified_by")
	private String lastModifiedBy;

	@Version
	@Column(name = "n_version", nullable = false)
	private Integer version;
}
