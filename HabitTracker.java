import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.io.*;
import java.time.*;
import java.util.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.event.*;

public class HabitTracker extends Application {
    // File to store habit data
    private static final String FILE_NAME = "habits.txt";
    private final ArrayList<String> habits = new ArrayList<>();
    private final ArrayList<Integer> streaks = new ArrayList<>();
    private final ArrayList<CheckBox> checkboxes = new ArrayList<>();

    // TextField to input new habits
    private final TextField newHabitField = new TextField();
    private final GridPane habitGrid = new GridPane();
    private final VBox layout = new VBox(10);
    private final ArrayList<LocalDate> lastConfirmed = new ArrayList<>();
    
    Label error = new Label("");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the application (load habits and configure UI)
        initializeApp();

        // Setting up the visual representation of habit streaks
        GridPane habitCalendar = new GridPane();
        ScrollPane scroller = new ScrollPane();
        scroller.setContent(habitCalendar);
        habitCalendar.setPadding(new Insets(10, 10, 10, 10));
        habitCalendar.setMinSize(300, 300);
        habitCalendar.setVgap(10);
        habitCalendar.setHgap(10);

        Scene habitList = new Scene(layout, 500, 400);
        Scene habitVisual = new Scene(scroller, 550, 400);

        // Button to navigate back to the list view
        Button habitListButton = new Button("Back to List");
        VBox visualLayout = new VBox(10, habitListButton, habitCalendar);
        scroller.setContent(visualLayout);
        habitListButton.setOnAction(event -> primaryStage.setScene(habitList));

        // Button to switch to the habit visual view
        Button habitVisualButton = new Button("Habit Visual");
        habitVisualButton.setOnAction(new EventHandler <ActionEvent>(){
            public void handle(ActionEvent event){
                streaksVisual(habitCalendar, habitListButton); //Update visual each time
                primaryStage.setScene(habitVisual);
            }
        });

        // Button to add a new habit
        Button addHabitButton = new Button("Add Habit");
        addHabitButton.setOnAction(event -> addHabit());
        
        

        // Button to confirm habits for today
        Button confirmChecked = new Button("Confirm Habits for Today");
        confirmChecked.setOnAction(event -> confirmHabits());

        // Layout setup
        HBox backButton = new HBox(10, habitListButton);
        HBox inputArea = new HBox(10, newHabitField, addHabitButton);
        HBox confirmArea = new HBox(10, confirmChecked, habitVisualButton);
        HBox errorCatch = new HBox(10, error);

        layout.getChildren().addAll(habitGrid, confirmArea, inputArea, errorCatch);
        layout.setPadding(new Insets(10));

        // Generate streaks visual and link navigation
        streaksVisual(habitCalendar, habitListButton);

        primaryStage.setTitle("Habit Tracker");
        primaryStage.setScene(habitList);
        primaryStage.show();
    }
    
    private void initializeApp() {
        // Load saved habits and set up initial UI
        loadHabitsFromFile();
        displayHabits();
        newHabitField.setPromptText("Enter a new habit");
        
        // Ensure lastConfirmed list aligns with habit size
        while (lastConfirmed.size() < habits.size()) {
            lastConfirmed.add(LocalDate.MIN);
        }

        // Spacing for the habit grid
        habitGrid.setVgap(10);
        habitGrid.setHgap(20);
    }

    private void addHabit() {
        String newHabit = newHabitField.getText().trim();
        
        if (!newHabit.isEmpty() && !newHabit.contains(",")) {
            // Add new habit to the lists and reset its streak and confirmation date
            habits.add(newHabit);
            streaks.add(0);
            lastConfirmed.add(LocalDate.MIN);
            saveAllHabitsToFile();
            newHabitField.clear();
            displayHabits();
            error.setText("");
        }
        if(newHabit.contains(",")){
            error.setText("You cannot include commas in your habit name");
        }
    }

    private void confirmHabits() {
        // Alert for user confirmation
        
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Habits");
        confirmationAlert.setHeaderText("Are you sure you want to confirm today's habits?");
        confirmationAlert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return; // Exit if the user cancels
        }
        
        LocalDate today = LocalDate.now();
        for (int i = 0; i < checkboxes.size(); i++) {
            if (checkboxes.get(i).isSelected()) {
                // Update streak if habit is not yet confirmed today
                if (!lastConfirmed.get(i).equals(today)) {
                    streaks.set(i, (streaks.get(i)+1));
                    lastConfirmed.set(i, today);
                }
            } else {
                // Reset streak if habit is unchecked
                streaks.set(i, 0);
            }
        }
        saveAllHabitsToFile();
        displayHabits();
    }

    private void displayHabits() {
        habitGrid.getChildren().clear();
        checkboxes.clear();

        for (int i = 0; i < habits.size(); i++) {
            String habit = habits.get(i);
            CheckBox checkBox = new CheckBox(habit);
            Label streakLabel = new Label("Streak: " + streaks.get(i));

            // Button to delete the habit
            Button deleteHabitButton = new Button("Delete");
            int index = i; // Capture the current index
            deleteHabitButton.setOnAction(event -> {
                deleteHabit(index);
                displayHabits();
            });

            habitGrid.add(checkBox, 0, i);
            habitGrid.add(streakLabel, 1, i);
            habitGrid.add(deleteHabitButton, 2, i);
            checkboxes.add(checkBox);
        }
    }

    private void deleteHabit(int index) {
        // Remove habit and its associated data
        habits.remove(index);
        streaks.remove(index);
        lastConfirmed.remove(index);
        saveAllHabitsToFile();
    }

    private void loadHabitsFromFile() {
        // Clear current lists before loading
        habits.clear();
        streaks.clear();
        lastConfirmed.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        // Parse and load habit data
                        habits.add(parts[0].trim());
                        streaks.add(Integer.parseInt(parts[1].trim()));
                        lastConfirmed.add(LocalDate.parse(parts[2].trim()));
                    } catch (Exception e) {
                        System.out.println("Skipping invalid line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveAllHabitsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < habits.size(); i++) {
                writer.write(habits.get(i) + "," + streaks.get(i) + "," + lastConfirmed.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void streaksVisual(GridPane habitCalendar, Button habitListButton) {
        habitCalendar.getChildren().clear();

        // Add navigation button to habit calendar view
        habitCalendar.add(habitListButton, 0, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int lineNumber = 1; // Row for habit names
            int returnNumber = 0; // Tracks new row requirements
            Color fillColor = Color.GREEN;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int streak = Integer.parseInt(parts[1]);

                habitCalendar.add(new Label(parts[0]), 0, lineNumber); // Display habit name
                if (streak > 0) {
                    for (int i = 0; i < streak; i++) {
                        // Change color based on streak length
                        if ((i + 1) % 7 == 0 && i >= 6) {
                            fillColor = Color.BLUE;
                        } else {
                            fillColor = Color.GREEN;
                        }

                        // Handle new row after 7 days
                        if ((i + 1) > 7 && (i + 1) % 7 == 1) {
                            lineNumber++;
                            returnNumber++;
                        }

                        // Add colored rectangle representing streak day
                        habitCalendar.add(new Rectangle(40, 40, fillColor), i + 1 - (returnNumber * 7), lineNumber);
                    }
                }
                lineNumber++;
                returnNumber = 0; // Reset for the next habit
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
