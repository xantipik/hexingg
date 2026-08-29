package com.xantipik.hexingg.vis;

import java.util.List;

import javax.annotation.Nullable;

import com.xantipik.hexingg.aspect.Aspect;
import com.xantipik.hexingg.aspect.AspectList;
import com.xantipik.hexingg.aspect.AspectRegistry;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Бутыль с Висом конкретного аспекта.
 * Хранит аспект + количество в NBT.
 */
public class VisBottleItem extends Item {

    public static final String TAG_ASPECT = "Aspect";
    public static final String TAG_AMOUNT = "Amount";

    public VisBottleItem(Properties properties) {
        super(properties);
    }

    // ====================== NBT helpers ======================

    public static void setAspect(ItemStack stack, Aspect aspect, int amount) {
        if (stack.isEmpty() || aspect == null || amount <= 0) return;

        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(TAG_ASPECT, aspect.getId());
        tag.putInt(TAG_AMOUNT, amount);
    }

    @Nullable
    public static Aspect getAspect(ItemStack stack) {
        if (!stack.hasTag()) return null;
        String id = stack.getTag().getString(TAG_ASPECT);
        return AspectRegistry.get(id);
    }

    public static int getAmount(ItemStack stack) {
        if (!stack.hasTag()) return 0;
        return stack.getTag().getInt(TAG_AMOUNT);
    }

    public static AspectList getAspectList(ItemStack stack) {
        Aspect aspect = getAspect(stack);
        int amount = getAmount(stack);
        if (aspect == null || amount <= 0) {
            return new AspectList();
        }
        return new AspectList().add(aspect, amount);
    }

    public static boolean hasVis(ItemStack stack) {
        return getAspect(stack) != null && getAmount(stack) > 0;
    }

    // ====================== Tooltip ======================

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        Aspect aspect = getAspect(stack);
        int amount = getAmount(stack);

        if (aspect != null && amount > 0) {
            tooltip.add(Component.literal("")
                    .append(aspect.getName())
                    .append(Component.literal(" × " + amount).withStyle(ChatFormatting.GRAY)));
        } else {
            tooltip.add(Component.literal("Пустая бутыль").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        // Лёгкое свечение, если внутри есть Вис
        return hasVis(stack) || super.isFoil(stack);
    }
}