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
import testing.producer.spi.TestServiceInstalled;

import static org.junit.Assert.*;

public class ExtensionsSourceTest {

    @Test
    public void getSource() {
        // Arrange
        final Source source = ExtensionsSource.getSource();
        final Service<TestServiceInstalled> service = new Service<>(TestServiceInstalled.class);
        service.addSource(source);

        // Act / Assert
        assert(service.getServiceStream().count() == 1);
        service.getServiceStream().forEach(impl -> {
            assert(impl.returnTrue());
        });
    }

    @Test
    public void getSourceDuplicatesAreSame() {
        // Arrange
        final Source a = ExtensionsSource.getSource();
        final Source b = ExtensionsSource.getSource();

        // Act - not needed
        // Assert
        assertEquals(a, b);
    }
}