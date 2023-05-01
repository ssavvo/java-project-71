package hexlet.code.formatters;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.KeyState;

import java.util.Map;

public class Json {
    public static String toJson(Map<String, KeyState> diff) throws Exception {
        return new ObjectMapper().writeValueAsString(diff);
    }
}
