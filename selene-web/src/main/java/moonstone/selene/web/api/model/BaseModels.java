package moonstone.selene.web.api.model;

import java.io.Serializable;
import java.time.Instant;

public class BaseModels {

	@SuppressWarnings("rawtypes")
	public abstract static class EnumOption<T extends Enum> implements Serializable {
		private static final long serialVersionUID = 6760890369994060824L;

		private String id;
		private String name;

		public EnumOption(T object) {
			this.id = object.name();
			this.name = object.name();
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	public abstract static class EntityAbstract implements Serializable {
		private static final long serialVersionUID = -5367191227620075524L;

		private long id;

		protected EntityAbstract(long id) {
			this.id = id;
		}

		public long getId() {
			return id;
		}
	}

	public abstract static class BaseEntity extends BaseModels.EntityAbstract {
		private static final long serialVersionUID = 2559406935248292755L;

		private boolean enabled;
		private Instant createdDate;
		private Instant lastModifiedDate;

		protected BaseEntity(long id, boolean enabled, Instant createdDate, Instant lastModifiedDate) {
			super(id);
			this.enabled = enabled;
			this.createdDate = createdDate;
			this.lastModifiedDate = lastModifiedDate;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public Instant getCreatedDate() {
			return createdDate;
		}

		public Instant getLastModifiedDate() {
			return lastModifiedDate;
		}
	}

}
