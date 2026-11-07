# KiteBlocks Custom Enchants v3 (source project)

Minecraft Paper 26.2 / Java 25; keeps all 62 v2 enchant names and previous custom-enchant keys.

## Changes
- Basic (5,000 XP), Advanced (20,000 XP), Extreme (50,000 XP) books still use **MultiCurrency `xp`**.
- White and Black Scrolls cost **$5,000** each via **Vault economy** (EssentialsX economy + Vault). Scrolls still improve success or reduce item-break chance by 1–10 percentage points.
- All generated enchant books have **Applies to: [equipment]** in their lore.
- `/kbenchant vanilla <name> <level>` lets server OP/admin apply high vanilla enchants directly to the held compatible item. Supports `sharpness 7`, `unbreaking 5`, `looting 5`, `fire_aspect 4`, `protection 7`, `bane_of_arthropods 5`, and `smite 5`. Unsafe vanilla enchant levels/conflicts are allowed (Bane of Arthropods can coexist with Sharpness and Smite). Admin-only.
- `/enchanter` now has **Reroll** and **Grinder** screens. Put one custom enchant book per slot into slots 10–12. Three of the same tier can be rerolled into a random book of that tier. The grinder accepts 1–3 books and refunds MultiCurrency XP (default Basic 1000, Advanced 4000, Extreme 10000 per book). Unused books are returned on closing the GUI. Grinder deposit failure refunds books.
- Custom enchants can still be added separately, with `/kbenchant apply <id> <level>` and book drag & drop.

## Important installation notes
1. Back up world and plugin config. Remove old version's JAR before installing this version.
2. Upload this project's **contents**, keeping `src/`, `libs/`, `.github/workflows/` and Gradle files intact, to a GitHub repository.
3. Build in Actions; download the artifact ZIP, **extract** the actual compiled JAR, and install only that JAR on Minehut.
4. Make sure `Vault`, `EssentialsX` economy, and `MultiCurrency` (with enabled `xp`) are installed. VaultAPI is needed for compilation only, not installed as a separate server plugin.
5. Since an old `plugins/KiteBlocksCustomEnchants/config.yml` won't automatically receive updated settings, update `shop.white-scroll-price` and `shop.black-scroll-price` to **5000** manually and add the `grinder` section from `src/main/resources/config.yml`.
6. Test each mechanic on a staging server with throwaway equipment before giving access to players.

**Limitations:** This retains v2's incomplete gameplay effect implementations for some custom enchant IDs. This ZIP is source code, not an independently compile-verified JAR. The conversion GUI and the Vault dependency require GitHub build and server testing.
