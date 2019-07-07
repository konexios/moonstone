package com.arrow.selene.engine.service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.arrow.selene.service.ServiceAbstract;

public final class NetworkService extends ServiceAbstract {
    private Map<String, InetSocketAddress> resolvedAddresses = new HashMap<>();
    private Collection<String> unresolvableAddresses = new HashSet<>();

    private static final class SingletonHolder {
        static final NetworkService SINGLETON = new NetworkService();
    }

    public static NetworkService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    private NetworkService() {
    }

    public synchronized InetSocketAddress resolveAddress(String address, int port) {
        String method = "resolveAddress";
        InetSocketAddress inetAddress = resolvedAddresses.get(address);
        if (inetAddress == null) {
            try {
                if(!unresolvableAddresses.contains(address)) {
                    inetAddress = new InetSocketAddress(InetAddress.getByName(address), port);
                }
                resolvedAddresses.put(address, inetAddress);
            } catch (UnknownHostException e) {
                unresolvableAddresses.add(address);
                logError(method, "cannot resolve address", e);
            }
        }
        return inetAddress;
    }

    public synchronized void cleanAddresses() {
        resolvedAddresses.clear();
        unresolvableAddresses.clear();
    }
}
