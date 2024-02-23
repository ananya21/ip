import commands.Command;
import exceptions.CharlieException;
import parser.Parser;
import storage.Storage;
import storage.TaskList;
import ui.Ui;

public class Charlie {

    private Ui ui;
    private Storage storage;
    private TaskList tasks;

    public Charlie(String filePath) {
        try {
            ui = new Ui();
            storage = new Storage(filePath);
            tasks = new TaskList(storage.loadTasks());
        } catch (CharlieException exception) {
            ui.showLoadingError(exception.getMessage());
            tasks = new TaskList();
        }
    }


    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (CharlieException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    public static void main(String[] args) {
        new Charlie("./data/charlie.txt").run();
    }


}
