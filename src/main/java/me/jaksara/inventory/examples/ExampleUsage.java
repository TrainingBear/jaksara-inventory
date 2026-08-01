package me.jaksara.inventory.examples;

import me.jaksara.inventory.CustomMenu;
import me.jaksara.inventory.ClickableButton;
import me.jaksara.inventory.InventoryMenuDsl;
import me.jaksara.inventory.ExecutionContext;
import org.bukkit.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class ExampleUsage {
    /**
     * Call this from your plugin's onEnable to register an example menu.
     * Demonstrates the new Java-friendly DSL that mirrors the Kotlin DSL.
     */
    public static void register(Plugin plugin) {
        // Initialize framework listeners
        CustomMenu.init(plugin);

        // Create menu instance (Java-friendly)
        InventoryMenuDsl menu = CustomMenu.createMenu("<blue>Example Menu", plugin);

        // Set layout (2 rows)
        menu.layout(
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 1, 0, 2, 0, 3, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0
        );
        menu.border(0, Material.GRAY_STAINED_GLASS_PANE);

        // Add a simple button using the DSL-style Java overload
        menu.button(1, btn -> {
            btn.material(Material.DIAMOND);
            btn.title("<green>Click me");
            btn.onClick(ctx -> {
                Player p = ctx.getPlayer();
                p.sendMessage("Button clicked from Java DSL example!");
            });
        });

        // Add an option button using optionButton overload with Consumer
        menu.optionButton(2, Material.PAPER, "<yellow>Choose", new ArrayList<>(), Arrays.asList("One", "Two", "Three"), 0, true, (ctx, sel) -> {
            // selection callback
            plugin.getLogger().info("Selected option: " + sel);
        });

        // Add a listButton using listButton overload with Consumer
        ArrayList<String> list = new ArrayList<>();
        list.add("alpha");
        menu.listButton(3, Material.BOOK, "<aqua>List", new ArrayList<>(), list, true,
                (ctx, l) -> {
                    plugin.getLogger().info("List changed: " + l);
                }
        );

        // Build is done internally in createMenu; opening is up to caller
    }
}
