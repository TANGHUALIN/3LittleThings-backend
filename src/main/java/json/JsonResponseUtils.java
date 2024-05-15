package json;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonResponseUtils {
    public static void writeJsonResponseWithObject(HttpServletResponse response, Object object) throws IOException {
        String jsonString = createJsonString(object);
        setResponse(response, jsonString);
    }

    public static void writeJsonResponseWithErrorMessage(HttpServletResponse resp, int statusCode, String errorMessage) throws IOException {
        writeJsonResponseWithStatus(resp, statusCode, errorMessage, "error");
    }

    public static void writeJsonResponseWithMessage(HttpServletResponse resp, int statusCode, String Message) throws IOException {
        writeJsonResponseWithStatus(resp, statusCode, Message, "message");
    }


    public static void writeJsonResponseWithPageInfo(HttpServletResponse resp, int currentPage, int totalPage, boolean hasNextPage, Object object) throws IOException {
        ObjectNode jsonObject = createJsonObject();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode meta = jsonObject.putObject("meta");
        meta.put("currentPage", currentPage);
        meta.put("totalPage", totalPage);
        meta.put("hasNextPage", hasNextPage);
        Integer nextPage = (currentPage + 1 < totalPage) ? currentPage + 1 : null;
        meta.put("nextPage", nextPage);
        jsonObject.set("data", mapper.valueToTree(object));
        setResponse(resp, jsonObject.toString());
    }

    private static ObjectNode createJsonObject() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode();
    }

    private static String createJsonString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private static void setResponse(HttpServletResponse response, String jsonObjectAsString) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonObjectAsString);
    }

    private static void writeJsonResponseWithStatus(HttpServletResponse resp, int statusCode, String Message, String type) throws IOException {
        ObjectNode jsonObject = createJsonObject();
        jsonObject.put(type, Message);
        resp.setStatus(statusCode);
        setResponse(resp, jsonObject.toString());
    }
}


