package to.tinypota.ollivanders.common.storage;

import net.minecraft.nbt.NbtCompound;

public abstract class State {
	protected OllivandersServerState serverState;
	
	public State(OllivandersServerState serverState) {
		this.serverState = serverState;
	}
	
	public abstract NbtCompound writeToNbt(NbtCompound nbt);
	
	public abstract void fromNbt(OllivandersServerState serverState, NbtCompound nbt);
	
	public void markDirty() {
		serverState.markDirty();
	}
}