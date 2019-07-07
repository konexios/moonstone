package com.arrow.pegasus.webapi.data;

import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class CoreUserModels {

    public static class UserOption extends CoreDefinitionModelOption {
        private static final long serialVersionUID = 6874319250294121253L;

        public UserOption(User user) {
            super(user.getId(), user.getHid(), user.getContact().fullName());
        }
    }

    public static class UserList extends CoreDocumentModel {
        private static final long serialVersionUID = 6846624439553260689L;

        private String fullName;
        private String login;
        private String companyName;
        private UserStatus status;

        public UserList(User user, Company company) {
            super(user.getId(), user.getHid());
            this.fullName = user.getContact().fullName();
            this.login = user.getLogin();
            this.companyName = company != null ? company.getName() : "UNKNOWN (" + user.getCompanyId() + ")";
            this.status = user.getStatus();
        }

        public String getFullName() {
            return fullName;
        }

        public String getLogin() {
            return login;
        }

        public String getCompanyName() {
            return companyName;
        }

        public UserStatus getStatus() {
            return status;
        }
    }
}
