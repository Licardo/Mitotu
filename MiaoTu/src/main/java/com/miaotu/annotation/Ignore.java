package com.miaotu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否忽略
 * @author sunfuyong
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
	public int intValue() default -1;
	public String stringValue() default "";
	public boolean byValue() default false;
}
