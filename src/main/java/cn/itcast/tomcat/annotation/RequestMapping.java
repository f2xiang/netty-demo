package cn.itcast.tomcat.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-29 19:43
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value() default "";
}
