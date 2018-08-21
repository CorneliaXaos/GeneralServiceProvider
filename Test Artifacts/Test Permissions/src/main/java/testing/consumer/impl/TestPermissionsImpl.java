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

package testing.consumer.impl;

import net.xaosdev.util.service.Service;
import net.xaosdev.util.service.sources.SystemSource;
import testing.producer.spi.TestPermissions;
import testing.producer.spi.TestService;

public class TestPermissionsImpl implements TestPermissions {

    @Override
    public void testCreatingService() {
        final Service<TestService> service = new Service<>(TestService.class);
    }

    @Override
    public void testAddingSources(final Object object) {
        final Service<TestService> service = (Service<TestService>) object;
        service.addSource(SystemSource.getSource());
    }

    @Override
    public void testGettingSources(final Object object) {
        final Service<TestService> service = (Service<TestService>) object;
        service.getSources();
    }

    @Override
    public void testRemovingSources(final Object object) {
        final Service<TestService> service = (Service<TestService>) object;
        service.removeSource(SystemSource.getSource());
    }

    @Override
    public void testGettingServiceStream(final Object object) {
        final Service<TestService> service = (Service<TestService>) object;
        service.getServiceStream();
    }
}
