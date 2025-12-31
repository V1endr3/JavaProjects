package org.example.model;

import lombok.Getter;
import org.apache.commons.lang3.Strings;

@Getter
public enum ExitentrypermitType {
    HK_MC_PASSPORT_FRONT("hk_mc_passport_front", "港澳通行证正面"),
    HK_MC_PASSPORT_BACK("hk_mc_passport_back", "港澳通行证反面"),
    TW_PASSPORT_FRONT("tw_passport_front", "台湾通行证正面"),
    TW_PASSPORT_BACK("tw_passport_back", "台湾通行证反面"),
    TW_RETURN_PASSPORT_FRONT("tw_return_passport_front", "台胞证正面"),
    TW_RETURN_PASSPORT_BACK("tw_return_passport_back", "台胞证反面"),
    HK_MC_RETURN_PASSPORT_FRONT("hk_mc_return_passport_front", "返乡证正面"),
    HK_MC_RETURN_PASSPORT_BACK("hk_mc_return_passport_back", "返乡证反面");

    private String value;

    private String desc;

    ExitentrypermitType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ExitentrypermitType getByValue(String value) {
        for (ExitentrypermitType item : ExitentrypermitType.values()) {
            if (Strings.CI.equals(item.getValue(), value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Failed to find Exitentrypermit type: " + value);
    }
}
