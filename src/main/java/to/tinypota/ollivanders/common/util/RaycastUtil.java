package to.tinypota.ollivanders.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.Optional;

public class RaycastUtil {
	public static BlockHitResult raycastBlocks(World world, LivingEntity entity, double maxDistance, boolean fluidHandling) {
		Vec3d start = entity.getEyePos();
		Vec3d end = start.add(entity.getRotationVector().multiply(maxDistance));
		return world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, fluidHandling ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, entity));
	}
	
	public static EntityHitResult raycastEntities(World world, LivingEntity entity, double maxDistance) {
		Vec3d start = entity.getEyePos();
		Vec3d end = start.add(entity.getRotationVector().multiply(maxDistance));
		Box box = new Box(start, end).expand(1);
		
		EntityHitResult closestEntity = null;
		double closestDistance = maxDistance;
		
		for (Entity otherEntity : world.getOtherEntities(entity, box, potentialHit -> potentialHit.isAlive())) {
			Box otherBox = otherEntity.getBoundingBox().expand(otherEntity.getTargetingMargin());
			Optional<Vec3d> optionalPoint = otherBox.raycast(start, end);
			
			if (otherBox.contains(start)) {
				if (closestDistance >= 0.0D) {
					closestEntity = new EntityHitResult(otherEntity);
					closestDistance = 0.0D;
				}
			} else if (optionalPoint.isPresent()) {
				Vec3d point = optionalPoint.get();
				double distance = start.distanceTo(point);
				
				if (distance < closestDistance || closestDistance == 0.0D) {
					// Perform a block raycast to this entity
					BlockHitResult blockHitResult = world.raycast(new RaycastContext(start, point, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
					
					// If the block raycast doesn't hit a block, or hits a block behind the entity
					if (blockHitResult.getType() == HitResult.Type.MISS || start.distanceTo(blockHitResult.getPos()) > distance) {
						if (otherEntity.getRootVehicle() == entity.getRootVehicle() && !entity.canSee(otherEntity)) {
							if (closestDistance == 0.0D) {
								closestEntity = new EntityHitResult(otherEntity, point);
							}
						} else {
							closestEntity = new EntityHitResult(otherEntity, point);
							closestDistance = distance;
						}
					}
				}
			}
		}
		
		return closestEntity;
	}
}
