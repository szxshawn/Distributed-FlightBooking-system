package utils;

import java.util.concurrent.*;

public class DuplicateFilterMap {
    // ConcurrentHashMap to store keys and values along with their expiration time
    private final ConcurrentHashMap<String, TimedValue> map = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    // Inner class to store value and its expiration time
    public static class TimedValue {
        private final String value;
        private final long expiryTime; // Expiration timestamp

        public TimedValue(String value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        public String getValue() {
            return value;
        }

        public long getExpiryTime() {
            return expiryTime;
        }
    }

    // Add a key-value pair to the map with a specified timeout (in milliseconds)
    public void put(String key, String value, long timeoutMillis) {
        long expiryTime = System.currentTimeMillis() + timeoutMillis;
        TimedValue timedValue = new TimedValue(value, expiryTime);
        map.put(key, timedValue);
    }

    // Get the value for a specified key; returns null if the value has expired or does not exist
    public String get(String key) {
        TimedValue timedValue = map.get(key);
        if (timedValue == null || System.currentTimeMillis() > timedValue.getExpiryTime()) {
            map.remove(key); // Remove the key if it has expired
            return null;
        }
        return timedValue.getValue();
    }

    // Remove a specified key-value pair
    public void remove(String key) {
        map.remove(key);
    }

    // Periodically check and remove expired key-value pairs
    private void startCleanupTask() {
        executor.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            for (String key : map.keySet()) {
                TimedValue timedValue = map.get(key);
                if (timedValue != null && currentTime > timedValue.getExpiryTime()) {
                    map.remove(key);
                }
            }
        }, 1, 1, TimeUnit.SECONDS); // Check every second
    }

    // Print all current key-value pairs (for testing purposes)
    public void printAll() {
        for (String key : map.keySet()) {
            TimedValue timedValue = map.get(key);
            if (timedValue != null) {
                System.out.println(key + " => " + timedValue.getValue() +
                        " (Expires at: " + timedValue.getExpiryTime() + ")");
            }
        }
    }

    // Constructor that starts the cleanup task
    public DuplicateFilterMap() {
        startCleanupTask();
    }
}

