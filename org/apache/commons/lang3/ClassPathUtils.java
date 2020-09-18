package org.apache.commons.lang3;

public class ClassPathUtils {
    public static String toFullyQualifiedName(final Class<?> context, final String resourceName) {
        Validate.<Class<?>>notNull(context, "Parameter '%s' must not be null!", new Object[] { "context" });
        Validate.<String>notNull(resourceName, "Parameter '%s' must not be null!", new Object[] { "resourceName" });
        return toFullyQualifiedName(context.getPackage(), resourceName);
    }
    
    public static String toFullyQualifiedName(final Package context, final String resourceName) {
        Validate.<Package>notNull(context, "Parameter '%s' must not be null!", new Object[] { "context" });
        Validate.<String>notNull(resourceName, "Parameter '%s' must not be null!", new Object[] { "resourceName" });
        return context.getName() + "." + resourceName;
    }
    
    public static String toFullyQualifiedPath(final Class<?> context, final String resourceName) {
        Validate.<Class<?>>notNull(context, "Parameter '%s' must not be null!", new Object[] { "context" });
        Validate.<String>notNull(resourceName, "Parameter '%s' must not be null!", new Object[] { "resourceName" });
        return toFullyQualifiedPath(context.getPackage(), resourceName);
    }
    
    public static String toFullyQualifiedPath(final Package context, final String resourceName) {
        Validate.<Package>notNull(context, "Parameter '%s' must not be null!", new Object[] { "context" });
        Validate.<String>notNull(resourceName, "Parameter '%s' must not be null!", new Object[] { "resourceName" });
        return context.getName().replace('.', '/') + "/" + resourceName;
    }
}
