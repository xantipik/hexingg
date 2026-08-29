package com.xantipik.hexingg.item;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xantipik.hexingg.aspect.Aspect;
import com.xantipik.hexingg.aspect.AspectList;
import com.xantipik.hexingg.aspect.AspectRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Умный калькулятор аспектов.
 * 
 * Приоритет:
 * 1. Кеш
 * 2. Базовые аспекты (сырьё)
 * 3. (позже) расчёт по рецептам
 * 4. Fallback
 */
public final class AspectCalculator {

    private static final Map<ResourceLocation, AspectList> CACHE = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, AspectList> BASE_ASPECTS = new HashMap<>();

    static {
        // ====================== БАЗОВЫЕ АСПЕКТЫ ======================

        // Дерево
        registerBase("minecraft:stick",          list -> list.add(AspectRegistry.TERRA, 1).add(AspectRegistry.AER, 1));
        registerBase("minecraft:oak_log",         list -> list.add(AspectRegistry.TERRA, 4).add(AspectRegistry.AER, 2).add(AspectRegistry.HERBA, 2));
        registerBase("minecraft:oak_planks",      list -> list.add(AspectRegistry.TERRA, 2).add(AspectRegistry.AER, 1).add(AspectRegistry.HERBA, 1));

        // Камни
        registerBase("minecraft:cobblestone",     list -> list.add(AspectRegistry.TERRA, 2).add(AspectRegistry.PERDITIO, 1));
        registerBase("minecraft:stone",           list -> list.add(AspectRegistry.TERRA, 3));
        registerBase("minecraft:smooth_stone",    list -> list.add(AspectRegistry.TERRA, 3).add(AspectRegistry.ORDO, 1));

        // TFC rocks (примеры)
        registerBase("tfc:rock/raw/granite",      list -> list.add(AspectRegistry.TERRA, 4));
        registerBase("tfc:rock/raw/basalt",       list -> list.add(AspectRegistry.TERRA, 4).add(AspectRegistry.IGNIS, 1));
        registerBase("tfc:rock/raw/limestone",    list -> list.add(AspectRegistry.TERRA, 3).add(AspectRegistry.AQUA, 1));

        // Металлы GT / TFC
        registerBase("gtceu:iron_ingot",          list -> list.add(AspectRegistry.METALLUM, 5).add(AspectRegistry.TERRA, 1));
        registerBase("gtceu:copper_ingot",        list -> list.add(AspectRegistry.METALLUM, 4).add(AspectRegistry.AQUA, 1));
        registerBase("gtceu:gold_ingot",          list -> list.add(AspectRegistry.METALLUM, 4).add(AspectRegistry.ORDO, 2));
        registerBase("gtceu:silver_ingot",        list -> list.add(AspectRegistry.METALLUM, 4).add(AspectRegistry.ORDO, 1).add(AspectRegistry.AQUA, 1));
        registerBase("gtceu:tin_ingot",           list -> list.add(AspectRegistry.METALLUM, 3).add(AspectRegistry.AQUA, 1));
        registerBase("gtceu:bronze_ingot",        list -> list.add(AspectRegistry.METALLUM, 5).add(AspectRegistry.TERRA, 1).add(AspectRegistry.ORDO, 1));

        registerBase("tfc:metal/ingot/wrought_iron", list -> list.add(AspectRegistry.METALLUM, 5).add(AspectRegistry.TERRA, 1));
        registerBase("tfc:metal/ingot/copper",    list -> list.add(AspectRegistry.METALLUM, 4).add(AspectRegistry.AQUA, 1));
        registerBase("tfc:metal/ingot/gold",      list -> list.add(AspectRegistry.METALLUM, 4).add(AspectRegistry.ORDO, 2));
        registerBase("tfc:metal/ingot/bronze",    list -> list.add(AspectRegistry.METALLUM, 5).add(AspectRegistry.TERRA, 1).add(AspectRegistry.ORDO, 1));

        // Органика
        registerBase("minecraft:bone",            list -> list.add(AspectRegistry.MORTUUS, 2).add(AspectRegistry.TERRA, 1));
        registerBase("minecraft:rotten_flesh",    list -> list.add(AspectRegistry.MORTUUS, 3).add(AspectRegistry.PERDITIO, 1));
        registerBase("minecraft:leather",         list -> list.add(AspectRegistry.BESTIA, 2).add(AspectRegistry.TERRA, 1));
        registerBase("minecraft:string",          list -> list.add(AspectRegistry.BESTIA, 1).add(AspectRegistry.AER, 1));

        // Прочее
        registerBase("minecraft:coal",            list -> list.add(AspectRegistry.IGNIS, 3).add(AspectRegistry.TERRA, 1).add(AspectRegistry.PERDITIO, 1));
        registerBase("minecraft:charcoal",        list -> list.add(AspectRegistry.IGNIS, 2).add(AspectRegistry.TERRA, 1).add(AspectRegistry.HERBA, 1));
        registerBase("minecraft:redstone",        list -> list.add(AspectRegistry.POTENTIA, 2).add(AspectRegistry.PERMUTATIO, 1));
        registerBase("minecraft:glowstone_dust",  list -> list.add(AspectRegistry.LUX, 3).add(AspectRegistry.POTENTIA, 1));
    }

    private static void registerBase(String id, java.util.function.Consumer<AspectList> consumer) {
        AspectList list = new AspectList();
        consumer.accept(list);
        BASE_ASPECTS.put(new ResourceLocation(id), list);
    }

    /**
     * Главный метод получения аспектов.
     */
    public static AspectList getAspects(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return new AspectList();
        }
        return getAspects(ForgeRegistries.ITEMS.getKey(stack.getItem()));
    }

    public static AspectList getAspects(Item item) {
        if (item == null) return new AspectList();
        return getAspects(ForgeRegistries.ITEMS.getKey(item));
    }

    public static AspectList getAspects(ResourceLocation id) {
        if (id == null) return new AspectList();

        // 1. Кеш
        AspectList cached = CACHE.get(id);
        if (cached != null) {
            return cached.copy();
        }

        // 2. Базовые аспекты
        AspectList base = BASE_ASPECTS.get(id);
        if (base != null) {
            CACHE.put(id, base);
            return base.copy();
        }

        // 3. TODO: расчёт по рецептам (calculateFromRecipes)
        // AspectList fromRecipe = calculateFromRecipes(id);
        // if (fromRecipe != null) { ... }

        // 4. Fallback
        AspectList fallback = new AspectList().add(AspectRegistry.TERRA, 1);
        CACHE.put(id, fallback);
        return fallback.copy();
    }

    /**
     * Очистить кеш (нужно при перезагрузке рецептов / датапаков).
     */
    public static void invalidateCache() {
        CACHE.clear();
    }

    public static void invalidateCache(ResourceLocation id) {
        CACHE.remove(id);
    }

    /**
     * Заготовка под будущий recipe walker.
     * Сюда потом добавим проход по shaped/shapeless/GT рецептам.
     */
    private static AspectList calculateFromRecipes(ResourceLocation id) {
        // TODO
        return null;
    }

    private AspectCalculator() {}
}