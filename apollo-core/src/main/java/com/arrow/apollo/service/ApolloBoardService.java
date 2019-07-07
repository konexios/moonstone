package com.arrow.apollo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.apollo.LastSystemDefaultBoardException;
import com.arrow.apollo.data.ApolloBoardCategory;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.dashboard.data.ConfigurationPage;
import com.arrow.pegasus.dashboard.data.Container;
import com.arrow.pegasus.dashboard.data.PageProperty;
import com.arrow.pegasus.dashboard.data.PropertyValue;
import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.dashboard.data.WidgetConfiguration;
import com.arrow.pegasus.dashboard.data.WidgetParentTypes;
import com.arrow.pegasus.dashboard.data.WidgetType;
import com.arrow.pegasus.dashboard.model.CopyBoardModels.CopyOfBoard;
import com.arrow.pegasus.dashboard.model.CopyBoardModels.CopyOfConfigurationPage;
import com.arrow.pegasus.dashboard.model.CopyBoardModels.CopyOfContainer;
import com.arrow.pegasus.dashboard.model.CopyBoardModels.CopyOfPageProperty;
import com.arrow.pegasus.dashboard.model.CopyBoardModels.CopyOfPropertyValue;
import com.arrow.pegasus.dashboard.model.CopyBoardModels.CopyOfWidget;
import com.arrow.pegasus.dashboard.model.CopyBoardModels.CopyOfWidgetConfiguration;
import com.arrow.pegasus.dashboard.repo.BoardSearchParams;
import com.arrow.pegasus.dashboard.repo.ConfigurationPageSearchParams;
import com.arrow.pegasus.dashboard.repo.ContainerSearchParams;
import com.arrow.pegasus.dashboard.repo.PagePropertySearchParams;
import com.arrow.pegasus.dashboard.repo.PropertyValueSearchParams;
import com.arrow.pegasus.dashboard.repo.WidgetConfigurationSearchParams;
import com.arrow.pegasus.dashboard.repo.WidgetSearchParams;
import com.arrow.pegasus.dashboard.repo.WidgetTypeSearchParams;
import com.arrow.pegasus.dashboard.service.BoardService;
import com.arrow.pegasus.dashboard.service.ConfigurationPageService;
import com.arrow.pegasus.dashboard.service.ContainerService;
import com.arrow.pegasus.dashboard.service.CopyBoardService;
import com.arrow.pegasus.dashboard.service.PagePropertyService;
import com.arrow.pegasus.dashboard.service.PropertyValueService;
import com.arrow.pegasus.dashboard.service.WidgetConfigurationService;
import com.arrow.pegasus.dashboard.service.WidgetService;
import com.arrow.pegasus.dashboard.service.WidgetTypeService;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.service.CoreCacheService;
import com.arrow.pegasus.service.ServiceAbstract;

@Service
public class ApolloBoardService extends ServiceAbstract {

    @Autowired
    private BoardService boardService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private WidgetService widgetService;
    @Autowired
    private WidgetTypeService widgetTypeService;
    @Autowired
    private WidgetConfigurationService widgetConfigurationService;
    @Autowired
    private ConfigurationPageService configurationPageService;
    @Autowired
    private PagePropertyService pagePropertyService;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private CopyBoardService copyBoardService;
    @Autowired
    private CoreCacheService coreCacheService;

    public BoardService getBoardService() {
        return boardService;
    }

    private String getApolloProductId() {
        Product product = coreCacheService.findProductBySystemName(ProductSystemNames.APOLLO);
        Assert.notNull(product, "Product not found! product: " + ProductSystemNames.APOLLO);

        return product.getId();
    }

    public Board findSystemDefault() {

        String method = "findSystemDefault";
        logDebug(method, "...");

        BoardSearchParams params = new BoardSearchParams();
        params.addProductIds(getApolloProductId());
        params.addCategories(ApolloBoardCategory.SystemDefault.name());

        return findBoard(params);
    }

    public Board findUserDefault(String applicationId, String userId) {
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(userId, "userId is empty");

        String method = "findUserDefault";
        logDebug(method, "...");

        BoardSearchParams params = new BoardSearchParams();
        params.addProductIds(getApolloProductId());
        params.addApplicationIds(applicationId);
        params.addUserIds(userId);
        params.addCategories(ApolloBoardCategory.UserDefault.name());

        return findBoard(params);
    }

    public List<Board> findAllBoards(String applicationId, String userId) {
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(userId, "userId is empty");

        BoardSearchParams params = new BoardSearchParams();
        params.addProductIds(getApolloProductId());
        params.addApplicationIds(applicationId);
        params.addUserIds(userId);

        List<Board> allUsersBoards = findBoards(params);

        Board systemDefaultBoard = findSystemDefault();

        if (systemDefaultBoard != null)
            allUsersBoards.add(systemDefaultBoard);

        return allUsersBoards;
    }

