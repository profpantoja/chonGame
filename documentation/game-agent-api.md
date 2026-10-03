# Chon Game External Agent API

The game exposes a newline-delimited JSON protocol over TCP. Jason, CArtAgO and Moise run in a separate JVM and connect as clients.

## Connection

The default endpoint is `localhost:8765`. Each message is one JSON object followed by a newline. The game can run with `LOCAL`, `API` or `HYBRID` control configured in `game.json`. `HYBRID` keeps the JavaFX keyboard for the active protagonist and enables this gateway for MAS clients.

The server sends:

```json
{"type":"hello","protocolVersion":1}
```

After `hello`, a MAS client requests one team and declares its stable Jason slots:

```json
{"type":"join","teamId":"enemies","slots":["enemy_1","enemy_2"]}
```

There is at most one connected MAS client per team. A team not present in the current level, or already owned by another client, remains pending and produces one `team_waiting` event. When available, the client receives `team_assigned`; the event's `reason` is the assigned team ID. For declared `slots`, allocation follows the agent declaration order in that level's `game.json`. If `slots` is omitted, runtime agent IDs act as implicit slots and an agent becomes MAS-owned only when the client sends an action for its current ID; unaddressed members remain autonomous. Declare slots for stable Jason-to-game-agent ownership and deterministic replacement.

## Observations

The game publishes an observation after each processed game tick:

```json
{
  "type": "observation",
  "protocolVersion": 1,
  "payload": {
    "tick": 10,
    "state": "PlayableState",
    "self": {
      "id": "entity-id",
      "kind": "Agent",
      "teamId": "allies",
      "controlOwner": "LOCAL",
      "x": 340,
      "y": 450,
      "health": 80,
      "maxHealth": 100,
      "direction": "RIGHT",
      "status": "IDLE",
      "dead": false
    },
    "agents": [],
    "objects": [],
    "shots": [],
    "score": 0,
    "collectedCount": 0,
    "completed": false
  }
}
```

Entity IDs remain stable until the entity is recreated by a reset or level change. The `tick` value is monotonic during a session.

`controlOwner` is one of `LOCAL`, `MAS`, `AI` or `DORMANT`. It identifies who currently owns the agent, independently of its `teamId`. In `HYBRID`, the active protagonist is reserved for `LOCAL`; MAS clients can control only their assigned, free team members. Agents not assigned to an MAS slot remain autonomous.

The server sends assignment lifecycle events to the owning client. For example:

```json
{"type":"agent_reassigned","slotId":"enemy_1","previousAgentId":"entity-12","agentId":"entity-18","reason":null}
```

When the controlled game agent dies, the same Jason slot is reassigned to the next living, free member of the same team in `game.json` order. If there is no replacement, the server sends `agent_dead` and `agent_dormant` with reason `NO_FREE_AGENT`; the Jason slot remains dormant. A client never receives an agent from another team. On a local-protagonist handoff, `agent_released` with reason `LOCAL_PLAYER_CONTROL` indicates that the protagonist was reclaimed by the human player.

## Actions

An external agent sends:

```json
{
  "type": "action",
  "protocolVersion": 1,
  "requestId": "request-1",
  "agentId": "agent-1",
  "slotId": "enemy_1",
  "expectedTick": 10,
  "action": {
    "name": "MOVE",
    "direction": "RIGHT"
  }
}
```

`agentId` is the runtime member currently assigned to `slotId`. The gateway verifies both against the live assignment, so stale IDs are ignored. Clients using implicit slots may omit `slotId`; the gateway resolves the current member ID to its slot.

Supported action names are `MOVE`, `ATTACK`, `CONFIRM`, `PAUSE`, `MENU_UP`, `MENU_DOWN`, `MENU_LEFT` and `MENU_RIGHT`. The aliases `UP`, `DOWN`, `LEFT` and `RIGHT` also press the corresponding menu command. `MOVE` expects a `direction` and keeps that direction held; the menu commands and `ATTACK` are single-frame presses. In `HYBRID`, only `MOVE` and `ATTACK` are accepted, and only while the game is in `PlayableState`; menu/global actions are rejected with `GLOBAL_ACTION_DISABLED`, and actions from other states are discarded. In `API`, the legacy global actions continue to target the external player joystick. The local player remains the owner of the hybrid protagonist.

When an MAS client disconnects, its allied agents become idle/dormant and its enemy agents return to their configured AI behavior, which is normally to chase living allies. The local protagonist is unaffected. Victory/progression remains team-based: all level enemies must be defeated and the allied protagonist/team must reach the level end, regardless of whether agents are controlled by the player, an MAS, or AI.

Invalid or stale actions are ignored by the game thread. The acknowledgement only confirms that the gateway accepted the message; the next observation is the authoritative result.

## Threading model

Network threads enqueue actions. Only the game thread mutates the game model. Outbound observations use a per-client queue so socket I/O cannot block JavaFX rendering.