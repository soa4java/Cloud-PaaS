/**
 * 
 */
package com.primeton.paas.collect.common;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-12-17
 *
 */
public class JsonSerializeUtil {
	
	/**
	 * 
	 * @param obj
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String serialize(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		return obj == null ? null : new ObjectMapper().writeValueAsString(obj);
	}
	
	/**
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T deserialize(String json, Class<T> clazz) throws JsonProcessingException, IOException {
		if (StringUtil.isEmpty(json)) {
			return null;
		}
		return (T)new ObjectMapper().reader(clazz).readValue(json);
	}

}