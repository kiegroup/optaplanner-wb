/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.optaplanner.workbench.screens.solver.backend.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import org.kie.soup.commons.xstream.XStreamUtils;

/**
 * TODO Remove once org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO
 * supports setting custom classloader & read() allows supplying java.io.InputStream parameter
 */
public class XStreamSolutionImporter<T> {

    private final XStream xStream;

    public XStreamSolutionImporter(ClassLoader classLoader) {
        xStream = XStreamUtils.createTrustingXStream();
        xStream.setClassLoader(classLoader);
        xStream.setMode(XStream.ID_REFERENCES);
    }

    public T read(InputStream inputStream) {
        try (Reader reader = new InputStreamReader(inputStream,
                                                   "UTF-8")) {
            return (T) xStream.fromXML(reader);
        } catch (XStreamException | IOException e) {
            throw new IllegalArgumentException("Failed reading solution.",
                                               e);
        }
    }
}
