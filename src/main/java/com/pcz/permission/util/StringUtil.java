package com.pcz.permission.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author picongzhi
 */
public class StringUtil {
    public static List<Integer> splitToIntList(String str) {
        List<String> strList = Splitter.on(",").omitEmptyStrings().splitToList(str);
       return strList.stream()
               .map(Integer::parseInt)
               .collect(Collectors.toList());
    }
}
