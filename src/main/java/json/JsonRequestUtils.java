package json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class JsonRequestUtils {
    public static String getValueFromJsonRequest(HttpServletRequest req, String name) throws IOException {
        JsonNode jsonNode = getJsonFromRequest(req);
        String value = jsonNode.get(name).asText();
        System.out.println("value from req:" + value);
        return value;
    }

    public static List<Integer> getListFromJsonRequest(HttpServletRequest req) throws IOException {
        JsonNode jsonNode = getJsonFromRequest(req);
        List<Integer> list = new ArrayList<>();

        for (JsonNode eidNode : jsonNode) {
            list.add(Integer.parseInt(eidNode.asText()));

        }
        System.out.println(list);
        return list;
    }

    public static Map<String, String> getValuesMapFromJsonRequest(HttpServletRequest req, String[] name) throws IOException {
        JsonNode jsonNode = getJsonFromRequest(req);
        Map<String, String> valuesMap = new HashMap<>();
        for (String a : name) {
            valuesMap.put(a, jsonNode.get(a).asText());
        }
        return valuesMap;
    }

    public static Map<Integer, String> getValuesMapFromJsonRequest(HttpServletRequest req) throws IOException {
        JsonNode jsonNode = getJsonFromRequest(req);
        System.out.println("JSON Node: " + jsonNode.toString());
        Map<Integer, String> valuesMap = new HashMap<>();
        for (JsonNode j : jsonNode) {
            Iterator<Map.Entry<String, JsonNode>> fields = j.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();

                Integer key = Integer.parseInt(entry.getKey());
                String value = entry.getValue().asText();


                valuesMap.put(key, value);
            }
        }

        return valuesMap;
    }

    private static JsonNode getJsonFromRequest(HttpServletRequest req) throws IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(json);
    }

}
