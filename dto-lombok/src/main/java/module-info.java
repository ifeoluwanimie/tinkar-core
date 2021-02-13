/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

@SuppressWarnings("module") // 7 in HL7 is not a version reference
module org.hl7.tinkar.dto {
    requires java.base;
    requires org.eclipse.collections.api;
    requires org.eclipse.collections.impl;
    requires org.hl7.tinkar.component;
    requires org.hl7.tinkar.common;
    requires lombok;
    exports org.hl7.tinkar.lombok.dto.binary;
    exports org.hl7.tinkar.lombok.dto.changeset;
    exports org.hl7.tinkar.lombok.dto.json;
    exports org.hl7.tinkar.lombok.dto;
    exports org.hl7.tinkar.lombok.dto.graph;
}