package org.anchor.lobby.commands;

import org.anchor.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;

public class LobbyCommands {

    @Command(aliases = { "setspawn" }, desc = "Sets the spawn", max = 0)
    @CommandPermissions("lobby.manage.setspawn")
    public static void setSpawn(CommandContext args, CommandSender sender) {
        if (!(sender instanceof Player))
            throw new CommandException("Only players may run this command!");

        Location location = ((Player) sender).getLocation();
        
        Lobby.getInstance().getConfig().set("spawn", location.getWorld() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch());
        Lobby.getInstance().setSpawn(location);
        sender.sendMessage(ChatColor.GREEN + "The spawn has been set successfully!");
    }

    public static class LobbyParentCommand {

        @Command(aliases = "lobby", desc = "Commands for managing the lobby", usage = "<setspawn>")
        @CommandPermissions("lobby.manage")
        @NestedCommand(LobbyCommands.class)
        public static void tourney(CommandContext args, CommandSender sender) {

        }

        @Command(aliases = { "spawn" }, desc = "Teleports to spawn", max = 0)
        @CommandPermissions("lobby.spawn")
        public static void setSpawn(CommandContext args, CommandSender sender) {
            if (!(sender instanceof Player))
                throw new CommandException("Only players may run this command!");

            ((Player) sender).teleport(Lobby.getInstance().getSpawn());
        }

    }

}
