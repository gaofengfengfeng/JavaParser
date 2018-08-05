package com.java.parse.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2018-07-24
 * @Description: 定义了一些集合类型的类，以及定义了一些取出响应格式内有效数据的方法
 */
public class CollectionType {

    public static List<String> isList = Arrays.asList("List", "ArrayList", "LinkedList", "AbstractList",
            "AbstractSequentialList", "Vector");

    public static List<String> isMap = Arrays.asList("Map", "HashMap", "LinkedHashMap", "TreeMap", "WeakHashMap",
            "IdentifyHashMap", "AbstractMap", "EnumMap", "NavigableMap", "SortedMap");

    public static List<String> isSet = Arrays.asList("Set", "HashSet", "EnumSet", "BitSet",
            "LinkedHashSet", "NavigableSet", "TreeSet", "AbstractSet", "SortedSet");

    // java对基本类型的包装类
    public static List<String> javaWrappedClass = Arrays.asList("String", "Byte", "Short", "Integer", "Long",
            "Float", "Double", "Boolean", "Character", "Void");

    /**
     * 判断是否是数组，判断方法：看type和common是否相同，如果不相同则是数组
     *
     * @param type
     * @param commonType
     *
     * @return
     */
    public static boolean isArray(String type, String commonType) {
        if (type != commonType && commonType.contains("[") && commonType.contains("]"))
            return true;
        return false;
    }

    /**
     * 获取泛型前的类型
     *
     * @param commonType
     *
     * @return
     */
    public static String getCollectionType(String commonType) {
        if (commonType.contains("<")) {
            return commonType.substring(0, commonType.indexOf("<"));
        } else {
            return commonType;
        }
    }

    /**
     * 获取泛型
     *
     * @return
     */
    public static String getGenericity(String commonType) {
        if (commonType.contains("<")) {
            return commonType.substring(commonType.indexOf("<") + 1, commonType.indexOf(">"));
        } else {
            return commonType;
        }
    }

    /**
     * 获得数组对象的类型
     *
     * @param commonType
     *
     * @return
     */
    public static String getArrayType(String commonType) {
        if (commonType.contains("[")) {
            return commonType.substring(0, commonType.indexOf("[")).trim();
        } else {
            return commonType;
        }
    }

    /**
     * 如map类型的泛型，获取其key值
     *
     * @param commonType
     *
     * @return
     */
    public static String getCommonTypeKey(String commonType) {
        String keyValue = getGenericity(commonType);
        return keyValue.split(",")[0].trim();
    }

    /**
     * 如map类型的泛型，获取其value值
     *
     * @param commonType
     *
     * @return
     */
    public static String getCommonTypeValue(String commonType) {
        String keyValue = getGenericity(commonType);
        return keyValue.split(",")[1].trim();
    }
}
