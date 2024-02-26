package org.example.backend;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Node {
    private final float x_position;
    private final float y_position;
    private Ellipse2D.Double body = null;
    private int number;
    private Color bodyColor = Color.gray;

    public Color getBodyColor() {
        return bodyColor;
    }
    public void setBodyColor(Color bodyColor) {
        this.bodyColor = bodyColor;
    }
    public Node(float x_position, float y_position) {
        this.x_position = x_position;
        this.y_position = y_position;
        this.body = new Ellipse2D.Double(this.getX_position() - 25,this.getY_position() - 25,50,50);
    }
    public float getX_position() {
        return x_position;
    }

    public float getY_position() {
        return y_position;
    }

    public Ellipse2D.Double getBody() {
        return body;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
