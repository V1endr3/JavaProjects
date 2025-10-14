package org.example.model.node;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Node {
    private String id;
    private String type;

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "type",
            visible = true
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = DataSample1.class, name = "startEntity"),
            @JsonSubTypes.Type(value = DataSample2.class, name = "end")
    })
    private AbstractData data;
//    private NodeData data;
    private Object output;
}
