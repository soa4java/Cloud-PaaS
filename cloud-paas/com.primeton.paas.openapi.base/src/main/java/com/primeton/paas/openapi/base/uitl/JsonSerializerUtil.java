/**
 * 
 */
package com.primeton.paas.openapi.base.uitl;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.primeton.paas.openapi.base.uitl.impl.JsonSerializable;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class JsonSerializerUtil {
	
	private static final String KEY_TYPE = "type"; // className

	private static final String KEY_DATA = "data";

	/**
	 * 
	 * @param data
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String serialize(Object data) throws JsonGenerationException, JsonMappingException, IOException {
		if (data == null)
			return "";

		ObjectMapper mapper = new ObjectMapper();
		JsonSerializable js = new JsonSerializable(data);
		if (data.getClass().getName().equals("[B")) {
			byte[] bytes = (byte[]) js.getData();
			short[] shorts = new short[bytes.length];
			for (int i = 0; i < bytes.length; i++) {
				shorts[i] = bytes[i];
			}
			js.setData(shorts);
		}
		String jsonStr = mapper.writeValueAsString(js);

		return jsonStr;
	}

	/**
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deserialize(String jsonStr) throws JSONException, JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		if (StringUtil.isEmpty(jsonStr.trim()))
			return null;

		JSONObject jo = new JSONObject(jsonStr);
		String className = jo.getString(KEY_TYPE);
		Object value = null;
		if ("java.lang.String".equals(className)) { //$NON-NLS-1$
			value = jo.getString(KEY_DATA);
		} else if ("[Ljava.lang.String;".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			String[] arrays = new String[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = array.getString(i);
			}
			value = arrays;
		} else if ("java.lang.Integer".equals(className)) { //$NON-NLS-1$
			value = jo.getInt(KEY_DATA);
		} else if ("[I".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			int[] arrays = new int[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = array.getInt(i);
			}
			value = arrays;
		} else if ("[Ljava.lang.Integer;".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			Integer[] arrays = new Integer[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = array.getInt(i);
			}
			value = arrays;
		} else if ("java.lang.Long".equals(className)) { //$NON-NLS-1$
			value = jo.getLong(KEY_DATA);
		} else if ("[J".equals(className)) {
			JSONArray array = jo.getJSONArray(KEY_DATA);
			long[] arrays = new long[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = array.getLong(i);
			}
			value = arrays;
		} else if ("[Ljava.lang.Long;".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			Long[] arrays = new Long[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = array.getLong(i);
			}
			value = arrays;
		} else if ("java.util.Date".equals(className)) { //$NON-NLS-1$
			value = new Date(jo.getLong(KEY_DATA));
		} else if ("[Ljava.util.Date;".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			Date[] arrays = new Date[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = new Date(array.getLong(i));
			}
			value = arrays;
		} else if ("java.sql.Date".equals(className)) { //$NON-NLS-1$
			value = java.sql.Date.valueOf(jo.getString(KEY_DATA));
		} else if ("[Ljava.sql.Date;".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			java.sql.Date[] arrays = new java.sql.Date[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = java.sql.Date.valueOf(array.getString(i));
			}
			value = arrays;
		} else if ("java.sql.Time".equals(className)) { //$NON-NLS-1$
			value = java.sql.Time.valueOf(jo.getString(KEY_DATA));
		} else if ("[Ljava.sql.Time;".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			java.sql.Time[] arrays = new java.sql.Time[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = java.sql.Time.valueOf(array.getString(i));
			}
			value = arrays;
		} else if ("java.sql.Timestamp".equals(className)) { //$NON-NLS-1$
			value = new java.sql.Timestamp(jo.getLong(KEY_DATA));
		} else if ("[Ljava.sql.Timestamp;".equals(className)) {
			JSONArray array = jo.getJSONArray(KEY_DATA);
			java.sql.Timestamp[] arrays = new java.sql.Timestamp[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = new java.sql.Timestamp(array.getLong(i));
			}
			value = arrays;
		} else if ("java.lang.Boolean".equals(className)) { //$NON-NLS-1$
			value = jo.getBoolean(KEY_DATA);
		} else if ("[Z".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			boolean[] arrays = new boolean[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = array.getBoolean(i);
			}
			value = arrays;
		} else if ("[Ljava.lang.Boolean;".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			Boolean[] arrays = new Boolean[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = array.getBoolean(i);
			}
			value = arrays;
		} else if ("java.lang.Double".equals(className)) { //$NON-NLS-1$
			value = jo.getDouble(KEY_DATA);
		} else if ("[D".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			double[] arrays = new double[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = array.getDouble(i);
			}
			value = arrays;
		} else if ("[Ljava.lang.Double;".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			Double[] arrays = new Double[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = array.getDouble(i);
			}
			value = arrays;
		} else if ("java.lang.Float".equals(className)) { //$NON-NLS-1$
			value = new Float(jo.getDouble(KEY_DATA));
		} else if ("[F".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			float[] arrays = new float[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = new Float(array.getDouble(i)).floatValue();
			}
			value = arrays;
		} else if ("[Ljava.lang.Float;".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			Float[] arrays = new Float[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = new Float(array.getDouble(i));
			}
			value = arrays;
		} else if ("java.lang.Short".equals(className)) { //$NON-NLS-1$
			value = Short.parseShort(String.valueOf(jo.getInt(KEY_DATA)));
		} else if ("[S".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			short[] arrays = new short[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = (short) array.getInt(i);
			}
			value = arrays;
		} else if ("[Ljava.lang.Short;".equals(className)) { //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			Short[] arrays = new Short[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = (short) array.getInt(i);
			}
			value = arrays;
		} else if ("java.lang.Byte".equals(className)) { //$NON-NLS-1$
			value = Byte.parseByte(String.valueOf(jo.getInt(KEY_DATA)));
		} else if ("[B".equals(className)) { // byte[]
			JSONArray array = jo.getJSONArray(KEY_DATA);
			byte[] arrays = new byte[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = (byte) array.getInt(i);
			}
			value = arrays;
		} else if ("[Ljava.lang.Byte;".equals(className)) { // Byte[]  //$NON-NLS-1$
			JSONArray array = jo.getJSONArray(KEY_DATA);
			Byte[] arrays = new Byte[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arrays[i] = (byte) array.getInt(i);
			}
			value = arrays;
		} else if (className.startsWith("[L") && className.endsWith(";")) { //$NON-NLS-1$
			String clazzName = className.substring(2, className.length() - 1);
			JSONArray array = jo.getJSONArray(KEY_DATA);
			Object[] arrays = new Object[array.length()];
			for (int i = 0; i < array.length(); i++) {
				ObjectMapper mapper = new ObjectMapper();
				arrays[i] = mapper.readValue(array.getString(i), Class.forName(clazzName));
			}
			value = arrays;
		} else {
			JSONObject jdata = jo.getJSONObject(KEY_DATA);
			ObjectMapper mapper = new ObjectMapper();
			value = mapper.readValue(jdata.toString(), Class.forName(className));
		}
		return value;
	}
	
}