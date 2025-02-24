package intefaces;

import java.util.List;

public interface Formatable {
    int MAX_CHAR_LIMIT = 20;
    int EXTRA_CHAR = 5;
    List<String> fieldsToDisplay();
    List<String> getFieldValues();
    String getDisplayabletitle();

}
