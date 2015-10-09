package net.nguyen.tryouts;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException{
        String json = "{\"test\":\"a\"}";
        ObjectMapper mapper = new ObjectMapper(); 
        
        Map<String, String> values = mapper.readValue(json, Map.class);
        System.out.println(values.get("test"));
    }
}
