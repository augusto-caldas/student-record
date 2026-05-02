package studentrecord;

/**
 * @param moduleName Fields
 */
public record ClassModule(String moduleName, double studentGrade) {
    @Override
    public String toString() {
        return "\nModule: " + moduleName + "\nGrade: " + studentGrade + "%" + "\n-------------------------------------";
    }
}
