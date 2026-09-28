package chon.group.game.loader.config.platform;

import chon.group.game.core.platform.config.ControlType;

public class PlatformConfig {

    private ControlType control;
    private ApiConfig api;

    public ControlType getControl() {
        return control;
    }

    public void setControl(ControlType control) {
        this.control = control;
    }

    public ApiConfig getApi() {
        return api;
    }

    public void setApi(ApiConfig api) {
        this.api = api;
    }
}