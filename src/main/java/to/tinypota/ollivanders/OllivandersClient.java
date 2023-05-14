package to.tinypota.ollivanders;

import net.fabricmc.api.ClientModInitializer;
import to.tinypota.ollivanders.registry.client.*;

public class OllivandersClient implements ClientModInitializer {
	//public static final Identifier PERSPECTIVE_SWITCH_PACKET_ID = new Identifier(Ollivandered.ID, "perspective_switch");
	
	@Override
	public void onInitializeClient() {
		OllivandersEvents.init();
		OllivandersKeyBindings.init();
		OllivandersEntityRenderers.init();
		OllivandersBlockRenderLayers.init();
		OllivandersModels.init();

//		ClientPlayNetworking.registerGlobalReceiver(PERSPECTIVE_SWITCH_PACKET_ID, (client, handler, buf, responseSender) -> {
//			var thirdPerson = buf.readBoolean();
//			client.execute(() -> {
//				client.options.setPerspective(thirdPerson ? Perspective.THIRD_PERSON_BACK : Perspective.FIRST_PERSON);
//			});
//		});
	}
}
