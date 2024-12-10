package chon.group.game.domain.movement;

public interface MovementImpl {
    public String getUp();
    public String getDown();
    public String getLeft();
    public String getRight();

    public void setUp(String up);
    public void setDown(String down);
    public void setLeft(String left);
    public void setRight(String right);

    public String getFinish();
    public void setFinish(String finish);
}
