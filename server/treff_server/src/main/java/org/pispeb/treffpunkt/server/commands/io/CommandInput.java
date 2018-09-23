package org.pispeb.treffpunkt.server.commands.io;

import org.apache.commons.validator.routines.EmailValidator;
import org.pispeb.treffpunkt.server.service.domain.Position;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

/**
 * @author tim
 */
public abstract class CommandInput {

    // compile pattern only once
    private static final Pattern nonAlphanumericPattern
            = Pattern.compile("[^a-zA-Z0-9]");

    public boolean syntaxCheck() {
        // by default, no syntax check is made
        return true;
    }

    protected static boolean stringMaxLength(String string, int maxLength) {
        return string.length() <= maxLength;
    }

    protected static boolean stringAlphanumeric(String string) {
        // check if string contains any non-alphanumeric char, then invert
        return !nonAlphanumericPattern.matcher(string).find();
    }

    protected static boolean validatePosition(Position position) {
        return (-90.0 <= position.getLatitude()
                && +90.0 >= position.getLatitude()
                && -180.0 <= position.getLongitude()
                && +180.0 >= position.getLongitude()
                && validateDate(new Date(position.getTimeMeasured())));
    }

    protected static boolean validateDate(Date date) {
        Date epoch = new GregorianCalendar(1970, Calendar.JANUARY, 0, 0, 0, 0)
                .getTime();
        Date maxDate = new GregorianCalendar(9999, Calendar.DECEMBER, 31,
                23, 59, 59).getTime();
        return (!date.before(epoch)
                && !date.after(maxDate));
    }

    protected static boolean validateDate(long date) {
        return validateDate(new Date(date));
    }

    protected static boolean validateUsername(String username) {
        return stringAlphanumeric(username)
                && stringMaxLength(username, 32);
    }

    protected static boolean validatePassword(String password) {
        return stringMaxLength(password, 128);
    }

    protected static boolean validateChatMessage(String chatMessage) {
        return stringMaxLength(chatMessage, 1024);
    }

    protected static boolean validatePollQuestion(String pollQuestion) {
        return stringMaxLength(pollQuestion, 64);
    }

    protected static boolean validateEventTitle(String eventTitle) {
        return stringMaxLength(eventTitle, 64);
    }

    protected static boolean validateGroupName(String groupName) {
        return stringMaxLength(groupName, 64);
    }

    protected static boolean validateEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}
