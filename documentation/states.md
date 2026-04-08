# States Package

The `chon.group.game.states` package implements the game flow using the State Pattern.

Each state implements the `GameState` interface, which defines three core responsibilities:

- `handleInput(Game game)`
- `update(Game game)`
- `render(Game game)`

This package is responsible for controlling how the game behaves in each major mode, such as start menu, active gameplay, pause, story screens, game over, and victory.

## Main Classes

- **GameState**: interface for all game states.
- **StartState**: handles the initial menu and starting the game.
- **PlayableState**: handles main gameplay input, world update, transitions, and rendering.
- **PauseState**: handles the pause menu and paused-world behavior.
- **StoryState**: handles story/menu progression between playable sections.
- **GameOverState**: handles retry and reset after defeat.
- **WinState**: handles the final victory state.

## Class Diagram

```mermaid
classDiagram
    class GameState {
        <<interface>>
        +update(Game game)
        +handleInput(Game game)
        +render(Game game)
    }

    class StartState
    class PlayableState
    class PauseState
    class StoryState
    class GameOverState
    class WinState

    GameState <|.. StartState
    GameState <|.. PlayableState
    GameState <|.. PauseState
    GameState <|.. StoryState
    GameState <|.. GameOverState
    GameState <|.. WinState