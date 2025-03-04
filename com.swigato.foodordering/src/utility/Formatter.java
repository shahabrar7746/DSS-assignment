package utility;

import java.util.List;

public class Formatter {
    public static <T extends Formatable> void tableTemplate(List<T> list) {
        if (list.isEmpty()) return;
        T firstObject = list.get(0);
        String fields = ColourCodes.PURPLE+"";
        String title = firstObject.getDisplayabletitle();
//        displayable.stream().forEach((item)-> System.out.print(item+multiplychar(Formatable.max_gap-item.length(), ' ')));
        for (String field : firstObject.fieldsToDisplay()) {
            fields += field + multiplychar(Formatable.MAX_CHAR_LIMIT - field.length(), ' ');
        }
        fields= fields+ColourCodes.RESET;

//        int half_side = (fields.trim().length() - title.length()) / 2;
        System.out.println(ColourCodes.BLUE+  title + ColourCodes.RESET );
//        System.out.println(multiplychar(half_side, '=') + title + multiplychar(half_side, '='));
        System.out.println(fields);

        for (T item : list) {
            item.getFieldValues().stream().forEach(x -> System.out.print((String) (x) + multiplychar(Formatable.MAX_CHAR_LIMIT - String.valueOf(x).length(), ' ')));
            System.out.println();
        }

//        System.out.println(multiplychar(fields.length() - title.length(), '=') + "\n");
        System.out.println(ColourCodes.BLUE+multiplychar(fields.length(),'-') + ColourCodes.RESET );

    }



    public static String multiplychar(int x, char ch) {
        String temp = "";
        for (int i = 0; i < x + Formatable.EXTRA_CHAR; i++) {
            temp += ch;
        }
        return temp;
    }
}
