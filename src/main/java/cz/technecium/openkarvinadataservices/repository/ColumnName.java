package cz.technecium.openkarvinadataservices.repository;

/**
 *
 *
 * @author Martin.Sobek
 */
public enum ColumnName {

    FIDE_ELO("Rtg_int"),
    LOC_ELO("Rtg_nat"),
    NAME("Name"),
    CLUB("ClubName"),
    FIDE_ID("FIDE_no"),
    LOC_ID("ID_no"),
    FIDE_TITLE("Title"),
    BIRTHDAY("BirthDay");

    private String columnName;

    ColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
