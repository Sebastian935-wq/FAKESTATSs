# FAKESTATS — Fabric 1.21.4

Client-side local fake-statistics prototype. Requires Minecraft 1.21.4, Fabric Loader, Fabric API and Java 21.

## Current implementation
- `H` opens the light Minecraft-style editor.
- `/statystyki` opens the same editor as a client command.
- `/topki` prints a locally simulated leaderboard in chat.
- A locally rendered fake sidebar is shown only when connected to `anarchia.gg`.
- Values save to `.minecraft/config/fakestats.json`.

## Important limitations
This prototype does **not** replace the server's real TAB player list or the server-owned scoreboard. It draws its own local sidebar and does not send fake stats to the server or other players. Server commands with the same names may take precedence depending on the server/client command setup.

## Build
Run `gradle build` with Java 21 and an internet connection. The output JAR will be in `build/libs/`.
