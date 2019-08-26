package moonstone.selene.web.api;

import java.util.List;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import moonstone.selene.data.Message;
import moonstone.selene.web.api.model.MessageModels;

@RestController
@RequestMapping(value = "/api/selene/messages")
public class MessageApi extends BaseApi {

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<Message> messages() {
		return getMessageService().findAll();
	}

	@RequestMapping(value = "/{messageId}/message", method = RequestMethod.GET)
	public MessageModels.MessageUpsert message(@PathVariable long messageId) {
		Assert.notNull(messageId, "messageId is null");

		Message message = getMessageService().findById(messageId);
		Assert.notNull(message, "message is null");

		return new MessageModels.MessageUpsert(new MessageModels.MessageModel(message));
	}
}
