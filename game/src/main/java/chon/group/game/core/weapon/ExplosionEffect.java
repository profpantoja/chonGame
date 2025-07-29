package chon.group.game.core.weapon;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.AgentState;
import chon.group.game.core.agent.Animation;
import chon.group.game.core.agent.Entity;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class ExplosionEffect extends Entity {

    private double lifetime;
    private double age = 0;
    private int damage;
    private List<Agent> hitAgents; // Lista de inimigos já atingidos por esta explosão

    public ExplosionEffect(int posX, int posY) {
        super(posX, posY, 250, 250, 0, 0, null, false, false);
        this.lifetime = 0.9;
        this.damage = 300; // Dano da explosão. Você pode ajustar este valor!
        this.hitAgents = new ArrayList<>();

        List<Image> frames = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            frames.add(loadImage("/images/weapons/explosion/Explosion" + i + ".png"));
        }

        Animation anim = new Animation(frames, 0.1, false);
        animations.put(AgentState.ATTACK, anim);
        setCurrentState(AgentState.ATTACK);
    }

    public void update(double deltaTime) {
        updateAnimation(deltaTime);
        age += deltaTime;
    }

    public boolean isExpired() {
        return age >= lifetime;
    }
    
    public int getDamage() {
        return damage;
    }

    // Verifica se um inimigo já foi atingido por esta explosão específica
    public boolean hasHit(Agent agent) {
        return hitAgents.contains(agent);
    }

    // Adiciona um inimigo à lista de alvos já atingidos
    public void registerHit(Agent agent) {
        hitAgents.add(agent);
    }
}