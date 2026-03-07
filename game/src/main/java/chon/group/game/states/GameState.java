package chon.group.game.states;

import chon.group.game.Game;

public interface GameState {

    public void update(Game game);

    public void handleInput(Game game);

}
