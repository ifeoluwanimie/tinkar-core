package org.hl7.tinkar.common.id;


import org.hl7.tinkar.common.binary.*;
import org.hl7.tinkar.common.util.text.NaturalOrder;

/**
 * Used when developer wants to have a key with a unique public identifier, but want to
 * have a potentially changeable string associated with that key for user comprehension
 * of intended objects associated with the key.
 *
 * https://www.honeybadger.io/blog/uuids-and-ulids/
 *
 * https://www.getuniqueid.com/cuid
 *
 * T is the class this is a key for, to help code comprehension
 */
public class PublicIdStringKey<T> implements Comparable<PublicIdStringKey>, Encodable {
    public static final int marshalVersion = 1;

    final PublicId publicId;
    String string;

    public PublicIdStringKey(PublicId publicId, String string) {
        this.publicId = publicId;
        this.string = string;
    }

    @Override
    public void encode(EncoderOutput out) {
        out.writeUuidArray(this.publicId.asUuidArray());
        out.writeString(this.string);
    }
    @Decoder
    public static PublicIdStringKey decode(DecoderInput in) {
        if (in.encodingFormatVersion() == marshalVersion) {
            return new PublicIdStringKey(PublicIds.of(in.readUuidArray()), in.readString());
        }
        throw EncodingExceptionUnchecked.makeWrongVersionException(marshalVersion, in);
    }

    @Override
    public int compareTo(PublicIdStringKey o) {
        int comparison = NaturalOrder.compareStrings(this.string, o.getString());
        if (comparison != 0) {
            return comparison;
        }
        return publicId.compareTo(o.getPublicId());
    }

    @Override
    public int hashCode() {
        return publicId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicIdStringKey that = (PublicIdStringKey) o;
        return publicId.equals(that.getPublicId());
    }

    public PublicId getPublicId() {
        return publicId;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }

    public void updateString(String string) {
        this.string = string;
    }
}