package charlie.parser;

import charlie.commands.AddCommand;
import charlie.commands.Command;
import charlie.commands.DeleteCommand;
import charlie.commands.ExitCommand;
import charlie.commands.FindCommand;
import charlie.commands.ListCommand;
import charlie.commands.MarkCommand;
import charlie.exceptions.CharlieException;
public class Parser {

    /**
     * takes in the user full command, and returns the appropriate type of command based on user input
     * @param fullCommand the user command in its full string form
     * @return the command to be executed
     * @throws CharlieException
     */
    public static Command parse(String fullCommand) throws CharlieException {

        if (fullCommand.startsWith("delete")) {
            String[] words = fullCommand.split(" ");
            assert words.length > 1 : "Delete command format is incorrect";
            return new DeleteCommand(Integer.valueOf(words[1]));
        } else if (fullCommand.startsWith("todo") || fullCommand.startsWith("event")
                || fullCommand.startsWith("deadline")) {
            return new AddCommand(fullCommand);
        } else if (fullCommand.startsWith("list")) {
            return new ListCommand();
        } else if (fullCommand.startsWith("bye")) {
            return new ExitCommand();
        } else if (fullCommand.startsWith("mark")) {
            String[] words = fullCommand.split(" ");
            assert words.length > 1 : "Mark command format is incorrect";
            return new MarkCommand(Integer.valueOf(words[1]));
        } else if (fullCommand.startsWith("unmark")) {
            String[] words = fullCommand.split(" ");
            assert words.length > 1 : "Unmark command format is incorrect";
            return new UnmarkCommand(Integer.valueOf(words[1]));
        } else if (fullCommand.startsWith("find")) {
            String[] words = fullCommand.split(" ");
            assert words.length > 1 : "Find command format is incorrect";
            return new FindCommand(words[1]);
        }
        else {
            throw new CharlieException("Sorry, unknown command, try again!");
        }
    }
}
