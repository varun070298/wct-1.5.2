/**
 * nz.govt.natlib.ndha.wctdpsdepositor - Software License
 *
 * Copyright 2007/2009 National Library of New Zealand.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * or the file "LICENSE.txt" included with the software.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */

package nz.govt.natlib.ndha.wctdpsdepositor.extractor;

import java.util.List;
import org.webcurator.core.archive.dps.DpsDepositFacade.HarvestType;
import com.exlibris.core.sdk.formatting.DublinCore;

/**
 * Interface for parsing a Wct Met's document and extracting required data.
 */
public interface WctDataExtractor extends WctRequiredData {

    String getTargetName();

    String getEvents();

    String getCreationDate();

    String getProvenanceNote();

    List<ArchiveFile> getArchiveFiles();

    ArchiveFile getArcIndexFile();

    void setArcIndexFile(ArchiveFile arcIndex);    

    List<ArchiveFile> getLogFiles();

    List<ArchiveFile> getReportFiles();

    List<ArchiveFile> getHomeDirectoryFiles();

    ArchiveFile getWctMetsFile();

    List<ArchiveFile> getAllFiles();

    String getWctTargetInstanceID();

    DublinCore getAdditionalDublinCoreElements();

    String getIeEntityType();

    HarvestType getHarvestType();

    void cleanUpCdxFile();

}
