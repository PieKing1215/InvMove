package me.pieking1215.invmove.module;

public class CVComponent {
    public boolean translate = false;
    public String text;

    public CVComponent(boolean translate, String text) {
        this.translate = translate;
        this.text = text;
    }

    public static CVComponent literal(String text) {
        return new CVComponent(false, text);
    }

    public static CVComponent translated(String text) {
        return new CVComponent(true, text);
    }
}
