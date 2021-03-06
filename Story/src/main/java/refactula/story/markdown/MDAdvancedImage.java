package refactula.story.markdown;

public class MDAdvancedImage implements MDParagraph {
    private final String src;
    private final String altText;
    private final String width;

    public MDAdvancedImage(String src, String altText, String width) {
        this.src = src;
        this.altText = altText;
        this.width = width;
    }

    @Override
    public MDLine toMDLine() {
        return MDLine.of("<img src=\"" + src + "\" alt=\"" + altText + "\" width=\"" + width + "\"/>");
    }
}