    public List<Board> findCustomBoards(String applicationId, String userId) {
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(userId, "userId is empty");

        BoardSearchParams params = new BoardSearchParams();
        params.addProductIds(getApolloProductId());
        params.addApplicationIds(applicationId);
        params.addUserIds(userId);
        params.addCategories(ApolloBoardCategory.Custom.name());

        return findBoards(params);
    }

    public List<Board> findFavoriteBoards(String applicationId, String userId) {
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(userId, "userId is empty");

        BoardSearchParams params = new BoardSearchParams();
        params.addProductIds(getApolloProductId());
        params.addApplicationIds(applicationId);
        params.addUserIds(userId);
        params.addCategories(ApolloBoardCategory.Favorite.name());

        return findBoards(params);
    }

    public List<Board> findArrowCertifiedBoards() {
        BoardSearchParams params = new BoardSearchParams();
        params.addProductIds(getApolloProductId());
        params.addCategories(ApolloBoardCategory.ArrowCertified.name());

        return findBoards(params);
    }

    public Board copyArrowCertifiedBoard(String boardId, String productId, String applicationId, String userId,
            Map<String, Object> propertyValues, String who) {
        Assert.notNull(propertyValues, "propertyValues is null");
        Assert.notEmpty(propertyValues, "propertyValues is empty");

        Board board = copy(boardId, productId, applicationId, userId, who);
        Assert.notNull(board, "board is null");

        // container widgets
        ContainerSearchParams containerSearchParams = new ContainerSearchParams();
        containerSearchParams.addBoardIds(board.getId());
        for (Container container : containerService.getContainerRepository().findContainers(containerSearchParams))
            setPropertyValues(container.getId(), WidgetParentTypes.BoardContainer, propertyValues, who);

        // board widgets
        setPropertyValues(board.getId(), WidgetParentTypes.Board, propertyValues, who);

        return board;
    }

    private void setPropertyValues(String parentId, WidgetParentTypes parentType, Map<String, Object> propertyValues,
            String who) {
        Assert.hasText(parentId, "parentId is empty");
        Assert.notNull(parentType, "parentType is null");
        Assert.notNull(propertyValues, "propertyValues is null");
        Assert.notEmpty(propertyValues, "propertyValues is empty");
        Assert.hasText(who, "who is empty");

        WidgetSearchParams widgetSearchParams = new WidgetSearchParams();
        // TODO add parentType
        widgetSearchParams.addParentIds(parentId);

        for (Widget widget : widgetService.getWidgetRepository().findWidgets(widgetSearchParams))
            setPropertyValues(widget, propertyValues, who);
    }

    private void setPropertyValues(Widget widget, Map<String, Object> propertyValues, String who) {
        Assert.notNull(widget, "widget is null");
        Assert.notNull(propertyValues, "propertyValues is null");
        Assert.notEmpty(propertyValues, "propertyValues is empty");
        Assert.hasText(who, "who is empty");

        WidgetConfigurationSearchParams widgetConfigurationSearchParams = new WidgetConfigurationSearchParams();
        widgetConfigurationSearchParams.addWidgetIds(widget.getId());
        for (WidgetConfiguration widgetConfiguration : widgetConfigurationService.getWidgetConfigurationRepository()
                .findWidgetConfigurations(widgetConfigurationSearchParams)) {

            ConfigurationPageSearchParams configurationPageSearchParams = new ConfigurationPageSearchParams();
            configurationPageSearchParams.addWidgetConfigurationIds(widgetConfiguration.getId());
            for (ConfigurationPage configurationPage : configurationPageService.getConfigurationPageRepository()
                    .findConfigurationPages(configurationPageSearchParams)) {

                PagePropertySearchParams pagePropertySearchParams = new PagePropertySearchParams();
                pagePropertySearchParams.addConfigurationPageIds(configurationPage.getId());
                for (PageProperty pageProperty : pagePropertyService.getPagePropertyRepository()
                        .findPageProperties(pagePropertySearchParams)) {
                    if (propertyValues.keySet().contains(pageProperty.getName())) {

                        Object value = propertyValues.get(pageProperty.getName());
                        PropertyValueSearchParams propertyValueSearchParams = new PropertyValueSearchParams();
                        propertyValueSearchParams.addPagePropertyIds(pageProperty.getId());
                        for (PropertyValue propertyValue : propertyValueService.getPropertyValueRepository()
                                .findPropertyValues(propertyValueSearchParams)) {
                            propertyValue.setValue(value);
                            propertyValueService.update(propertyValue, who);
                        }
                    }
                }
            }
        }
    }

