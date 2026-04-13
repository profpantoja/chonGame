# Agent Package

The `chon.group.game.core.agent` package defines the core actor model of the game.

It is centered on the abstract class `Entity`, which provides the common structure for all interactive world elements, including position, movement, health, hitbox, animation, and sound support.

From this base, the package defines two main specializations:

- **Agent**: active entities capable of moving, chasing targets, receiving damage with temporary invulnerability, and using weapons.
- **Object**: passive world entities that may be collectible, destructible, and capable of following a target under attraction rules.

The package also includes supporting types for collision and state control:
- **Hitbox**
- **Direction**
- **EntityStatus**

## Main Classes

- **Entity**: abstract base class for all world entities.
- **Agent**: active entity with energy, invulnerability, and weapon support.
- **Object**: passive entity with collectible/destructible behavior.
- **Hitbox**: defines collision dimensions.
- **Direction**: movement orientation enum.
- **EntityStatus**: gameplay/animation status enum.

## Class Diagram

```mermaid
classDiagram
    class Entity {
        <<abstract>>
        -int posX
        -int posY
        -int widthOffset
        -int speed
        -int health
        -int fullHealth
        -Direction direction
        -EntityStatus status
        -boolean visibleBars
        -Hitbox hitbox
        -AnimationSet animationSet
        -AnimationState animationState
        -SoundSet soundSet
        +move(List~Direction~ movements)
        +chase(int targetX, int targetY)
        +takeDamage(int damage, List~Message~ messages, List~Sound~ sounds)
        +idle()
        +isTerminated()
        +canRemove()
        +getFlippedWidth()
        +getFlippedPosX()
    }

    class Agent {
        -long lastHitTime
        -boolean invulnerable
        -long INVULNERABILITY_COOLDOWN
        -Weapon weapon
        -double energy
        -double fullEnergy
        -double recoveryFactor
        +consumeEnergy(double amount)
        +recoverEnergy()
        +isEnergyEmpty()
        +isDead()
        +useWeapon()
        +copy(int posX, int posY)
    }

    class Object {
        -boolean collected
        -boolean destructible
        -boolean collectible
        -double attractionRadius
        +onCollect()
        +onDestroy()
        +follow(Entity target)
        +isDestroyed()
        +blocksMovement()
        +copy(int posX, int posY)
    }

    class Hitbox {
        -int width
        -int height
        -double ratio
    }

    class Direction {
        <<enumeration>>
        RIGHT
        LEFT
        UP
        DOWN
        IDLE
    }

    class EntityStatus {
        <<enumeration>>
        IDLE
        WALK
        JUMP
        TERMINATE
        ATTACK
        DAMAGE
    }

    Entity <|-- Agent
    Entity <|-- Object
    Entity --> Hitbox
    Entity --> Direction
    Entity --> EntityStatus
```
## Entity Behavior Flow

```mermaid
flowchart TD
    A[Entity created] --> B[Idle state]

    B --> C[Move]
    C --> D[Direction updated]
    D --> E[Status becomes WALK]

    B --> F[Chase target]
    F --> C

    B --> G[Take damage]
    C --> G
    E --> G

    G --> H[Health reduced]
    H --> I[Damage message created]
    H --> J[Status becomes DAMAGE]

    J --> K{Health <= 0?}
    K -- No --> B
    K -- Yes --> L[Terminate]
    L --> M[Status becomes TERMINATE]
    M --> N[Animation reset to first frame]

    N --> O{Animation finished?}
    O -- No --> P[Remain in world]
    O -- Yes --> Q[Can be removed]

    B --> R[Animation update]
    C --> R
    J --> R
    M --> R
```    
