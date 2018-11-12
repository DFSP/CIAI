package pt.unl.fct.ciai.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Utils {

    public static void copyNonNullProperties(Object source, Object target) {
        List<String> ignoreProperties = new LinkedList<>();

        Arrays.stream(target.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            try {
                Object attribute = field.get(source);
                System.out.println(attribute);
                if (attribute == null) {
                    ignoreProperties.add(field.getName());
                }
            } catch (IllegalAccessException e) { }
        });

        BeanUtils.copyProperties(source, target, ignoreProperties.toArray(new String[0]));
    }

}