package net.nguyen.journal.inspect;

import java.util.Arrays;
import java.util.List;

public class EventType {

    private List<String> keys;

    public EventType(String... keysArray) {
        super();
        this.keys = Arrays.asList(keysArray);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((keys == null) ? 0 : keys.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EventType other = (EventType) obj;
        if (keys == null) {
            if (other.keys != null) return false;
        }
        else if (!keys.equals(other.keys)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        boolean first = true;
        for (String key : keys) {
            if (!first)
                sb.append(",");
            else
                first = false;

            sb.append(key);
        }
        sb.append(")");
        return sb.toString();
    }

}
