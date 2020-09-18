package com.mojang.brigadier.suggestion;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import java.util.concurrent.CompletableFuture;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class SuggestionsBuilder {
    private final String input;
    private final int start;
    private final String remaining;
    private final List<Suggestion> result;
    
    public SuggestionsBuilder(final String input, final int start) {
        this.result = (List<Suggestion>)new ArrayList();
        this.input = input;
        this.start = start;
        this.remaining = input.substring(start);
    }
    
    public String getInput() {
        return this.input;
    }
    
    public int getStart() {
        return this.start;
    }
    
    public String getRemaining() {
        return this.remaining;
    }
    
    public Suggestions build() {
        return Suggestions.create(this.input, (Collection<Suggestion>)this.result);
    }
    
    public CompletableFuture<Suggestions> buildFuture() {
        return (CompletableFuture<Suggestions>)CompletableFuture.completedFuture(this.build());
    }
    
    public SuggestionsBuilder suggest(final String text) {
        if (text.equals(this.remaining)) {
            return this;
        }
        this.result.add(new Suggestion(StringRange.between(this.start, this.input.length()), text));
        return this;
    }
    
    public SuggestionsBuilder suggest(final String text, final Message tooltip) {
        if (text.equals(this.remaining)) {
            return this;
        }
        this.result.add(new Suggestion(StringRange.between(this.start, this.input.length()), text, tooltip));
        return this;
    }
    
    public SuggestionsBuilder suggest(final int value) {
        this.result.add(new IntegerSuggestion(StringRange.between(this.start, this.input.length()), value));
        return this;
    }
    
    public SuggestionsBuilder suggest(final int value, final Message tooltip) {
        this.result.add(new IntegerSuggestion(StringRange.between(this.start, this.input.length()), value, tooltip));
        return this;
    }
    
    public SuggestionsBuilder add(final SuggestionsBuilder other) {
        this.result.addAll((Collection)other.result);
        return this;
    }
    
    public SuggestionsBuilder createOffset(final int start) {
        return new SuggestionsBuilder(this.input, start);
    }
    
    public SuggestionsBuilder restart() {
        return new SuggestionsBuilder(this.input, this.start);
    }
}
