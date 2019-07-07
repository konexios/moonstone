package com.arrow.pegasus.webapi.data;

import java.io.Serializable;

import org.springframework.data.domain.Page;

public class CoreSearchResultModel<R extends CoreDocumentModel, F extends CoreSearchFilterModel> implements Serializable {

    private static final long serialVersionUID = -4077518346319274910L;

    private Page<R> result;
    private F filter;

    public CoreSearchResultModel(Page<R> result, F filter) {
        this.result = result;
        this.filter = filter;
    }

    public Page<R> getResult() {
        return result;
    }

    public F getFilter() {
        return filter;
    }
}
