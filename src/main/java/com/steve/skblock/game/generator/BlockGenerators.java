package com.steve.skblock.game.generator;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.EnumMap;
import java.util.Map;

public class BlockGenerators {


    public static final Map<NamespacedKey, GeneratorType> GENERATOR_BIOMES = Map.of(
            NamespacedKey.minecraft("desert"), GeneratorType.SAND,
            NamespacedKey.minecraft("jungle"), GeneratorType.GRASS_BLOCK,
            NamespacedKey.minecraft("badlands"), GeneratorType.TERRACOTTA,
            NamespacedKey.minecraft("savanna"), GeneratorType.DEEPSLATE,
            new NamespacedKey("skyblock", "sky_nether"), GeneratorType.NETHERRACK,
            new NamespacedKey("skyblock", "sky_end"), GeneratorType.END_STONE
    );


    public static final MegaGenerator<Material> DEFAULT_GENERATOR = MegaGenerator.<Material>builder()
            .add(Material.COBBLESTONE, 170)
            .add(Material.COAL_ORE, 30)
            .add(Material.IRON_ORE, 25)
            .add(Material.COPPER_ORE, 20)
            .add(Material.LAPIS_ORE, 5)
            .build();

    public static final MegaGenerator<Material> SAND_GENERATOR = MegaGenerator.<Material>builder()
            .add(Material.SAND, 170)
            .add(Material.REDSTONE_ORE, 30)
            .add(Material.COAL_ORE, 20)
            .add(Material.IRON_ORE, 10)
            .add(Material.COPPER_ORE, 10)
            .add(Material.LAPIS_ORE, 5)
            .add(Material.RED_SAND, 5)
            .build();

    public static final MegaGenerator<Material> GRASS_GENERATOR = MegaGenerator.<Material>builder()
            .add(Material.GRASS_BLOCK, 85)
            .add(Material.DIRT, 85)
            .add(Material.EMERALD_ORE, 30)
            .add(Material.COAL_ORE, 20)
            .add(Material.GRAVEL, 10)
            .add(Material.IRON_ORE, 10)
            .add(Material.REDSTONE_ORE, 5)
            .add(Material.LAPIS_ORE, 5)
            .build();

    public static final MegaGenerator<Material> TERRACOTTA_GENERATOR = MegaGenerator.<Material>builder()
            .add(Material.TERRACOTTA, 33)
            .add(Material.WHITE_TERRACOTTA, 33)
            .add(Material.RED_TERRACOTTA, 33)
            .add(Material.YELLOW_TERRACOTTA, 33)
            .add(Material.BROWN_TERRACOTTA, 33)
            .add(Material.GOLD_ORE, 30)
            .add(Material.COAL_ORE, 20)
            .add(Material.RED_SAND, 15)
            .add(Material.IRON_ORE, 10)
            .add(Material.REDSTONE_ORE, 5)
            .add(Material.LAPIS_ORE, 5)
            .build();

    public static final MegaGenerator<Material> DEEPSLATE_GENERATOR = MegaGenerator.<Material>builder()
            .add(Material.DEEPSLATE, 170)
            .add(Material.DEEPSLATE_COAL_ORE, 20)
            .add(Material.DEEPSLATE_IRON_ORE, 20)
            .add(Material.DEEPSLATE_GOLD_ORE, 15)
            .add(Material.DEEPSLATE_REDSTONE_ORE, 15)
            .add(Material.DEEPSLATE_DIAMOND_ORE, 5)
            .add(Material.DEEPSLATE_LAPIS_ORE, 5)
            .build();

    public static final MegaGenerator<Material> NETHERRACK_GENERATOR = MegaGenerator.<Material>builder()
            .add(Material.NETHERRACK, 70)
            .add(Material.CRIMSON_NYLIUM, 50)
            .add(Material.WARPED_NYLIUM, 50)
            .add(Material.NETHER_GOLD_ORE, 30)
            .add(Material.NETHER_QUARTZ_ORE, 20)
            .add(Material.BONE_BLOCK, 12)
            .add(Material.GLOWSTONE, 12)
            .add(Material.MAGMA_BLOCK, 5)
            .add(Material.ANCIENT_DEBRIS, 1)
            .build();


    public static final MegaGenerator<Material> END_STONE_GENERATOR = MegaGenerator.<Material>builder()
            .add(Material.END_STONE, 140)
            .add(Material.IRON_ORE, 35)
            .add(Material.COAL_ORE, 30)
            .add(Material.GOLD_ORE, 30)
            .add(Material.DIAMOND_ORE, 15)
            .build();


    public static final Map<GeneratorType, MegaGenerator<Material>> GENERATORS = new EnumMap<>(GeneratorType.class);

    static {
        GENERATORS.put(GeneratorType.DEFAULT, DEFAULT_GENERATOR);
        GENERATORS.put(GeneratorType.SAND, SAND_GENERATOR);
        GENERATORS.put(GeneratorType.GRASS_BLOCK, GRASS_GENERATOR);
        GENERATORS.put(GeneratorType.TERRACOTTA, TERRACOTTA_GENERATOR);
        GENERATORS.put(GeneratorType.DEEPSLATE, DEEPSLATE_GENERATOR);
        GENERATORS.put(GeneratorType.NETHERRACK, NETHERRACK_GENERATOR);
        GENERATORS.put(GeneratorType.END_STONE, END_STONE_GENERATOR);
    }


}
