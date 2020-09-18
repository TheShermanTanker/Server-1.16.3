package io.netty.handler.codec.xml;

public class XmlDTD {
    private final String text;
    
    public XmlDTD(final String text) {
        this.text = text;
    }
    
    public String text() {
        return this.text;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final XmlDTD xmlDTD = (XmlDTD)o;
        if (this.text != null) {
            if (this.text.equals(xmlDTD.text)) {
                return true;
            }
        }
        else if (xmlDTD.text == null) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        return (this.text != null) ? this.text.hashCode() : 0;
    }
    
    public String toString() {
        return "XmlDTD{text='" + this.text + '\'' + '}';
    }
}
