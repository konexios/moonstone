package moonstone.selene.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GatewayService extends moonstone.selene.service.GatewayService {
    @Value("${webLogin}")
    private String webLogin;

    @Value("${webPassword}")
    private String webPassword;

    private static class SingletonHolder {
        private final static GatewayService singleton = new GatewayService();
    }

    public static GatewayService getInstance() {
        return SingletonHolder.singleton;
    }

    public boolean webAuthenticate(String login, String password) {
        String method = "webAuthenticate";
        if (StringUtils.equals(login, getCryptoService().decrypt(webLogin))) {
            if (StringUtils.equals(getCryptoService().hash(password), webPassword)) {
                return true;
            } else {
                logInfo(method, "wrong password");
                return false;
            }
        } else {
            logInfo(method, "wrong login");
            return false;
        }
    }
}
