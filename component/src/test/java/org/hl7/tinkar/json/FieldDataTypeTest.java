package org.hl7.tinkar.json;
import org.hl7.tinkar.dto.FieldDataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;

public class FieldDataTypeTest {
    @Test
    public void testEnums() {

        for (FieldDataType fieldDataType: FieldDataType.values()) {
            Assertions.assertTrue(fieldDataType == FieldDataType.fromToken(fieldDataType.token));
        }
        Assertions.assertTrue(FieldDataType.FLOAT == FieldDataType.getFieldDataType(Double.parseDouble("1.0")));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> FieldDataType.fromToken((byte) 255));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> FieldDataType.getFieldDataType(new URI("test")));
    }
}
