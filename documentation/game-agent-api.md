# Chon Game External Agent API

The game exposes a newline-delimited JSON protocol over TCP. Jason, CArtAgO and Moise run in a separate JVM and connect as clients.

## Connection

The default endpoint is `localhost:8765`. Each message is one JSON object followed by a newline.

The server sends:

```json
{"type":"hello","protocolVersion":1}
```

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

## Actions

An external agent sends:

```json
{
  "type": "action",
  "protocolVersion": 1,
  "requestId": "request-1",
  "agentId": "agent-1",
  "expectedTick": 10,
  "action": {
    "name": "MOVE",
    "direction": "RIGHT"
  }
}
```

Supported action names are `MOVE`, `ATTACK`, `CONFIRM`, `PAUSE`, `MENU_UP`, `MENU_DOWN`, `MENU_LEFT` and `MENU_RIGHT`. The aliases `UP`, `DOWN`, `LEFT` and `RIGHT` also press the corresponding menu command. `MOVE` expects a `direction` and keeps that direction held; the menu commands and `ATTACK` are single-frame presses. Invalid or stale actions are ignored by the game thread. The acknowledgement only confirms that the gateway accepted the message; the next observation is the authoritative result.

## Threading model

Network threads enqueue actions. Only the game thread mutates the game model. Outbound observations use a per-client queue so socket I/O cannot block JavaFX rendering.