package charlie.commands;

import java.util.ArrayList;

import charlie.exceptions.CharlieException;
import charlie.models.Deadline;
import charlie.models.Event;
import charlie.models.Task;
import charlie.models.Todo;
import charlie.storage.Storage;
import charlie.storage.TaskList;

public class AddCommand extends Command {

    private String fullCommand;
    private String response;
    private Integer priorityNumber;

    /**
     * constructor for AddCommand
     * @param fullCommand the user command in its full string form
     */
    public AddCommand(String fullCommand, Integer priorityNumber) {
        assert fullCommand != null && !fullCommand.trim().isEmpty() : "Full command must not be null or empty";
        this.fullCommand = fullCommand;
        this.priorityNumber = priorityNumber;
    }

    /**
     * executes an add command, the method decides between tasks to-do, event and deadline, and
     * saves the tasks to storage
     * @param taskList - task list loaded at the start of the program.
     * @param storage  - class responsible for adding and loading tasks from and into the file
     * @throws CharlieException
     */
    @Override
    public String execute(TaskList taskList, Storage storage) throws CharlieException {
        assert taskList != null && storage != null : "TaskList and Storage must not be null";
        if (fullCommand.startsWith("todo")) {
            this.response = handleTodo(taskList.getTasks(), fullCommand);
        } else if (fullCommand.startsWith("event")) {
            this.response = handleEvent(taskList.getTasks(), fullCommand);
        } else if (fullCommand.startsWith("deadline")) {
            this.response = handleDeadline(taskList.getTasks(), fullCommand);
        }
        storage.saveTasks(taskList.getTasks());
        isExit = false;
        return this.response;
    }

    /**
     * adds the to-do task to the current task list, and returns intended generated output
     * @param taskList task list loaded at the start of the program.
     * @param input to-do string which specifies the task saved
     * @throws CharlieException
     */
    private String handleTodo(ArrayList<Task> taskList, String input) throws CharlieException {
        assert input != null && !input.trim().equals("todo") : "Input for todo must not be empty";
        if (input.trim().equals("todo")) {
            throw new CharlieException("Sorry, the description of a todo cannot be empty.");
        }
        String description = input.substring(5);
        Todo todo = new Todo(description, priorityNumber);
        taskList.add(todo);
        return generateResponse(todo, taskList.size());
    }

    /**
     * adds the deadline task to the current task list, and returns intended generated output
     * @param taskList task list loaded at the start of the program.
     * @param input to-do string which specifies the task saved
     * @throws CharlieException
     */
    private String handleDeadline(ArrayList<Task> taskList, String input) throws CharlieException {
        assert input != null && input.trim().length() > 9 : "Input for deadline must be properly formatted";
        String[] parts = input.substring(9).split(" /by ");
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new CharlieException("Sorry, the description of a deadline "
                    + "cannot be empty and must include a /by time.");
        }
        Deadline deadline = new Deadline(parts[0], priorityNumber, parts[1]);
        taskList.add(deadline);
        return generateResponse(deadline, taskList.size());
    }

    /**
     * adds the event task to the current task list, and returns intended generated output
     * @param taskList task list loaded at the start of the program.
     * @param input to-do string which specifies the task saved
     * @throws CharlieException
     */
    private String handleEvent(ArrayList<Task> taskList, String input) throws CharlieException {
        assert input != null && input.trim().length() > 6 : "Input for event must be properly formatted";
        String[] parts = input.substring(6).split(" /from ");
        if (parts.length < 2 || parts[0].trim().isEmpty() || !parts[1].contains(" /to ")) {
            throw new CharlieException("Sorry, the description of an event cannot be empty "
                    + "and must include start /from and /to end times.");
        }
        String[] timeParts = parts[1].split(" /to ");
        Event event = new Event(parts[0], priorityNumber, timeParts[0], timeParts[1]);
        taskList.add(event);
        return generateResponse(event, taskList.size());
    }

    private String generateResponse(Task task, int size) {
        return "Got it. I've added this task:\n  " + task + "Now you have " + size + " tasks in the list.";
    }

}
