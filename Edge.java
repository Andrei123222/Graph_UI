package org.example.backend;

import java.awt.geom.Path2D;
public class Edge {
    private final Node start, end;
    private final Path2D line;
    private final boolean isOriented;
    public Edge(Node start, Node end, boolean orientation) {
        this.start = start;
        this.end = end;
        this.isOriented = orientation;
        double diffx = end.getX_position() - start.getX_position();
        double diffy = end.getY_position() - start.getY_position();
        double length = Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffy, 2));
        if (length != 0) {
            diffx /= length;
            diffy /= length;
        }
        this.line = createPath(start.getX_position() + diffx * 25, start.getY_position() + diffy * 25, end.getX_position() - diffx * 25, end.getY_position() - diffy * 25);
    }
    public Path2D getLine(){return line;}
    private Path2D createPath(double x1, double y1, double x2, double y2) {
        Path2D path = new Path2D.Double();

        path.moveTo(x1, y1);
        path.lineTo(x2, y2);

        if(this.isOriented){
            double arrowSize = 20.0; // Adjust as needed
            double angle = Math.atan2(y2 - y1, x2 - x1);
            path.lineTo(x2 - arrowSize * Math.cos(angle - Math.PI / 6), y2 - arrowSize * Math.sin(angle - Math.PI / 6));
            path.moveTo(x2, y2);
            path.lineTo(x2 - arrowSize * Math.cos(angle + Math.PI / 6), y2 - arrowSize * Math.sin(angle + Math.PI / 6));
        }
        else{
            path.lineTo(x2 , y2 );
            path.moveTo(x2, y2);
            path.lineTo(x2 , y2 );
        }
        return path;
    }
    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }
}
