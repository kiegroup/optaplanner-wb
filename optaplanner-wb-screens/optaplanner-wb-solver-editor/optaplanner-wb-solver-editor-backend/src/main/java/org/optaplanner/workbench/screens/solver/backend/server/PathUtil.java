/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

import org.kie.soup.commons.validation.PortablePreconditions;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;

public class PathUtil {

    /**
     * Subtsring the second path from the first. This can be used to for example to substring the repository prefix from a Path.
     * @param from The Path we want to substring
     * @param what What we want to substring from the first Path
     * @return The result. For example the File URI from the Repository root or from a submodule root.
     */
    public static String removePrefix(final Path from,
                                      final Path what) {
        PortablePreconditions.checkNotNull("From Path", from);
        PortablePreconditions.checkNotNull("What Path", what);

        return normalizePath(from).toURI().substring(normalizePath(what).toURI().length());
    }

    /**
     * git:// and default:// can point to the same location. This normalizes the Paths for easier use with length and removePrefix functions.
     * @param path
     * @return
     */
    public static Path normalizePath(final Path path) {
        return Paths.convert(Paths.convert(path));
    }
}
