package com.walmart.labs.ads.keyword.serialization;

import com.walmart.labs.ads.keyword.exception.SerializationException;

import java.util.Arrays;

/**
 * Keyword Bid's key is an adgroup id, it's an integer in keyword bid service. The implementation is a base64 like
 * conversion of integer. Key's length will be
 */
public class KeywordBidKeySerializer implements Serializer<Long> {
    private static final char[] toBase64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };

    private static final int[] fromBase64 = new int[256];
    static {
        Arrays.fill(fromBase64, -1);
        for (int i = 0; i < toBase64.length; i++)
            fromBase64[toBase64[i]] = i;
    }

    @Override
    public byte[] serialize(Long key) throws SerializationException {
        byte[] bytes = new byte[11];
        int a = 0;
        bytes[a++] = (byte)toBase64[(int)((key >>> 60) & 0x0F)];
        for (int c = 60; c > 0; c -= 6) {
            int b = (int)(key >>> (c - 6)) & 0x3F;
            bytes[a++] = (byte)toBase64[b];
        }
        return bytes;
    }

    @Override
    public Long deserialize(byte[] bytes) throws SerializationException {
        long r = 0;

        for (int i = 0; i < bytes.length; i++) {
            r <<= 6;
            r |= fromBase64[bytes[i]];
        }
        return r;
    }
}
