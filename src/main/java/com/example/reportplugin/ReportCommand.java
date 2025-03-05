package com.example.reportplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /report <player> <reason>");
            return true;
        }

        String targetName = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName); // Supports both online and offline players

        if (!target.hasPlayedBefore() && target.getPlayer() == null) {
            sender.sendMessage(ChatColor.RED + "Player '" + targetName + "' not found.");
            return true;
        }

        String reason = String.join(" ", args).substring(targetName.length()).trim();
        sender.sendMessage(ChatColor.GREEN + "You have successfully reported " + target.getName() + " for '" + reason + "'.");
        sender.sendMessage(ChatColor.GREEN + "Our staff team will take a look shortly.");

        sendReportToDiscord(target.getName(), reason, sender.getName());
        return true;
    }

    private void sendReportToDiscord(String player, String reason, String reporter) {
        String webhookUrl = ReportPlugin.getInstance().getConfig().getString("webhook-url", "");

        if (webhookUrl.isEmpty()) {
            Bukkit.getLogger().warning("[ReportPlugin] Webhook URL is empty. Check config.yml.");
            return;
        }

        Bukkit.getLogger().info("[ReportPlugin] Preparing to send report to Discord...");

        JSONObject json = new JSONObject();
        json.put("content", "**New Report**\n" +
                "**Player:** `" + player + "`\n" +
                "**Reason:** `" + reason + "`\n" +
                "**Reported by:** `" + reporter + "`");

        new Thread(() -> {
            try {
                Bukkit.getLogger().info("[ReportPlugin] Sending webhook request...");
                URL url = new URL(webhookUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(json.toJSONString());
                writer.flush();
                writer.close();

                int responseCode = connection.getResponseCode();
                Bukkit.getLogger().info("[ReportPlugin] Webhook Response Code: " + responseCode);

                connection.getInputStream().close();
            } catch (Exception e) {
                Bukkit.getLogger().warning("[ReportPlugin] Error sending webhook: " + e.getMessage());
            }
        }).start();
    }
}
