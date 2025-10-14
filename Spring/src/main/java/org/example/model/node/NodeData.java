package org.example.model.node;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeData {
    @Getter
    @Setter
    private List<Object> filterCondition;

    private Map<String, Object> otherProperties = new HashMap<>();

    @JsonAnySetter
    void setProperties(String key, Object value) {
        otherProperties.put(key, value);
    }

    @JsonAnyGetter
    Map<String, Object> getProperties() {
        return otherProperties;
    }
}
