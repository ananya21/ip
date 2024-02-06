import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileReader;

public class Charlie {
    public static void main(String[] args) {
        ArrayList<Task> taskList = loadTasks();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello, I'm Charlie");
        System.out.println("What can I do for you?");

        while (true) {
            try {
                String input = scanner.nextLine();
                if (input.startsWith("todo")) {
                    handleTodo(taskList, input);
                    saveTasks(taskList);
                } else if (input.startsWith("deadline")) {
                    handleDeadline(taskList, input);
                    saveTasks(taskList);
                } else if (input.startsWith("event")) {
                    handleEvent(taskList, input);
                    saveTasks(taskList);
                } else if (input.equals("list")) {
                    listTasks(taskList);
                    saveTasks(taskList);
                } else if (input.startsWith("mark ")) {
                    int index = Integer.parseInt(input.substring(5)) - 1;
                    taskList.get(index).markAsDone();
                    System.out.println("Nice! I've marked this task as done:\n  " + taskList.get(index));
                    saveTasks(taskList);
                } else if (input.startsWith("unmark ")) {
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    taskList.get(index).markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:\n  " + taskList.get(index));
                    saveTasks(taskList);
                } else if (input.startsWith("delete ")) {
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    Task deleteTask = taskList.remove(index);
                    System.out.println("Noted. I've removed this task:\n  " + deleteTask);
                    System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                    saveTasks(taskList);
                } else if (input.startsWith("bye")) {
                    System.out.println("Bye! Hope to see you again!");
                    break;
                } else {
                    throw new CharlieException("I'm sorry, but there are no commands related to/recognizable by your input!");
                }
            } catch (CharlieException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void saveTasks(ArrayList<Task> taskList) {
        try {
            Files.createDirectories(Paths.get("./data"));
            BufferedWriter writer = new BufferedWriter(new FileWriter("./data/charlie.txt"));

            for (Task task : taskList) {
                writer.write(taskToFileFormat(task));
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving tasks to disk.");
        }
    }
    private static String taskToFileFormat(Task task) {
        String type = task instanceof Todo ? "T" :
                      task instanceof Deadline ? "D" :
                              task instanceof Event ? "E" : "";
        int done = task.isDone ? 1 : 0;
        String details = task.description;

        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            details += " | " + deadline.getBy();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            details += " | " + event.getStartsAt() + " | " + event.getEndsAt();
        }

        return String.join(" | ", type, String.valueOf(done), details);
    }

    public static ArrayList<Task> loadTasks() {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./data/charlie.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    loadedTasks.add(parseTaskFromFile(line));
                } catch (IllegalArgumentException e) {
                    System.out.println("Found a corrupted task in save file, skipping: " + line);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("No saved tasks found, starting with an empty list.");
        }
        return loadedTasks;
    }

    private static Task parseTaskFromFile(String line) throws IllegalArgumentException {
        String[] parts = line.split(" \\| ");
        Task task = null;

        switch (parts[0]) {
            case "T":
                task = new Todo(parts[2]);
                break;
            case "D":
                task = new Deadline(parts[2], parts[3]);
                break;
            case "E":
                task = new Event(parts[2], parts[3], parts[4]);
                break;
            default:
                throw new IllegalArgumentException("Invalid task type in file.");
        }

        if (parts[1].equals("1")) task.markAsDone();
        return task;
    }

    private static void handleTodo(ArrayList<Task> taskList, String input) throws CharlieException {
        if (input.trim().equals("todo")) {
            throw new CharlieException("Sorry, the description of a todo cannot be empty.");
        }
        String description = input.substring(5);
        Todo todo = new Todo(description);
        taskList.add(todo);
        System.out.println("Got it. I've added this task:\n  " + todo);
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
    }

    private static void handleDeadline(ArrayList<Task> taskList, String input) throws CharlieException {
        String[] parts = input.substring(9).split(" /by ");
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new CharlieException("Sorry, the description of a deadline cannot be empty and must include a /by time.");
        }
        Deadline deadline = new Deadline(parts[0], parts[1]);
        taskList.add(deadline);
        System.out.println("Got it. I've added this task:\n  " + deadline);
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
    }


    private static void handleEvent(ArrayList<Task> taskList, String input) throws CharlieException {
        String[] parts = input.substring(6).split(" /from ");
        if (parts.length < 2 || parts[0].trim().isEmpty() || !parts[1].contains(" /to ")) {
            throw new CharlieException("Sorry, the description of an event cannot be empty and must include start /from and /to end times.");
        }
        String[] timeParts = parts[1].split(" /to ");
        Event event = new Event(parts[0], timeParts[0], timeParts[1]);
        taskList.add(event);
        System.out.println("Got it. I've added this task:\n  " + event);
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
    }

    private static void listTasks(ArrayList<Task> taskList) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println((i + 1) + "." + taskList.get(i));
        }
    }
}
