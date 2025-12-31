package org.example.constant;

import com.mybatisflex.annotation.EnumValue;
import lombok.Getter;
import org.example.util.AppException;

import java.util.Objects;

@Getter
public enum GenderEnum {
    MALE(0, "男"),
    FEMALE(1, "女");

    @EnumValue
    private final Integer value;

    private final String label;

    GenderEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public static GenderEnum getGender(Integer value) {
        if (value == null) {
            throw AppException.create("传入参数为空");
        }

        for (GenderEnum gender : GenderEnum.values()) {
            if (Objects.equals(gender.value, value)) {
                return gender;
            }
        }

        throw AppException.create("未找到对应的性别枚举：%s", value);
    }

}
