package chon.group.game.loader.factory;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Team;
import chon.group.game.core.agent.TeamBehavior;
import chon.group.game.core.agent.TeamType;
import chon.group.game.joystick.GameCommand;
import chon.group.game.loader.config.entity.agent.AgentConfig;
import chon.group.game.loader.config.entity.agent.TeamConfig;

public class TeamFactory {

    private final Map<String, Team> teams = new LinkedHashMap<>();
    private final AgentFactory agentFactory;

    public TeamFactory(Map<String, TeamConfig> teamConfigs, AgentFactory agentFactory) {
        this.agentFactory = agentFactory;
        if (teamConfigs == null || teamConfigs.isEmpty()) {
            throw new IllegalArgumentException("At least one team must be configured");
        }
        teamConfigs.forEach((id, config) -> teams.put(id, createTeam(id, config)));
    }

    public Agent createAgent(String agentRef, AgentConfig config, String teamId) {
        Team team = getTeam(teamId);
        if (!team.allowsAgent(agentRef)) {
            throw new IllegalArgumentException(
                    "Agent '" + agentRef + "' is not allowed in team '" + teamId + "'");
        }
        Agent agent = agentFactory.create(agentRef, config);
        agent.setTeam(team);
        return agent;
    }

    public Team getTeam(String teamId) {
        Team team = teams.get(teamId);
        if (team == null) {
            throw new IllegalArgumentException("Team not found: " + teamId);
        }
        return team;
    }

    private Team createTeam(String id, TeamConfig config) {
        if (config == null || config.getType() == null || config.getBehavior() == null
                || config.getAgentRefs() == null) {
            throw new IllegalArgumentException("Invalid configuration for team '" + id + "'");
        }

        try {
            Set<GameCommand> allowedCommands = EnumSet.noneOf(GameCommand.class);
            if (config.getAllowedCommands() != null) {
                for (String command : config.getAllowedCommands()) {
                    allowedCommands.add(GameCommand.valueOf(command));
                }
            }
            return new Team(
                    id,
                    TeamType.valueOf(config.getType()),
                    TeamBehavior.valueOf(config.getBehavior()),
                    Set.copyOf(config.getAgentRefs()),
                    allowedCommands);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid configuration for team '" + id + "'", exception);
        }
    }
}