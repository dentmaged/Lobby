package org.anchor.lobby.rank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Rank {

    @Getter private String prefix;
    @Getter private List<UUID> members;

    @Getter private static List<Rank> ranks = new ArrayList<Rank>();

    public Rank(String prefix, List<UUID> members) {
        this.prefix = ChatColor.translateAlternateColorCodes('`', ChatColor.translateAlternateColorCodes('&', prefix.replace("%star", "\u2756")));
        this.members = members;

        ranks.add(this);
    }

    public static String getPrefix(Player player) {
        String prefix = "";
        for (Rank rank : getPlayerRanks(player))
            prefix += rank.getPrefix();
        return prefix;
    }

    public static List<Rank> getPlayerRanks(Player player) {
        List<Rank> playerRanks = new ArrayList<Rank>();
        for (Rank rank : ranks) {
            if (rank.getMembers().contains(player.getUniqueId()))
                playerRanks.add(rank);
        }
        return playerRanks;
    }

    public static List<Rank> parseRanks(FileConfiguration config) {
        for (String child : config.getConfigurationSection("ranks").getKeys(false)) {
            ConfigurationSection cs = config.getConfigurationSection("ranks." + child);
            List<UUID> uuids = new ArrayList<UUID>();
            for (String string : cs.getStringList("members")) {
                uuids.add(UUID.fromString(string));
            }
            new Rank(cs.getString("prefix"), uuids);
        }
        return ranks;
    }

}
