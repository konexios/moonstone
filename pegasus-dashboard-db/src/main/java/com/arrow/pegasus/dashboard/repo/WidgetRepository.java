package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.dashboard.data.Widget;

@Repository
public interface WidgetRepository extends MongoRepository<Widget, String>, WidgetRepositoryExtension {

    public List<Widget> findByParentId(String boardId);

    public Widget findByWidgetTypeId(String id);

    public Widget findByParentIdAndWidgetTypeId(String boardId, String widgetTypeId);

}
