package com.arrow.pegasus.webapi.data;

import java.io.Serializable;

import org.springframework.data.domain.Sort.Direction;

public class CoreSearchFilterModel implements Serializable {

	private static final long serialVersionUID = 3089169912780550764L;

	public static final int DEFAULT_PAGE_INDEX = 0;
	public static final int DEFAULT_ITEMS_PER_PAGE = 10;

	protected Integer pageIndex;
	protected Integer itemsPerPage;
	protected String sortField;
	protected String sortDirection;

	public CoreSearchFilterModel() {
		pageIndex = DEFAULT_PAGE_INDEX;
		itemsPerPage = DEFAULT_ITEMS_PER_PAGE;
		sortField = "id";
		sortDirection = Direction.ASC.name();
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
}
