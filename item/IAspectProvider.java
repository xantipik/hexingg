package com.xantipik.hexingg.item;

import com.xantipik.hexingg.aspect.AspectList;

import net.minecraft.world.item.ItemStack;

/**
 * Интерфейс для всего, что может предоставлять аспекты.
 * В первую очередь — предметы.
 */
public interface IAspectProvider {

    /**
     * Получить аспекты данного ItemStack.
     * Должен возвращать новый или скопированный AspectList (не мутировать внутренний).
     */
    AspectList getAspects(ItemStack stack);

    /**
     * Можно ли извлекать аспекты из этого предмета (для Виса и т.д.).
     * По умолчанию — да, если аспекты есть.
     */
    default boolean canExtractAspects(ItemStack stack) {
        AspectList aspects = getAspects(stack);
        return aspects != null && !aspects.isEmpty();
    }
}