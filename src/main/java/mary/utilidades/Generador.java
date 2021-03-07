package mary.util;

import java.util.HashMap;
import java.util.Map;

public class IdGenerator {
	
    private Map<String, Integer> map = new HashMap<String, Integer>();

    public String id(String key) {
        int value = map.containsKey(key) ? map.get(key) : 0;
        map.put(key, value + 1);
        return key + value;
    }
}
