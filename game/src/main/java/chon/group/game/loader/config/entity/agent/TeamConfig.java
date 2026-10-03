package chon.group.game.loader.config.entity.agent;

import java.util.List;

public class TeamConfig {

    private String type;
    private String behavior;
    private List<String> agentRefs;
    private List<String> allowedCommands;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public List<String> getAgentRefs() {
        return agentRefs;
    }

    public void setAgentRefs(List<String> agentRefs) {
        this.agentRefs = agentRefs;
    }

    public List<String> getAllowedCommands() {
        return allowedCommands;
    }

    public void setAllowedCommands(List<String> allowedCommands) {
        this.allowedCommands = allowedCommands;
    }
}