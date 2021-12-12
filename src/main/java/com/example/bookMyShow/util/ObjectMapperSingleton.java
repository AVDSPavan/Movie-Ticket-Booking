package com.example.bookMyShow.util;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
public class ObjectMapperSingleton
{
	private static final ObjectMapper OBJECT_MAPPER = construct();

	public static ObjectMapper getInstance()
	{
		return OBJECT_MAPPER;
	}

	private static ObjectMapper construct()
	{
		ObjectMapper mapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper;
	}
}
