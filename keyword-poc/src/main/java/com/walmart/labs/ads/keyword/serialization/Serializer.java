package com.walmart.labs.ads.keyword.serialization;


import com.walmart.labs.ads.keyword.exception.SerializationException;
import org.springframework.lang.Nullable;

public interface Serializer<T>  {
    @Nullable
     byte[] serialize(@Nullable T var1) throws SerializationException;

    @Nullable
    T deserialize(@Nullable byte[] var1) throws SerializationException;
}
