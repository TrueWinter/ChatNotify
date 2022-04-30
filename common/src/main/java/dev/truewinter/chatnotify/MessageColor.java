package dev.truewinter.chatnotify;

/**
 * MessageColor contains Minecraft color codes in a cross-platform manner.
 */
public enum MessageColor {
    BLACK("0"),
    DARK_BLUE("1"),
    DARK_GREEN("2"),
    DARK_AQUA("3"),
    DARK_RED("4"),
    DARK_PURPLE("5"),
    GOLD("6"),
    GREY("7"),
    DARK_GRAY("8"),
    BLUE("9"),
    GREEN("a"),
    AQUA("b"),
    RED("c"),
    LIGHT_PURPLE("d"),
    YELLOW("e"),
    WHITE("f"),
    OBFUSCATED("k"),
    BOLD("l"),
    STRIKETHROUGH("m"),
    UNDERLINE("n"),
    ITALIC("o"),
    RESET("r");

    private final String COLOR_CODE = "§";
    private final String color;

    MessageColor(String color) {
        this.color = color;
    }

    public String toString() {
        return COLOR_CODE + color;
    }
}
