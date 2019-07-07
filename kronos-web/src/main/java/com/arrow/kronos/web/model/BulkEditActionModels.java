package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arrow.kronos.data.Node;
import com.arrow.pegasus.data.profile.User;

/**
 * Created by dantonov on 18.10.2017.
 */
public class BulkEditActionModels {

    public static class BulkEditFieldValue implements Serializable{

        private static final long serialVersionUID = -8244861468939666266L;
        private String name;
        private String id;

        public BulkEditFieldValue() {
        }

        public BulkEditFieldValue(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class BulkEditField implements Serializable{

        private static final long serialVersionUID = -493607960512674923L;
        private String name;
        private boolean edit;
        private boolean canBeNull;
        private List<BulkEditFieldValue> values;
        private String choice;

        public BulkEditField() {
        }

        public BulkEditField(String name, boolean canBeNull) {
            this.name = name;
            this.canBeNull = canBeNull;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isEdit() {
            return edit;
        }

        public void setEdit(boolean edit) {
            this.edit = edit;
        }

        public boolean isCanBeNull() {
            return canBeNull;
        }

        public void setCanBeNull(boolean canBeNull) {
            this.canBeNull = canBeNull;
        }

        public List<BulkEditFieldValue> getValues() {
            return values;
        }

        public void setValues(List<BulkEditFieldValue> values) {
            this.values = values;
        }

        public String getChoice() {
            return choice;
        }

        public void setChoice(String choice) {
            this.choice = choice;
        }

        public boolean toClear() {
            return canBeNull && getChoice()==null;
        }
    }


    public static class GroupBulkEditField extends BulkEditField {
        private static final long serialVersionUID = 6189056593238051084L;

        public static final String FIELD_NAME = "Group";

        public GroupBulkEditField() {
            super(FIELD_NAME, true);
        }

        public static GroupBulkEditField build(List<Node> nodes) {
            GroupBulkEditField groupBulkEditField = new GroupBulkEditField();
            Map<String, Node> nodesMap = new HashMap<>();
            for (Node node : nodes) {
                nodesMap.put(node.getId(), node);
            }
            List<BulkEditFieldValue> values = new ArrayList<>(nodes.size());
            for (Node node : nodes) {
                values.add(new BulkEditFieldValue(NodeModels.NodeOption.getNodeFullName(node, nodesMap),node.getId()));
            }
            groupBulkEditField.setValues(values);
            return groupBulkEditField;
        }
    }

    public static class OwnerBulkEditField extends BulkEditField {
        private static final long serialVersionUID = -3370579751609645738L;

        public OwnerBulkEditField() {
            super(FIELD_NAME, true);
        }

        public static final String FIELD_NAME = "Owner";

        public static OwnerBulkEditField build(List<User> users) {
            OwnerBulkEditField ownerBulkEditField = new OwnerBulkEditField();
            List<BulkEditFieldValue> values = new ArrayList<>(users.size());
            for (User user : users) {
                values.add(new BulkEditFieldValue(user.getContact().fullName(),user.getId()));
            }
            ownerBulkEditField.setValues(values);
            return ownerBulkEditField;
        }
    }

    public static class EnabledBulkEditField extends BulkEditField {
        private static final long serialVersionUID = 6367536364229239636L;

        public static final String FIELD_NAME = "Enabled";

        public EnabledBulkEditField() {
            super(FIELD_NAME, false);
        }

        public static EnabledBulkEditField build() {
            EnabledBulkEditField enabledBulkEditField = new EnabledBulkEditField();
            List<BulkEditFieldValue> values = new ArrayList<>(2);
            values.add(new BulkEditFieldValue("true","True"));
            values.add(new BulkEditFieldValue("false","False"));
            enabledBulkEditField.setValues(values);
            enabledBulkEditField.setChoice("True");
            return enabledBulkEditField;
        }
    }

    public static class BulkEditModel implements Serializable {
        private static final long serialVersionUID = 6105683892073354941L;

        private String[] ids;

        public String[] getIds() {
            return ids;
        }

        public void setIds(String[] ids) {
            this.ids = ids;
        }
    }

    public static class AssetsBulkEditModel implements Serializable{
        private static final long serialVersionUID = -2868118601422980302L;

        private GroupBulkEditField groupBulkEditField;
        private OwnerBulkEditField ownerBulkEditField;
        private EnabledBulkEditField enabledBulkEditField;

        public GroupBulkEditField getGroupBulkEditField() {
            return groupBulkEditField;
        }

        public void setGroupBulkEditField(GroupBulkEditField groupBulkEditField) {
            this.groupBulkEditField = groupBulkEditField;
        }

        public OwnerBulkEditField getOwnerBulkEditField() {
            return ownerBulkEditField;
        }

        public void setOwnerBulkEditField(OwnerBulkEditField ownerBulkEditField) {
            this.ownerBulkEditField = ownerBulkEditField;
        }

        public EnabledBulkEditField getEnabledBulkEditField() {
            return enabledBulkEditField;
        }

        public void setEnabledBulkEditField(EnabledBulkEditField enabledBulkEditField) {
            this.enabledBulkEditField = enabledBulkEditField;
        }
    }

    public static class GatewayBulkEdit extends BulkEditModel {
        private static final long serialVersionUID = -196972448749024675L;

        private AssetsBulkEditModel editModel;

        public AssetsBulkEditModel getEditModel() {
            return editModel;
        }

        public void setEditModel(AssetsBulkEditModel editModel) {
            this.editModel = editModel;
        }
    }

    public static class DeviceBulkEdit extends BulkEditModel {
        private static final long serialVersionUID = -5424840261198516215L;

        private AssetsBulkEditModel editModel;

        public AssetsBulkEditModel getEditModel() {
            return editModel;
        }

        public void setEditModel(AssetsBulkEditModel editModel) {
            this.editModel = editModel;
        }
    }

}
