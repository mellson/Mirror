package mirror;

// Helperclass that can decorate types, eg turn an array into a pretty string.
public class TypeDecorator {
	public static String stringRepresentation(Object s) {
            if (s.getClass().isArray())
                return arrayDecorator(s);
            else
                return s.toString();
    }

    private static String arrayDecorator(Object a) {
        String decoratedString = "[ ";
        if (int[].class==a.getClass()) {
            int[] b = (int[])a;
            for (int i = 0; i < b.length; i++) {
                String separator = i < b.length - 1 ? " ; " : "";
                decoratedString += b[i] + separator;
            }
        }
        if (double[].class==a.getClass()) {
            double[] b = (double[])a;
            for (int i = 0; i < b.length; i++) {
                String separator = i < b.length - 1 ? " ; " : "";
                decoratedString += b[i] + separator;
            }
        }
        if (float[].class==a.getClass()) {
            float[] b = (float[])a;
            for (int i = 0; i < b.length; i++) {
                String separator = i < b.length - 1 ? " ; " : "";
                decoratedString += b[i] + separator;
            }
        }
        if (byte[].class==a.getClass()) {
            byte[] b = (byte[])a;
            for (int i = 0; i < b.length; i++) {
                String separator = i < b.length - 1 ? " ; " : "";
                decoratedString += b[i] + separator;
            }
        }
        if (short[].class==a.getClass()) {
            short[] b = (short[])a;
            for (int i = 0; i < b.length; i++) {
                String separator = i < b.length - 1 ? " ; " : "";
                decoratedString += b[i] + separator;
            }
        }
        if (long[].class==a.getClass()) {
            long[] b = (long[])a;
            for (int i = 0; i < b.length; i++) {
                String separator = i < b.length - 1 ? " ; " : "";
                decoratedString += b[i] + separator;
            }
        }
        if (char[].class==a.getClass()) {
            char[] b = (char[])a;
            for (int i = 0; i < b.length; i++) {
                String separator = i < b.length - 1 ? " ; " : "";
                decoratedString += b[i] + separator;
            }
        }
        if (String[].class==a.getClass()) {
            String[] b = (String[])a;
            for (int i = 0; i < b.length; i++) {
                String separator = i < b.length - 1 ? " ; " : "";
                decoratedString += b[i] + separator;
            }
        }
        if (Boolean[].class==a.getClass()) {
            Boolean[] b = (Boolean[])a;
            for (int i = 0; i < b.length; i++) {
                String separator = i < b.length - 1 ? " ; " : "";
                decoratedString += b[i] + separator;
            }
        }
        decoratedString += " ]";
        return decoratedString;
    }
}
