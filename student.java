package assignment1.part2;

import java.util.ArrayList;
import java.util.List;

public class student {
    /* Fields */
    private String name;
    private String ID;
    private String DOB;
    private List<module> moduleArray;

    /* Constructor */
    public student(String ID, String name, String DOB){
        this.name = name;
        this.ID = ID;
        this.DOB = DOB;
        this.moduleArray = new ArrayList<>();
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
    public List<module> getModuleArray() {
        return moduleArray;
    }

    /* Setters */
    public void setName(String name) {
        this.name = name;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public void setDOB(String DOB) {
        this.DOB = DOB;
    }
    public void setModuleArray(List<module> moduleArray) {
        this.moduleArray = moduleArray;
    }

    /* Extra Methods */
    public void addModule(String name, double grade){
        module newModule = new module(name, grade);
        moduleArray.add(newModule);
    }

    public void addModule(module toAdd){
        moduleArray.add(toAdd);
    }

    public void removeModule(String name){
        moduleArray.removeIf(toRemove -> name.equals(toRemove.getModuleName()));
    }

    @Override
    public String toString() {
        return "\nStudent ID: " + ID + "\nName: " + name + "\nDOB: " + DOB + "\n---------------------------";
    }
}