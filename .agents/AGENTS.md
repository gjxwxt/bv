# BV (Bug Video) - Agent Operations Guide

Welcome to the **BV** workspace! This is a third-party Bilibili client adapted for Android Mobile and Android TV, built natively with Kotlin and Jetpack Compose.

## 1. Project Context & Principles
- **Target Platforms**: Android (Mobile & TV). Ensure UI components adapt properly to their respective form factors (check `:app:mobile` vs `:app:tv`).
- **Tech Stack**: Kotlin, Jetpack Compose, Coroutines.
- **Architecture**: Heavily modularized Gradle project (`:app`, `:bili-api`, `:player`, `:libs`).
- **Media Playback**: Includes custom native decoders and players (`:libs:av1Decoder`, `:libs:ffmpegDecoder`, `:libs:libVLC`).

## 2. Execution Rules
- **Build System**: Always use `./gradlew` from the project root for build tasks (e.g., `./gradlew assembleDebug`). 
- **Dependencies**: Centralized in `gradle/` (`gradle.versions.toml`, `androidx.versions.toml`). Do not hardcode versions in individual `build.gradle.kts` files.
- **UI Framework**: Strictly use Jetpack Compose for UI development. Avoid legacy XML layouts.
- **API Logic**: Bilibili API requests and definitions belong in the `:bili-api` module. Keep UI and network logic decoupled.

## 3. Lazy Loading Context (The "Skill" Pattern)
To maintain a concise context window (< 100 lines), detailed technical analyses, API reverse-engineering notes, and player architecture specs are stored in the `docs/` directory.

**MANDATORY**: Before implementing complex UI components, modifying player cores, or adding new Bilibili API endpoints, you MUST read the relevant detailed documentation in the `docs/` folder using file viewing tools.

*Rule for Agents: If you are asked to produce extensive architectural analysis, document a newly discovered Bilibili API endpoint, or debug complex Media3/VLC issues, write it as a new Markdown file in the `docs/` folder. Do NOT expand this file.*
