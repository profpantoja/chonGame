package chon.group.game.sound;

public enum SoundEvent {

    /*
     * The sound that the entity does when it isn't doing nothing. Some entities
     * such as agents can have idle sounds.
     */
    IDLE,
    /*
     * The sound of an entity walking. Some entities such as
     * agents or shots can have walking sounds.
     */
    WALK,
    /* The sound of an entity jumping. */
    JUMP,
    /* The sound of an entity diying, terminating or breaking. */
    TERMINATE,
    /* The sound of an entity attacking, shooting or using any kind of weapon. */
    ATTACK,
    /* The sound of an entity suffering damage or a hit. */
    DAMAGE,
    /* The background music. */
    BACKGROUND,
    /* The ambient sounds. */
    AMBIENT

}
