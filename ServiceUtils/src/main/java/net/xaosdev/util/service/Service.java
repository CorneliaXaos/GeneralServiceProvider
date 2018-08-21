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

package net.xaosdev.util.service;

import net.xaosdev.util.service.internal.IsolatedServiceLoader;
import net.xaosdev.util.service.security.ServiceUtilityPermission;

import java.security.AccessController;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A Service is a set of automatically managed java.util.ServiceLoader instances.
 *
 * An IsolatedServiceLoader instance will be created for each source allowing new sources to be added and removed on
 * the fly by the user.  This will allow users to use the ServiceLoader indirectly, and only need to be concerned with
 * where the service classes are being loaded from.
 *
 * java.util.ServiceLoader best practices should still be enforced!  This means that, ideally, your SPI classes should
 * be interfaces, and the implementations should have no-argument or default constructors to ensure no unexpected
 * exceptions are fired.
 * @param <T> the SPI to find implementations for.
 */
public final class Service<T> {

    //region Fields (Private)

    /**
     * The SPI Class.
     */
    private final Class<T> clazz;

    /**
     * A mapping of UUIDs to Sources, for source management.
     */
    private final Map<UUID, Source> sourceMap = new HashMap<>();

    /**
     * A mapping of UUIDs to IsolatedServiceLoaders, for loader management.
     */
    private final Map<UUID, IsolatedServiceLoader<T>> loaderMap = new HashMap<>();

    //endregion

    //region Constructors (Public)

    /**
     * Creates a new Service.
     * @param clazz the Class object used to identify service implementations.
     */
    public Service(final Class<T> clazz) {
        if (System.getSecurityManager() != null) {
            AccessController.checkPermission(new ServiceUtilityPermission(ServiceUtilityPermission.Type.UPDATE));
        }

        this.clazz = clazz;
    }

    //endregion

    //region Interface (Public)

    /**
     * Adds a source to this Service.
     * @param source the Source to add.
     */
    public void addSource(final Source source) {
        if (System.getSecurityManager() != null) {
            AccessController.checkPermission(new ServiceUtilityPermission(ServiceUtilityPermission.Type.UPDATE));
        }

        if (sourceMap.containsKey(source.getUUID())) {
            throw new IllegalArgumentException("Source with UUID already added to this Service.");
        }

        sourceMap.put(source.getUUID(), source);
        loaderMap.put(source.getUUID(), IsolatedServiceLoader.load(clazz, source.getClassLoader()));
    }

    /**
     * Gets an unmodifiable view of all the sources within this Service.
     * @return an unmodifiable view of all the sources added to this Service.
     */
    public Collection<Source> getSources() {
        if (System.getSecurityManager() != null) {
            AccessController.checkPermission(new ServiceUtilityPermission(ServiceUtilityPermission.Type.UPDATE));
        }

        return Collections.unmodifiableCollection(sourceMap.values());
    }

    /**
     * Removes a Source from this Service.
     * @param source the Source to remove.
     * @return a boolean indicating if the source was removed.
     */
    public boolean removeSource(final Source source) {
        if (System.getSecurityManager() != null) {
            AccessController.checkPermission(new ServiceUtilityPermission(ServiceUtilityPermission.Type.UPDATE));
        }

        return removeSource(source.getUUID()) != null;
    }

    /**
     * Removes a Source from this Service.
     * @param uuid the UUID of the Source to remove.
     * @return the Source removed from this Service or null if none present.
     */
    public Source removeSource(final UUID uuid) {
        if (System.getSecurityManager() != null) {
            AccessController.checkPermission(new ServiceUtilityPermission(ServiceUtilityPermission.Type.UPDATE));
        }

        if (!sourceMap.containsKey(uuid)) {
            return null;
        }

        final Source source = sourceMap.remove(uuid);
        loaderMap.remove(uuid);
        return source;
    }

    /**
     * Gets a stream of all the service implementations within this Service.
     * @return a Stream to the implementations found by this Service.
     */
    public Stream<T> getServiceStream() {
        if (System.getSecurityManager() != null) {
            AccessController.checkPermission(new ServiceUtilityPermission(ServiceUtilityPermission.Type.ACCESS));
        }

        Stream<T> stream = Stream.empty();
        for (IsolatedServiceLoader<T> loader : loaderMap.values()) {
            stream = Stream.concat(stream, StreamSupport.stream(loader.spliterator(), false));
        }
        return stream;
    }

    //endregion
}
