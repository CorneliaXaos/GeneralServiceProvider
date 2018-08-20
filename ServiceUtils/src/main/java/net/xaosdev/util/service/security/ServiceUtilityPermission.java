package net.xaosdev.util.service.security;

import java.security.BasicPermission;

/**
 * The Permission governing access to the Service Utility.
 */
public final class ServiceUtilityPermission extends BasicPermission {

    //region Fields (Public)

    /**
     * The base name of a ServiceUtilityPermission.
     */
    public static final String NAME_BASE = "net.xaosdev.util.service.";

    //endregion

    //region Constructors (Public)

    /**
     * Constructs a new ServiceUtilityPermission from name.
     * @param name the String name identifying this permission.
     */
    public ServiceUtilityPermission(final String name) {
        super(name);
    }

    /**
     * Constructs a new ServiceUtilityPermission based on the provided permission type.
     *
     * This constructor allows users to ignore the specific name requirements when creating a permission instance.
     * @param type the type of ServiceUtilityPermission to create.
     */
    public ServiceUtilityPermission(final Type type) {
        this(type.getName());
    }

    //endregion

    //region Enums (Public)

    /**
     * An enumeration of the types of ServiceUtilityPermissions.
     */
    public enum Type {
        /**
         * Permission governing whether or not access is allowed to the service providers enumerated by a Service.
         * This includes creating new services.
         */
        ACCESS("access"),
        /**
         * Permission governing whether or not a domain can edit a Service via its mutation methods.
         */
        UPDATE("update"),
        /**
         * All ServiceUtilityPermissions.
         */
        ALL("*");

        //region Fields (Private)

        /**
         * The identifying name / suffix of a ServiceUtilityPermission.
         */
        final String name;

        //endregion

        //region Constructor (Private)

        /**
         * Constructs a ServiceUtilityPermission type.
         * @param name the identifying name / suffix of a ServiceUtilityPermission.
         */
        Type(final String name) {
            this.name = name;
        }

        //endregion

        //region Interface (Public)

        /**
         * Gets the full name for a ServiceUtilityPermission.
         * @return the full name for a ServiceUtilityPermission.
         */
        public String getName() {
            return NAME_BASE + name;
        }

        //endregion
    }

    //endregion
}
