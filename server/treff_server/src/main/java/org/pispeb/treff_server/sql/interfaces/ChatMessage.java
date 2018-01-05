package org.pispeb.treff_server.sql.interfaces;

import java.util.Date;

// natural ordering = ascending order of timeSent
public interface ChatMessage extends Comparable<ChatMessage> {

    Account getSender();
    String getContent();
    Date getTimeSent();

}
