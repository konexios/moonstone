package com.arrow.apollo.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.AcsLogicalException;
import com.arrow.apollo.data.ApolloBoardCategory;
import com.arrow.apollo.service.ApolloBoardService;
import com.arrow.apollo.web.exception.NotFoundException;
import com.arrow.apollo.web.model.AllBoardsModels;
import com.arrow.apollo.web.model.ApolloModelUtil;
import com.arrow.apollo.web.model.BoardModels;
import com.arrow.apollo.web.model.DeviceModels;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.repo.DeviceSearchParams;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.KronosCache;
import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.widget.WidgetConstants;

@RestController
@RequestMapping("/api/apollo/boards")
public class BoardController extends ApolloControllerAbstract {

	@Autowired
	private KronosCache kronosCache;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private ApolloBoardService apolloBoardService;

	@RequestMapping(value = "/board/create", method = RequestMethod.POST)
	public Board createBoard(@RequestBody BoardModels.CreateBoardModel model, HttpSession session) {

		String method = "createBoard";
		logDebug(method, "...");

		String applicationId = getApplicationId(session);
		String productId = getApolloProductId();
		String userId = getUserId();

		Board board = ApolloModelUtil.toBoard(model);

		// all Apollo boards use Gridster
		// GridsterGridOptions options = new GridsterGridOptions();
		// board.setLayout(JsonUtils.toJson(options));
		// board.setLayoutClass(options.getClass().getName());

		board = apolloBoardService.create(board, productId, applicationId, userId, userId);

		return board;
	}

	@RequestMapping("/default/board")
	public AllBoardsModels.BoardModel findUserDefaultBoard(HttpSession session) {

		String method = "findMyDefaultBoard";
		logDebug(method, "...");

		AllBoardsModels.BoardModel model = null;

		// user default
		Board board = apolloBoardService.findUserDefault(getApplicationId(session), getUserId());
		if (board != null)
			model = ApolloModelUtil.toAllBoardModel(board);

		if (board == null) {
			// system default
			board = apolloBoardService.findSystemDefault();
			if (board != null)
				model = ApolloModelUtil.toAllBoardModel(board);
		}

		return model;
	}

	@RequestMapping(value = "/{boardId}/board", method = RequestMethod.GET)
	public BoardModels.BoardUpsertModel readBoard(@PathVariable String boardId) {

		String method = "readBoard";
		logDebug(method, "...");

		Board board = new Board();

		if (!boardId.equalsIgnoreCase("new")) {
			board = apolloBoardService.getBoardService().getBoardRepository().findById(boardId).orElse(null);
			if (board == null) {
				throw new NotFoundException("Board \"" + boardId + "\" not found");
			}
			// Assert.notNull(board, "board not found: boardId=[" + boardId +
			// "]");
		}

		return new BoardModels.BoardUpsertModel(ApolloModelUtil.toBoardModel(board), ApolloBoardCategory.values());
	}

	@RequestMapping(value = "/{boardId}/board/update", method = RequestMethod.PUT)
	public void updateBoard(@PathVariable String boardId, @RequestBody BoardModels.UpdateBoardModel model,
	        HttpSession session) {

		String method = "updateBoard";
		logDebug(method, "...");

		Board existing = apolloBoardService.getBoardService().getBoardRepository().findById(boardId).orElse(null);
		Assert.notNull(existing, "Board not found! boardId: " + boardId);

		String applicationId = getApplicationId(session);
		String productId = getApolloProductId();
		String userId = getUserId();

		Board board = ApolloModelUtil.toBoard(model, existing);
		board = apolloBoardService.update(board, productId, applicationId, userId, userId);
	}

	@RequestMapping(value = "/{boardId}/board/delete", method = RequestMethod.DELETE)
	public void deleteBoard(@PathVariable String boardId, HttpSession session) {

		String method = "deleteBoard";
		logDebug(method, "...");

		apolloBoardService.delete(boardId);
	}

	@RequestMapping(value = "/{boardId}/board/user-default", method = RequestMethod.GET)
	public BoardModels.BoardModel assignBoardAsUserDefault(@PathVariable String boardId, HttpSession session) {

		String method = "assignBoardAsUserDefault";
		logDebug(method, "...");

		Board existing = apolloBoardService.getBoardService().getBoardRepository().findById(boardId).orElse(null);
		Assert.notNull(existing, "Board not found! boardId: " + boardId);

		String applicationId = getApplicationId(session);
		String productId = getApolloProductId();
		String userId = getUserId();

		existing.setCategory(ApolloBoardCategory.UserDefault.name());
		existing = apolloBoardService.update(existing, productId, applicationId, userId, userId);

		return ApolloModelUtil.toBoardModel(existing);
	}

	@RequestMapping(value = "/{boardId}/board/favorite", method = RequestMethod.GET)
	public BoardModels.BoardModel assignBoardAsFavorite(@PathVariable String boardId, HttpSession session) {

		String method = "assignBoardAsFavorite";
		logDebug(method, "...");

		Board existing = apolloBoardService.getBoardService().getBoardRepository().findById(boardId).orElse(null);
		Assert.notNull(existing, "Board not found! boardId: " + boardId);

		String applicationId = getApplicationId(session);
		String productId = getApolloProductId();
		String userId = getUserId();

		existing.setCategory(ApolloBoardCategory.Favorite.name());
		existing = apolloBoardService.update(existing, productId, applicationId, userId, userId);

		return ApolloModelUtil.toBoardModel(existing);
	}

