package com.gmail.trentech.pjb.commands;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import com.gmail.trentech.pjb.utils.Help;

public class CMDCenter implements CommandExecutor {

	public CMDCenter() {
		Help help = new Help("center", "center", " Set the center coordinates of border");
		help.setPermission("pjb.cmd.border.center");
		help.setSyntax(" /border center <world> <x> <z>\n /b d <world> <x> <z>");
		help.setExample(" /border center MyWorld 100 -250");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		WorldProperties properties = args.<WorldProperties> getOne("world").get();

		Optional<World> optionalWorld = Sponge.getServer().getWorld(properties.getUniqueId());

		if (!optionalWorld.isPresent()) {
			throw new CommandException(Text.of(TextColors.RED, properties.getWorldName(), " must be loaded"));
		}
		World world = optionalWorld.get();

		WorldBorder border = world.getWorldBorder();

		double x;
		double z;
		if(args.hasAny(Text.of("x")) && args.hasAny(Text.of("z"))) {
			x = args.<Double> getOne("x").get();
			z = args.<Double> getOne("z").get();
		} else if(src instanceof Player) {
			if(!((Player) src).getWorld().equals(world)) {
				throw new CommandException(Text.of(TextColors.RED, "You need to specify x and z coordinates. You are not standing in the provided world."), true);
			}
			Location<World> location = ((Player) src).getLocation();
			
			x = location.getX();
			z = location.getZ();
		} else {
			throw new CommandException(Text.of(TextColors.RED, "Must be a player"), false);
		}

		border.setCenter(x, z);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set center of ", world.getName(), " to x: ", x, " z: ", z));

		return CommandResult.success();
	}
}
