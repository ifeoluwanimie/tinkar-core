/*
 * Copyright 2020-2021 HL7.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hl7.tinkar.dto;

import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.tinkar.component.ConceptVersion;
import org.hl7.tinkar.component.Stamp;
import org.hl7.tinkar.dto.binary.*;
import org.hl7.tinkar.dto.json.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.UUID;


public record ConceptVersionDTO(ImmutableList<UUID> componentUuids, StampDTO stampDTO)
        implements ConceptVersion, DTO, JsonMarshalable, Marshalable {

    private static final int localMarshalVersion = 3;


    public static ConceptVersionDTO make(ConceptVersion conceptVersion) {
        return new ConceptVersionDTO(conceptVersion.componentUuids(), StampDTO.make(conceptVersion.stamp()));
    }

    @Override
    public Stamp stamp() {
        return stampDTO;
    }

    /**
     * Marshaler for ConceptVersionDTO using JSON
     * @param writer
     */
    @JsonMarshaler
    @Override
    public void jsonMarshal(Writer writer) {
        final JSONObject json = new JSONObject();
        json.put(ComponentFieldForJson.STAMP, stampDTO);
        json.writeJSONString(writer);
    }

    /**
     * Version unmarshaler for ConceptVersionDTO using JSON
     * @param jsonObject
     * @param componentUuids
     * @return
     */
    @JsonVersionUnmarshaler
    public static ConceptVersionDTO make(JSONObject jsonObject, ImmutableList<UUID> componentUuids) {
        return new ConceptVersionDTO(
                componentUuids,
                StampDTO.make((JSONObject) jsonObject.get(ComponentFieldForJson.STAMP)));
    }

    /**
     * Version unmarshaler for ConceptVersionDTO.
     * @param in
     * @param componentUuids
     * @return new instance of ConceptVersionDTO created from the input.
     */
    @VersionUnmarshaler
    public static ConceptVersionDTO make(TinkarInput in, ImmutableList<UUID> componentUuids) {
        if (localMarshalVersion == in.getTinkerFormatVersion()) {
            return new ConceptVersionDTO(componentUuids, StampDTO.make(in));
        } else {
            throw new UnsupportedOperationException("Unsupported version: " + in.getTinkerFormatVersion());
        }
    }

    /**
     * Version marshaler for ConceptVersionDTO
     * @param out
     */
    @Override
    @Marshaler
    public void marshal(TinkarOutput out) {
        // note that componentUuids are not written redundantly here,
        // they are written with the ConceptChronologyDTO...
        stampDTO.marshal(out);
    }
}