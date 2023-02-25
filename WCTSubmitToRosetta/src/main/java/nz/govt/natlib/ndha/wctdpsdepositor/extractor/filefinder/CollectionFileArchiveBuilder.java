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

package nz.govt.natlib.ndha.wctdpsdepositor.extractor.filefinder;

import nz.govt.natlib.ndha.wctdpsdepositor.extractor.FileSystemArchiveFile;

import java.io.File;
import java.util.Map;

/**
 * Concrete instances of this class are used to find files referenced in a Wct Met's document.
 *
 * This class builds an instance of @{link FileSystemArchiveFile} from a @{link Map}
 * containing the names and Files.
 */
public class CollectionFileArchiveBuilder implements FileArchiveBuilder {
    private final Map<String, File> fileCollection;

    public CollectionFileArchiveBuilder(Map<String, File> fileCollection) {
        this.fileCollection = fileCollection;
    }

    public FileSystemArchiveFile createFileFrom(String mimeType, String expectedCheckSum, String fileName) {
        File file = getFile(fileName);
        return new FileSystemArchiveFile(mimeType, expectedCheckSum, fileName, file.getParent());
    }

    protected File getFile(String fileName) {
        File file = fileCollection.get(fileName);

        if (file == null)
            throw new RuntimeException("The file " + fileName + " was not found in the files collection.");

        if (!file.exists())
            throw new RuntimeException("The file " + fileName + " was not found.");

        return file;
    }

}