	public void assignBoardAsCustom() {

		String method = "assignBoardAsCustom";
		logDebug(method, "...");
	}

	@RequestMapping(value = "/{boardId}/board/copy", method = RequestMethod.GET)
	public BoardModels.BoardModel copyBoard(@PathVariable String boardId, HttpSession session) {

		String method = "copyBoard";
		logDebug(method, "...");

		Board existing = apolloBoardService.getBoardService().getBoardRepository().findById(boardId).orElse(null);
		Assert.notNull(existing, "Board not found! boardId: " + boardId);

		String applicationId = getApplicationId(session);
		String productId = getApolloProductId();
		String userId = getUserId();

		Board copy = apolloBoardService.copy(boardId, productId, applicationId, userId, userId);

		return ApolloModelUtil.toBoardModel(copy);
	}

	@RequestMapping(value = "/{boardId}/board/certified/devices", method = RequestMethod.GET)
	public List<DeviceModels.DeviceOptionModel> findArrowCertifiedDevices(@PathVariable String boardId,
	        HttpSession session) {

		String method = "copyBoard";
		logDebug(method, "...");

		Board existing = apolloBoardService.getBoardService().getBoardRepository().findById(boardId).orElse(null);
		Assert.notNull(existing, "Board not found! boardId: " + boardId);

		String userId = getUserId();
		String applicationId = getApplicationId(session);

		Set<String> deviceTypeNameSet = new HashSet<>();
		if (existing.getName().equalsIgnoreCase("iPhone")) {
			deviceTypeNameSet.add("IPhoneDevice");
		} else if (existing.getName().equalsIgnoreCase("Android")) {
			deviceTypeNameSet.add("android");
		} else if (existing.getName().equalsIgnoreCase("Thundeboard React")) {
			deviceTypeNameSet.add("silabs-thunderboard-react");
		} else if (existing.getName().equalsIgnoreCase("SIMBA-PRO")) {
			deviceTypeNameSet.add("simba-pro");
		} else if (existing.getName().equalsIgnoreCase("STM32 IoT node")) {
			deviceTypeNameSet.add("B-L475E-IOT01");
		} else {
			throw new AcsLogicalException("Unsupported device type! name:" + existing.getName());
		}

		List<String> deviceTypeIds = new ArrayList<>();
		for (String deviceTypeName : deviceTypeNameSet) {
			DeviceType deviceType = kronosCache.findDeviceTypeByName(applicationId, deviceTypeName);
			if (deviceType != null)
				deviceTypeIds.add(deviceType.getId());
		}

		List<DeviceModels.DeviceOptionModel> models = new ArrayList<>();
		if (!deviceTypeIds.isEmpty()) {
			DeviceSearchParams deviceSearchParams = new DeviceSearchParams();
			deviceSearchParams.addApplicationIds(applicationId);
			deviceSearchParams.addUserIds(userId);
			deviceSearchParams.addDeviceTypeIds(deviceTypeIds.toArray(new String[deviceTypeIds.size()]));
			deviceSearchParams.setEnabled(true);
			List<Device> devices = deviceService.getDeviceRepository().doFindAllDevices(deviceSearchParams);

			for (Device device : devices)
				models.add(new DeviceModels.DeviceOptionModel().withUid(device.getUid()).withName(device.getName()));
		}

		return models;
	}

	@RequestMapping(value = "/board/copy/certified", method = RequestMethod.POST)
	public BoardModels.BoardModel copyArrowCertifiedBoard(@RequestBody BoardModels.CopyArrowCertifiedBoardModel model,
	        HttpSession session) {

		String method = "copyArrowCertifiedBoard";
		logDebug(method, "...");

		Board existing = apolloBoardService.getBoardService().getBoardRepository().findById(model.getBoardId())
		        .orElse(null);
		Assert.notNull(existing, "Board not found! boardId: " + model.getBoardId());
		Assert.isTrue(existing.getCategory().equalsIgnoreCase(ApolloBoardCategory.ArrowCertified.name()),
		        "Board is not an Arrow Certified board.");

		// TODO validate board and device type match

		String applicationId = getApplicationId(session);
		String productId = getApolloProductId();
		String userId = getUserId();

		Device device = deviceService.getDeviceRepository().findByApplicationIdAndUid(applicationId, model.getDeviceUid());
		Assert.notNull(device, "Device not found! boardId: " + model.getDeviceUid());
		
		Map<String, Object> propertyValues = new HashMap<>();
		propertyValues.put(WidgetConstants.Property.Name.DEVICE_UID, model.getDeviceUid());

		Board copy = apolloBoardService.copyArrowCertifiedBoard(existing.getId(), productId, applicationId, userId,
		        propertyValues, getUserId());

		return ApolloModelUtil.toBoardModel(copy);
	}
}