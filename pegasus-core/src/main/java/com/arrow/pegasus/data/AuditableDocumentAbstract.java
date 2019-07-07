package com.arrow.pegasus.data;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.arrow.pegasus.CoreConstant;

public abstract class AuditableDocumentAbstract extends TsDocumentAbstract {
	private static final long serialVersionUID = 9117749998377843189L;

	@NotNull
	private Instant lastModifiedDate;
	@NotBlank
	private String lastModifiedBy;

	@Override
	public void defaultAudit(String who) {
		super.defaultAudit(who);
		if (StringUtils.isEmpty(who)) {
			who = CoreConstant.ADMIN_USER;
		}
		lastModifiedDate = Instant.now();
		lastModifiedBy = who;
	}

	public Instant getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}
