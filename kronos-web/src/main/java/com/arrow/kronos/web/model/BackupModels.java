package com.arrow.kronos.web.model;

import java.time.Instant;

import com.arrow.kronos.data.ConfigBackup;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;

import moonstone.acn.client.model.CreateConfigBackupModel;


public class BackupModels {

    public static class BackupOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -7981967979043922987L;

		public BackupOption(ConfigBackup configBackup) {
            super(configBackup.getId(), configBackup.getHid(), configBackup.getName());
        }

    }

    public static class BackupModel extends BackupOption {
		private static final long serialVersionUID = -4547222479055297795L;
		
		private String type;
        private String createBy;
        private Instant createdDate;

        public BackupModel(ConfigBackup configBackup) {
            super(configBackup);

            this.type = configBackup.getType().toString();
            this.createBy = configBackup.getCreatedBy();
            this.createdDate = configBackup.getCreatedDate();
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public Instant getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Instant createdDate) {
            this.createdDate = createdDate;
        }
    }

	public static class BulkConfigBackupModel extends CreateConfigBackupModel {
		private static final long serialVersionUID = 6105683892073354941L;

		private String[] ids;

		public String[] getIds() {
			return ids;
		}

		public void setIds(String[] ids) {
			this.ids = ids;
		}
	}
}
