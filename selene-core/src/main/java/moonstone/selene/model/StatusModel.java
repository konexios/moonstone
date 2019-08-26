package moonstone.selene.model;

public class StatusModel extends moonstone.acs.client.model.StatusModel {
	private static final long serialVersionUID = 1665418958606322751L;
	public static final StatusModel OK = (StatusModel) new StatusModel().withStatus("OK").withMessage("").withName("");
	private String id;
	private String name;

	public StatusModel() {
	}

	public StatusModel withId(String id) {
		setId(id);
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StatusModel withMessage(String message) {
		setMessage(message);
		return this;
	}

	public StatusModel withStatus(String status) {
		setStatus(status);
		return this;
	}

	public StatusModel withName(String name) {
		setName(name);
		return this;
	}
}
