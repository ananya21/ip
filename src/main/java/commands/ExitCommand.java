package commands;

import storage.Storage;
import storage.TaskList;
import ui.Ui;

public class ExitCommand extends Command {

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        isExit = true;
    }
}
