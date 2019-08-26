package moonstone.selene.data;

import java.io.Serializable;

public abstract class EntityAbstract implements Serializable {
	private static final long serialVersionUID = -6154205663170294792L;

	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
