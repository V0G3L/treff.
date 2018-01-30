package org.pispeb.treff_server.networking;

import org.pispeb.treff_server.commands.AbstractCommand;
import org.pispeb.treff_server.interfaces.AccountManager;

import java.lang.reflect.InvocationTargetException;

/**
 * @author tim
 */
public class Request {

    private Class<? extends AbstractCommand> commandClass;

    public void setCmd(String cmd) {
        // convert from kebab-case to upper camel case
        StringBuilder convCmd = new StringBuilder();
        for (String part : cmd.split("-")) {
            // change first letter of each part to uppercase
            part = part.substring(0, 1).toUpperCase() + part.substring(1);
            convCmd.append(part);
        }

        // append "Command" to get class name
        String cmdClassName = convCmd.toString() + "Command";
        // get package of command classes
        // assumes that this class is in package a.b.c.networking
        // and commands are in a.b.c.commands
        String packageName
                = this.getClass().getPackage().getName()
                .replaceAll("networking", "commands");

        try {
            //noinspection unchecked
            commandClass = (Class<? extends AbstractCommand>)
                    Class.forName(packageName + "." + cmdClassName);
        } catch (ClassNotFoundException | ClassCastException e) {
            commandClass = null;
        }
    }

    public AbstractCommand getCommandObject(AccountManager accountManager) {
        if (commandClass == null)
            return null;
        // construct a new command object for the chosen class
        try {
            return commandClass.getConstructor(AccountManager.class)
                    .newInstance(accountManager);
        } catch (InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e) {
            throw new AssertionError("A Request object failed " +
                    "to construct an object of the chosen command class. " +
                    "This should only happen when a command class uses " +
                    "a non-standard constructor and indicates a " +
                    "programming error that cannot be fixed at runtime.");
        }
    }

}
