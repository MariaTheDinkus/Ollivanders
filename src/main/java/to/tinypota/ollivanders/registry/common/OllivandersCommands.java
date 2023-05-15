package to.tinypota.ollivanders.registry.common;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.core.Core;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;

import static net.minecraft.server.command.CommandManager.*;

public class OllivandersCommands {
	public static final SuggestionProvider<ServerCommandSource> AVAILABLE_CORES = SuggestionProviders.register(Ollivanders.id("available_cores"), (context, builder) -> CommandSource.suggestIdentifiers(OllivandersRegistries.CORE.getIds(), builder));
	
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(literal("getcore")
					.then(argument("player", EntityArgumentType.player())
							.executes(context -> {
								ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
								context.getSource().sendFeedback(Text.literal("Their current preferred core is " + OllivandersServerState.getSuitedCore(player)), false);
								return 1;
							})
					)
			);
			
			dispatcher.register(literal("setcore")
					.then(argument("player", EntityArgumentType.player())
							.then(argument("core", IdentifierArgumentType.identifier())
									.suggests(AVAILABLE_CORES)
									.executes(context -> {
										ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
										Identifier coreId = IdentifierArgumentType.getIdentifier(context, "core");
										// Ensure the core exists
										Core core = OllivandersRegistries.CORE.get(coreId);
										if (core == null) {
											context.getSource().sendFeedback(Text.translatable("command.ollivanders.setcore.fail", coreId), false);
											return 0;
										}
										OllivandersServerState.setSuitedCore(player, coreId.toString());
										context.getSource().sendFeedback(Text.translatable("command.ollivanders.setcore.success", player.getDisplayName(), Text.translatable(core.getTranslationKey())), true);
										return 1;
									})
							)
					)
			);
		});
	}
}
