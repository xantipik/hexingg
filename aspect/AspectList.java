package com.xantipik.hexingg.aspect;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Список аспектов с количеством (Thaumcraft AspectList).
 * Хранит Map<Aspect, Integer>.
 */
public class AspectList {

    private final Map<Aspect, Integer> aspects;

    public AspectList() {
        this.aspects = new HashMap<>();
    }

    public AspectList(Map<Aspect, Integer> map) {
        this.aspects = new HashMap<>();
        if (map != null) {
            map.forEach(this::add);
        }
    }

    public AspectList add(Aspect aspect, int amount) {
        if (aspect == null || amount <= 0) return this;

        aspects.merge(aspect, amount, Integer::sum);
        return this;
    }

    public AspectList remove(Aspect aspect, int amount) {
        if (aspect == null || amount <= 0) return this;

        Integer current = aspects.get(aspect);
        if (current == null) return this;

        int newAmount = current - amount;
        if (newAmount <= 0) {
            aspects.remove(aspect);
        } else {
            aspects.put(aspect, newAmount);
        }
        return this;
    }

    public int get(Aspect aspect) {
        return aspects.getOrDefault(aspect, 0);
    }

    public boolean has(Aspect aspect) {
        return get(aspect) > 0;
    }

    public AspectList merge(AspectList other) {
        if (other == null) return this;
        other.aspects.forEach(this::add);
        return this;
    }

    /**
     * Разложить все составные аспекты до прималов.
     */
    public AspectList toPrimals() {
        AspectList result = new AspectList();

        for (Map.Entry<Aspect, Integer> entry : aspects.entrySet()) {
            Aspect aspect = entry.getKey();
            int amount = entry.getValue();

            if (aspect.isPrimal()) {
                result.add(aspect, amount);
            } else {
                // Рекурсивно разбиваем
                AspectList broken = breakDown(aspect, amount);
                result.merge(broken);
            }
        }

        return result;
    }

    private AspectList breakDown(Aspect aspect, int amount) {
        AspectList result = new AspectList();

        if (aspect.isPrimal()) {
            result.add(aspect, amount);
            return result;
        }

        for (Aspect component : aspect.getComponents()) {
            result.merge(breakDown(component, amount));
        }

        return result;
    }

    public AspectList copy() {
        return new AspectList(this.aspects);
    }

    public boolean isEmpty() {
        return aspects.isEmpty();
    }

    public int size() {
        return aspects.size();
    }

    public Set<Aspect> getAspects() {
        return Collections.unmodifiableSet(aspects.keySet());
    }

    public Map<Aspect, Integer> getMap() {
        return Collections.unmodifiableMap(aspects);
    }

    public void clear() {
        aspects.clear();
    }

    @Override
    public String toString() {
        return "AspectList" + aspects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AspectList that)) return false;
        return Objects.equals(aspects, that.aspects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aspects);
    }
}