package chon.group.game.core.agent;

import java.util.Set;

import chon.group.game.joystick.GameCommand;

public final class Team {

    private final String id;
    private final TeamType type;
    private final TeamBehavior behavior;
    private final Set<String> agentRefs;
    private final Set<GameCommand> allowedCommands;

    public Team(
            String id,
            TeamType type,
            TeamBehavior behavior,
            Set<String> agentRefs,
            Set<GameCommand> allowedCommands) {
        this.id = id;
        this.type = type;
        this.behavior = behavior;
        this.agentRefs = Set.copyOf(agentRefs);
        this.allowedCommands = Set.copyOf(allowedCommands);
    }

    public String getId() {
        return id;
    }

    public TeamType getType() {
        return type;
    }

    public TeamBehavior getBehavior() {
        return behavior;
    }

    public boolean allowsAgent(String agentRef) {
        return agentRefs.contains(agentRef);
    }

    public boolean allows(GameCommand command) {
        return allowedCommands.contains(command);
    }
}