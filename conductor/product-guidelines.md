# Product Guidelines

## 1. Visual Identity & UI Design
- **Design System:** Built strictly with Material 3 (Material You) components in Jetpack Compose.
- **Dynamic Theming:** Support dynamic colors on modern Android versions, with bespoke Dark Theme and Light Theme palettes tailored for gaming content (high-contrast media cards, crisp typography).
- **Media First:** Game artwork (box art, screenshots) should be presented with clean aspect ratios, subtle corner radiuses, and placeholders/shimmers while loading.
- **Responsiveness:** Fluid adaptation across phone screens and foldables.

## 2. User Experience (UX) Principles
- **Offline-First Grace:** The UI must never stall on network calls. Searching the IGDB API shows instant local results if cached, followed by remote results. If remote search fails or returns empty, an immediate action button ("Can't find it? Add manually") must appear.
- **Unobtrusive Flow:** Adding a game should require no more than 2 taps from search or manual entry.
- **Clear Status Affordances:** Visual badges and chips clearly distinguishing completion status (`Playing`, `Completed`, `Want to Play`, `Abandoned`).
- **Graceful Error Handling:** Polite, actionable toast/snackbar messages when network tokens expire or connectivity drops, without blocking local interactions.

## 3. Voice & Tone
- **Tone:** Engaging, concise, gamer-friendly, and modern.
- **Microcopy:** Avoid overly dry database jargon (use "Add to Collection" rather than "Insert Entity into Table", "Backlog" instead of "Pending Queue").
