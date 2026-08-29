package com.xantipik.hexingg.aspect;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.network.chat.Component;


//  Центральный реестр всех аспектов.

public final class AspectRegistry {

    private static final Map<String, Aspect> ASPECTS = new LinkedHashMap<>();

    // ====================== PRIMALS ======================

    public static final Aspect AER = registerPrimal("aer", "aspect.hexingg.aer", 0x87CEEB);
    public static final Aspect AQUA = registerPrimal("aqua", "aspect.hexingg.aqua", 0x1E90FF);
    public static final Aspect IGNIS = registerPrimal("ignis", "aspect.hexingg.ignis", 0xFF4500);
    public static final Aspect TERRA = registerPrimal("terra", "aspect.hexingg.terra", 0x8B4513);
    public static final Aspect ORDO = registerPrimal("ordo", "aspect.hexingg.ordo", 0xF5F5F5);
    public static final Aspect PERDITIO = registerPrimal("perditio", "aspect.hexingg.perditio", 0x4B0082);

    // ====================== COMPOUNDS Tier 1 ======================

    public static final Aspect LUX = registerCompound("lux", "aspect.hexingg.lux", 0xFFFF99, AER, IGNIS);
    public static final Aspect MOTUS = registerCompound("motus", "aspect.hexingg.motus", 0xAADDFF, AER, ORDO);
    public static final Aspect VICTUS = registerCompound("victus", "aspect.hexingg.victus", 0x32CD32, AQUA, TERRA);
    public static final Aspect GELUM = registerCompound("gelum", "aspect.hexingg.gelum", 0xADD8E6, IGNIS, PERDITIO);
    public static final Aspect VACUOS = registerCompound("vacuos", "aspect.hexingg.vacuos", 0x555555, AER, PERDITIO);
    public static final Aspect POTENTIA = registerCompound("potentia", "aspect.hexingg.potentia", 0xFFD700, ORDO, IGNIS);
    public static final Aspect METALLUM = registerCompound("metallum", "aspect.hexingg.metallum", 0xC0C0C0, TERRA, ORDO);
    public static final Aspect VITREUS = registerCompound("vitreus", "aspect.hexingg.vitreus", 0xE0FFFF, TERRA, ORDO);
    public static final Aspect PERMUTATIO = registerCompound("permutatio", "aspect.hexingg.permutatio", 0x00FF7F, PERDITIO, ORDO);

    // ====================== COMPOUNDS Tier 2+ ======================

    public static final Aspect HERBA = registerCompound("herba", "aspect.hexingg.herba", 0x228B22, VICTUS, TERRA);
    public static final Aspect BESTIA = registerCompound("bestia", "aspect.hexingg.bestia", 0x8B4513, MOTUS, VICTUS);
    public static final Aspect MORTUUS = registerCompound("mortuus", "aspect.hexingg.mortuus", 0x696969, VICTUS, PERDITIO);
    public static final Aspect SPIRITUS = registerCompound("spiritus", "aspect.hexingg.spiritus", 0xDDA0DD, VICTUS, MORTUUS);
    public static final Aspect COGNITIO = registerCompound("cognitio", "aspect.hexingg.cognitio", 0x9932CC, ORDO, AER);
    public static final Aspect PRAECANTATIO = registerCompound("praecantatio", "aspect.hexingg.praecantatio", 0x9400D3, POTENTIA, COGNITIO);
    public static final Aspect INSTRUMENTUM = registerCompound("instrumentum", "aspect.hexingg.instrumentum", 0xCD853F, METALLUM, ORDO);
    public static final Aspect TELUM = registerCompound("telum", "aspect.hexingg.telum", 0xB22222, INSTRUMENTUM, PERDITIO);
    public static final Aspect VITIUM = registerCompound("vitium", "aspect.hexingg.vitium", 0x8B0000, PERDITIO, AER); // порча

    // ====================== INTERNAL ======================

    private static Aspect registerPrimal(String id, String translationKey, int color) {
        Aspect aspect = Aspect.primal(id, translationKey, color);
        ASPECTS.put(id, aspect);
        return aspect;
    }

    private static Aspect registerCompound(String id, String translationKey, int color, Aspect... components) {
        Aspect aspect = Aspect.compound(id, translationKey, color, components);
        ASPECTS.put(id, aspect);
        return aspect;
    }

    public static Aspect get(String id) {
        return ASPECTS.get(id);
    }

    public static Collection<Aspect> getAll() {
        return Collections.unmodifiableCollection(ASPECTS.values());
    }

    public static Collection<Aspect> getPrimals() {
        return ASPECTS.values().stream()
                .filter(Aspect::isPrimal)
                .toList();
    }

    public static boolean exists(String id) {
        return ASPECTS.containsKey(id);
    }

    // Вызывается при загрузке мода (можно оставить пустым, раз всё static)
    public static void init() {
        // Просто чтобы класс загрузился и все static-поля инициализировались
    }

    private AspectRegistry() {}
}