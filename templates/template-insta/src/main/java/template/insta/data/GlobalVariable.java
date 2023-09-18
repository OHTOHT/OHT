package template.insta.data;

public class GlobalVariable {

    private static boolean grid_mode = true;

    public static boolean isGrid_mode() {
        return grid_mode;
    }

    public static void setGrid_mode(boolean grid_mode) {
        GlobalVariable.grid_mode = grid_mode;
    }
}
