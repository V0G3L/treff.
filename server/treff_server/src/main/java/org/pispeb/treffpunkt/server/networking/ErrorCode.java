package org.pispeb.treffpunkt.server.networking;


import javax.ws.rs.WebApplicationException;

public enum ErrorCode {

    /**
     * error: parameter wasn't given or has a wrong format
     */
    SYNTAXINVALID(1000),

    /**
     * error: unknown command
     */
    UNKNOWN_COMMAND(1001),

    /**
     * error: internal server error
     */
    INTERNAL_SERVER_ERROR(1002),

    /**
     * error: authentication token invalid
     */
    TOKENINVALID(1100),

    /**
     * error: username/password combination invalid
     */
    CREDWRONG(1101),

    /**
     * error: password reset code invalid
     */
    RESETCODEINVALID(1102),

    /**
     * error: at least one account identification number is invalid
     */
    USERIDINVALID(1200),

    /**
     * error: group identification number is invalid or account not part of the group
     */
    GROUPIDINVALID(1201),

    /**
     * error: event identification number invalid
     */
    EVENTIDINVALID(1202),

    /**
     * error: poll identification number invalid
     */
    POLLIDINVALID(1203),

    /**
     * error: poll option identification number invalid
     */
    POLLOPTIONIDINVALID(1204),

    /**
     * error: username already in use
     */
    USERNAMEALREADYINUSE(1300),

    /**
     * error: email is invalid
     */
    EMAILINVALID(1301),

    /**
     * error: username is invalid
     */
    USERNAMEINVALID(1302),

    /**
     * error: at least one end time is in the past
     */
    TIMEENDINPAST(1400),

    /**
     * error: at least one end time is not after the corresponding start time
     */
    TIMEENDSTARTCONFLICT(1401),

    /**
     * error: the time of measurement is to far in the future
     */
    TIMEMEASUREDFUTURE(1402),

    /**
     * error: account already in contact list
     */
    ALREADYINCONTACT(1500),

    /**
     * error: account isn't part of the own contact list (not-in-contact)
     */
    NOTINCONTACT(1501),

    /**
     * error: account can't be a contact of itself
     */
    REFLEXIVCONTACT(1502),

    /**
     * error: contact request already sent and still pending
     */
    CONTACTREQUESTPENDING(1503),

    /**
     * error: no contact request from specified user
     */
    NOCONTACTREQUEST(1504),

    /**
     * error: this account was blocked by the given account
     */
    BEINGBLOCKED(1505),

    /**
     * error: the given account was blocked by this account
     */
    BLOCKINGALREADY(1506),

    /**
     * error: the given account wasn't blocked by this account
     */
    NOTBLOCKING(1507),

    /**
     * error: account can't block itself
     */
    REFLEXIVBLOCK(1508),

    /**
     * error: no account invited except the own account
     */
    NOUSERINVITED(1509),

    /**
     * error: at least one account is already part of the group
     */
    USERALREADYINGROUP(1510),

    /**
     * error: at least one account is not part of the group
     */
    USERNOTINGROUP(1511),

    /**
     * error: account already part of this event
     */
    ALREADYPARTICIPATINGEVENT(1512),

    /**
     * error: account no part of this event
     */
    NOTPARTICIPATINGEVENT(1513),

    /**
     * error: account already voted for this option
     */
    ALREADYVOTINGFOROPTION(1514),

    /**
     * error: multi-choice is not available for this poll
     */
    POLLNOTMULTICHOICE(1515),

    /**
     * error: account didn't vote for this option
     */
    NOTVOTINGFOROPTION(1516),

    /**
     * error: account doesn't possess the permission to edit permissions
     */
    NOPERMISSIONEDITPERMISSION(2000),

    /**
     * error: account doesn't possess the permission to edit the group
     */
    NOPERMISSIONEDITGROUP(2100),

    /**
     * error: account doesn't possess the permission to edit the list
     * of group members
     */
    NOPERMISSIONMANAGEMEMBERS(2101),

    /**
     * error: account doesn't possess the permission to create events
     * in this group
     */
    NOPERMISSIONCREATEEVENT(2200),

    /**
     * error: account doesn't possess the permission to edit the event
     */
    NOPERMISSIONEDITANYEVENT(2201),

    /**
     * error: account doesn't possess the permission to create polls
     * in this group
     */
    NOPERMISSIONCREATEPOLL(2300),

    /**
     * error: account doesn't possess the permission to edit the poll
     */
    NOPERMISSIONEDITANYPOLL(2301);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public WebApplicationException toWebException() {
        return new WebApplicationException(String.format("{\"error\":%d}", getCode()), 400);
    }
}
