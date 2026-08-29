package com.xantipik.hexingg.aspect;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

 (как в Thaumcraft 4).
public final class Aspect {

    private final String id;
    private final Component name;
    private final int color;
    private final boolean primal;
    private final List<Aspect> components; 

    public Aspect(String id, Component name, int color, boolean primal, List<Aspect> components) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.color = color;
        this.primal = primal;
        this.components = components == null ? List.of() : List.copyOf(components);
    }

    // Удобный конструктор для прималов
    public static Aspect primal(String id, String translationKey, int color) {
        return new Aspect(id, Component.translatable(translationKey), color, true, List.of());
    }

    // Удобный конструктор для составных
    public static Aspect compound(String id, String translationKey, int color, Aspect... components) {
        return new Aspect(id, Component.translatable(translationKey), color, false, List.of(components));
    }

    public String getId() {
        return id;
    }

    public ResourceLocation getResourceLocation(String modid) {
        return new ResourceLocation(modid, id);
    }

    public Component getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public boolean isPrimal() {
        return primal;
    }

    public List<Aspect> getComponents() {
        return components;
    }

    public boolean isCompound() {
        return !primal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aspect aspect)) return false;
        return id.equals(aspect.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Aspect{" + id + (primal ? " (primal)" : "") + "}";
    }
}