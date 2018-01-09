package com.sinoservices.doppler2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ConfigManager {

	private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
	private Class configClass = null;

	public Class getConfigClass() {
		return configClass;
	}

	public void setConfigClass(Class configClass) {
		this.configClass = configClass;
	}

	public void init() {
		logger.info("config init.....");
		// 根据 configClass 中的静态方法，初始化。
		initConfigObjectByField();
		logger.info("config init.....ok");
	}




	private void initConfig(Class clazz) {
		Object config = null;
		try {
			config = clazz.newInstance();
		} catch (Exception e) {
			logger.error("config object init error", e);
		}
		logger.info("config init....." + config.getClass().getName());

		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			String fieldName = f.getName();
			String propValue = PropertiesHolder.getProperty(fieldName);
			logger.debug("f:" + f.getName() + "--" +propValue);
			if (propValue != null) {
				BeanUtil.setFieldValue(config, fieldName, propValue);
			} else {
				logger.warn("don't config! fieldName={} ", fieldName);
			}
		}
	}

	private void initConfigObjectByField() {
		initConfig(configClass);
	}
}
