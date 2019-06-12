package com.springboot.web.common.library.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.web.common.library.exception.SystemException;
import com.springboot.web.common.library.logger.MaskingUtil;

public class CustomJacksonMapper {
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static Map<String, Object> converToTypedMap(String jsonString,
			Class<?> clazz) {
		try {
			objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JavaType mapType = objectMapper.getTypeFactory()
					.constructMapType(HashMap.class, String.class, clazz);
			return objectMapper.readValue(jsonString, mapType);
		} catch (IOException e) {
			throw new SystemException(
					ApplicationConstants.JSON_TRANSFORMATION_EXCEPTION, e);
		}
	}

	public static Map<String, Object> convertToGenericMap(String jsonString) {
		try {
			objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return objectMapper.readValue(jsonString,
					new TypeReference<Map<String, Object>>() {
					});
		} catch (IOException e) {
			throw new SystemException(
					ApplicationConstants.JSON_TRANSFORMATION_EXCEPTION, e);
		}
	}

	public static List<Object> convertToTypedList(String jsonString,
			Class<?> clazz) {
		try {
			objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JavaType listType = objectMapper.getTypeFactory()
					.constructCollectionType(List.class, clazz);
			return objectMapper.readValue(jsonString, listType);
		} catch (IOException e) {
			throw new SystemException(
					ApplicationConstants.JSON_TRANSFORMATION_EXCEPTION, e);
		}
	}

	public static List<Map<String, Object>> convertToListOfMap(
			String jsonString) {
		try {
			objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return objectMapper.readValue(jsonString,
					new TypeReference<List<Map<String, Object>>>() {
					});
		} catch (IOException e) {
			throw new SystemException(
					ApplicationConstants.JSON_TRANSFORMATION_EXCEPTION, e);
		}
	}

	public static Object convertToObject(String jsonString, Class<?> clazz) {
		try {
			objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return objectMapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			throw new SystemException(
					ApplicationConstants.JSON_TRANSFORMATION_EXCEPTION, e);
		}
	}

	public static String convertToJson(Object object) {
		try {
			if (object instanceof byte[]) {
				byte[] bytes = (byte[]) object;
				return new String(bytes, StandardCharsets.UTF_8);
			} /*else if (object instanceof Message) {
				return object.toString();
			}*/
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			return object.toString();
		}
	}

	public static String converToSingleLineString(String string) {
		try {
			objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JsonNode jsonNode = objectMapper.readValue(string, JsonNode.class);
			return MaskingUtil.maskPaylod(jsonNode.toString());
		} catch (IOException e) {
			return string;
		}
	}

	public static ObjectMapper getMapper() {
		objectMapper.configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}
}
