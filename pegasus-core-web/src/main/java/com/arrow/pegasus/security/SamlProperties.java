package com.arrow.pegasus.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.arrow.pegasus.saml")
public class SamlProperties implements Serializable {
    private static final long serialVersionUID = -1024699983233232945L;

    private String spEntityId;
    private List<String> idpMetadataUrls = new ArrayList<>();
    private String jksStore;
    private String jksStorePassword;
    private List<String> jksPasswords = new ArrayList<>();
    private String jksDefaultKey;

    public String getSpEntityId() {
        return spEntityId;
    }

    public void setSpEntityId(String spEntityId) {
        this.spEntityId = spEntityId;
    }

    public List<String> getIdpMetadataUrls() {
        return idpMetadataUrls;
    }

    public void setIdpMetadataUrls(List<String> idpMetadataUrls) {
        this.idpMetadataUrls = idpMetadataUrls;
    }

    public String getJksStore() {
        return jksStore;
    }

    public void setJksStore(String jksStore) {
        this.jksStore = jksStore;
    }

    public String getJksStorePassword() {
        return jksStorePassword;
    }

    public void setJksStorePassword(String jksStorePassword) {
        this.jksStorePassword = jksStorePassword;
    }

    public List<String> getJksPasswords() {
        return jksPasswords;
    }

    public void setJksPasswords(List<String> jksPasswords) {
        this.jksPasswords = jksPasswords;
    }

    public String getJksDefaultKey() {
        return jksDefaultKey;
    }

    public void setJksDefaultKey(String jksDefaultKey) {
        this.jksDefaultKey = jksDefaultKey;
    }
}
