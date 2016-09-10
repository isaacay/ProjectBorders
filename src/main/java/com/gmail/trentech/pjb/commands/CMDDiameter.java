package com.gmail.trentech.pjb.commands;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import com.gmail.trentech.pjb.utils.Help;

public class CMDDiameter implements CommandExecutor {

	public CMDDiameter() {
		Help help = new Help("diameter", "diameter", " Set the diameter of border");
		help.setSyntax(" /border diameter <world> <startDiameter> [<time> [endDiameter]]\n /b d <world> <startDiameter> [<time> [endDiameter]]");
		help.setExample(" /border diameter MyWorld 5000\n /border diameter MyWorld 5000 60\n /border diameter MyWorld 5000 120 1000");
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

		if (!args.hasAny("startDiameter")) {
			throw new CommandException(invalidArg());
		}

		double startDiameter = args.<Double> getOne("startDiameter").get();

		long time = 0;
		double endDiameter = 0;
		if (args.hasAny("time")) {
			time = args.<Long> getOne("time").get();

			if (args.hasAny("endDiameter")) {
				endDiameter = args.<Double> getOne("endDiameter").get();
			}
		}

		if (time != 0) {
			if (endDiameter != 0) {
				border.setDiameter(startDiameter, endDiameter, time);
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set diameter of ", world.getName(), " to start: ", startDiameter, "end: ", endDiameter, " time: ", time));
			} else {
				border.setDiameter(startDiameter, time);
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set diameter of ", world.getName(), " to start: ", startDiameter, " time: ", time));
			}
		} else {
			border.setDiameter(startDiameter);
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set diameter of ", world.getName(), " to ", startDiameter));
		}

		return CommandResult.success();
	}

	private Text invalidArg() {
		Text t1 = Text.of(TextColors.YELLOW, "/border diameter ");
		Text t2 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter world name"))).append(Text.of("<world> ")).build();
		Text t3 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter start diameter"))).append(Text.of("<startDiameter> ")).build();
		Text t4 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter time in seconds it takes, to expand/contract border"))).append(Text.of("[<time> ")).build();
		Text t5 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter end diameter border will expand/contract to"))).append(Text.of("[endDiameter]]")).build();

		return Text.of(t1, t2, t3, t4, t5);
	}
}
