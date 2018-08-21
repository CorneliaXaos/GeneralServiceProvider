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

import java.util.UUID;

/**
 * A representation of where services may be loaded from.
 *
 * A source is used to identify / create a ClassLoader for loading potential plugin classes.  This object should be
 * immutable after its construction and the source should NOT change with subsequent calls to `getClassLoader`.
 */
public interface Source {

    /**
     * Gets the UUID used for uniquely identifying this source.
     * @return the UUID used for uniquely identifying this source.
     */
    UUID getUUID();

    /**
     * Gets the ClassLoader this source uses to acquire service classes.
     * @return the ClassLoader this source uses to acquire service classes.
     */
    ClassLoader getClassLoader();
}
