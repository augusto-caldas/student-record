package assignment1.part2;

public class module {
    /* Fields */
    private String moduleName;
    private double studentGrade;

    /* Constructor */
    public module(String name, double grade){
        moduleName = name;
        studentGrade = grade;
    }

    /* Getters */
    public String getModuleName() {
        return moduleName;
    }
    public double getStudentGrade() {
        return studentGrade;
    }

    /* Setters */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    public void setStudentGrade(double studentGrade) {
        this.studentGrade = studentGrade;
    }

    @Override
    public String toString() {
        return "\nModule: " + moduleName + "\nGrade: " + studentGrade + "%" + "\n-------------------------------------";
    }
}
