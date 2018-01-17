package com.zjr.technologytest.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.reflect.Type;

public class DefaultSerializer<T> implements JsonSerializer<T> {

	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		String tName = src.getClass().getName();
		if("java.lang.Class".equalsIgnoreCase(tName)){
			return new JsonPrimitive(((Class<?>)src).getName());
		}else if(src instanceof java.util.Date){
			return new JsonPrimitive(DateFormatUtils.format((java.util.Date)src, "yyyy-MM-dd HH:mm:ss"));
		}else{
			return new JsonPrimitive(String.valueOf(src));
		}
	}

}
