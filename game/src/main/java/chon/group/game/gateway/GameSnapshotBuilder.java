package chon.group.game.gateway;

import chon.group.game.Game;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;

import java.util.List;

/** Converts the mutable game model into a transport-neutral snapshot. */
public class GameSnapshotBuilder {

    public GameSnapshot build(Game game, long tick) {
        Environment environment = game.getEnvironment();
        Level level = environment.getCurrentLevel();
        Agent protagonist = environment.getProtagonist();

        return new GameSnapshot(
                tick,
                game.getCurrentState().getClass().getSimpleName(),
                level == null ? null : new LevelSnapshot(
                        level.getDescription(),
                        level.getType().name(),
                        level.getWidth(),
                        level.getTopY(),
                        level.getBottomY()),
                protagonist == null ? null : entitySnapshot(protagonist),
                level == null ? List.of() : level.getAgents().stream()
                        .map(this::entitySnapshot)
                        .toList(),
                level == null ? List.of() : level.getObjects().stream()
                        .map(this::entitySnapshot)
                        .toList(),
                level == null ? List.of() : level.getShots().stream()
                        .map(this::entitySnapshot)
                        .toList(),
                environment.getScore(),
                environment.getCollectedCount(),
                game.isGameCompleted());
    }

    private EntitySnapshot entitySnapshot(Entity entity) {
        return new EntitySnapshot(
                entity.getId(),
                entity.getClass().getSimpleName(),
                entity.getPosX(),
                entity.getPosY(),
                entity.getWidthOffset(),
                entity.getHitbox().getHeight(),
                entity.getHealth(),
                entity.getFullHealth(),
                entity.getDirection().name(),
                entity.getStatus().name(),
                entity.isTerminated(),
                entity instanceof chon.group.game.core.agent.Object object
                        && object.isCollectible());
    }

    public record GameSnapshot(
            long tick,
            String state,
            LevelSnapshot level,
            EntitySnapshot self,
            List<EntitySnapshot> agents,
            List<EntitySnapshot> objects,
            List<EntitySnapshot> shots,
            int score,
            int collectedCount,
            boolean completed) {
    }

    public record LevelSnapshot(
            String description,
            String type,
            int width,
            int topY,
            int bottomY) {
    }

    public record EntitySnapshot(
            String id,
            String kind,
            int x,
            int y,
            int width,
            int height,
            int health,
            int maxHealth,
            String direction,
            String status,
            boolean dead,
            boolean collectible) {
    }
}