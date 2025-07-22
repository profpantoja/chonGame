package chon.group.game.core.environment;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import javafx.scene.image.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Level {

    private String backgroundPath;
    private Image image;
    private ArrayList<Agent> agents;
    private List<Object> objects;
    private Level nextLevel;

    public Level(String backgroundPath, List<Agent> agents, List<Object> objects) {
        this.backgroundPath = backgroundPath;

        URL resource = getClass().getResource(backgroundPath);
        if (resource == null) {
            throw new RuntimeException("Imagem não encontrada: " + backgroundPath);
        }

        this.image = new Image(resource.toExternalForm());
        this.agents = new ArrayList<>(agents);
        this.objects = new ArrayList<>(objects); 
    }

    public void applyTo(Environment env, Agent protagonist) {
        env.setImage(image);
        env.setObjects(objects); 
        env.setAgents(agents);   
        env.setProtagonist(protagonist);
    }

    public boolean isCompleted(Environment env) {
        if (!env.getProtagonist().isDead() && env.getProtagonist().getPosX() >= 0.9 * env.getWidth()) {
            for (Agent agent : env.getAgents()) {
                if (agent != env.getProtagonist() && !agent.isDead()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Level getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(Level nextLevel) {
        this.nextLevel = nextLevel;
    }
}
