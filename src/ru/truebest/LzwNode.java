package ru.truebest;

public class LzwNode {
    int prev;
    int next;
    byte ch;

    public LzwNode(int prew, int next, byte ch) {
        this.prev = prew;
        this.next = next;
        this.ch = ch;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public void setCh(byte ch) {
        this.ch = ch;
    }
}
