package com.arrow.pegasus.dashboard.data;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Definition of Board Created by dantonov on 26.09.2017.
 */
@Document(collection = "pegasus_dd_board")

public class Board extends ContainerAbstract {

	private static final long serialVersionUID = 7896308522837468860L;

	private String productId;
	private String applicationId;
	private String userId;
	private String category;

	@Transient
	@JsonIgnore
	private Product refProduct;
	@Transient
	@JsonIgnore
	private Application refApplication;
	@Transient
	@JsonIgnore
	private User refUser;

	public Board() {
	}

	public String getDescription() {
		return super.getDescription();
	}

	public void setDescription(String description) {
		super.setDescription(description);
	}

	public String getName() {
		return super.getName();
	}

	public void setName(String name) {
		super.setName(name);
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Product getRefProduct() {
		return refProduct;
	}

	public void setRefProduct(Product refProduct) {
		this.refProduct = refProduct;
	}

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public User getRefUser() {
		return refUser;
	}

	public void setRefUser(User refUser) {
		this.refUser = refUser;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.DYNAMIC_DASHBOARD_BOARD;
	}
}