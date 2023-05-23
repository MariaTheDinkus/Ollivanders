package to.tinypota.ollivanders.registry.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class OllivandersKeyBindings {
	public static final KeyBinding DECREASE_POWER_LEVEL = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.ollivanders.decrease_power_level", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_COMMA, KeyBinding.MISC_CATEGORY));
	public static final KeyBinding INCREASE_POWER_LEVEL = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.ollivanders.increase_power_level", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_PERIOD, KeyBinding.MISC_CATEGORY));
	
	public static void init() {
	
	}
}
