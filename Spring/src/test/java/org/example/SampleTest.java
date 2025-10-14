package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.node.Node;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SampleTest {

    @Test
    public void parseJson() {
        String jsonContent = """
                [
                    {
                        "id": "start_entity_0",
                        "type": "startEntity",
                        "data":
                        {
                            "id": "start_entity_0",
                            "entityId": "46999363287089152",
                            "title": "表单(实体)触发节点",
                            "triggerType": "before",
                            "triggerEvents":
                            [
                                "beforeCreate",
                                "beforeUpdate",
                                "beforeDelete"
                            ],
                            "filterCondition":
                            [
                                {
                                    "conditions":
                                    [
                                        {
                                            "fieldId": "46999569445519360",
                                            "op": "CONTAINS",
                                            "operatorType": "value",
                                            "value": "年级",
                                            "jdbcType":"variable",
                                            "fieldType": "TEXT"
                                        },
                                        {
                                            "fieldId": "dsfsfdsaf.50028191407505411",
                                            "op": "GREATER_THAN",
                                            "operatorType": "value",
                                            "value": 4,
                                            "fieldType": "NUMBER"
                                        }
                                    ]
                                }
                            ]
                        },
                        "output":
                        {
                            "conditionFields":
                            [
                                {
                                    "label": "班级名称",
                                    "value": "46999569445519360",
                                    "fieldType": "TEXT"
                                },
                                {
                                    "label": "入学时间",
                                    "value": "50026937276661762",
                                    "fieldType": "DATE"
                                },
                                {
                                    "label": "主键ID",
                                    "value": "46999363287089153",
                                    "fieldType": "ID"
                                },
                                {
                                    "label": "拥有者ID",
                                    "value": "46999363287089154",
                                    "fieldType": "USER"
                                },
                                {
                                    "label": "学生人数",
                                    "value": "50028191407505411",
                                    "fieldType": "NUMBER"
                                },
                                {
                                    "label": "拥有部门ID",
                                    "value": "46999363287089155",
                                    "fieldType": "DEPARTMENT"
                                },
                                {
                                    "label": "创建人ID",
                                    "value": "46999363287089156",
                                    "fieldType": "USER"
                                },
                                {
                                    "label": "更新人ID",
                                    "value": "46999363287089157",
                                    "fieldType": "USER"
                                },
                                {
                                    "label": "创建时间",
                                    "value": "46999363287089158",
                                    "fieldType": "DATETIME"
                                },
                                {
                                    "label": "更新时间",
                                    "value": "46999363287089159",
                                    "fieldType": "DATETIME"
                                },
                                {
                                    "label": "乐观锁",
                                    "value": "46999363287089160",
                                    "fieldType": "NUMBER"
                                },
                                {
                                    "label": "删除标识",
                                    "value": "46999363287089161",
                                    "fieldType": "NUMBER"
                                },
                                {
                                    "label": "关联主表ID",
                                    "value": "46999363287089162",
                                    "fieldType": "NUMBER"
                                }
                            ]
                        }
                    },
                    {
                        "id": "end_0",
                        "type": "end",
                        "data":
                        {
                            "title": "结束"
                        },
                        "output":
                        {}
                    }
                ]""";

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            TypeReference<List<Node>> typeReference = new TypeReference<>() {
            };
            List<Node> nodes = objectMapper.readValue(jsonContent, typeReference);
//            assert nodes.get(0).getData() instanceof DataSample1;
//            assert nodes.get(1).getData() instanceof DataSample2;

            String deserializedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodes);
            System.out.println(deserializedJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
