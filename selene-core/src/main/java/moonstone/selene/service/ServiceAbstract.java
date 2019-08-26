package moonstone.selene.service;

import moonstone.selene.Loggable;

public abstract class ServiceAbstract extends Loggable {

    private final CryptoService cryptoService;

    protected ServiceAbstract() {
        cryptoService = CryptoService.getInstance();
    }

    protected CryptoService getCryptoService() {
        return cryptoService;
    }
}
