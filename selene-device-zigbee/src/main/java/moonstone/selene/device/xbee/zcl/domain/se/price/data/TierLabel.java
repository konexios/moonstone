package moonstone.selene.device.xbee.zcl.domain.se.price.data;

public class TierLabel {
	private int tierId;
	private String tierLabel;

	public int getTierId() {
		return tierId;
	}

	public TierLabel withTierId(int tierId) {
		this.tierId = tierId;
		return this;
	}

	public String getTierLabel() {
		return tierLabel;
	}

	public TierLabel withTierLabel(String tierLabel) {
		this.tierLabel = tierLabel;
		return this;
	}
}
