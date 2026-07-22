package me.jaksara.inventory

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalListener
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.time.Duration
import java.util.UUID

internal val AIR: ItemStack = ItemStack(Material.AIR)
internal val cachedKey = HashMap<String, NamespacedKey>()
internal val cache: Cache<UUID, PlayerData> = Caffeine.newBuilder()
    .expireAfterAccess(Duration.ofMinutes(20))
    .removalListener(RemovalListener<UUID, PlayerData> { key, value, cause ->
    }
    )
    .build()

internal fun Player.jplayer(): PlayerData {
    return cache.get(uniqueId) {
        PlayerData(uniqueId)
    }
}

internal fun String.info(player: Player) {
    val message = this.deserialize().color(NamedTextColor.GREEN)
    player.sendMessage(message)
}

internal fun String.error(plugin: Plugin, player: Player? = null) {
    val message = this.deserialize().color(NamedTextColor.GREEN)
    player?.sendMessage(message) ?: plugin.logger.log(java.util.logging.Level.WARNING, this)
}

internal fun String.deserialize(vararg tags: TagResolver): Component = MiniMessage.miniMessage().deserialize(this, *tags).decoration(TextDecoration.ITALIC, false)
internal fun String.regex(): String = lowercase().replace(Regex("[^a-z0-9._/-]"), "_")
internal fun String.namespacedKey(): NamespacedKey = cachedKey.getOrPut(this) { NamespacedKey("jaksara", regex()) }
internal fun Component.plainString() = rawString()
internal fun Component.rawString(): String = PlainTextComponentSerializer.plainText().serialize(this)