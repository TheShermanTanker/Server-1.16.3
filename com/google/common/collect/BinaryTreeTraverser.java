package com.google.common.collect;

import java.util.BitSet;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.base.Optional;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public abstract class BinaryTreeTraverser<T> extends TreeTraverser<T> {
    public abstract Optional<T> leftChild(final T object);
    
    public abstract Optional<T> rightChild(final T object);
    
    @Override
    public final Iterable<T> children(final T root) {
        Preconditions.<T>checkNotNull(root);
        return (Iterable<T>)new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return (Iterator<T>)new AbstractIterator<T>() {
                    boolean doneLeft;
                    boolean doneRight;
                    
                    @Override
                    protected T computeNext() {
                        if (!this.doneLeft) {
                            this.doneLeft = true;
                            final Optional<T> left = (Optional<T>)BinaryTreeTraverser.this.leftChild(root);
                            if (left.isPresent()) {
                                return left.get();
                            }
                        }
                        if (!this.doneRight) {
                            this.doneRight = true;
                            final Optional<T> right = (Optional<T>)BinaryTreeTraverser.this.rightChild(root);
                            if (right.isPresent()) {
                                return right.get();
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            public void forEach(final Consumer<? super T> action) {
                BinaryTreeTraverser.acceptIfPresent((java.util.function.Consumer<? super Object>)action, BinaryTreeTraverser.this.leftChild(root));
                BinaryTreeTraverser.acceptIfPresent((java.util.function.Consumer<? super Object>)action, BinaryTreeTraverser.this.rightChild(root));
            }
        };
    }
    
    @Override
    UnmodifiableIterator<T> preOrderIterator(final T root) {
        return new PreOrderIterator(root);
    }
    
    @Override
    UnmodifiableIterator<T> postOrderIterator(final T root) {
        return new PostOrderIterator(root);
    }
    
    public final FluentIterable<T> inOrderTraversal(final T root) {
        Preconditions.<T>checkNotNull(root);
        return new FluentIterable<T>() {
            public UnmodifiableIterator<T> iterator() {
                return new InOrderIterator((T)root);
            }
            
            public void forEach(final Consumer<? super T> action) {
                Preconditions.<Consumer<? super T>>checkNotNull(action);
                new Consumer<T>() {
                    public void accept(final T t) {
                        BinaryTreeTraverser.acceptIfPresent((java.util.function.Consumer<? super Object>)this, (Optional<Object>)BinaryTreeTraverser.this.leftChild(t));
                        action.accept(t);
                        BinaryTreeTraverser.acceptIfPresent((java.util.function.Consumer<? super Object>)this, (Optional<Object>)BinaryTreeTraverser.this.rightChild(t));
                    }
                }.accept(root);
            }
        };
    }
    
    private static <T> void pushIfPresent(final Deque<T> stack, final Optional<T> node) {
        if (node.isPresent()) {
            stack.addLast(node.get());
        }
    }
    
    private static <T> void acceptIfPresent(final Consumer<? super T> action, final Optional<T> node) {
        if (node.isPresent()) {
            action.accept(node.get());
        }
    }
    
    private final class PreOrderIterator extends UnmodifiableIterator<T> implements PeekingIterator<T> {
        private final Deque<T> stack;
        
        PreOrderIterator(final T root) {
            (this.stack = (Deque<T>)new ArrayDeque(8)).addLast(root);
        }
        
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }
        
        @Override
        public T next() {
            final T result = (T)this.stack.removeLast();
            BinaryTreeTraverser.pushIfPresent((java.util.Deque<Object>)this.stack, BinaryTreeTraverser.this.rightChild(result));
            BinaryTreeTraverser.pushIfPresent((java.util.Deque<Object>)this.stack, BinaryTreeTraverser.this.leftChild(result));
            return result;
        }
        
        @Override
        public T peek() {
            return (T)this.stack.getLast();
        }
    }
    
    private final class PostOrderIterator extends UnmodifiableIterator<T> {
        private final Deque<T> stack;
        private final BitSet hasExpanded;
        
        PostOrderIterator(final T root) {
            (this.stack = (Deque<T>)new ArrayDeque(8)).addLast(root);
            this.hasExpanded = new BitSet();
        }
        
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }
        
        public T next() {
            T node;
            while (true) {
                node = (T)this.stack.getLast();
                final boolean expandedNode = this.hasExpanded.get(this.stack.size() - 1);
                if (expandedNode) {
                    break;
                }
                this.hasExpanded.set(this.stack.size() - 1);
                BinaryTreeTraverser.pushIfPresent((java.util.Deque<Object>)this.stack, BinaryTreeTraverser.this.rightChild(node));
                BinaryTreeTraverser.pushIfPresent((java.util.Deque<Object>)this.stack, BinaryTreeTraverser.this.leftChild(node));
            }
            this.stack.removeLast();
            this.hasExpanded.clear(this.stack.size());
            return node;
        }
    }
    
    private final class InOrderIterator extends AbstractIterator<T> {
        private final Deque<T> stack;
        private final BitSet hasExpandedLeft;
        
        InOrderIterator(final T root) {
            this.stack = (Deque<T>)new ArrayDeque(8);
            this.hasExpandedLeft = new BitSet();
            this.stack.addLast(root);
        }
        
        @Override
        protected T computeNext() {
            while (!this.stack.isEmpty()) {
                final T node = (T)this.stack.getLast();
                if (this.hasExpandedLeft.get(this.stack.size() - 1)) {
                    this.stack.removeLast();
                    this.hasExpandedLeft.clear(this.stack.size());
                    BinaryTreeTraverser.pushIfPresent((java.util.Deque<Object>)this.stack, BinaryTreeTraverser.this.rightChild(node));
                    return node;
                }
                this.hasExpandedLeft.set(this.stack.size() - 1);
                BinaryTreeTraverser.pushIfPresent((java.util.Deque<Object>)this.stack, BinaryTreeTraverser.this.leftChild(node));
            }
            return this.endOfData();
        }
    }
}
