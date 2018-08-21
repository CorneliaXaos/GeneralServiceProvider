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

import static org.junit.Assert.*;

public class SystemSourceTest {

    @Test
    public void getSource() {
        // Arrange
        final Source source = SystemSource.getSource();
        final Service<TestService> service = new Service<>(TestService.class);
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
        final Source a = SystemSource.getSource();
        final Source b = SystemSource.getSource();

        // Act - not needed
        // Assert
        assertEquals(a, b);
    }
}