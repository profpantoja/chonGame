# Drawer Package (GoF-Oriented)

The `chon.group.game.drawer` subsystem is responsible for rendering the visual state of the game on screen.

This design follows the **Mediator pattern** with a clear separation between coordination and rendering:

- **Abstract Mediator**: `GameDrawer`
- **Concrete Mediator**: `GameMediator`
- **Abstract Colleague**: `Drawer`
- **Concrete Colleague**: `JavaFxDrawer`

The mediator (`GameMediator`) controls *what* should be rendered and *when*, while the drawer (`Drawer`) defines *how* rendering is performed.

Only the `Game` is exposed as the external system, from which the mediator retrieves state.

## Class Diagram (GoF Layout)

```mermaid
classDiagram
    direction LR

    class Game

    class GameDrawer {
        <<interface>>
        + renderGame()
        + drawBackground()
        + drawAgents()
        + drawPanel()
    }

    class GameMediator {
        - Game game
        - Drawer drawer
        + renderGame()
        + drawBackground()
        + drawAgents()
        + drawPanel()
    }

    class Drawer {
        <<abstract>>
        + drawImage(...)
        + drawLifeBar(...)
        + drawEnergyBar(...)
        + drawPanel(...)
        + drawMenu(...)
    }

    class JavaFxDrawer {
        - GraphicsContext gc
        + drawImage(...)
        + drawLifeBar(...)
        + drawEnergyBar(...)
        + drawPanel(...)
        + drawMenu(...)
    }

    GameDrawer <|.. GameMediator
    Drawer <|-- JavaFxDrawer
    GameMediator --> Drawer : uses
    GameMediator --> Game : reads
```

## Rendering Flow

```mermaid
sequenceDiagram
    actor GameLoop
    participant Mediator as GameMediator
    participant Game
    participant Drawer as JavaFxDrawer

    GameLoop->>Mediator: renderGame()
    Mediator->>Game: getEnvironment()
    Mediator->>Drawer: drawImage(background,...)

    loop entities
        Mediator->>Game: query state
        Mediator->>Drawer: drawImage(...)
        Mediator->>Drawer: drawLifeBar(...)
        Mediator->>Drawer: drawEnergyBar(...)
    end

    Mediator->>Drawer: drawMessages(...)
    Mediator->>Drawer: drawPanel(...)
```

## Interpretation

- The **Mediator** centralizes rendering logic and removes direct dependencies between the game model and the UI.
- The **Drawer hierarchy** encapsulates rendering technology (JavaFX in this case).
- The system is extensible: new rendering backends can be added without changing the mediator logic.

