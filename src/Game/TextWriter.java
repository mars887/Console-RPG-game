package Game;

public class TextWriter {

    public static void printToSides(String[] side1,String[] side2,int border) {
        if(side1 == null || side2 == null) return;
        for(int i = 0;i < Math.max(side1.length,side2.length);i++) {
            int leftSize = 0;
            if(side1.length > i) {
                System.out.print(side1[i]);
                leftSize = side1[i].length();
            } else {
                leftSize = 0;
            }
            if(side2.length > i) {
                countPrint(" ", (border - leftSize) - side2[i].length());
                System.out.println(side2[i]);
            } else System.out.println();
        }
    }

    public static void countPrint(String message, int i) {
        for (int j = 0; j < i; j++) System.out.print(message);
    }

    public static void printWithRightBorder(String message, int i) {
        System.out.print(message);
        countPrint(" ", i - message.length());
    }

    public static void printComparisonWithBorder(String message, int i, String message1) {
        printWithRightBorder(message, i - message1.length());
        System.out.print(message1);
    }

    public static void printComparisonWithBorder(int value, int i, int value1) {
        printComparisonWithBorder(String.valueOf(value), i, String.valueOf(value1));
    }
}
