package org.pispeb.treff_server.networking;


public enum ErrorCode {

    /**
     * error: parameter wasn't given or has a wrong format
     */
    SYNTAXINVALID(100),

    /**
     * error: unknown command
     */
    UNKNOWN_COMMAND(101),

    /**
     * error: authentication token invalid
     */
    TOKENINVALID(110),

    /**
     * error: username/password combination invalid
     */
    CREDWRONG(111),

    /**
     * error: password reset code invalid
     */
    RESETCODEINVALID(112),

    /**
     * error: at least one account identification number is invalid
     */
    USERIDINVALID(120),

    /**
     * error: group identification number is invalid or account not part of the group
     */
    GROUPIDINVALID(121),

    /**
     * error: event identification number invalid
     */
    EVENTIDINVALID(122),

    /**
     * error: poll identification number invalid
     */
    POLLIDINVALID(123),

    /**
     * error: poll option identification number invalid
     */
    POLLOPTIONIDINVALID(124),

    /**
     * error: username already in use
     */
    USERNAMEALREADYINUSE(130),

    /**
     * error: email is invalid
     */
    EMAILINVALID(131),

    /**
     * error: username is invalid
     */
    USERNAMEINVALID(132),

    /**
     * error: at least one end time is in the past
     */
    TIMEENDINPAST(140),

    /**
     * error: at least one end time is not after the corresponding start time
     */
    TIMEENDSTARTCONFLICT(141),

    /**
     * error: the time of measurement is to far in the future
     */
    TIMEMEASUREDFUTURE(142),

    /**
     * error: account isn't part of the own contact list (not-in-contact)
     */
    NOTINCONTACT(150),

    /**
     * error: account can't be a contact of itself
     */
    REFLEXIVCONTACT(151),

    /**
     * error: this account was blocked by the given account
     */
    BEINGBLOCKED(152),

    /**
     * error: the given account was blocked by this account
     */
    BLOCKINGALREADY(153),

    /**
     * error: the given account wasn't blocked by this account
     */
    NOTBLOCKING(154),

    /**
     * error: account can't block itself
     */
    REFLEXIVBLOCK(155),

    /**
     * error: no account invited except the own account
     */
    NOUSERINVITED(156),

    /**
     * error: at least one account is already part of the group
     */
    USERALREADYINGROUP(157),

    /**
     * error: at least one account is not part of the group
     */
    USERNOTINGROUP(158),

    /**
     * error: account already part of this event
     */
    ALREADYPARTICIPATINGEVENT(159),

    /**
     * error: account no part of this event
     */
    NOTPARTICIPATINGEVENT(161),

    /**
     * error: account already voted for this option
     */
    ALREADYVOTINGFOROPTION(162),

    /**
     * error: multi-choice is not available for this poll
     */
    POLLNOTMULTICHOICE(163),

    /**
     * error: account didn't vote for this option
     */
    NOTVOTINGFOROPTION(164),

    /**
     * error: account doesn't possess the permission to edit permissions
     */
    NOPERMISSIONEDITPERMISSION(200),

    /**
     * error: account doesn't possess the permission to edit the group
     */
    NOPERMISSIONEDITGROUP(210),

    /**
     * error: account doesn't possess the permission to edit the list of group members
     */
    NOPERMISSIONMANAGEMEMBERS(211),

    /**
     * error: account doesn't possess the permission to create events in this group
     */
    NOPERMISSIONCREATEEVENT(220),

    /**
     * error: account doesn't possess the permission to edit the event
     */
    NOPERMISSIONEDITANYEVENT(221),

    /**
     * error: account doesn't possess the permission to create polls in this group
     */
    NOPERMISSIONCREATEPOLL(230),

    /**
     * error: account doesn't possess the permission to edit the poll
     */
    NOPERMISSIONEDITANYPOLL(231);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
