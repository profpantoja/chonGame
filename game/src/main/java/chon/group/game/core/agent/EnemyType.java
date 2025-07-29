package chon.group.game.core.agent;

public enum EnemyType {
    // Define os 3 tipos de inimigos, com o caminho para suas imagens
    ORC("/images/agents/inimigo1/orc.png"),
    GOLEM("/images/agents/inimigo2/golem.png"),
    SKELETON("/images/agents/inimigo3/skeleton.png");

    private final String imagePath;

    EnemyType(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}