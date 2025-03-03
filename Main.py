import discord
import json
import os
from cogs.ticket_commands import Ticket_Command
from cogs.ticket_system import Ticket_System

with open("config.json", mode="r") as config_file:
    config = json.load(config_file)


TOKEN = config["token"]
ANNOUNCEMENT_CHANNEL_ID =1345974668540121240   # Replace with your channel ID
WHITELISTED_USERS = [905089893573726318,1313023808571052085] # Replace with allowed user IDs

# Load announcements
ANNOUNCEMENT_FILE = "announcements.json"
if os.path.exists(ANNOUNCEMENT_FILE):
    with open(ANNOUNCEMENT_FILE, "r") as f:
        announcements = json.load(f)
else:
    announcements = {}

bot = discord.Bot()

@bot.event
async def on_ready():
    print(f"Logged in as {bot.user}")

@bot.slash_command(name="announcement", description="Send an announcement (whitelisted users only)")
async def announcement(ctx, text: str):
    if ctx.author.id not in WHITELISTED_USERS:
        return await ctx.respond("❌ You are not authorized to use this command.", ephemeral=True)
    
    channel = bot.get_channel(ANNOUNCEMENT_CHANNEL_ID)
    if not channel:
        return await ctx.respond("❌ Announcement channel not found.", ephemeral=True)

    # Generate a new announcement ID
    announcement_id = str(len(announcements) + 1)

    # Send the announcement
    message = await channel.send(f"📢 **Announcement {announcement_id}:** {text}")

    # Store the announcement
    announcements[announcement_id] = {"message_id": message.id, "text": text}
    with open(ANNOUNCEMENT_FILE, "w") as f:
        json.dump(announcements, f, indent=4)

    await ctx.respond(f"✅ Announcement sent! (ID: {announcement_id})", ephemeral=True)

@bot.slash_command(name="announcementedit", description="Edit an existing announcement (whitelisted users only)")
async def announcementedit(ctx, id: str, text: str):
    if ctx.author.id not in WHITELISTED_USERS:
        return await ctx.respond("❌ You are not authorized to use this command.", ephemeral=True)
    
    if id not in announcements:
        return await ctx.respond("❌ Invalid announcement ID.", ephemeral=True)

    channel = bot.get_channel(ANNOUNCEMENT_CHANNEL_ID)
    if not channel:
        return await ctx.respond("❌ Announcement channel not found.", ephemeral=True)

    try:
        message = await channel.fetch_message(announcements[id]["message_id"])
        await message.edit(content=f"📢 **Announcement {id}:** {text}")

        # Update the announcement in the JSON file
        announcements[id]["text"] = text
        with open(ANNOUNCEMENT_FILE, "w") as f:
            json.dump(announcements, f, indent=4)

        await ctx.respond(f"✅ Announcement {id} edited successfully!", ephemeral=True)
    except discord.NotFound:
        await ctx.respond("❌ Message not found. It may have been deleted.", ephemeral=True)
        

bot.add_cog(Ticket_System(bot))
bot.add_cog(Ticket_Command(bot))


bot.run(TOKEN)




