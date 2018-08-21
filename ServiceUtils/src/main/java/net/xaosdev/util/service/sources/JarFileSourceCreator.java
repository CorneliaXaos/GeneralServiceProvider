/*
 * Copyright 2018 Cornelia Ada Schultz
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.xaosdev.util.service.sources;

import net.xaosdev.util.service.Source;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This class contains utility methods for creating service sources that come from Jar files stored locally on the
 * execution machine.
 *
 * This class is not, actually, a source itself.  Instead, it returns sources that it identifies from parameters
 * passed in.  It will perform certain validity checks to attempt to ensure that the input file or directory only
 * returns sources that point to valid jar files.  Please see the static interface for more information.
 */
public final class JarFileSourceCreator {

    //region Interface (Public)

    /**
     * Attempts to create a source file for a single file.
     *
     * This method will fail if the input file fails validation checks.
     * @param jar the File to attempt to validate and convert to plugin source.
     * @return a Source file accessing the validated jar file or null if the file failed to validate.
     */
    public static Source tryCreateSourceFromFile(final File jar) {
        try {
            return validate(jar) ? new URLSource(jar.toURI().toURL()) : null;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * Attempts to create a source file for a single file.
     *
     * This method will fail if the input file fails validation checks.  Additionally, the new source will use the
     * provided ClassLoader as its parent loader.
     * @param jar the File to attempt to validate and convert to plugin source.
     * @param classLoader the parent ClassLoader to form a hierarchy with.
     * @return a Source file accessing the validated jar file or null if the file failed to validate.
     */
    public static Source tryCreateSourceFromFile(final File jar, final ClassLoader classLoader) {
        try {
            return validate(jar) ? new URLSource(jar.toURI().toURL(), classLoader) : null;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * Attempts to filter a directory for all files contained within that may be Jar files.
     *
     * Does NOT recurse the directory tree.
     * @param dir the directory to inspect.
     * @return a list of validated Source objects which will be empty if none validated and null if an error occurred.
     */
    public static List<Source> tryCreateSourceFromFilesInDirectory(final File dir) {
        return tryCreateSourceFromFilesInDirectory(dir, false);
    }

    /**
     * Attempts to filter a directory for all files contained within that may be Jar files.
     *
     * Does NOT recurse the directory tree.  Additionally, the new sources will use the provided ClassLoader as
     * their parent loaders.
     * @param dir the directory to inspect.
     * @param classLoader the parent ClassLoader to form a hierarchy with.
     * @return a list of validated Source objects which will be empty if none validated and null if an error occurred.
     */
    public static List<Source> tryCreateSourceFromFilesInDirectory(final File dir, final ClassLoader classLoader) {
        return tryCreateSourceFromFilesInDirectory(dir, false, classLoader);
    }

    /**
     * Attemps to filter a directory for all files contained within that may be Jar files.
     *
     * May recurse the children directories if indicated to do so.
     * @param dir the directory to inspect.
     * @param recurseChildren if true, recurse child directories for Jar files.
     * @return a list of validated Source objects which will be empty if none validated and null if an error occurred
     *         or if the input file is not a directory.
     */
    public static List<Source> tryCreateSourceFromFilesInDirectory(final File dir, final boolean recurseChildren) {
        final List<File> files = acquireFiles(dir, recurseChildren);
        if (files == null)
            return null;

        return files.stream()
                .map(JarFileSourceCreator::tryCreateSourceFromFile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Attemps to filter a directory for all files contained within that may be Jar files.
     *
     * May recurse the children directories if indicated to do so.
     * @param dir the directory to inspect.
     * @param recurseChildren if true, recurse child directories for Jar files.
     * @param classLoader the parent ClassLoader to form a hierarchy with.
     * @return a list of validated Source objects which will be empty if none validated and null if an error occurred
     *         or if the input file is not a directory.
     */
    public static List<Source> tryCreateSourceFromFilesInDirectory(final File dir, final boolean recurseChildren,
                                                                   final ClassLoader classLoader) {
        final List<File> files = acquireFiles(dir, recurseChildren);
        if (files == null)
            return null;

        return files.stream()
                .map(file -> tryCreateSourceFromFile(file, classLoader))
                .filter(Objects::isNull)
                .collect(Collectors.toList());
    }

    //endregion

    //region Interface (Private)

    /**
     * Validates that File should have a Source instanced for it.
     * @param file the File to validate.
     * @return true if the file should be Sourced from, false otherwise.
     */
    private static boolean validate(final File file) {
        return validateIsFile(file) &&
                validateExtension(file) &&
                validateFileIsJar(file) &&
                validateJarHasServices(file);
    }

    /**
     * Validates that a file is a file and not a directory.
     * @param file the file to validate.
     * @return true if the file is not a directory.
     */
    private static boolean validateIsFile(final File file) {
        return file.isFile();
    }

    /**
     * Validates that the file ends with the "jar" extension.
     * @param file the file to validate.
     * @return true if the file ends with the "jar" extension.
     */
    private static boolean validateExtension(final File file) {
        return file.getAbsolutePath().endsWith(".jar");
    }

    /**
     * Validates that the file is a Jar file.
     *
     * A jar file is simply a zip archive, therefore, if we can open the file with the ZipFile object, it passes this
     * test.  This isn't the most efficient way, so.. TODO: implement configurable checks
     * @param file the file to validate.
     * @return true if the file is a valid jar file.
     */
    private static boolean validateFileIsJar(final File file) {
        try (ZipFile zip = new ZipFile(file)){
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate the Jar file contains a service provider entry.
     *
     * Checks for the presence of any entries underneath the "META-INF/services/" directory within the archive.
     * @param file the file to validate.
     * @return true if the jar contains services.
     */
    private static boolean validateJarHasServices(final File file) {
        try (ZipFile jar = new ZipFile(file)) {
            ZipEntry services = jar.getEntry("META-INF/services/");
            return services != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Acquires files in a file tree.
     *
     * @param dir the directory to scan for files in.
     * @param recurseChildren whether or not to walk the whole file tree.
     * @return a List of file found or null if an error occurred or the input file is not a directory.
     */
    private static List<File> acquireFiles(final File dir, final boolean recurseChildren) {
        if (!dir.isDirectory()) {
            return null;
        }

        final List<File> files = new ArrayList<>();
        final Stream<Path> stream;

        try {
            if (recurseChildren) {
                stream = Files.walk(dir.toPath());
            } else {
                stream = Files.list(dir.toPath());
            }
        } catch (IOException e) {
            return null;
        }

        stream.map(Path::toFile).forEach(files::add);
        return files;
    }

    //endregion
}
