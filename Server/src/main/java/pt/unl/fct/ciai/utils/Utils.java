package pt.unl.fct.ciai.utils;

import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Utils {

    public static void copyNonNullProperties(Object source, Object target) {
        List<String> ignoreProperties = new LinkedList<>();

        // loop to include super classes except Object
        for (Class<?> current = target.getClass(); current.getSuperclass() != null; current = current.getSuperclass()) {

            Arrays.stream(current.getDeclaredFields()).forEach(field -> {

                field.setAccessible(true);
                try {
                    Object attribute = field.get(source);
                    if (attribute == null) {
                        ignoreProperties.add(field.getName());
                    }
                } catch (IllegalAccessException e) { }

            });

        }

        BeanUtils.copyProperties(source, target, ignoreProperties.toArray(new String[0]));
    }

}
