package spectrum.sexplugin.hardcore

import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentIteratorFlag
import net.kyori.adventure.text.ComponentIteratorType
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.title.Title
import org.bson.types.ObjectId
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.litote.kmongo.findOne
import org.litote.kmongo.replaceOneById
import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.hardcore.models.Stat
import spectrum.sexplugin.hardcore.models.UserStatistics
import java.awt.TextComponent
import java.time.Duration

class HardcoreListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        var user = Mongo.UserStatistics.findOne(eq("username", event.player.name))
        if (user == null) {
            SexPlugin.plugin.logger.warning("${event.player.name} not found, creating")
            Mongo.UserStatistics.insertOne(
                UserStatistics(
                    ObjectId.get(), event.player.name, 0, System.currentTimeMillis(), emptyList<Stat>().toMutableList()
                )
            )
        } else {
            if (System.currentTimeMillis() > user.stats.last().timeToRespawn && user.lastServerTime < user.stats.last().timeToRespawn && event.player.gameMode == GameMode.SPECTATOR) {
                spawnPlayer(event.player)
            }
            if (System.currentTimeMillis() < user.stats.last().timeToRespawn) {
                respawnTask(user, event.player)
            }
            user = updateLastServerTime(user)
            Mongo.UserStatistics.replaceOneById(user._id, user)
        }
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        SexPlugin.plugin.logger.warning("player dead ${event.player.name}")
        var user = Mongo.UserStatistics.findOne(eq("username", event.player.name))
        if (user == null) {
            event.player.gameMode = GameMode.SURVIVAL
            event.player.spawnAt(Location(Bukkit.getWorld(event.player.name), 0.0, 80.0, 0.0))
            return
        }
        if(user.stats.size > 0) {
            if (System.currentTimeMillis() < user.stats.last().timeToRespawn) {
                return
            }
        }
        user = updateTimeOnServer(user)
        user = updateLastServerTime(user)
        val death = event.deathMessage()!!
        var deathReason = ""
        var deathIssuer = ""
            death.iterator(
                ComponentIteratorType.DEPTH_FIRST,
                ComponentIteratorFlag.INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS,
                ComponentIteratorFlag.INCLUDE_HOVER_SHOW_TEXT_COMPONENT).forEach {
                when (it) {
                    is TranslatableComponent -> {
                        deathReason = it.key()
                    }
                    is TextComponent -> {
                        deathIssuer = it.text
                    }
                }
            }
        user.stats.add(
            Stat(
                user.timeOnServer / 2 + System.currentTimeMillis(),
                System.currentTimeMillis(),
                deathReason,
                deathIssuer
            )
        )
        Mongo.UserStatistics.replaceOneById(user._id, user)
        //Game Logic
    }
    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent)
    {
        val user = Mongo.UserStatistics.findOne(eq("username", event.player.name))
        if (user == null) {
            event.player.gameMode = GameMode.SURVIVAL
            event.player.spawnAt(Location(Bukkit.getWorld(event.player.name), 0.0, 80.0, 0.0))
            return
        }
        respawnTask(user, event.player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        var user = Mongo.UserStatistics.findOne(eq("username", event.player.name))
        if (event.player.gameMode == GameMode.SPECTATOR || event.player.isDead) {
            return
        }
        user = updateTimeOnServer(user!!)
        user = updateLastServerTime(user)
        Mongo.UserStatistics.replaceOneById(user._id, user)
    }

    private fun updateTimeOnServer(user: UserStatistics): UserStatistics {
        return user.copy(timeOnServer = user.timeOnServer + System.currentTimeMillis() - user.lastServerTime)
    }

    private fun updateLastServerTime(user: UserStatistics): UserStatistics {
        return user.copy(lastServerTime = System.currentTimeMillis())
    }

    private fun spawnPlayer(player: Player) {
        if(player.gameMode == GameMode.SPECTATOR) {
            player.teleport(Bukkit.getWorld("World")!!.spawnLocation)
            player.gameMode = GameMode.SURVIVAL
        }
    }

    private fun respawnTask(user: UserStatistics, player: Player) {
        SexPlugin.defaultScope.launch {
            var message = false
            val startTime = user.stats.last().deathTime
            var bartext = "До возрождения: " + DateUtil.genStringTime(user.stats.last().timeToRespawn - System.currentTimeMillis())
            var bar = net.kyori.adventure.bossbar.BossBar.bossBar(
                Component.text(bartext),
                0.0f,
                net.kyori.adventure.bossbar.BossBar.Color.GREEN,
                net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS
            )
            while (System.currentTimeMillis() < user.stats.last().timeToRespawn && this.isActive) {
                val progress = ((user.stats.last().timeToRespawn - System.currentTimeMillis()).toFloat() / (user.stats.last().timeToRespawn - startTime).toFloat())
                Thread.sleep(100)
                bartext = "До возрождения: " + DateUtil.genStringTime(user.stats.last().timeToRespawn - System.currentTimeMillis())
                try {
                    if (!player.isDead) {
                        //Show title
                        if (!message) {
                            val title = Title.title(
                                Component.text(HardcoreModule.hardcoreConfig.getString("title")!!),
                                Component.text(HardcoreModule.hardcoreConfig.getString("subtitle")!!),
                                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(5), Duration.ofSeconds(1))
                            )
                            player.showTitle(title)
                            message = true
                        }
                        player.hideBossBar(bar)
                        val newbar = net.kyori.adventure.bossbar.BossBar.bossBar(
                            Component.text(bartext),
                            progress,
                            net.kyori.adventure.bossbar.BossBar.Color.GREEN,
                            net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS
                        )
                        player.showBossBar(newbar)
                        bar = newbar
                    }
                } catch (_: Exception) {
                    SexPlugin.plugin.logger.warning("Respawn task ended but player has been left. Aborting...")
                    return@launch
                }
            }
            launch(SexPlugin.MainDispatcher) {
                try {
                    player.hideBossBar(bar)
                    spawnPlayer(player)
                } catch (_: Exception) {
                }
            }
        }
    }
}