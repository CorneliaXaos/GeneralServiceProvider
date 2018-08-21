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

import net.xaosdev.util.service.Service;
import net.xaosdev.util.service.Source;
import org.junit.Test;
import testing.producer.spi.TestService;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class JarFileSourceCreatorTest {

    private static final String TO_EXPORTED_DIR = "../Test Artifacts/build/exported";
    private static final String IMPL_1 = TO_EXPORTED_DIR + "/Test Impl 1.jar";

    private static final String BAD_ARTIFACT_1 = TO_EXPORTED_DIR + "/notAJarFile.txt";
    private static final String BAD_ARTIFACT_2 = TO_EXPORTED_DIR + "/fakeJarFile.jar";
    private static final String BAD_ARTIFACT_3 = TO_EXPORTED_DIR + "/noMetaInf.jar";

    @Test
    public void tryCreateSourceFromFile() {
        // Arrange
        final File file = new File(IMPL_1);
        final Source source = JarFileSourceCreator.tryCreateSourceFromFile(file);

        // Act / Assert
        testSources(Collections.singletonList(source));
    }

    @Test
    public void tryCreateSourceFromFileFails() {
        // Arrange
        final List<File> badFiles = Arrays.asList(
                new File(TO_EXPORTED_DIR), // Sources can't be made from directories.
                new File(BAD_ARTIFACT_1), // Plain text file
                new File(BAD_ARTIFACT_2), // Not-a-jar file with 'jar' extension
                new File(BAD_ARTIFACT_3) // Valid jar file, but not services enumerated in META-INF
        );

        // Act / Assert
        for (File file : badFiles) {
            assertNull(JarFileSourceCreator.tryCreateSourceFromFile(file));
        }
    }

    @Test
    public void tryCreateSourceFromFilesInDirectory() {
        // Arrange
        final File file = new File(TO_EXPORTED_DIR);
        final List<Source> sources = JarFileSourceCreator.tryCreateSourceFromFilesInDirectory(file);
        final List<Source> sourcesCopy = JarFileSourceCreator.tryCreateSourceFromFilesInDirectory(file, false);

        // Act / Assert
        assert(sources.size() == 2);
        testSources(sources);
        assert(sourcesCopy.size() == 2);
        testSources(sourcesCopy);
    }

    @Test
    public void tryCreateSourceFromFilesInDirectoryRecursingChildren() {
        // Arrange
        final File file = new File(TO_EXPORTED_DIR);
        final List<Source> sources = JarFileSourceCreator.tryCreateSourceFromFilesInDirectory(file, true);

        // Act / Assert
        assert(sources.size() == 3);
        testSources(sources);
    }

    @Test
    public void tryCreateSourceFromFilesInDirectoryFails() {
        // Arrange
        final File file = new File(IMPL_1);

        // Act Assert
        assertNull(JarFileSourceCreator.tryCreateSourceFromFilesInDirectory(file));
        assertNull(JarFileSourceCreator.tryCreateSourceFromFilesInDirectory(file, true));
    }

    private void testSources(final List<Source> sources) {
        final Service<TestService> service = new Service<>(TestService.class);
        sources.forEach(service::addSource);

        // Act / Assert
        assert(service.getServiceStream().count() == sources.size());
        service.getServiceStream().forEach((impl) -> {
            assert(impl.returnTrue());
        });
    }
}