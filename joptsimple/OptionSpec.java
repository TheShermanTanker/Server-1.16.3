package joptsimple;

import java.util.List;

public interface OptionSpec<V> {
    List<V> values(final OptionSet optionSet);
    
    V value(final OptionSet optionSet);
    
    List<String> options();
    
    boolean isForHelp();
}
