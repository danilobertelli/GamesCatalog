# Initial Concept

GamesCatalog is an Android application designed for gamers to organize and track their personal video game library. Users can catalogue games they have played, rate them, mark completion statuses (e.g., Backlog/Want to Play, Playing, Completed, Abandoned), and write personal notes.

The application integrates with the IGDB API (https://api-docs.igdb.com/) as an enhancement layer to search, discover, and autofill game metadata and cover art. Crucially, the app adopts an **offline-first and flexible philosophy**: if an API search yields no results or if the device is offline, users can seamlessly add and manage games manually.

# Product Guide

## 1. Vision & Core Value Proposition
- **Seamless Logging:** Make logging and managing games effortless, intuitive, and fun.
- **Offline-First Reliability:** Every feature works locally without requiring network connectivity. The remote IGDB API acts as a companion service to enrich data, never as a hard blocker.
- **Player Autonomy:** If a game is indie, unreleased, retro, or missing from online databases, the player can manually register title, platforms, genre, release date, and cover images.

## 2. Target Audience
- Gamers with backlogs who want to track what they play across multiple platforms (PC, PlayStation, Xbox, Nintendo, mobile, retro).
- Enthusiasts who like scoring games and keeping a personal gaming journal.

## 3. Key Functional Capabilities
1. **Game Library & Collection:**
   - View library grouped or filtered by status (`Want to Play`, `Playing`, `Completed`, `Dropped/Abandoned`).
   - Filter and sort by rating, release date, title, or platform.
2. **Game Details & Journaling:**
   - Assign user rating (e.g., 1-5 stars or 1-10 rating).
   - Track hours played, completion dates, and personal thoughts/reviews.
3. **IGDB Integration ("Plus" Experience):**
   - Live query of IGDB database to search titles, retrieve synopsis, developer/publisher, release year, genre tags, and official cover art.
4. **Manual Game Entry & Edit:**
   - Dedicated fallback workflow to manually input all metadata and attach custom or local artwork.
5. **Data Ownership:**
   - Local database persistence guaranteeing instant responsiveness and zero network dependency.
