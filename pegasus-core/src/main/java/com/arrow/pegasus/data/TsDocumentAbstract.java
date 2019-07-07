package com.arrow.pegasus.data;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.arrow.pegasus.CoreConstant;

public abstract class TsDocumentAbstract extends DocumentAbstract implements Auditable {
	private static final long serialVersionUID = 8710614860653787905L;

	@NotNull
	private Instant createdDate;
	@NotBlank
	private String createdBy;

	@Override
	public void defaultAudit(String who) {
		if (StringUtils.isEmpty(who)) {
			who = CoreConstant.ADMIN_USER;
		}
		if (createdDate == null) {
			createdDate = Instant.now();
		}
		if (StringUtils.isEmpty(createdBy)) {
			createdBy = who;
		}
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}
}
