package org.anchor.lobby;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.anchor.lobby.commands.LobbyCommands;
import org.anchor.lobby.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandsManager;

public class Lobby extends CommandPlugin implements Listener {

    @Getter @Setter private Location spawn;

    @Getter private static Lobby instance;

    @Override
    public void enable() {
        Lobby.instance = this;

        FileConfiguration config = getConfig();
        config.addDefault("walk-speed", 0.4);
        config.addDefault("spawn", "world;383.5;68;397.5;-90;0");
        config.addDefault("ranks.owner.prefix", ChatColor.GOLD + "%star");
        config.addDefault("ranks.owner.members", Arrays.asList("8d02e486-66d5-46bf-8e6b-81e18ef9e6c7") /* TiTo_418 */);
        config.options().copyDefaults(true);
        this.saveDefaultConfig();
        Rank.parseRanks(config);

        List<String> spawnList = Arrays.asList(config.getString("spawn").split(";"));
        World world = Bukkit.getWorld(spawnList.get(0));
        spawn = new Location(world, Double.parseDouble(spawnList.get(1)), Double.parseDouble(spawnList.get(2)), Double.parseDouble(spawnList.get(3)), Float.parseFloat(spawnList.get(4)), Float.parseFloat(spawnList.get(5)));

        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Lobby is now enabled!");
    }

    @Override
    public void disable() {
        getLogger().info("Lobby is now disabled!");
        Lobby.instance = null;
    }

    @Override
    public void registerCommands(CommandsManager<CommandSender> manager, CommandsManagerRegistration register) {
        register.register(LobbyCommands.LobbyParentCommand.class);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        player.setPlayerListName(Rank.getPrefix(player) + ChatColor.AQUA + player.getName());
        player.setDisplayName(Rank.getPrefix(player) + ChatColor.AQUA + player.getName());
        player.setWalkSpeed(getConfig().getLong("walk-speed"));
        for (Player bukkit : Bukkit.getOnlinePlayers()) {
            if (bukkit.equals(player))
                continue;

            player.sendMessage(player.getDisplayName() + ChatColor.YELLOW + " joined the game.");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.equals(event.getPlayer()))
                continue;

            player.sendMessage(event.getPlayer().getDisplayName() + ChatColor.YELLOW + " left the game.");
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getY() <= -64) {
            event.getPlayer().teleport(spawn);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

}
