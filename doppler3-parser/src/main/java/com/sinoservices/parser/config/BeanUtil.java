package com.sinoservices.parser.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *
 *
 *
 */

class BeanUtil {
	private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

	public static void setFieldValue(Object o, String key, Object val) {
		try {
			Field fff = o.getClass().getField(key);

			// 如果 final 不用设置
			String modifier = Modifier.toString(fff.getModifiers());
			logger.debug("field:" + fff.getName() + "|modifier:" + modifier + "|val:" + val + ";" + fff.getGenericType());
			// logger.debug("field modifier:"+modifier);
			if (modifier != null && modifier.indexOf("final") > -1) {
				logger.warn("final field dnot set:" + fff.getName() + " " + modifier);
				return;
			}

			if ((fff.getGenericType() + "").equals("int") || fff.getGenericType() == Integer.class) {
				logger.debug("field type is integer");
				fff.set(o, Integer.parseInt((String) val));
			} else if ((fff.getGenericType() + "").equals("boolean") || fff.getGenericType() == Boolean.class) {
				logger.debug("field type is boolan");
				fff.set(o, Boolean.parseBoolean((String) val));
			} else if ((fff.getGenericType() + "").equals("float") || fff.getGenericType() == Float.class) {
				logger.debug("field type is float");
				fff.set(o, Float.parseFloat((String) val));
			} else if ((fff.getGenericType() + "").equals("long") || fff.getGenericType() == Long.class) {
				logger.debug("field type is long");
				fff.set(o, Long.parseLong((String) val));
			} else {
				fff.set(o, val);
			}
		} catch (Exception e) {
			logger.error("config setFieldValue error", e);
		}
	}
}