    public Board copy(String boardId, String productId, String applicationId, String userId, String who) {
        Assert.hasText(boardId, "boardId is empty");
        Assert.hasText(productId, "productId is empty");
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(userId, "userId is empty");
        Assert.hasText(who, "who is empty");

        CopyOfBoard copyOfBoard = copyBoardService.copyBoard(boardId);
        Assert.notNull(copyOfBoard, "board is null");

        // process copy
        Board board = copyOfBoard.getBoard();
        // any board that is copied shall be custom
        board.setCategory(ApolloBoardCategory.Custom.name());
        board.setName("Copy of " + board.getName());
        board = processBoard(board, productId, applicationId, userId);
        board = boardService.create(copyOfBoard.getBoard(), who);

        // containers
        for (CopyOfContainer copyOfContainer : copyOfBoard.getContainers()) {
            Container container = copyOfContainer.getContainer();
            container.setBoardId(board.getId());

            container = containerService.create(container, who);

            // container widgets
            copyWidgets(container.getId(), copyOfContainer.getWidgets(), who);
        }

        // board widgets
        copyWidgets(board.getId(), copyOfBoard.getWidgets(), who);

        return board;
    }

    private List<Widget> copyWidgets(String parentId, List<CopyOfWidget> copyOfWidgets, String who) {
        Assert.hasText(parentId, "parentId is empty");
        Assert.notNull(copyOfWidgets, "copyOfWidgets is empty");
        Assert.hasText(who, "who is empty");

        List<Widget> widgets = new ArrayList<>();
        for (CopyOfWidget copyOfWidget : copyOfWidgets) {
            Widget widget = copyOfWidget.getWidget();
            widget.setParentId(parentId);
            widget.setParentType(copyOfWidget.getWidget().getParentType());
            widgetService.create(widget, who);

            for (CopyOfWidgetConfiguration copyOfWidgetConfiguration : copyOfWidget.getWidgetConfigurations()) {
                WidgetConfiguration widgetConfiguration = copyOfWidgetConfiguration.getWidgetConfiguration();
                widgetConfiguration.setWidgetId(widget.getId());
                widgetConfiguration = widgetConfigurationService.create(widgetConfiguration, who);

                for (CopyOfConfigurationPage copyOfConfigurationPage : copyOfWidgetConfiguration
                        .getConfigurationPages()) {
                    ConfigurationPage configurationPage = copyOfConfigurationPage.getConfigurationPage();
                    configurationPage.setWidgetConfigurationId(widgetConfiguration.getId());
                    configurationPage = configurationPageService.create(configurationPage, who);

                    for (CopyOfPageProperty copyOfPageProperty : copyOfConfigurationPage.getPageProperties()) {
                        PageProperty pageProperty = copyOfPageProperty.getPageProperty();
                        pageProperty.setConfigurationPageId(configurationPage.getId());
                        pageProperty = pagePropertyService.create(pageProperty, who);

                        for (CopyOfPropertyValue copyOfPropertyValue : copyOfPageProperty.getPropertyValues()) {
                            PropertyValue propertyValue = copyOfPropertyValue.getPropertyValue();
                            propertyValue.setPagePropertyId(pageProperty.getId());
                            propertyValue = propertyValueService.create(propertyValue, who);
                        }
                    }
                }
            }

            widgets.add(widget);
        }

        return widgets;
    }

    public Board create(Board board, String productId, String applicationId, String userId, String who) {
        Assert.notNull(board, "board is null");
        Assert.hasText(board.getCategory(), "category is empty");
        Assert.hasText(productId, "productId is empty");
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(userId, "userId is empty");
        Assert.hasText(who, "who is empty");

        String method = "create";
        logDebug(method, "...");

        board = processBoard(board, productId, applicationId, userId);

        checkUpdateDefaultBoard(null, board.getCategory(), productId, applicationId, userId, who);

        board = boardService.create(board, who);

        return board;
    }

    public Board update(Board board, String productId, String applicationId, String userId, String who) {
        Assert.notNull(board, "board is null");
        Assert.hasText(board.getId(), "id is empty");
        Assert.hasText(board.getCategory(), "category is empty");
        Assert.hasText(productId, "productId is empty");
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(userId, "userId is empty");
        Assert.hasText(who, "who is empty");

        String method = "update";
        logDebug(method, "...");

        board = processBoard(board, productId, applicationId, userId);

        checkUpdateDefaultBoard(board.getId(), board.getCategory(), productId, applicationId, userId, who);

        board = boardService.update(board, who);

        return board;
    }

