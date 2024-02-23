package parser;

import commands.*;
import exceptions.CharlieException;
import models.Deadline;
import models.Event;
import models.Task;
import models.Todo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Parser {

    public static Command parse(String fullCommand) throws CharlieException {

        if (fullCommand.startsWith("delete")) {
            String[] words = fullCommand.split(" ");
            return new DeleteCommand(Integer.valueOf(words[1]));
        } else if (fullCommand.startsWith("todo") || fullCommand.startsWith("event") || fullCommand.startsWith("deadline")) {
            return new AddCommand(fullCommand);
        } else if (fullCommand.startsWith("list")) {
            return new ListCommand();
        } else if (fullCommand.startsWith("bye")) {
            return new ExitCommand();
        } else if (fullCommand.startsWith("mark")) {
            String[] words = fullCommand.split(" ");
            return new MarkCommand(Integer.valueOf(words[1]));
        }
        else{
            throw new CharlieException("Sorry, unknown command, try again!");
        }
    }


}
