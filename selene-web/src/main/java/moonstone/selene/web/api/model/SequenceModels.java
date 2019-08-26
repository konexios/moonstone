package moonstone.selene.web.api.model;

import java.io.Serializable;

import moonstone.selene.data.Sequence;

public class SequenceModels {

	public static class SequenceModel extends BaseModels.EntityAbstract {
		private static final long serialVersionUID = -8490534634496195873L;

		private String name;
		private long count;

		public SequenceModel(Sequence sequence) {
			super(sequence.getId());
			this.name = sequence.getName();
			this.count = sequence.getCount();
		}

		public String getName() {
			return name;
		}

		public long getCount() {
			return count;
		}
	}

	public static class SequenceUpsert implements Serializable {
		private static final long serialVersionUID = 472958677851542408L;

		private SequenceModel sequence;

		public SequenceUpsert(SequenceModel sequence) {
			this.sequence = sequence;
		}

		public SequenceModel getSequence() {
			return sequence;
		}
	}

}
