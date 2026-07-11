package me.jaksara.inventory.listener

import io.papermc.paper.event.player.AsyncChatEvent
import me.jaksara.inventory.jplayer
import me.jaksara.inventory.plainString
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

internal class ChatInputListener(val plugin: Plugin) : Listener {
    @EventHandler
    fun onChat(e: AsyncChatEvent){
        val callback = e.player.jplayer().chatInputCallback ?: return
        e.isCancelled = true
        Bukkit.getScheduler().runTask(plugin, Runnable{
            callback.invoke(e.message().plainString())
        })
    }
}