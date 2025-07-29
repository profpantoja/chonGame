package chon.group.game.domain.weapon;

import chon.group.game.core.agent.AgentState;
import chon.group.game.core.agent.Animation;
import chon.group.game.core.weapon.Shot;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class EssenceOrb extends Shot {

    public EssenceOrb(int posX, int posY, String direction) {
        super(posX, posY, 100, 100, 3, 1, null, false, 150, direction);

        List<Image> frames = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            frames.add(loadImage("/images/weapons/essenceOrb/EssenceOrb" + i + ".png"));
        }
        
        Animation anim = new Animation(frames, 0.08, false); 
        animations.put(AgentState.IDLE, anim);
        setCurrentState(AgentState.IDLE);
    }
}