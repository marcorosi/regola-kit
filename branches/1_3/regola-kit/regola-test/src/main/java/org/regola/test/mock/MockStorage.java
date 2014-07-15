package org.regola.test.mock;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;

public class MockStorage {

	 
	static public <T> T loadJson(Class<T> valueType, String fileName, Object TestObject) throws Exception {
		
		File file = new File(TestObject.getClass().getResource(fileName).getFile());
		String json = FileUtils.readFileToString(file);
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json,  valueType);
		
	}
	
	static public String saveJson(Object obj, String fileName, Object TestObject) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(obj);
		
		json = mapper.defaultPrettyPrintingWriter().writeValueAsString(obj);
		//System.out.println(json);
		
		File file = getFile(fileName, TestObject);
		file.getParentFile().mkdirs();
		FileUtils.writeStringToFile(file, json, false);

		return json;
		
	}
	
	static public File getFile(String fileName,  Object TestObject) {
		URL url = TestObject.getClass().getResource(".");
		String path = url.getPath().replaceAll("target/test-classes", "src/test/resources") + "/" + fileName;
		//System.out.println(path);
		return new File(path);
	
	}
	
}
