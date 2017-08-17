import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private final Node sentinel;
    private int size;

    private class Node {
        private Item item;
        private Node prev;
        private Node next;
    }

    // construct an empty deque
    public Deque() {
        sentinel = new Node();
        sentinel.item = null;
        sentinel.next = sentinel;
        sentinel.prev = sentinel;

        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return sentinel.next == sentinel;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        add(item, sentinel, sentinel.next);
    }

    // add the item to the end
    public void addLast(Item item) {
        add(item, sentinel.prev, sentinel);
    }

    private void add(Item item, Node prev, Node next) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node newNode = new Node();
        newNode.item = item;

        newNode.prev = prev;
        prev.next = newNode;

        newNode.next = next;
        next.prev = newNode;

        ++size;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        return remove(sentinel.next);
    }

    // remove and return the item from the end
    public Item removeLast() {
        return remove(sentinel.prev);
    }

    private Item remove(Node removed) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        removed.prev.next = removed.next;
        removed.next.prev = removed.prev;

        --size;

        return removed.item;
    }

    private class DequeIterator implements Iterator<Item> {

        private Node next = sentinel.next;

        @Override
        public boolean hasNext() {
            return next != sentinel;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = next.item;
            next = next.next;
            return item;
        }
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
}