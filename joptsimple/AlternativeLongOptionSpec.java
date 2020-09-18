package joptsimple;

import joptsimple.internal.Messages;
import java.util.Locale;
import java.util.Collections;

class AlternativeLongOptionSpec extends ArgumentAcceptingOptionSpec<String> {
    AlternativeLongOptionSpec() {
        super(Collections.singletonList("W"), true, Messages.message(Locale.getDefault(), "joptsimple.HelpFormatterMessages", AlternativeLongOptionSpec.class, "description"));
        this.describedAs(Messages.message(Locale.getDefault(), "joptsimple.HelpFormatterMessages", AlternativeLongOptionSpec.class, "arg.description"));
    }
    
    @Override
    protected void detectOptionArgument(final OptionParser parser, final ArgumentList arguments, final OptionSet detectedOptions) {
        if (!arguments.hasMore()) {
            throw new OptionMissingRequiredArgumentException(this);
        }
        arguments.treatNextAsLongOption();
    }
}
