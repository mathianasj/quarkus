package org.jboss.shamrock.deployment.configuration;

import io.smallrye.config.SmallRyeConfig;
import org.jboss.protean.gizmo.BytecodeCreator;
import org.jboss.protean.gizmo.ResultHandle;
import org.jboss.shamrock.deployment.AccessorFinder;
import org.jboss.shamrock.runtime.configuration.NameIterator;

/**
 * A node which can be a root.
 */
public abstract class CompoundConfigType extends ConfigType {
    CompoundConfigType(final String containingName, final CompoundConfigType container, final boolean consumeSegment) {
        super(containingName, container, consumeSegment);
    }

    /**
     * Get or create a child instance of this node.
     *
     * @param name the property name of the child instance (must not be {@code null})
     * @param config the configuration (must not be {@code null})
     * @param self the instance of this node (must not be {@code null})
     * @param childName the static child name, or {@code null} if the child name is dynamic
     * @return the child instance
     */
    abstract Object getChildObject(NameIterator name, SmallRyeConfig config, Object self, String childName);

    abstract ResultHandle generateGetChildObject(BytecodeCreator body, ResultHandle name, ResultHandle config, ResultHandle self, String childName);

    /**
     * Set a child object on the given instance.
     *
     * @param name the child property name iterator
     * @param self the instance of this configuration type
     * @param containingName the child property name
     * @param value the child property value
     */
    abstract void setChildObject(NameIterator name, Object self, String containingName, Object value);

    abstract void generateSetChildObject(BytecodeCreator body, ResultHandle name, ResultHandle self, String containingName, ResultHandle value);

    /**
     * Get or create the instance of this root, recursively adding it to its parent if necessary.
     *
     * @param name the name of this property node (must not be {@code null})
     * @param config the configuration (must not be {@code null})
     * @return the possibly new object instance
     */
    abstract Object getOrCreate(NameIterator name, SmallRyeConfig config);

    abstract ResultHandle generateGetOrCreate(BytecodeCreator body, ResultHandle name, ResultHandle config);

    /**
     * Register a realized instance.
     *
     * @param key the partial actual configuration key of the instance
     * @param type the type to associate
     * @param instance the registered instance
     */
    void registerInstance(String key, CompoundConfigType type, Object instance) {
        getContainer().registerInstance(key, type, instance);
    }

    public void registerRootType(final CompoundConfigType type, final AccessorFinder accessorFinder) {
        getContainer().registerRootType(type, accessorFinder);
    }

    public boolean isRoot() {
        return getContainer() == getConfigDefinition();
    }

    public abstract String getClassName();

    abstract void acceptConfigurationValueIntoLeaf(LeafConfigType leafType, NameIterator name, SmallRyeConfig config);

    abstract void generateAcceptConfigurationValueIntoLeaf(BytecodeCreator body, LeafConfigType leafType, ResultHandle name, ResultHandle config);
}