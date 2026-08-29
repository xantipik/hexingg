package com.xantipik.hexingg.aspect;

import java.util.HashMap;
import java.util.Map;


public final class AspectHelper {

    private AspectHelper() {}

    /**
     * Разложить аспект до прималов.
     * Возвращает Map<Aspect, Integer>.
     */
    public static Map<Aspect, Integer> breakDownToPrimals(Aspect aspect, int amount) {
        Map<Aspect, Integer> result = new HashMap<>();
        breakDownRecursive(aspect, amount, result);
        return result;
    }

    /**
     * Разложить AspectList до прималов.
     */
    public static AspectList breakDownToPrimals(AspectList list) {
        AspectList result = new AspectList();

        for (Map.Entry<Aspect, Integer> entry : list.getMap().entrySet()) {
            Map<Aspect, Integer> broken = breakDownToPrimals(entry.getKey(), entry.getValue());
            broken.forEach(result::add);
        }

        return result;
    }

    private static void breakDownRecursive(Aspect aspect, int amount, Map<Aspect, Integer> result) {
        if (aspect == null || amount <= 0) return;

        if (aspect.isPrimal()) {
            result.merge(aspect, amount, Integer::sum);
            return;
        }

        for (Aspect component : aspect.getComponents()) {
            breakDownRecursive(component, amount, result);
        }
    }

    /**
     * Получить "стоимость" аспекта (сколько прималов в нём содержится).
     * Полезно для балансировки Виса и исследований.
     */
    public static int getPrimalCost(Aspect aspect) {
        if (aspect.isPrimal()) return 1;

        int cost = 0;
        for (Aspect component : aspect.getComponents()) {
            cost += getPrimalCost(component);
        }
        return cost;
    }

    /**
     * Проверить, содержит ли список нужное количество аспекта (с учётом разложения).
     */
    public static boolean contains(AspectList list, Aspect needed, int amount) {
        if (list.has(needed) && list.get(needed) >= amount) {
            return true;
        }

        // Можно добавить более умную проверку через toPrimals, 
        return false;
    }
}