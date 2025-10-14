package org.example.model.node;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataSample1 extends AbstractData {
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
