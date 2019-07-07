package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.NodeType;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface NodeTypeRepositoryExtension extends RepositoryExtension<NodeType> {
    List<NodeType> doFindAllByIdsApplicationIdAndEnabled(List<String> ids, String applicationId, boolean enabled);

    Page<NodeType> findNodeTypes(Pageable pageable, KronosDocumentSearchParams params);
}
