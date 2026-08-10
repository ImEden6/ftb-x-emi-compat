# Changelog

All notable changes to this project will be documented in this file.

## [1.0.3] - 2026-08-10

### Fixed

- **Crash after reloading resources with AmbientSounds installed**: reloading resources (e.g. pressing F3+T) could leave ambient sounds in a broken state that crashed the game on every subsequent tick.

## [1.0.1] - 2026-08-04

### Fixed

- **Crash colliding with FTB XMod Compat**: `FTBQuests.setRecipeModHelper` throws if called more than once, and both this mod and FTB XMod Compat register their own helper unconditionally, crashing the client during init when both are installed ([#1](https://github.com/ImEden6/ftb-x-emi-compat/issues/1)). A mixin now intercepts the second registration attempt and ignores it (with a log warning) instead of letting the game crash.
