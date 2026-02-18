package com.invadermonky.attributekeeper.commands;

import com.invadermonky.attributekeeper.AttributeKeeper;
import com.invadermonky.attributekeeper.config.FileHandlerAK;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandReloadConfigAK extends CommandBase {
    @Override
    public @NotNull String getName() {
        return AttributeKeeper.MOD_ID + "_reload";
    }

    @Override
    public @NotNull List<String> getAliases() {
        return Collections.singletonList("ak_reload");
    }

    @Override
    public @NotNull String getUsage(@NotNull ICommandSender sender) {
        return new TextComponentTranslation("command.ak_reload.usage").getUnformattedText();
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args) throws CommandException {
        if(args.length > 0) {
            throw new CommandException("command.ak_reload.invalid_usage");
        }
        FileHandlerAK.syncConfig();
        sender.sendMessage(new TextComponentTranslation("command.ak_reload.success"));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
