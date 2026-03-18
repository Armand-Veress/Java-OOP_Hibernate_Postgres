package Anexe;

@SuppressWarnings("SpellCheckingInspection")
public enum ZiASaptamanii {
    Luni, Marti, Miercuri, Joi, Vineri, Sambata, Duminica;

    public static ZiASaptamanii get(int d){
        return switch (d) {
            case 1 -> Luni;
            case 2 -> Marti;
            case 3 -> Miercuri;
            case 4 -> Joi;
            case 5 -> Vineri;
            case 6 -> Sambata;
            case 7 -> Duminica;
            default -> null;
        };
    }

    public static int getValue(String zi) throws Exception {
        return switch (zi.toUpperCase()) {
            case "LUNI" -> 1;
            case "MARTI" -> 2;
            case "MERCURI" -> 3;
            case "JOI" -> 4;
            case "VINERI" -> 5;
            case "SAMBATA" -> 6;
            case "DUMINA" -> 7;
            default -> throw new Exception();
        };
    }
}
