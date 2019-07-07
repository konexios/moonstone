package com.arrow.pegasus.repo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.arrow.pegasus.LifeCycleAbstract;
import com.arrow.pegasus.data.Auditable;
import com.arrow.pegasus.data.DocumentAbstract;
import com.arrow.pegasus.security.Crypto;

public abstract class RepositoryExtensionAbstract<T extends DocumentAbstract> extends LifeCycleAbstract
		implements RepositoryExtension<T> {

	private final Class<T> documentClass;

	@Autowired
	private MongoOperations operations;
	@Autowired
	private Crypto crypto;

	public RepositoryExtensionAbstract(Class<T> documentClass) {
		this.documentClass = documentClass;
	}

	public T doFindByHid(String hid) {
		return operations.findOne(new Query(Criteria.where("hid").is(hid)), documentClass);
	}

	public T doInsert(T document, String who) {
		Assert.notNull(document, "document is null");
		if (Auditable.class.isAssignableFrom(document.getClass())) {
			((Auditable) document).defaultAudit(who);
		}
		document.setHid(crypto.hashId(crypto.randomToken()));
		operations.insert(document);
		return document;
	}

	public void doInsert(Collection<T> documents, String who) {
		Assert.notNull(documents, "documents is null");
		for (T document : documents) {
			if (Auditable.class.isAssignableFrom(document.getClass())) {
				((Auditable) document).defaultAudit(who);
			}
			document.setHid(crypto.hashId(crypto.randomToken()));
		}
		operations.insert(documents, documentClass);
	}

	public T doSave(T document, String who) {
		Assert.notNull(document, "document is null");
		if (Auditable.class.isAssignableFrom(document.getClass())) {
			((Auditable) document).defaultAudit(who);
		}
		operations.save(document);
		return document;
	}

	protected Query doProcessCriteria(List<Criteria> criteria) {
		return CriteriaUtils.createQuery(criteria);
	}

	protected long doCount(Query query) {
		Assert.notNull(query, "query is null");
		return operations.count(query, documentClass);
	}

	protected List<DistinctCountResult> doDistinctCount(String collectionName, List<Criteria> criteria, String field) {
		List<AggregationOperation> ops = new ArrayList<>();
		for (Criteria c : criteria) {
			ops.add(Aggregation.match(c));
		}
		ops.add(Aggregation.group(field).count().as("count"));
		ops.add(Aggregation.project(field, "count"));
		return operations.aggregate(Aggregation.newAggregation(ops), collectionName, DistinctCountResult.class)
				.getMappedResults();
	}

	protected List<T> doFind(Query query) {
		Assert.notNull(query, "query is null");
		return operations.find(query, documentClass);
	}

	protected Page<T> doProcessQuery(Pageable pageable, List<Criteria> criteria) {
		Assert.notNull(pageable, "pageable is null");
		Assert.notNull(criteria, "criteria is null");

		String methodName = "doProcessQuery";
		Query query = doProcessCriteria(criteria);
		logDebug(methodName, query.toString());
		long total = doCount(query);
		query.with(pageable);
		List<T> result = doFind(query);

		logInfo(methodName, "total: %d, size: %d, limit: %d, skip: %d", total, result.size(), pageable.getPageSize(),
				pageable.getOffset());

		return new PageImpl<T>(result, pageable, total);
	}

	protected List<T> doProcessQuery(List<Criteria> criteria) {
		Assert.notNull(criteria, "criteria is null");

		String methodName = "doProcessQuery";
		logInfo(methodName, "...");
		Query query = doProcessCriteria(criteria);
		logDebug(methodName, query.toString());
		List<T> result = doFind(query);

		logInfo(methodName, "size: %d", result.size());

		return result;
	}

	protected MongoOperations getOperations() {
		return operations;
	}

	protected Class<T> getDocumentClass() {
		return documentClass;
	}

	// CRITERIA HELPERS

	protected List<Criteria> addCriteria(List<Criteria> criteria, String property, Instant fromFilter,
			Instant toFilter) {
		return CriteriaUtils.addCriteria(criteria, property, fromFilter, toFilter);
	}

	protected List<Criteria> addCriteria(List<Criteria> criteria, String property, Long fromFilter, Long toFilter) {
		return CriteriaUtils.addCriteria(criteria, property, fromFilter, toFilter);
	}

	protected List<Criteria> addCriteria(List<Criteria> criteria, String property, Set<String> filters) {
		return CriteriaUtils.addCriteria(criteria, property, filters);
	}

	protected List<Criteria> addNotCriteria(List<Criteria> criteria, String property, Set<String> filters) {
		return CriteriaUtils.addNotCriteria(criteria, property, filters);
	}

	protected List<Criteria> addCriteria(List<Criteria> criteria, String property, EnumSet<?> filters) {
		return CriteriaUtils.addCriteria(criteria, property, filters);
	}

	protected List<Criteria> addCriteria(List<Criteria> criteria, String property, List<String> filters) {
		return CriteriaUtils.addCriteria(criteria, property, filters);
	}

	protected List<Criteria> addNotCriteria(List<Criteria> criteria, String property, List<String> filters) {
		return CriteriaUtils.addNotCriteria(criteria, property, filters);
	}

	protected List<Criteria> addCriteria(List<Criteria> criteria, String property, String[] filters) {
		return CriteriaUtils.addCriteria(criteria, property, filters);
	}

	protected List<Criteria> addNotCriteria(List<Criteria> criteria, String property, String[] filters) {
		return CriteriaUtils.addNotCriteria(criteria, property, filters);
	}

	protected List<Criteria> addCriteria(List<Criteria> criteria, String property, Boolean filter) {
		return CriteriaUtils.addCriteria(criteria, property, filter);
	}

	protected List<Criteria> addCriteria(List<Criteria> criteria, String property, String filter) {
		return CriteriaUtils.addCriteria(criteria, property, filter);
	}

	protected List<Criteria> addNotCriteria(List<Criteria> criteria, String property, String filter) {
		return CriteriaUtils.addNotCriteria(criteria, property, filter);
	}

	protected List<Criteria> addExistsCriteria(List<Criteria> criteria, String property, Boolean exists) {
		return CriteriaUtils.addExistsCriteria(criteria, property, exists);
	}

	protected Crypto getCrypto() {
		return crypto;
	}
}
