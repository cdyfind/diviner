package com.diviner.netty.serialize;

/**
 * 序列化接口类
 * @Author caody
 * @Date 2022/4/20 10:30
 * @Param
 * @return {@link }
 **/
public interface Serializer {

    /**
     * java对象转换成二进制
     * @Author caody
     * @Date 2022/4/20 10:30
     * @Param
     * @return {@link }
     **/
    byte[] serializer(Object object);

    /**
     * 二进制转换成java对象
     * @Author caody
     * @Date 2022/4/20 10:31
     * @Param
     * @return {@link }
     **/
    <T> T deserialize(Class<T> clazz,byte[] bytes);
}
