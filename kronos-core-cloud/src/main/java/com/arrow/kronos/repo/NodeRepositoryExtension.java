package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.Node;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface NodeRepositoryExtension extends RepositoryExtension<Node> {
    List<Node> doFindAllByIdsApplicationIdAndEnabled(List<String> ids, String applicationId, boolean enabled);

    Page<Node> findNodes(Pageable pageable, NodeSearchParams params);

    long findNodeCount(NodeSearchParams params);
}
