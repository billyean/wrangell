package com.walmart.labs.ads.keyword.serialization;

import com.walmart.labs.ads.keyword.datatype.MatchType;
import com.walmart.labs.ads.keyword.exception.SerializationException;
import com.walmart.labs.ads.keyword.model.ParsedKeywordBid;

import java.util.HashSet;
import java.util.Set;

/**
 * The deserialization format of List<ParsedKeywordBid> will be define as follows:
 * ParsedKeywordBids:
 *
 * [1 byte]     : number of ParsedKeywordBid
 * [1 byte]     : match type
 * [8 bytes]    : cpc
 * [1 byte]     : keyword index length, assume the value of keyword index length is N.
 * [N * 4 bytes]: keyword index.
 */
public class ParsedKeywordBidSerializer implements Serializer<Set<ParsedKeywordBid>> {
    @Override
    public byte[] serialize(Set<ParsedKeywordBid> parsedKeywordBids) throws SerializationException {
        // Allocate memory everytime might be not efficient, this can be thread local buffer.
        byte[] buffer = new byte[1 << 16];
        int c = 0;
        int len = parsedKeywordBids.size();
        buffer[c++] = (byte)len;
        for (ParsedKeywordBid parsedKeywordBid : parsedKeywordBids) {
            buffer[c++] = (byte) (parsedKeywordBid.getMatchType().ordinal());
            long lcpc = Double.doubleToRawLongBits(parsedKeywordBid.getCpc());
            // Big endian for keep consistent with Java language.
            buffer[c++] = (byte) ((lcpc >> 56) & 0xFF);
            buffer[c++] = (byte) ((lcpc >> 48) & 0xFF);
            buffer[c++] = (byte) ((lcpc >> 40) & 0xFF);
            buffer[c++] = (byte) ((lcpc >> 32) & 0xFF);
            buffer[c++] = (byte) ((lcpc >> 24) & 0xFF);
            buffer[c++] = (byte) ((lcpc >> 16) & 0xFF);
            buffer[c++] = (byte) ((lcpc >> 8) & 0xFF);
            buffer[c++] = (byte) (lcpc & 0xFF);
            int[] indices = parsedKeywordBid.getKeywordIndices();
            buffer[c++] = (byte) indices.length;
            for (int i = 0; i < indices.length; i++) {
                int index = indices[i];
                buffer[c++] = (byte) ((index >> 24) & 0xFF);
                buffer[c++] = (byte) ((index >> 16) & 0xFF);
                buffer[c++] = (byte) ((index >> 8) & 0xFF);
                buffer[c++] = (byte) (index & 0xFF);
            }
        }
        byte[] finalBuffer = new byte[c];
        assert(c <= 1 << 16);
        System.arraycopy(buffer, 0, finalBuffer, 0, c);
        return finalBuffer;
    }

    @Override
    public Set<ParsedKeywordBid> deserialize(byte[] bytes) throws SerializationException {
        Set<ParsedKeywordBid> keywordBids = new HashSet<>();
        int c = 0;
        int len = 0xFF & bytes[c++];

        for (int i = 0; i < len; i++) {
            MatchType mt = MatchType.values()[bytes[c++]];
            long lbits = (0xFFL & bytes[c++]) << 56
                    | (0xFFL & bytes[c++]) << 48
                    | (0xFFL & bytes[c++]) << 40
                    | (0xFFL & bytes[c++]) << 32
                    | (0xFFL & bytes[c++]) << 24
                    | (0xFFL & bytes[c++]) << 16
                    | (0xFFL & bytes[c++]) << 8
                    | (0xFFL & bytes[c++]) ;
            double cpc = Double.longBitsToDouble(lbits);
            int ilen = bytes[c++];
            int[] indices = new int[ilen];
            for (int ii = 0; ii < ilen; ii++) {
                indices[ii] = (0xFF & bytes[c++]) << 24
                        | (0xFF & bytes[c++]) << 16
                        | (0xFF & bytes[c++]) << 8
                        | (0xFF & bytes[c++]) ;
            }
            ParsedKeywordBid keywordBid = new ParsedKeywordBid(indices, cpc, mt);
            keywordBids.add(keywordBid);
        }
        return keywordBids;
    }
}
