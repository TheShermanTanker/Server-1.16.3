package org.apache.logging.log4j.core.config;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;

public class Node {
    public static final String CATEGORY = "Core";
    private final Node parent;
    private final String name;
    private String value;
    private final PluginType<?> type;
    private final Map<String, String> attributes;
    private final List<Node> children;
    private Object object;
    
    public Node(final Node parent, final String name, final PluginType<?> type) {
        this.attributes = (Map<String, String>)new HashMap();
        this.children = (List<Node>)new ArrayList();
        this.parent = parent;
        this.name = name;
        this.type = type;
    }
    
    public Node() {
        this.attributes = (Map<String, String>)new HashMap();
        this.children = (List<Node>)new ArrayList();
        this.parent = null;
        this.name = null;
        this.type = null;
    }
    
    public Node(final Node node) {
        this.attributes = (Map<String, String>)new HashMap();
        this.children = (List<Node>)new ArrayList();
        this.parent = node.parent;
        this.name = node.name;
        this.type = node.type;
        this.attributes.putAll((Map)node.getAttributes());
        this.value = node.getValue();
        for (final Node child : node.getChildren()) {
            this.children.add(new Node(child));
        }
        this.object = node.object;
    }
    
    public Map<String, String> getAttributes() {
        return this.attributes;
    }
    
    public List<Node> getChildren() {
        return this.children;
    }
    
    public boolean hasChildren() {
        return !this.children.isEmpty();
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public Node getParent() {
        return this.parent;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isRoot() {
        return this.parent == null;
    }
    
    public void setObject(final Object obj) {
        this.object = obj;
    }
    
    public <T> T getObject() {
        return (T)this.object;
    }
    
    public <T> T getObject(final Class<T> clazz) {
        return (T)clazz.cast(this.object);
    }
    
    public boolean isInstanceOf(final Class<?> clazz) {
        return clazz.isInstance(this.object);
    }
    
    public PluginType<?> getType() {
        return this.type;
    }
    
    public String toString() {
        if (this.object == null) {
            return "null";
        }
        return this.type.isObjectPrintable() ? this.object.toString() : (this.type.getPluginClass().getName() + " with name " + this.name);
    }
}
