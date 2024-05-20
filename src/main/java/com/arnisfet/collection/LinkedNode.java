package com.arnisfet.collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Realisation of Doubly linked loop node.
 * @param <T>
 */
@Getter
@Setter
@NoArgsConstructor
public class LinkedNode <T> {
    private T data;

    private LinkedNode<T> first;
    private LinkedNode<T> next;
    private LinkedNode<T> prev;
    private LinkedNode<T> neighbour;

    public LinkedNode(T data) {
        this.data = data;
    }

    public LinkedNode(T data, LinkedNode<T> next, LinkedNode<T> prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public void add(LinkedNode<T> insertElement) {
        first.getPrev().setNext(insertElement);
        insertElement.setPrev(first.getPrev());
        insertElement.setFirst(first);
        insertElement.setNext(first);
        first.setPrev(insertElement);
    }

    public void push (LinkedNode<T> prevElem, LinkedNode<T> pusher) {
        LinkedNode<T> nextElem = prevElem.getNext();
        nextElem.setPrev(pusher);
        prevElem.setNext(pusher);
        pusher.setNext(nextElem);
        pusher.setPrev(prevElem);
        pusher.setFirst(prevElem.getFirst());
    }
}
