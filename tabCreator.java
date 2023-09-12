package assignment1.part2;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class tabCreator {
    // Fields
    private final Tab studentTab;
    private final Tab moduleTab;
    private final Tab reviewTab;

    // Global Variables
    private static final String FILE_PATH = "src/main/resources";
    private static final List<student> studentArray = new ArrayList<>();
    private static String idSelected;
    private static student currStudent;

    public tabCreator(){
        studentTab = createStudentTab();
        moduleTab = createModuleTab();
        reviewTab = createReviewTab();
    }

    public Tab getStudentTab() {
        return studentTab;
    }

    public Tab getModuleTab() {
        return moduleTab;
    }

    public Tab getReviewTab() {
        return reviewTab;
    }

    /* Student Tab */
    private static Tab createStudentTab(){
        // Load file on startup
        readStudentFile();

        // Create Labels
        Label studentIdLabel = new Label("Enter Student ID: ");
        Label studentNameLabel = new Label("Enter Name: ");
        Label studentDOBLabel = new Label("Enter Date of Birth: ");

        // Create TextFields
        TextField studentIdInput = new TextField();
        TextField studentNameInput = new TextField();
        TextField studentDOBInput = new TextField();

        // TextFields Handler
        studentIdInput.setOnAction(actionEvent -> studentIdInput.setText(studentIdInput.getText()));
        studentNameInput.setOnAction(actionEvent -> studentNameInput.setText(studentNameInput.getText()));
        studentDOBInput.setOnAction(actionEvent -> studentDOBInput.setText(studentDOBInput.getText()));

        // Create TextAreas
        TextArea studentPrintArray = new TextArea("Students in the application");
        studentPrintArray.setEditable(false);
        studentPrintArray.setPrefSize(250, 250);
        if (!studentArray.isEmpty())
            studentPrintArray.setText(studentArray.toString());

        // Create Buttons
        Button addStudent = new Button("Add");
        Button removeStudent = new Button("Remove");
        Button listStudent = new Button("List");
        Button saveStudentToFile = new Button("Save");
        Button loadStudentFromFile = new Button("Load");
        Button saveStudentAndExit = new Button("Save & Exit");
        setButtonSize(addStudent, removeStudent, listStudent, saveStudentToFile, loadStudentFromFile,
                saveStudentAndExit);

        // Buttons handlers
        addStudent.setOnAction(actionEvent -> {
            // if ID text field is not empty and not in list add student to array
            if (!studentIdInput.getText().equals("") && !studentNameInput.getText().equals("") && !studentDOBInput.getText().equals("")) {
                if (isStudentNotInList(studentIdInput.getText())) {
                    student newStudent = new student(studentIdInput.getText(), studentNameInput.getText(), studentDOBInput.getText());
                    studentArray.add(newStudent);
                }

                // Clear fields
                studentNameInput.clear();
                studentIdInput.clear();
                studentDOBInput.clear();
                // Print array
                studentPrintArray.setText(studentArray.toString());
            }
        });

        removeStudent.setOnAction(actionEvent -> {
            // if ID text field is not empty and ID in array remove student from array
            String toRemove = studentIdInput.getText();
            if (!toRemove.equals("")) {
                for (int i = 0; i < studentArray.size(); i++) {
                    student currStudent = studentArray.get(i);
                    if (toRemove.equals(currStudent.getID()))
                        studentArray.remove(currStudent);
                }
                // Clear fields
                studentNameInput.clear();
                studentIdInput.clear();
                studentDOBInput.clear();
            }
            studentPrintArray.setText(studentArray.toString());
        });

        listStudent.setOnAction(actionEvent -> studentPrintArray.setText(studentArray.toString()));

        saveStudentToFile.setOnAction(actionEvent -> writeStudentFile());

        loadStudentFromFile.setOnAction(actionEvent -> {
            readStudentFile();
            studentPrintArray.setText(studentArray.toString());
        });

        saveStudentAndExit.setOnAction(actionEvent -> {
            writeStudentFile();
            if (currStudent != null)
                writeModuleFile(currStudent);
            Platform.exit();
        });

        // Setting Tool tips for buttons
        setToolTip(addStudent, "Enter all fields to add new student");
        setToolTip(removeStudent, "Enter Student ID to remove it");
        setToolTip(listStudent, "Refresh student list");
        setToolTip(saveStudentToFile, "Save list to file");
        setToolTip(loadStudentFromFile, "Load from file (Unsaved information is lost)");
        setToolTip(saveStudentAndExit, "Save to file and close program");

        // Create Grid Pane
        GridPane inputPane = new GridPane();
        inputPane.setAlignment(Pos.CENTER);
        inputPane.setVgap(10);
        inputPane.add(studentIdLabel,0,0,1,1);
        inputPane.add(studentIdInput,1,0,1,1);
        inputPane.add(studentNameLabel,0,1,1,1);
        inputPane.add(studentNameInput,1,1,1,1);
        inputPane.add(studentDOBLabel,0,2,1,1);
        inputPane.add(studentDOBInput,1,2,1,1);

        GridPane editStudentButtons = setGrid(addStudent, removeStudent, listStudent);
        GridPane fileButtons = setGrid(saveStudentToFile, loadStudentFromFile, saveStudentAndExit);

        // Create Hboxes
        HBox listText = new HBox(studentPrintArray);
        listText.setAlignment(Pos.CENTER);

        // Create Vboxes
        VBox createStudent = new VBox(inputPane, editStudentButtons, listText, fileButtons);
        createStudent.setSpacing(20);
        createStudent.setPadding(new Insets(20));

        // Create Tab
        Tab editStudentTab = new Tab("Edit Students");
        editStudentTab.setContent(createStudent);
        editStudentTab.setClosable(false);

        return editStudentTab;
    }

    /* Module Tab */
    private static Tab createModuleTab(){
        // Creating ComboBox
        ComboBox<String> studentDropList = createStudentDropList();

        // Create Labels
        Label moduleNameLabel = new Label("Enter Module: ");
        Label moduleGradeLabel = new Label("Enter Grade: ");

        // Create TextFields
        TextField moduleNameInput = new TextField();
        TextField moduleGradeInput = new TextField();

        // Create TextArea
        TextArea modulePrintList = new TextArea("Select a student");
        modulePrintList.setEditable(false);
        modulePrintList.setPrefSize(250, 250);

        // Create Buttons
        Button refreshDropList = new Button("Refresh");
        Button addModule = new Button("Add");
        Button removeModule = new Button("Remove");
        Button listModule = new Button("List");
        Button saveModule = new Button("Save");
        Button loadModule = new Button("Load");
        Button saveAndExit = new Button("Save and Exit");
        setButtonSize(addModule, removeModule, listModule, saveModule, loadModule, saveAndExit);

        // Hiding controls
        hideModuleControls(moduleNameLabel, moduleGradeLabel, moduleNameInput, moduleGradeInput, addModule,
                removeModule, listModule, saveModule, loadModule, saveAndExit);

        // ComboBox handler
        studentDropList.setOnAction(actionEvent -> {
            idSelected = studentDropList.getSelectionModel().getSelectedItem();
            if (idSelected != null) {
                currStudent = findStudent(idSelected);
                if (currStudent != null) {
                    readModuleFile(currStudent);
                    modulePrintList.setText(currStudent.getModuleArray().toString());

                    // Showing controls
                    moduleNameLabel.setVisible(true);
                    moduleGradeLabel.setVisible(true);
                    moduleNameInput.setVisible(true);
                    moduleGradeInput.setVisible(true);
                    addModule.setVisible(true);
                    removeModule.setVisible(true);
                    listModule.setVisible(true);
                    saveModule.setVisible(true);
                    loadModule.setVisible(true);
                    saveAndExit.setVisible(true);
                }
            }
        });

        // TextField Handlers
        moduleNameInput.setOnAction(actionEvent -> moduleNameInput.setText(moduleNameInput.getText()));
        moduleGradeInput.setOnAction(actionEvent -> moduleGradeInput.setText(moduleGradeInput.getText()));

        // Buttons handlers
        refreshDropList.setOnAction(actionEvent -> {
            hideModuleControls(moduleNameLabel, moduleGradeLabel, moduleNameInput, moduleGradeInput, addModule,
                    removeModule, listModule, saveModule, loadModule, saveAndExit);
            modulePrintList.setText("Select a student");
            studentDropList.getItems().clear();
            refreshComboBox(studentDropList);
        });

        addModule.setOnAction(actionEvent -> {
            // If input fields not empty
            String moduleToAdd = moduleNameInput.getText();
            String gradeToAdd = moduleGradeInput.getText();
            if (!moduleToAdd.equals("") && !gradeToAdd.equals("")){

                // Check if module is already signed to student
                if (isModuleNotInList(currStudent, moduleToAdd)){
                    // Create module and add to student module array
                    try {
                        currStudent.addModule(moduleToAdd, Double.parseDouble(gradeToAdd));
                    } catch (NumberFormatException invalidNumber){
                        throw new RuntimeException(invalidNumber);
                    }

                    // Clear fields
                    moduleNameInput.clear();
                    moduleGradeInput.clear();
                    // Print array
                    modulePrintList.setText(currStudent.getModuleArray().toString());
                }
            }
        });

        removeModule.setOnAction(actionEvent -> {
            // If input fields not empty
            String moduleToRemove = moduleNameInput.getText();
            if (!moduleToRemove.equals("")) {
                // If module is in array
                if (!isModuleNotInList(currStudent, moduleToRemove)) {
                    // Remove module from array
                    currStudent.removeModule(moduleToRemove);

                    // Clear fields
                    moduleNameInput.clear();
                    moduleGradeInput.clear();
                    // Print array
                    modulePrintList.setText(currStudent.getModuleArray().toString());
                }
            }
        });

        listModule.setOnAction(actionEvent -> modulePrintList.setText(currStudent.getModuleArray().toString()));

        saveModule.setOnAction(actionEvent -> writeModuleFile(currStudent));

        loadModule.setOnAction(actionEvent -> {
            readModuleFile(currStudent);
            modulePrintList.setText(currStudent.getModuleArray().toString());
        });

        saveAndExit.setOnAction(actionEvent -> {
            writeModuleFile(currStudent);
            Platform.exit();
        });

        // Setting Tool tips for buttons
        setToolTip(addModule, "Enter all fields to add new module");
        setToolTip(removeModule, "Enter module name to remove it");
        setToolTip(listModule, "Refresh student list");
        setToolTip(saveModule, "Save list to file");
        setToolTip(loadModule, "Load from file (Unsaved information is lost)");
        setToolTip(saveAndExit, "Save to file and close program");

        // Create GridPanes
        GridPane editModuleButtons = setGrid(addModule, removeModule, listModule);
        GridPane fileButtonsLine = setGrid(saveModule, loadModule, saveAndExit);

        // Create Hboxes
        HBox studentSelectLine = new HBox(studentDropList, refreshDropList);
        studentSelectLine.setSpacing(10);
        HBox printLine = new HBox(modulePrintList);
        printLine.setAlignment(Pos.CENTER);

        // Create Grid
        GridPane inputGrid = new GridPane();
        inputGrid.add(moduleNameLabel,0,0,1,1);
        inputGrid.add(moduleNameInput,1,0,1,1);
        inputGrid.add(moduleGradeLabel,0,1,1,1);
        inputGrid.add(moduleGradeInput,1,1,1,1);
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        // Create Vbox
        VBox mainVbox = new VBox(studentSelectLine, inputGrid, editModuleButtons, printLine, fileButtonsLine);
        mainVbox.setSpacing(10);
        mainVbox.setPadding(new Insets(10));

        // Create Tab
        Tab editModulesTab = new Tab("Edit Modules");
        editModulesTab.setClosable(false);
        editModulesTab.setContent(mainVbox);
        return editModulesTab;
    }

    /* Review Tab */
    private static Tab createReviewTab(){
        // Create ComboBox
        ComboBox<String> studentDropList = createStudentDropList();

        // Create Buttons
        Button refreshList = new Button("Refresh");

        // Create TextArea
        TextArea studentInfoToPrint = new TextArea("Select a student");
        studentInfoToPrint.setEditable(false);
        studentInfoToPrint.setPrefSize(250, 300);

        // Handlers
        studentDropList.setOnAction(actionEvent -> {
            idSelected = studentDropList.getSelectionModel().getSelectedItem();
            if (idSelected != null) {
                currStudent = findStudent(idSelected);
                if (currStudent != null) {
                    readModuleFile(currStudent);
                    studentInfoToPrint.clear();
                    studentInfoToPrint.setText(currStudent.toString());
                    studentInfoToPrint.appendText(currStudent.getModuleArray().toString());
                }
            }
        });

        refreshList.setOnAction(actionEvent -> {
            studentDropList.getItems().clear();
            refreshComboBox(studentDropList);
            studentInfoToPrint.setText("Select a student");
        });

        // Create Hboxes
        HBox selectStudentLine = new HBox(studentDropList, refreshList);
        selectStudentLine.setSpacing(10);
        HBox printLine = new HBox(studentInfoToPrint);
        printLine.setAlignment(Pos.CENTER);

        // Create Vbox
        VBox mainVbox = new VBox(selectStudentLine, printLine);
        mainVbox.setPadding(new Insets(10));
        mainVbox.setSpacing(10);

        // Create Tab
        Tab reviewStudentTab = new Tab("Review Student");
        reviewStudentTab.setClosable(false);
        reviewStudentTab.setContent(mainVbox);
        return reviewStudentTab;
    }

    /* Helper Methods */
    // Set ToolTip of Buttons
    private static void setToolTip(Button toSet, String text){
        Tooltip newTip = new Tooltip(text);
        newTip.setShowDelay(Duration.seconds(0.5));
        toSet.setTooltip(newTip);
    }

    // Check if given ID is not in list
    private static boolean isStudentNotInList(String idToCheck){
        for (student currStudent : studentArray) {
            if (idToCheck.equals(currStudent.getID())) {
                return false;
            }
        }
        return true;
    }
    // Crete drop down menu of students
    private static ComboBox<String> createStudentDropList(){
        ComboBox<String> studentDropList = new ComboBox<>();
        studentDropList.setPromptText("Students");
        for (assignment1.part2.student student : studentArray) {
            studentDropList.getItems().add(student.getID());
        }
        return studentDropList;
    }
    // Re-write variables to combobox
    private static void refreshComboBox(ComboBox<String> toRefresh){
        for (assignment1.part2.student student : studentArray) {
            toRefresh.getItems().add(student.getID());
        }
    }

    // Check if given module from student is not in list
    private static boolean isModuleNotInList(student studentToCheck, String moduleToCheck){
        if (studentToCheck.getModuleArray() == null)
            return true;
        if (studentToCheck.getModuleArray().isEmpty())
            return true;
        for (module currModule : studentToCheck.getModuleArray()){
            if (moduleToCheck.equals(currModule.getModuleName())){
                return false;
            }
        }
        return true;
    }

    // Find Student by ID
    private static student findStudent(String studentID){
        student studentToAdd = null;
        for (student currStudent : studentArray){
            if (studentID.equals(currStudent.getID()))
                studentToAdd=currStudent;
        }
        return studentToAdd;
    }

    // Read student file to array
    private static void readStudentFile() {
        // Clear student array
        studentArray.clear();
        try {
            // Open file
            Scanner studentFile = new Scanner(new File(FILE_PATH + "/studentRecord.txt"));
            // Create list from file
            while (studentFile.hasNext()) {
                // Get information from file
                String studentID = studentFile.nextLine();
                String studentName = studentFile.nextLine();
                String studentDOB = studentFile.nextLine();
                // Create student using information from file
                student currStudent = new student(studentID, studentName, studentDOB);
                // Add students to list
                if (isStudentNotInList(studentID))
                    studentArray.add(currStudent);
            }
            // Close file
            studentFile.close();
        }
        // Create file and recursively call function
        catch (FileNotFoundException fileError) {
            try {
                FileWriter studentFile = new FileWriter(FILE_PATH + "/studentRecord.txt");
                studentFile.close();
            } catch (IOException error) {
                throw new RuntimeException(error);
            }
            readStudentFile();
        }
    }

    // Read module file
    private static void readModuleFile(student toRead){
        // Clear module array
        toRead.getModuleArray().clear();
        try {
            // Open file
            Scanner moduleFile = new Scanner(new File(FILE_PATH + "/modules" + toRead.getID() + ".txt"));
            // Create list from file
            while (moduleFile.hasNext()){
                // Get information from file
                String moduleName = moduleFile.nextLine();
                double moduleGrade = Double.parseDouble(moduleFile.nextLine());
                // Create module using information from file
                module currModule = new module(moduleName, moduleGrade);
                if (isModuleNotInList(toRead, moduleName))
                    toRead.addModule(currModule);
            }

        }
        // Create file and recursively call function
        catch (FileNotFoundException fileError) {
            try {
                FileWriter moduleFile = new FileWriter(FILE_PATH + "/modules" + toRead.getID() + ".txt");
                moduleFile.close();
                readModuleFile(toRead);
            } catch (IOException error){
                throw new RuntimeException(error);
            }
        }
    }

    // Write array to student file
    private static void writeStudentFile() {
        try {
            // Open file
            FileWriter studentFile = new FileWriter(FILE_PATH + "/studentRecord.txt");
            // Write student objects to file
            for (assignment1.part2.student student : studentArray) {
                studentFile.write(student.getID() + "\n");
                studentFile.write(student.getName() + "\n");
                studentFile.write(student.getDOB() + "\n");
            }
            // Close file
            studentFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Write module file
    private static void writeModuleFile(student toWrite) {
        try {
            // Open file
            FileWriter moduleFile = new FileWriter(FILE_PATH + "/modules" + toWrite.getID() + ".txt");
            // Write module objects to file
            for (assignment1.part2.module module : toWrite.getModuleArray()) {
                moduleFile.write(module.getModuleName() + "\n");
                moduleFile.write(module.getStudentGrade() + "\n");
            }
            // Close file
            moduleFile.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    // Hide module controls
    private static void hideModuleControls(Label moduleNameLabel, Label moduleGradeLabel, TextField moduleNameInput,
                                           TextField moduleGradeInput, Button addModule,
                                           Button removeModule, Button listModule, Button saveModule, Button loadModule,
                                           Button saveAndExit) {
        moduleNameLabel.setVisible(false);
        moduleGradeLabel.setVisible(false);
        moduleNameInput.setVisible(false);
        moduleGradeInput.setVisible(false);
        addModule.setVisible(false);
        removeModule.setVisible(false);
        listModule.setVisible(false);
        saveModule.setVisible(false);
        loadModule.setVisible(false);
        saveAndExit.setVisible(false);
    }

    private static GridPane setGrid(Button button1, Button button2, Button button3) {
        GridPane newGrid = new GridPane();
        newGrid.add(button1, 0, 0, 1, 1);
        newGrid.add(button2, 1, 0, 1, 1);
        newGrid.add(button3, 2, 0, 1, 1);
        newGrid.setHgap(10);
        newGrid.setAlignment(Pos.CENTER);
        return newGrid;
    }

    private static void setButtonSize(Button button1, Button button2, Button button3, Button button4, Button button5,
                                      Button button6){
        button1.setPrefSize(90,10);
        button1.setAlignment(Pos.CENTER);
        button2.setPrefSize(90,10);
        button2.setAlignment(Pos.CENTER);
        button3.setPrefSize(90,10);
        button3.setAlignment(Pos.CENTER);
        button4.setPrefSize(90,10);
        button4.setAlignment(Pos.CENTER);
        button5.setPrefSize(90,10);
        button5.setAlignment(Pos.CENTER);
        button6.setPrefSize(90,10);
        button6.setAlignment(Pos.CENTER);
    }
}