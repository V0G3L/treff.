package org.pispeb.treffpunkt.server.service.domain;

public class ContactList {

    private int[] contacts;
    private int[] incoming;
    private int[] outgoing;
    private int[] blocks;

    public ContactList() { }

    public ContactList(int[] contacts, int[] incoming,
                       int[] outgoing, int[] blocks) {
        this.contacts = contacts;
        this.incoming = incoming;
        this.outgoing = outgoing;
        this.blocks = blocks;
    }

    public int[] getContacts() {
        return contacts;
    }

    public void setContacts(int[] contacts) {
        this.contacts = contacts;
    }

    public int[] getIncoming() {
        return incoming;
    }

    public void setIncoming(int[] incoming) {
        this.incoming = incoming;
    }

    public int[] getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(int[] outgoing) {
        this.outgoing = outgoing;
    }

    public int[] getBlocks() {
        return blocks;
    }

    public void setBlocks(int[] blocks) {
        this.blocks = blocks;
    }
}
