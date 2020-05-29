package sunghs.java.utils.string;

import java.util.Map;

public class StringMapperEx {

    public static final String EMPTY_STRING = "";

    public static final String MAPPING_PREFIX = StringSubstitutor.DEFAULT_VAR_START;

    public static final String MAPPING_SUFFIX = StringSubstitutor.DEFAULT_VAR_END;

    public static final String MAPPING_INDEX_WILDCARD = "\\$\\{[a-z0-9]*}";

    /**
     * ${key} 를 치환합니다.
     * @param template 템플릿
     * @param value ${key}에 대한 key의 value를 가지고 있는 Map
     * @param discard  값이 없는 경우 공백처리여부
     * @return replaced String
     */
    public static String doReplace(final String template, final Map<String, String> value,
        final boolean discard) {
        if (discard) {
            return doDiscardMissingValue(StringSubstitutor.replace(template, value));
        }
        return StringSubstitutor.replace(template, value);
    }

    /**
     * ${} 형태의 값을 전부 empty string으로 치환합니다.
     * @param template 템플릿
     * @return
     */
    private static String doDiscardMissingValue(final String template) {
        return template.replaceAll(MAPPING_INDEX_WILDCARD, EMPTY_STRING);
    }
}
