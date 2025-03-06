package utility;

import java.util.List;

public interface Formatable {
    int MAX_CHAR_LIMIT = 15;
    int EXTRA_CHAR = 5;

    List<String> fieldsToDisplay();

    List<String> getFieldValues();

    String getDisplayabletitle();

}