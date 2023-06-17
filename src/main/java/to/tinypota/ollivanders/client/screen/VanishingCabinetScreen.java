package to.tinypota.ollivanders.client.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class VanishingCabinetScreen extends CottonInventoryScreen<VanishingCabinetGuiDescription> {
    public VanishingCabinetScreen(VanishingCabinetGuiDescription gui, PlayerEntity player, Text title) {
        super(gui, player, title);
    }
}