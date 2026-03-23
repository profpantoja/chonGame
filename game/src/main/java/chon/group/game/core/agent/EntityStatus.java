package chon.group.game.core.agent;

public enum EntityStatus {

    /* What the entity does when it doesn't do nothing. */
    IDLE,
    /* The entity is walking. */
    WALK,
    /* The entity is jumping. */
    JUMP,
    /* An entity dies, terminates or break. */
    TERMINATE,
    /* An entity attacks, shoots or use any kind of weapon. */
    ATTACK,
    /* An entity suffers a Damage or a Hit. */
    DAMAGE

}
