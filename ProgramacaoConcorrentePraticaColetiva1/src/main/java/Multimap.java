import java.util.*;

public class Multimap<K, V> {
    private final Map<K, Collection<V>> map;

    public Multimap() {
        this.map = new HashMap<>();
    }

    public void put(K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public Collection<V> get(K key) {
        return map.getOrDefault(key, Collections.emptyList());
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean containsValue(K key, V value) {
        Collection<V> values = map.get(key);
        return values != null && values.contains(value);
    }

    public Collection<V> remove(K key) {
        return map.remove(key);
    }

    public boolean remove(K key, V value) {
        Collection<V> values = map.get(key);
        return values != null && values.remove(value);
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Collection<V> values() {
        Collection<V> result = new ArrayList<>();
        map.values().forEach(result::addAll);
        return result;
    }

    public Set<Map.Entry<K, Collection<V>>> entrySet() {
        return map.entrySet();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public int size() {
        int size = 0;
        for (Collection<V> values : map.values()) {
            size += values.size();
        }
        return size;
    }

    public void clear() {
        map.clear();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}