package to.tinypota.ollivanders.client.screen;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import to.tinypota.ollivanders.common.item.SplitCabinetCoreItem;
import to.tinypota.ollivanders.registry.common.OllivandersScreenHandlers;

public class VanishingCabinetGuiDescription extends SyncedGuiDescription {
    private static final int INVENTORY_SIZE = 1;

    public VanishingCabinetGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(OllivandersScreenHandlers.VANISHING_CABINET, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(100, 100);
        root.setInsets(Insets.ROOT_PANEL);

        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
        itemSlot.setInputFilter(stack -> stack.getItem() instanceof SplitCabinetCoreItem);
        root.add(itemSlot, 4, 1);

        root.add(this.createPlayerInventoryPanel(), 0, 3);

        root.validate(this);
    }
}