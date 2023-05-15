package to.tinypota.ollivanders.common.core;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Core {
	@Nullable
	private String translationKey;
    private final Item item;
    private final double rarity;

    public Core(Item item, double rarity) {
        this.item = item;
        this.rarity = rarity;
    }
	
	public Item getItem() {
    	return item;
	}
	
	public double getRarity() {
		return rarity;
	}
	
	protected String getOrCreateTranslationKey() {
		if (translationKey == null) {
			translationKey = Util.createTranslationKey("item", Registries.ITEM.getId(item));
		}
		return translationKey;
	}
	
	public String getTranslationKey() {
		return this.getOrCreateTranslationKey();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		var core = (Core) o;
		return Double.compare(core.rarity, rarity) == 0 && Objects.equals(translationKey, core.translationKey) && Objects.equals(item, core.item);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(translationKey, item, rarity);
	}
}
