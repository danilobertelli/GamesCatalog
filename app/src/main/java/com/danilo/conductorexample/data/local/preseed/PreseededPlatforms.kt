package com.danilo.conductorexample.data.local.preseed

import com.danilo.conductorexample.data.local.entity.PlatformEntity

object PreseededPlatforms {
    val list: List<PlatformEntity> = listOf(
        // PlayStation
        PlatformEntity("ps5", "PlayStation 5"),
        PlatformEntity("ps4", "PlayStation 4"),
        PlatformEntity("ps3", "PlayStation 3"),
        PlatformEntity("ps2", "PlayStation 2"),
        PlatformEntity("ps1", "PlayStation"),
        PlatformEntity("psp", "PlayStation Portable (PSP)"),
        PlatformEntity("psvita", "PlayStation Vita"),

        // Xbox
        PlatformEntity("xbox_series", "Xbox Series X/S"),
        PlatformEntity("xbox_one", "Xbox One"),
        PlatformEntity("xbox_360", "Xbox 360"),
        PlatformEntity("xbox_original", "Xbox (Original)"),

        // Nintendo
        PlatformEntity("switch", "Nintendo Switch"),
        PlatformEntity("wii_u", "Nintendo Wii U"),
        PlatformEntity("wii", "Nintendo Wii"),
        PlatformEntity("gamecube", "Nintendo GameCube"),
        PlatformEntity("n64", "Nintendo 64"),
        PlatformEntity("snes", "Super Nintendo (SNES)"),
        PlatformEntity("nes", "Nintendo Entertainment System (NES)"),
        PlatformEntity("3ds", "Nintendo 3DS"),
        PlatformEntity("ds", "Nintendo DS"),
        PlatformEntity("gba", "Game Boy Advance"),
        PlatformEntity("gbc", "Game Boy Color"),
        PlatformEntity("gb", "Game Boy"),

        // PC & Handhelds
        PlatformEntity("pc", "PC (Windows)"),
        PlatformEntity("mac", "Mac"),
        PlatformEntity("linux", "Linux"),
        PlatformEntity("steam_deck", "Steam Deck"),

        // Mobile & Other
        PlatformEntity("android", "Android"),
        PlatformEntity("ios", "iOS"),
        PlatformEntity("other", "Outro")
    )
}
