package to.tinypota.ollivanders.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class RaycastUtil {
	public static BlockHitResult raycastBlocks(World world, LivingEntity entity, double maxDistance, boolean fluidHandling) {
		var start = entity.getEyePos();
		var end = start.add(entity.getRotationVector().multiply(maxDistance));
		return world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, fluidHandling ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, entity));
	}
	
	public static EntityHitResult raycastEntities(World world, LivingEntity entity, double maxDistance) {
		var start = entity.getEyePos();
		var end = start.add(entity.getRotationVector().multiply(maxDistance));
		var box = new Box(start, end).expand(1);
		
		EntityHitResult closestEntity = null;
		var closestDistance = maxDistance;
		
		for (var otherEntity : world.getOtherEntities(entity, box, potentialHit -> potentialHit.isAlive())) {
			var otherBox = otherEntity.getBoundingBox().expand(otherEntity.getTargetingMargin());
			var optionalPoint = otherBox.raycast(start, end);
			
			if (otherBox.contains(start)) {
				if (closestDistance >= 0.0D) {
					closestEntity = new EntityHitResult(otherEntity);
					closestDistance = 0.0D;
				}
			} else if (optionalPoint.isPresent()) {
				var point = optionalPoint.get();
				var distance = start.distanceTo(point);
				
				if (distance < closestDistance || closestDistance == 0.0D) {
					// Perform a block raycast to this entity
					var blockHitResult = world.raycast(new RaycastContext(start, point, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
					
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
