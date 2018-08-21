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

package net.xaosdev.util.service.security;

import java.security.Policy;

/**
 * This class is used to facilitate the common security actions of securing a Java environment.
 */
public final class Security {

    //region Constructors (Private)

    /**
     * Private constructor to prevent instantiation.
     */
    private Security() {}

    //endregion

    //region Interface (Public)

    /**
     * Sets up a SecurityManager using whatever policy has already been declared and installed in the manager.
     *
     * If none have, this action merely attempts to install a new SecurityManager.  If one is already present then this
     * method throws an IllegalStateException.
     */
    public static void install() {
        if (System.getSecurityManager() != null) {
            throw new IllegalStateException("A SecurityManager has already been installed!");
        }
        System.setSecurityManager(new SecurityManager());
    }

    /**
     * Installs both the provided policy and a SecurityManager.
     *
     * This action will always overwrite the current Policy as the Policy should, generally, be installed prior to
     * the activation of a SecurityManager.  If a SecurityManager has already been installed then this method throws
     * an IllegalStateException.
     */
    public static void installWithPolicy(Policy policy) {
        Policy.setPolicy(policy);
        install();
    }

    /**
     * A convenience method for acquiring the SourceFilteringPolicy from the System-wide policy location.
     *
     * @return the SourceFilteringPolicy or null if the current policy is not a SourceFilteringPolicy.
     */
    public static SourceFilteringPolicy getSourceFilteringPolicy() {
        Policy current = Policy.getPolicy();
        if (current instanceof SourceFilteringPolicy) {
            return (SourceFilteringPolicy) current;
        } else {
            return null;
        }
    }

    //endregion
}
