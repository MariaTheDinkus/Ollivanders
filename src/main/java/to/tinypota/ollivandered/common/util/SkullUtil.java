package to.tinypota.ollivandered.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;

public class SkullUtil {
    private static final String KEY_SKULL_OWNER = "SkullOwner";
    private static final String KEY_ID = "Id";

    @NotNull
    public static ItemStack create(PlayerEntity player) {
        var stack = Items.PLAYER_HEAD.getDefaultStack();

        var nbt = stack.getOrCreateSubNbt(KEY_SKULL_OWNER);
        nbt.putUuid(KEY_ID, player.getUuid());

        return stack;
    }
}
