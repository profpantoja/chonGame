package chon.group.game.sound;

public class Sound {

    private String path;
    private SoundType type;
    private boolean inLoop;

    public Sound(String path, SoundType type, boolean inLoop) {
        this.path = path;
        this.type = type;
        this.inLoop = inLoop;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SoundType getType() {
        return type;
    }

    public void setType(SoundType type) {
        this.type = type;
    }

    public boolean isInLoop() {
        return inLoop;
    }

    public void setInLoop(boolean inLoop) {
        this.inLoop = inLoop;
    }

}
