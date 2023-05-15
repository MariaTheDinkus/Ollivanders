package to.tinypota.ollivanders.common.util;

import net.minecraft.entity.LivingEntity;

import java.util.*;

public class WeightedRandomBag<T> {
    public class Entry {
        double accumulatedWeight;
        T object;
	
		public T getObject() {
			return object;
		}
	}

    private Random rand;
    private double accumulatedWeight;
    private List<Entry> entries;

    public WeightedRandomBag() {
        this(new Random());
    }

    public WeightedRandomBag(Random rand) {
        this.rand = rand;
		accumulatedWeight = 0;
		entries = new ArrayList<>();
    }

    public void addEntry(T object, double weight) {
        accumulatedWeight += weight;
        Entry e = new Entry();
        e.object = object;
        e.accumulatedWeight = accumulatedWeight;
        entries.add(e);
    }
	
	public List<Entry> getEntries() {
		return entries;
	}
	
	public T getRandom() {
        double r = rand.nextDouble() * accumulatedWeight;

        for (Entry entry: entries) {
            if (entry.accumulatedWeight >= r) {
                return entry.object;
            }
        }
        return null; //should only happen when there are no entries
    }
	
	public T getRandom(LivingEntity entity) {
    	var uuid = entity.getUuid();
    	rand.setSeed(uuid.getLeastSignificantBits() ^ uuid.getMostSignificantBits());
		double r = rand.nextDouble() * accumulatedWeight;
		
		for (Entry entry: entries) {
			if (entry.accumulatedWeight >= r) {
				return entry.object;
			}
		}
		return null; //should only happen when there are no entries
	}
}
