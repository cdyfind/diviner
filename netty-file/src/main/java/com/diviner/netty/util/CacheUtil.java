package com.diviner.netty.util;

import com.diviner.netty.domain.FileBurstInstruct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存
 * @Author caody
 * @Date 2022/5/10 17:08
 * @Param
 * @return {@link }
 **/
public class CacheUtil {

    public static Map<String, FileBurstInstruct> burstInstructMap = new ConcurrentHashMap<>();

}