    public void delete(String boardId) {
        Assert.hasText(boardId, "boardId is empty");

        String method = "delete";
        logDebug(method, "...");

        Board board = boardService.getBoardRepository().findById(boardId).orElse(null);
        Assert.notNull(board, "Board not found! boardId:" + boardId);

        if (board.getCategory().equalsIgnoreCase(ApolloBoardCategory.SystemDefault.name())) {
            Board systemDefault = findSystemDefault();

            if (board.getId().equals(systemDefault.getId()))
                throw new LastSystemDefaultBoardException();
        }

        boardService.delete(boardId);
    }

    public List<WidgetType> findWidgetTypes() {
        return widgetTypeService.getWidgetTypeRepository().findWidgetTypes(new WidgetTypeSearchParams());
    }

    private Board findBoard(BoardSearchParams params) {
        Assert.notNull(params, "params are null");

        List<Board> boards = boardService.getBoardRepository().findBoards(params);

        if (boards == null || boards.isEmpty())
            return null;

        return boards.get(0);
    }

    private List<Board> findBoards(BoardSearchParams params) {
        Assert.notNull(params, "params are null");

        return boardService.getBoardRepository().findBoards(params);
    }

    private void checkUpdateDefaultBoard(String boardId, String category, String productId, String applicationId,
            String userId, String who) {
        Assert.hasText(category, "category is empty");
        Assert.hasText(productId, "productId is empty");
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(userId, "userId is empty");

        String method = "checkUpdateDefaultBoard";
        logDebug(method, "...");

        if (category.equalsIgnoreCase(ApolloBoardCategory.SystemDefault.name())) {
            // lookup system default board if exists and set to custom
            checkUpdateSystemDefaultBoard(boardId, productId, applicationId, userId, who);
        } else if (category.equalsIgnoreCase(ApolloBoardCategory.UserDefault.name())) {
            // lookup user default board if exists and set to custom
            checkUpdateUserDefaultBoard(boardId, productId, applicationId, userId, who);
        }
    }

    private void checkUpdateSystemDefaultBoard(String boardId, String productId, String applicationId, String userId,
            String who) {
        Assert.hasText(productId, "productId is empty");
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(userId, "userId is empty");

        String method = "checkUpdateSystemDefaultBoard";
        logDebug(method, "...");

        Board systemDefault = findSystemDefault();
        if (systemDefault != null) {
            if (StringUtils.isEmpty(boardId) || !systemDefault.getId().equals(boardId)) {
                systemDefault.setCategory(ApolloBoardCategory.Custom.name());
                processBoard(systemDefault, productId, applicationId, userId);
                boardService.update(systemDefault, who);
            }
        }
    }

    private void checkUpdateUserDefaultBoard(String boardId, String productId, String applicationId, String userId,
            String who) {
        Assert.hasText(productId, "productId is empty");
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(userId, "userId is empty");

        String method = "checkUpdateUserDefaultBoard";
        logDebug(method, "...");

        Board userDefault = findUserDefault(applicationId, userId);
        if (userDefault != null) {
            if (StringUtils.isEmpty(boardId) || !userDefault.getId().equals(boardId)) {
                userDefault.setCategory(ApolloBoardCategory.Custom.name());
                processBoard(userDefault, productId, applicationId, userId);
                boardService.update(userDefault, who);
            }
        }
    }

    private Board processBoard(Board board, String productId, String applicationId, String userId) {
        if (board == null)
            return null;

        if (board.getCategory().equalsIgnoreCase(ApolloBoardCategory.SystemDefault.name())
                || board.getCategory().equalsIgnoreCase(ApolloBoardCategory.ArrowCertified.name())) {
            board.setProductId(productId);
            board.setApplicationId(null);
            board.setUserId(null);
        } else if (isRequireApplication(board)) {
            board.setProductId(productId);
            board.setApplicationId(applicationId);
            board.setUserId(userId);
        } else {
            // TODO throw exception
        }

        return board;
    }

    private boolean isRequireApplication(Board board) {

        return board.getCategory().equalsIgnoreCase(ApolloBoardCategory.Custom.name())
                || board.getCategory().equalsIgnoreCase(ApolloBoardCategory.Favorite.name())
                || board.getCategory().equalsIgnoreCase(ApolloBoardCategory.UserDefault.name())
                || board.getCategory().equalsIgnoreCase(ApolloBoardCategory.XconCustomer.name())
                || board.getCategory().equalsIgnoreCase(ApolloBoardCategory.XconGateway.name())
                || board.getCategory().equalsIgnoreCase(ApolloBoardCategory.XconGroup.name())
                || board.getCategory().equalsIgnoreCase(ApolloBoardCategory.XconDevice.name());
    }
}
