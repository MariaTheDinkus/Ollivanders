package to.tinypota.ollivanders.registry.common;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.core.Core;
import to.tinypota.ollivanders.common.item.WandItem;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.common.util.WeightedRandomBag;

import java.util.ArrayList;

import static net.minecraft.server.command.CommandManager.*;

public class OllivandersCommands {
	public static final SuggestionProvider<ServerCommandSource> AVAILABLE_CORES = SuggestionProviders.register(Ollivanders.id("available_cores"), (context, builder) -> CommandSource.suggestIdentifiers(OllivandersRegistries.CORE.getIds(), builder));
	public static final SuggestionProvider<ServerCommandSource> AVAILABLE_WANDS = SuggestionProviders.register(Ollivanders.id("available_wands"), (context, builder) -> CommandSource.suggestIdentifiers(() -> {
		ArrayList<Identifier> wandItemIDs = new ArrayList<>();
		for (var entry : OllivandersItems.WANDS.getEntries()) {
			var item = entry.getObject();
			wandItemIDs.add(Registries.ITEM.getId(item));
		}
		return wandItemIDs.iterator();
	}, builder));
	
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(literal("getwand")
					.then(argument("player", EntityArgumentType.player())
							.executes(context -> {
								var player = EntityArgumentType.getPlayer(context, "player");
								context.getSource().sendFeedback(Text.literal("Their current preferred wand wood is " + OllivandersServerState.getSuitedWand(player)), false);
								return 1;
							})
					)
			);
			
			dispatcher.register(literal("setwand")
					.then(argument("player", EntityArgumentType.player())
							.then(argument("wand", IdentifierArgumentType.identifier())
									.suggests(AVAILABLE_WANDS)
									.executes(context -> {
										var player = EntityArgumentType.getPlayer(context, "player");
										var wandId = IdentifierArgumentType.getIdentifier(context, "wand");
										// Ensure the wand exists
										var wand = (WandItem) Registries.ITEM.get(wandId);
										if (wand == null) {
											context.getSource().sendFeedback(Text.translatable("command.ollivanders.setwand.fail", wandId), false);
											return 0;
										}
										OllivandersServerState.setSuitedWand(player, wandId.toString());
										context.getSource().sendFeedback(Text.translatable("command.ollivanders.setwand.success", player.getDisplayName(), Text.translatable(wand.getTranslationKey())), true);
										return 1;
									})
							)
					)
			);
			
			dispatcher.register(literal("getcore")
					.then(argument("player", EntityArgumentType.player())
							.executes(context -> {
								var player = EntityArgumentType.getPlayer(context, "player");
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
										var player = EntityArgumentType.getPlayer(context, "player");
										var coreId = IdentifierArgumentType.getIdentifier(context, "core");
										// Ensure the core exists
										var core = OllivandersRegistries.CORE.get(coreId);
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
