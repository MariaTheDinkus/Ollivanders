package to.tinypota.ollivandered;

import net.fabricmc.api.ClientModInitializer;
import to.tinypota.ollivandered.registry.client.*;

public class OllivanderedClient implements ClientModInitializer {
    //public static final Identifier PERSPECTIVE_SWITCH_PACKET_ID = new Identifier(Ollivandered.ID, "perspective_switch");

    @Override
    public void onInitializeClient() {
        OllivanderedEvents.init();
        OllivanderedKeyBindings.init();
        OllivanderedEntityRenderers.init();
        OllivanderedBlockRenderLayers.init();
        OllivanderedModels.init();

//		ClientPlayNetworking.registerGlobalReceiver(PERSPECTIVE_SWITCH_PACKET_ID, (client, handler, buf, responseSender) -> {
//			var thirdPerson = buf.readBoolean();
//			client.execute(() -> {
//				client.options.setPerspective(thirdPerson ? Perspective.THIRD_PERSON_BACK : Perspective.FIRST_PERSON);
//			});
//		});
    }
}
