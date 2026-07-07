package serialization;

import java.util.Map;

public final class JsonSerializer {

    public static String serialize(Map<String, ?> data) {
        StringBuilder json = new StringBuilder("{");

        for (Map.Entry<String, ?> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            json.append("\"")
                .append(key)
                .append("\":")
                .append(serializeValue(value))
                .append(",");
        }

        if (!data.isEmpty()) json.deleteCharAt(json.length() - 1);

        json.append("}");

        return json.toString();
    }

    private static String serializeValue(Object value) throws UnsupportedOperationException {
        if (value == null) return "null";
        else if (value instanceof String) return "\"" + value + "\"";
        else if (value instanceof Number || value instanceof Boolean) return value.toString();
        else {
            throw new UnsupportedOperationException("Unsupported JSON type: " + value.getClass().getSimpleName());
        }
    }
}
