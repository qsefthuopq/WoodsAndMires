package juuxel.woodsandmires.feature

import com.mojang.serialization.Codec
import java.util.Random
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Heightmap
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.Feature

class MeadowFeature(
    configCodec: Codec<MeadowFeatureConfig>
) : Feature<MeadowFeatureConfig>(configCodec) {
    override fun generate(
        world: ServerWorldAccess, structureAccessor: StructureAccessor, generator: ChunkGenerator,
        random: Random, pos: BlockPos, config: MeadowFeatureConfig
    ): Boolean {
        val mut = BlockPos.Mutable()
        var generated = false

        for (x in 0..15) {
            for (z in 0..15) {
                if (random.nextFloat() > config.chance) continue

                val xo = pos.x + x
                val zo = pos.z + z
                mut.set(xo, world.getTopY(Heightmap.Type.MOTION_BLOCKING, xo, zo), zo)

                val vegetation = config.stateProvider.getBlockState(random, mut)
                if (world.isAir(mut) && vegetation.canPlaceAt(world, mut)) {
                    world.setBlockState(mut, vegetation, 2)
                    generated = true
                }
            }
        }

        return generated
    }
}
