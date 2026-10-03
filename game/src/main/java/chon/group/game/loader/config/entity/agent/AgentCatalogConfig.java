package chon.group.game.loader.config.entity.agent;

import java.util.Map;

public class AgentCatalogConfig {

    private Map<String, AgentConfig> definitions;
    private Map<String, TeamConfig> teams;

    public Map<String, AgentConfig> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Map<String, AgentConfig> definitions) {
        this.definitions = definitions;
    }

    public Map<String, TeamConfig> getTeams() {
        return teams;
    }

    public void setTeams(Map<String, TeamConfig> teams) {
        this.teams = teams;
    }
}