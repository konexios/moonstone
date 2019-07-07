package com.arrow.pegasus.dashboard.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class BoardRepositoryImpl extends RepositoryExtensionAbstract<Board> implements BoardRepositoryExtension {

	public BoardRepositoryImpl() {
		super(Board.class);
	}

	@Override
	public Page<Board> findBoards(Pageable pageable, BoardSearchParams params) {
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<Board> findBoards(BoardSearchParams params) {
		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(BoardSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "_id", params.getIds());
			criteria = addCriteria(criteria, "hid", params.getHids());
			criteria = addCriteria(criteria, "productId", params.getProductIds());
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "userId", params.getUserIds());
			criteria = addCriteria(criteria, "category", params.getCategories());
		}
		return criteria;
	}
}
