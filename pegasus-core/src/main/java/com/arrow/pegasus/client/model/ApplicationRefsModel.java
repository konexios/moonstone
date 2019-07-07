package com.arrow.pegasus.client.model;

import java.io.Serializable;
import java.util.List;

import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.Zone;

public class ApplicationRefsModel implements Serializable {

	private static final long serialVersionUID = 1968609834237705598L;

	private Application application;
	private Zone refZone;
	private Company refCompany;
	private Product refProduct;
	private Subscription refSubscription;
	private ApplicationEngine refApplicationEngine;
	private List<Product> refProductExtensions;

	public ApplicationRefsModel withApplication(Application application) {
		setApplication(application);
		if (application != null) {
			setRefZone(application.getRefZone());
			setRefCompany(application.getRefCompany());
			setRefProduct(application.getRefProduct());
			setRefSubscription(application.getRefSubscription());
			setRefApplicationEngine(application.getRefApplicationEngine());
			setRefProductExtensions(application.getRefProductExtensions());
		}
		return this;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public Zone getRefZone() {
		return refZone;
	}

	public void setRefZone(Zone refZone) {
		this.refZone = refZone;
	}

	public Company getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(Company refCompany) {
		this.refCompany = refCompany;
	}

	public Product getRefProduct() {
		return refProduct;
	}

	public void setRefProduct(Product refProduct) {
		this.refProduct = refProduct;
	}

	public Subscription getRefSubscription() {
		return refSubscription;
	}

	public void setRefSubscription(Subscription refSubscription) {
		this.refSubscription = refSubscription;
	}

	public ApplicationEngine getRefApplicationEngine() {
		return refApplicationEngine;
	}

	public void setRefApplicationEngine(ApplicationEngine refApplicationEngine) {
		this.refApplicationEngine = refApplicationEngine;
	}

	public List<Product> getRefProductExtensions() {
		return refProductExtensions;
	}

	public void setRefProductExtensions(List<Product> refProductExtensions) {
		this.refProductExtensions = refProductExtensions;
	}

}
