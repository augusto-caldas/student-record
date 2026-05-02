package studentrecord;

import java.util.ArrayList;
import java.util.List;

public class Student {
    /* Fields */
    private final String name;
    private final String ID;
    private final String DOB;
    private final List<ClassModule> classModuleArray;

    /* Constructor */
    public Student(String ID, String name, String DOB){
        this.name = name;
        this.ID = ID;
        this.DOB = DOB;
        this.classModuleArray = new ArrayList<>();
    }

    /* Getters */
    public String getID() {
        return ID;
    }
    public String getName() {
        return name;
    }
    public String getDOB() {
        return DOB;
    }
    public List<ClassModule> getModuleArray() {
        return classModuleArray;
    }

    /* Extra Methods */
    public void addModule(String name, double grade){
        ClassModule newClassModule = new ClassModule(name, grade);
        classModuleArray.add(newClassModule);
    }

    public void addModule(ClassModule toAdd){
        classModuleArray.add(toAdd);
    }

    public void removeModule(String name){
        classModuleArray.removeIf(toRemove -> name.equals(toRemove.moduleName()));
    }

    @Override
    public String toString() {
        return "\nStudent ID: " + ID + "\nName: " + name + "\nDOB: " + DOB + "\n---------------------------";
    }
}