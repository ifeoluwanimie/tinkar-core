package org.hl7.tinkar.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hl7.tinkar.dto.ConceptChronologyDTO;
import org.hl7.tinkar.dto.DefinitionForSemanticChronologyDTO;
import org.hl7.tinkar.dto.FieldDataType;
import org.hl7.tinkar.dto.SemanticChronologyDTO;
import org.hl7.tinkar.dto.binary.TinkarInput;
import org.hl7.tinkar.entity.internal.Get;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LoadEntitiesFromDTO {
    protected static final Logger LOG = LogManager.getLogger();
    final File importFile;
    final AtomicInteger importCount = new AtomicInteger();


    public LoadEntitiesFromDTO(File importFile) {
        this.importFile = importFile;
    }

    public Integer call() throws IOException {
        try {

            try (ZipFile zipFile = new ZipFile(importFile, Charset.forName("UTF-8"))) {
                ZipEntry tinkZipEntry = zipFile.getEntry("export.tink");
                TinkarInput tinkIn = new TinkarInput(zipFile.getInputStream(tinkZipEntry));
                LOG.info(":TIME:Objects: begin processing");

                while (true) {
                    FieldDataType fieldDataType = FieldDataType.fromToken(tinkIn.readByte());
                    switch (fieldDataType) {
                        case CONCEPT_CHRONOLOGY: {
                            ConceptChronologyDTO ccDTO = ConceptChronologyDTO.make(tinkIn);
                            Get.entityService().putChronology(ccDTO);
                            importCount.incrementAndGet();
                        }
                        break;
                        case SEMANTIC_CHRONOLOGY: {
                            SemanticChronologyDTO scDTO = SemanticChronologyDTO.make(tinkIn);
                            Get.entityService().putChronology(scDTO);
                            importCount.incrementAndGet();
                        }
                        break;
                        case DEFINITION_FOR_SEMANTIC_CHRONOLOGY: {
                            DefinitionForSemanticChronologyDTO dsDTO = DefinitionForSemanticChronologyDTO.make(tinkIn);
                            Get.entityService().putChronology(dsDTO);
                            importCount.incrementAndGet();
                        }
                        break;

                        default:
                            throw new UnsupportedOperationException("Can't handle fieldDataType: " + fieldDataType);

                    }
                }

            } catch (EOFException eof) {
                // continue, will autoclose.
            }
            LOG.info("Imported: " + importCount + " items");
            LOG.info(":TIME: write load script");

            return importCount.get();
        } finally {
            //Get.activeTasks().remove(this);
        }
    }}