package joptsimple;

import java.util.List;

public interface OptionDeclarer {
    OptionSpecBuilder accepts(final String string);
    
    OptionSpecBuilder accepts(final String string1, final String string2);
    
    OptionSpecBuilder acceptsAll(final List<String> list);
    
    OptionSpecBuilder acceptsAll(final List<String> list, final String string);
    
    NonOptionArgumentSpec<String> nonOptions();
    
    NonOptionArgumentSpec<String> nonOptions(final String string);
    
    void posixlyCorrect(final boolean boolean1);
    
    void allowsUnrecognizedOptions();
    
    void recognizeAlternativeLongOptions(final boolean boolean1);
}
