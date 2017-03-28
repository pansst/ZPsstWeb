package com.psst.common.datastructure;

public class Node {
    private Object value;
    private Node next;
    
    public Node(Object value) {
        super();
        this.value = value;
        this.next = null;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    public Node getNext() {
        return next;
    }
    public void setNext(Node next) {
        this.next = next;
    }
    
}
