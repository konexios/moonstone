package com.arrow.kronos.repo;

import java.util.List;

import com.arrow.kronos.data.IbmGateway;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface IbmGatewayRepositoryExtension extends RepositoryExtension<IbmGateway> {
    List<IbmGateway> findIbmGateways(IbmGatewaySearchParams params);
}
