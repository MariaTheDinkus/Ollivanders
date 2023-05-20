package to.tinypota.ollivanders.registry.client;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import to.tinypota.ollivanders.Ollivanders;

public class OllivandersParticleTypes {
	public static final DefaultParticleType FLOO_FLAME = register("floo_flame", FabricParticleTypes.simple());
	
	public static void init() {
	
	}
	
	public static <P extends ParticleType<?>> P register(String name, P particleType) {
		return Registry.register(Registries.PARTICLE_TYPE, Ollivanders.id(name), particleType);
	}
}
