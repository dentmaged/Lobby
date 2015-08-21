package org.anchor.lobby;

import lombok.Getter;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;

public abstract class CommandPlugin extends JavaPlugin {

    @Getter private CommandsManager<CommandSender> commands;

    public void onEnable() {
        setupCommands();
        enable();
    }

    public abstract void enable();

    public void onDisable() {
        disable();
    }

    public abstract void disable();

    private void setupCommands() {
        this.commands = new BukkitCommandsManager();
        CommandsManagerRegistration register = new CommandsManagerRegistration(this, this.commands);
        registerCommands(commands, register);
    }

    public abstract void registerCommands(CommandsManager<CommandSender> commands, CommandsManagerRegistration register);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string recieved instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "An unknown error occured. Please refer back to the server console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

}
