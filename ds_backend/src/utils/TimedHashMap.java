package utils;

import java.util.*;
import java.util.concurrent.*;

public class TimedHashMap<K, V> {
    // ConcurrentHashMap stores keys and a collection of TimedValue objects
    private final ConcurrentHashMap<K, ConcurrentLinkedQueue<TimedValue<V>>> map = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    // Inner class to store the value and expiry time
    public static class TimedValue<V> {
        private V value;
        private long expiryTime; // Expiry timestamp

        public TimedValue(V value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        public V getValue() {
            return value;
        }

        public long getExpiryTime() {
            return expiryTime;
        }
    }

    // Add a key-value pair with a specified timeout (in milliseconds)
    public void put(K key, V value, long timeoutMillis) {
        long expiryTime = System.currentTimeMillis() + timeoutMillis;
        TimedValue<V> timedValue = new TimedValue<>(value, expiryTime);

        // Get or create the collection of values for this key
        ConcurrentLinkedQueue<TimedValue<V>> values = map.computeIfAbsent(key, k -> new ConcurrentLinkedQueue<>());

        // Add the new timed value
        values.add(timedValue);
    }

    // Get the list of values; returns null if all values have expired or key doesn't exist
    public List<TimedValue<V>> get(K key) {
        ConcurrentLinkedQueue<TimedValue<V>> values = map.get(key);
        if (values == null) {
            return null;
        }

        long currentTime = System.currentTimeMillis();

        // Remove expired values
        values.removeIf(timedValue -> currentTime > timedValue.expiryTime);

        // If after removal, the collection is empty, remove the key from the map
        if (values.isEmpty()) {
            map.remove(key);
            return null;
        }

        // Return non-expired TimedValues
        return new ArrayList<>(values);
    }

    // Remove a specific value associated with a key
    public void remove(K key, V value) {
        ConcurrentLinkedQueue<TimedValue<V>> values = map.get(key);
        if (values != null) {
            values.removeIf(timedValue -> timedValue.value.equals(value));
            if (values.isEmpty()) {
                map.remove(key);
            }
        }
    }

    // Regularly check and remove expired TimedValues
    private void startCleanupTask() {
        executor.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            for (Map.Entry<K, ConcurrentLinkedQueue<TimedValue<V>>> entry : map.entrySet()) {
                ConcurrentLinkedQueue<TimedValue<V>> values = entry.getValue();
                values.removeIf(timedValue -> currentTime > timedValue.expiryTime);

                if (values.isEmpty()) {
                    map.remove(entry.getKey());
                }
            }
        }, 1, 1, TimeUnit.SECONDS); // Check every second
    }

    // Print all current key-value pairs (for testing)
    public void printAll() {
        for (Map.Entry<K, ConcurrentLinkedQueue<TimedValue<V>>> entry : map.entrySet()) {
            K key = entry.getKey();
            for (TimedValue<V> timedValue : entry.getValue()) {
                System.out.println(key + " => " + timedValue.value + " (Expires at: " + timedValue.expiryTime + ")");
            }
        }
    }

    // Constructor that starts the cleanup task
    public TimedHashMap() {
        startCleanupTask();
    }
}
