package com.arrow.pegasus.data;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.arrow.pegasus.CoreConstant;

public class PriInfo implements Serializable {
    private static final long serialVersionUID = 4738037866443293964L;

    private String root;
    private String product;
    private String type;
    private String id;

    public PriInfo() {
    }

    public PriInfo(String root, String product, String type, String id) {
        super();
        this.root = root;
        this.product = product;
        this.type = type;
        this.id = id;
    }

    public static PriInfo parse(String pri) {
        String[] tokens = pri.split(":", -1);
        if (tokens.length == 4) {
            return new PriInfo(tokens[0], tokens[1], tokens[2], tokens[3]);
        } else {
            return null;
        }
    }

    public boolean isOfType(DocumentAbstract document) {
        boolean result = false;
        if (document != null) {
            result = StringUtils.equals(root, CoreConstant.ROOT_PRI);
            result &= StringUtils.equals(product, document.getProductPri());
            result &= StringUtils.equals(type, document.getTypePri());
        }
        return result;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
