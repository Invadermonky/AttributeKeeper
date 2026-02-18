/*  MIT License
 *  
 *  Copyright (c) 2024 ACGaming
 *  
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.invadermonky.attributekeeper.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * CommandPlayerAttribute is a variation of the Player Attribute Command mod, included courtesy of ACGaming and Ski_Z.
 * <p>
 * Original mod (MIT License):<br>
 * <a href="https://www.curseforge.com/minecraft/mc-mods/player-attribute-commands">Player Attribute Commands download</a><br>
 * <a href="https://github.com/ACGaming/PlayerAttributeCommands">Player Attribute Commands source</a>
 */
public class CommandPlayerAttribute extends CommandBase {
    @Override
    public @NotNull String getName() {
        return "attributekeeper";
    }

    @Override
    public @NotNull List<String> getAliases() {
        return Collections.singletonList("ak");
    }

    @Override
    public @NotNull String getUsage(@NotNull ICommandSender sender) {
        return new TextComponentTranslation("command.playerattribute.usage").getUnformattedText();
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 3) {
            throw new CommandException("command.playerattribute.invalid_usage");
        }

        EntityPlayer player = getPlayer(server, sender, args[0]);
        IAttributeInstance attributeInstance = player.getAttributeMap().getAttributeInstanceByName(args[1]);

        if (attributeInstance == null) {
            throw new CommandException("command.playerattribute.invalid_attribute", args[1]);
        }

        String action = args[2];
        double value = args.length == 4 ? parseDouble(args[3]) : 0;

        switch (action) {
            case "get":
                sender.sendMessage(new TextComponentTranslation("command.playerattribute.get", attributeInstance.getAttribute().getName(), attributeInstance.getBaseValue()));
                break;
            case "add":
                attributeInstance.setBaseValue(attributeInstance.getBaseValue() + value);
                updatePlayerCapabilities(player, attributeInstance);
                sender.sendMessage(new TextComponentTranslation("command.playerattribute.add", attributeInstance.getAttribute().getName(), value, attributeInstance.getBaseValue()));
                break;
            case "set":
                attributeInstance.setBaseValue(value);
                updatePlayerCapabilities(player, attributeInstance);
                sender.sendMessage(new TextComponentTranslation("command.playerattribute.set", attributeInstance.getAttribute().getName(), value));
                break;
            case "mult":
                attributeInstance.setBaseValue(attributeInstance.getBaseValue() * value);
                updatePlayerCapabilities(player, attributeInstance);
                sender.sendMessage(new TextComponentTranslation("command.playerattribute.mult", attributeInstance.getAttribute().getName(), value, attributeInstance.getBaseValue()));
                break;
            default:
                throw new CommandException("command.playerattribute.unknown_action", action);
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public @NotNull List<String> getTabCompletions(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else if (args.length == 2) {
            EntityPlayer player;
            try {
                player = getPlayer(server, sender, args[0]);
            } catch (CommandException e) {
                return new ArrayList<>();
            }

            AbstractAttributeMap attributeMap = player.getAttributeMap();
            Collection<IAttributeInstance> attributes = attributeMap.getAllAttributes();
            List<String> attributeNames = new ArrayList<>();
            for (IAttributeInstance attribute : attributes) {
                attributeNames.add(attribute.getAttribute().getName());
            }
            return getListOfStringsMatchingLastWord(args, attributeNames.toArray(new String[0]));
        } else if (args.length == 3) {
            return getListOfStringsMatchingLastWord(args, "get", "add", "set", "mult");
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Override
    public boolean isUsernameIndex(String @NotNull [] args, int index) {
        return index == 0;
    }

    private void updatePlayerCapabilities(EntityPlayer player, IAttributeInstance attributeInstance) {
        if ("generic.movementSpeed".equals(attributeInstance.getAttribute().getName())) {
            player.capabilities.walkSpeed = (float) attributeInstance.getBaseValue();
        }
    }
}
