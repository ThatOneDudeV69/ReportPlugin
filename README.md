# Report Plugin for Paper 1.21.4

A simple Paper plugin that allows players to report others, sending the report to a webhook URL specified in `config.yml`. The webhook URL is automatically replaced in the config after the plugin runs for security.

## Features
- Allows players to report others using a command.
- Sends report details to a Discord webhook.
- The webhook URL is removed from `config.yml` after the first run for security.
- Simple and lightweight.

## Commands
- `/report <player> <reason>` - Reports a player for a specific reason.

## Permissions
- `report.use` - Allows a player to use the `/report` command.

## Installation
1. Download the latest release of the plugin.
2. Place the `.jar` file in your server’s `plugins` folder.
3. Start the server to generate the `config.yml` file.
4. Edit `plugins/ReportPlugin/config.yml` and set your Discord webhook URL.
5. Restart the server.

## Configuration (`config.yml`)
```yaml
webhook_url: "YOUR_WEBHOOK_HERE"
