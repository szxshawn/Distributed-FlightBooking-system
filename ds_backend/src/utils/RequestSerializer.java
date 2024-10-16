package utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestSerializer {
    public static HashMap<String, String> deserialize(String request) {
        String ret = isValidMessageFormat(request);
        HashMap<String, String> req = new HashMap<>();
        if(!ret.equals("true")){
            req.put("error",ret);
            return req;
        }
        String[] componet = request.split(",");
        for (String str : componet) {
            String[] tmp = str.split("=");
            req.put(tmp[0], tmp[1]);
        }
        return req;
    }
    public static String isValidMessageFormat(String message) {
        // Split the message into key-value pairs
        String[] pairs = message.split(",");
        Map<String, String> map = new HashMap<>();

        // Populate the map with keys and values
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length != 2) {
                //System.out.println("Invalid key-value pair: " + pair);
                return ("Invalid key-value pair: " + pair);
            }
            map.put(keyValue[0].trim(), keyValue[1].trim());
        }

        // Validate each field
        if (!map.containsKey("request")) {
            //System.out.println("Missing 'request' field.");
            return ("Missing 'request' field.");
        } else if (!isValidRequest(map.get("request"))) {
            //System.out.println("Invalid 'request' value: " + map.get("request"));
            return ("Invalid 'request' value: " + map.get("request"));
        }

        if (map.containsKey("source") && !isValidNonEmptyString(map.get("source"))) {
            //System.out.println("Invalid 'source' value: " + map.get("source"));
            return ("Invalid 'source' value: " + map.get("source"));
        }

        if (map.containsKey("destination") && !isValidNonEmptyString(map.get("destination"))) {
            //System.out.println("Invalid 'destination' value: " + map.get("destination"));
            return ("Invalid 'destination' value: " + map.get("destination"));
        }

        if (map.containsKey("id") && !isValidPositiveInteger(map.get("id"))) {
            //System.out.println("Invalid 'id' value: " + map.get("id"));
            return ("Invalid 'id' value: " + map.get("id"));
        }

        if (map.containsKey("seats") && !isValidPositiveInteger(map.get("seats"))) {
            //System.out.println("Invalid 'seats' value: " + map.get("seats"));
            return ("Invalid 'seats' value: " + map.get("seats"));
        }

        if (map.containsKey("timeinterval") && !isValidPositiveInteger(map.get("timeinterval"))) {
            //System.out.println("Invalid 'timeinterval' value: " + map.get("timeinterval"));
            return ("Invalid 'timeinterval' value: " + map.get("timeinterval"));
        }

        if (!map.containsKey("semantic")) {
            //System.out.println("Missing 'semantic' field.");
            return ("Missing 'semantic' field.");
        } else if (!"at-least-once".equals(map.get("semantic")) && !"at-most-once".equals(map.get("semantic"))) {
            //System.out.println("Invalid 'semantic' value: " + map.get("semantic"));
            return ("Invalid 'semantic' value: " + map.get("semantic"));
        }

        return "true";
    }

    // Validate 'request' field
    private static boolean isValidRequest(String request) {
        Set<String> validRequests = new HashSet<>(Arrays.asList(
                "QueryFlightBySrcAndDes",
                "QueryFlightById",
                "MakeReservationById",
                "SubscribeById",
                "RandomChooseSeat",
                "GetBookingInfo"
        ));
        return validRequests.contains(request);
    }

    // Validate non-empty strings
    private static boolean isValidNonEmptyString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // Validate positive integers
    private static boolean isValidPositiveInteger(String value) {
        try {
            int number = Integer.parseInt(value);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

