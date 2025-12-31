package org.example.model;

import lombok.Getter;
import org.apache.commons.lang3.Strings;

@Getter
public enum IdCardSideEnum {

    FRONT("front", "身份证正面"),
    BACK("back", "身份证反面");

    private String value;

    private String desc;

    IdCardSideEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static IdCardSideEnum getByValue(String value) {
        for (IdCardSideEnum item : IdCardSideEnum.values()) {
            if (Strings.CI.equals(item.getValue(), value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Failed to find IDCard side: " + value);
    }
}
