package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.arrow.pegasus.LifeCycleAbstract;

public class EsRepositoryExtensionAbstract<T> extends LifeCycleAbstract {

    private static final int DEFAULT_SCROLL_SIZE = 1000;
    private static final long DEFAULT_SCROLL_TIMEOUT = 60000;

    private final Class<T> documentClass;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public EsRepositoryExtensionAbstract(Class<T> documentClass) {
        this.documentClass = documentClass;
    }

    public ElasticsearchTemplate getElasticsearchTemplate() {
        return elasticsearchTemplate;
    }

    protected Class<T> getDocumentClass() {
        return documentClass;
    }

    protected List<T> doSearch(SearchQuery query) {
        String method = "doSearch";
        List<T> items = elasticsearchTemplate.queryForList(query, documentClass);
        logDebug(method, "found items: %s", items.size());
        return items;
    }

    protected List<T> doSearch(CriteriaQuery query) {
        String method = "doSearch";
        List<T> items = elasticsearchTemplate.queryForList(query, documentClass);
        logDebug(method, "found items: %s", items.size());
        return items;
    }

    protected long doCount(SearchQuery query, Class<T> clazz) {
        return elasticsearchTemplate.count(query, clazz);
    }

    protected Aggregations doAggregation(SearchQuery query) {
        String method = "doAggregation";
        logDebug(method, "...");

        return elasticsearchTemplate.query(query, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
    }

    protected List<T> doScroll(SearchQuery query) {
        String method = "doScroll";
        logDebug(method, "...");

        query.setPageable(PageRequest.of(0, DEFAULT_SCROLL_SIZE));
        List<T> items = new ArrayList<>();
        ScrolledPage<T> scroll = (ScrolledPage<T>) elasticsearchTemplate.startScroll(DEFAULT_SCROLL_TIMEOUT, query,
                getDocumentClass());
        while (scroll.hasContent()) {
            items.addAll(scroll.getContent());
            scroll = (ScrolledPage<T>) elasticsearchTemplate.continueScroll(scroll.getScrollId(),
                    DEFAULT_SCROLL_TIMEOUT, getDocumentClass());
        }
        logDebug(method, "found items: %s", items.size());
        elasticsearchTemplate.clearScroll(scroll.getScrollId());
        return items;
    }

    protected void doDelete(DeleteQuery query, Class<T> clazz) {
        String method = "doDelete";
        logDebug(method, "...");
        elasticsearchTemplate.delete(query, clazz);
        elasticsearchTemplate.refresh(clazz);
    }
}
