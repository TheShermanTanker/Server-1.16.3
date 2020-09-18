package joptsimple.internal;

class Row {
    final String option;
    final String description;
    
    Row(final String option, final String description) {
        this.option = option;
        this.description = description;
    }
    
    public boolean equals(final Object that) {
        if (that == this) {
            return true;
        }
        if (that == null || !this.getClass().equals(that.getClass())) {
            return false;
        }
        final Row other = (Row)that;
        return this.option.equals(other.option) && this.description.equals(other.description);
    }
    
    public int hashCode() {
        return this.option.hashCode() ^ this.description.hashCode();
    }
}
