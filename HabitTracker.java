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

public class HabitTracker extends Application {
    private static final String FILE_NAME = "habits.txt";
    private final ArrayList<String> habits = new ArrayList<>();
    private final ArrayList<Integer> streaks = new ArrayList<>();
    private final ArrayList<CheckBox> checkboxes = new ArrayList<>();

    private final TextField newHabitField = new TextField();
    private final GridPane habitGrid = new GridPane();
    private final VBox layout = new VBox(10);
    private final ArrayList<LocalDate> lastConfirmed = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //habitList
        initializeApp();

        //habitVisual
        GridPane habitCalendar = new GridPane();
        ScrollPane scroller = new ScrollPane();
        scroller.setContent(habitCalendar);
        habitCalendar.setPadding(new Insets(10, 10, 10, 10));
        habitCalendar.setMinSize(300, 300);
        habitCalendar.setVgap(10);
        habitCalendar.setHgap(10);
        
        Scene habitList = new Scene(layout, 500, 400);
        Scene habitVisual = new Scene(scroller, 550, 500);
        
        Button habitListButton = new Button("Back to List");
        habitListButton.setOnAction(event -> primaryStage.setScene(habitList));
        
        Button habitVisualButton = new Button("Habit Visual");
        habitVisualButton.setOnAction(event -> primaryStage.setScene(habitVisual));
        
        Button addHabitButton = new Button("Add Habit");
        addHabitButton.setOnAction(event -> addHabit());

        Button confirmChecked = new Button("Confirm Habits for Today");
        confirmChecked.setOnAction(event -> confirmHabits());

        HBox backButton = new HBox(10, habitListButton);
        HBox inputArea = new HBox(10, newHabitField, addHabitButton);
        HBox confirmArea = new HBox(10, confirmChecked, habitVisualButton);

        layout.getChildren().addAll(habitGrid, confirmArea, inputArea);
        layout.setPadding(new Insets(10));
        
        habitCalendar.add(habitListButton, 0, 0);

        //Gather rectangles
        streaksVisual(habitCalendar);
        //
        
        primaryStage.setTitle("Habit Tracker");
        primaryStage.setScene(habitList);
        primaryStage.show();
        
    }
    
    private void initializeApp() {
        loadHabitsFromFile();
        displayHabits();
        newHabitField.setPromptText("Enter a new habit");

        for (int i = 0; i < habits.size(); i++) {
            lastConfirmed.add(LocalDate.MIN); // 1970 date / default
        }
        
        habitGrid.setVgap(10);
        habitGrid.setHgap(20);
    }

    private void addHabit() {
        String newHabit = newHabitField.getText().trim();
        if (!newHabit.isEmpty()) {
            habits.add(newHabit);
            streaks.add(0);
            lastConfirmed.add(LocalDate.MIN); 
            saveAllHabitsToFile();
            newHabitField.clear();
            displayHabits();
        }
    }



    private void confirmHabits() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Habits");
        confirmationAlert.setHeaderText("Are you sure you want to confirm today's habits?");
        confirmationAlert.setContentText("This action cannot be undone.");
        
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return; // if user cancels, then return
        }
    
        LocalDate today = LocalDate.now();
        for (int i = 0; i < checkboxes.size(); i++) {
            if (checkboxes.get(i).isSelected()) {
                if (!lastConfirmed.get(i).equals(today)) {
                    streaks.set(i, streaks.get(i) + 1);
                    lastConfirmed.set(i, today);
                }
            } else {
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
        habits.remove(index);
        streaks.remove(index);
        saveAllHabitsToFile();
    }

    private void loadHabitsFromFile() 
    {
        habits.clear();
        streaks.clear();
        lastConfirmed.clear();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) { // Adjust for date field
                    habits.add(parts[0].trim());
                    streaks.add(Integer.parseInt(parts[1].trim()));
                    lastConfirmed.add(LocalDate.parse(parts[2].trim()));
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

   public void streaksVisual(GridPane habitCalendar) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int lineNumber = 1; //Stores row value
            int returnNumber = 0; //Multiplier for carriage returns
            Color fillColor = Color.GREEN;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int streak = Integer.parseInt(parts[1]);
                
                habitCalendar.add(new Label(parts[0]), 0, lineNumber); //Habit name
                if(streak > 0){
                    for(int i = 0; i < streak; i++){
                        //Determines Color
                        if((i+1)%7 == 0 && i >=6){
                            fillColor = Color.BLUE; //Every 7 days of keeping up is blue
                        } else {
                            fillColor = Color.GREEN;
                        }
                        
                        //Carriage Return
                        if((i+1) > 7 && (i+1)%7 == 1) {
                            lineNumber++; //Newline
                            returnNumber++; //Pull rectangles back to index 0 visually
                        } 
                        
                        habitCalendar.add(new Rectangle(40, 40, fillColor), i+1-(returnNumber*7), lineNumber);
                    }
                }
                lineNumber++; //New Habit line
                returnNumber = 0; //No returns yet for new habit
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
    