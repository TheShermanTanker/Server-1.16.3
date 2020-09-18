package com.google.common.collect;

import java.util.Collection;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.function.Consumer;
import com.google.common.base.Preconditions;
import com.google.common.base.Function;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public abstract class TreeTraverser<T> {
    public static <T> TreeTraverser<T> using(final Function<T, ? extends Iterable<T>> nodeToChildrenFunction) {
        Preconditions.<Function<T, ? extends Iterable<T>>>checkNotNull(nodeToChildrenFunction);
        return new TreeTraverser<T>() {
            @Override
            public Iterable<T> children(final T root) {
                return (Iterable<T>)nodeToChildrenFunction.apply(root);
            }
        };
    }
    
    public abstract Iterable<T> children(final T object);
    
    public final FluentIterable<T> preOrderTraversal(final T root) {
        Preconditions.<T>checkNotNull(root);
        return new FluentIterable<T>() {
            public UnmodifiableIterator<T> iterator() {
                return (UnmodifiableIterator<T>)TreeTraverser.this.preOrderIterator(root);
            }
            
            public void forEach(final Consumer<? super T> action) {
                Preconditions.<Consumer<? super T>>checkNotNull(action);
                new Consumer<T>() {
                    public void accept(final T t) {
                        action.accept(t);
                        TreeTraverser.this.children(t).forEach((Consumer)this);
                    }
                }.accept(root);
            }
        };
    }
    
    UnmodifiableIterator<T> preOrderIterator(final T root) {
        return new PreOrderIterator(root);
    }
    
    public final FluentIterable<T> postOrderTraversal(final T root) {
        Preconditions.<T>checkNotNull(root);
        return new FluentIterable<T>() {
            public UnmodifiableIterator<T> iterator() {
                return (UnmodifiableIterator<T>)TreeTraverser.this.postOrderIterator(root);
            }
            
            public void forEach(final Consumer<? super T> action) {
                Preconditions.<Consumer<? super T>>checkNotNull(action);
                new Consumer<T>() {
                    public void accept(final T t) {
                        TreeTraverser.this.children(t).forEach((Consumer)this);
                        action.accept(t);
                    }
                }.accept(root);
            }
        };
    }
    
    UnmodifiableIterator<T> postOrderIterator(final T root) {
        return new PostOrderIterator(root);
    }
    
    public final FluentIterable<T> breadthFirstTraversal(final T root) {
        Preconditions.<T>checkNotNull(root);
        return new FluentIterable<T>() {
            public UnmodifiableIterator<T> iterator() {
                return new BreadthFirstIterator((T)root);
            }
        };
    }
    
    private final class PreOrderIterator extends UnmodifiableIterator<T> {
        private final Deque<Iterator<T>> stack;
        
        PreOrderIterator(final T root) {
            (this.stack = (Deque<Iterator<T>>)new ArrayDeque()).addLast(Iterators.singletonIterator((Object)Preconditions.<T>checkNotNull((T)root)));
        }
        
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }
        
        public T next() {
            final Iterator<T> itr = (Iterator<T>)this.stack.getLast();
            final T result = Preconditions.<T>checkNotNull(itr.next());
            if (!itr.hasNext()) {
                this.stack.removeLast();
            }
            final Iterator<T> childItr = (Iterator<T>)TreeTraverser.this.children(result).iterator();
            if (childItr.hasNext()) {
                this.stack.addLast(childItr);
            }
            return result;
        }
    }
    
    private static final class PostOrderNode<T> {
        final T root;
        final Iterator<T> childIterator;
        
        PostOrderNode(final T root, final Iterator<T> childIterator) {
            this.root = Preconditions.<T>checkNotNull(root);
            this.childIterator = Preconditions.<Iterator<T>>checkNotNull(childIterator);
        }
    }
    
    private final class PostOrderIterator extends AbstractIterator<T> {
        private final ArrayDeque<PostOrderNode<T>> stack;
        
        PostOrderIterator(final T root) {
            (this.stack = (ArrayDeque<PostOrderNode<T>>)new ArrayDeque()).addLast(this.expand(root));
        }
        
        @Override
        protected T computeNext() {
            while (!this.stack.isEmpty()) {
                final PostOrderNode<T> top = (PostOrderNode<T>)this.stack.getLast();
                if (!top.childIterator.hasNext()) {
                    this.stack.removeLast();
                    return top.root;
                }
                final T child = (T)top.childIterator.next();
                this.stack.addLast(this.expand(child));
            }
            return this.endOfData();
        }
        
        private PostOrderNode<T> expand(final T t) {
            return new PostOrderNode<T>(t, (java.util.Iterator<T>)TreeTraverser.this.children(t).iterator());
        }
    }
    
    private final class BreadthFirstIterator extends UnmodifiableIterator<T> implements PeekingIterator<T> {
        private final Queue<T> queue;
        
        BreadthFirstIterator(final T root) {
            (this.queue = (Queue<T>)new ArrayDeque()).add(root);
        }
        
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }
        
        @Override
        public T peek() {
            return (T)this.queue.element();
        }
        
        @Override
        public T next() {
            final T result = (T)this.queue.remove();
            Iterables.addAll((java.util.Collection<Object>)this.queue, TreeTraverser.this.children(result));
            return result;
        }
    }
}
