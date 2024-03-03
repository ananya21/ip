package charlie.commands;

import charlie.exceptions.CharlieException;
import charlie.models.Task;
import charlie.storage.Storage;
import charlie.storage.TaskList;

public class DeleteCommand extends Command {

    private Integer index;

    /**
     * constructor for DeleteCommand
     * @param index index of list item to be removed
     */
    public DeleteCommand(Integer index) {
        assert index != null : "Task number to be deleted has to be specified";
        this.index = index;
    }

    /**
     * executes a delete command, the finds the task to be deleted within taskList, deletes the
     * task and saves the new taskList to storage
     * @param taskList - task list loaded at the start of the program.
     * @param storage  - class responsible for adding and loading tasks from and into the file
     * @throws CharlieException
     */
    @Override
    public String execute(TaskList taskList, Storage storage) throws CharlieException {
        assert taskList != null && storage != null : "TaskList and Storage must not be null";
        Task taskToBeDeleted = taskList.getTasks().get(index - 1);
        taskList.getTasks().remove(index - 1);
        String response = "Deleted task: '" + taskToBeDeleted.getDescription();
        storage.saveTasks(taskList.getTasks());
        isExit = false;
        return response;
    }
}
