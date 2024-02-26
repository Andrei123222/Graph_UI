package org.example.backend;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Graph extends JPanel {
    private Integer nrNodes = 0;
    private final HashMap<Node, List<Node>> adjacencyList = new HashMap<>();
    private final List<Edge> edges = new ArrayList<>();
    private final List<Node> noduri = new ArrayList<>();
    private boolean isOriented = false;

    public List<Node> getNoduri() {
        return noduri;
    }
    public void setOriented(boolean oriented) {
        isOriented = oriented;
    }
    public Graph(){}
    public HashMap<Node, List<Node>> getAdjacencyList() {
        return adjacencyList;
    }
    public boolean getOrientation() {
        return isOriented;
    }
    public void addEdge(Edge edge)
    {
        if(isOriented){
            edges.add(edge);
            Node start=edge.getStart();
            Node end=edge.getEnd();
            List<Node> currentNodes = adjacencyList.get(start);
            if(currentNodes == null){
                currentNodes = new ArrayList<>();
                currentNodes.add(end);
                adjacencyList.put(start,currentNodes);
            }
            else{
                if(!currentNodes.contains(end)) {
                    currentNodes.add(end);
                }
                adjacencyList.replace(start,adjacencyList.get(start),currentNodes);
            }
        }
        else{
            edges.add(edge);
            Edge sameEdge = new Edge(edge.getEnd(),edge.getStart(),this.getOrientation());
            edges.add(sameEdge);
            Node start=edge.getStart();
            Node end=edge.getEnd();
            List<Node> currentNodesStart = adjacencyList.get(start);
            List<Node> currentNodesEnd;
            if(adjacencyList.get(end)==null){
                currentNodesEnd = new ArrayList<>();
            }
            else {
                currentNodesEnd = adjacencyList.get(end);
            }
            if(currentNodesStart == null){
                currentNodesStart = new ArrayList<>();
                //currentNodesEnd = new ArrayList<>();
                currentNodesStart.add(end);
                currentNodesEnd.add(start);
                adjacencyList.put(start,currentNodesStart);
                adjacencyList.put(end,currentNodesEnd);
            }
            else{
                if(!currentNodesStart.contains(end)) {
                    currentNodesStart.add(end);
                    currentNodesEnd.add(start);
                }
                adjacencyList.replace(start,adjacencyList.get(start),currentNodesStart);
                if(adjacencyList.get(end)==null) {
                    adjacencyList.put(end,currentNodesEnd);
                }
                else{
                    adjacencyList.replace(end,adjacencyList.get(end),currentNodesEnd);
                }
            }
        }
    }
    public List<Edge> getEdges(){
        return edges;
    }

    public void addNode(Node node){
        boolean overlap = false;
        for (Node existingNode : this.noduri) {
            if(isOverlap(node.getX_position(),node.getY_position(),existingNode.getX_position(),existingNode.getY_position())){
                overlap = true;
                break;
            }
        }
        if(!overlap){
            node.setNumber(noduri.size());
            noduri.add(node);
            nrNodes++;
        }
    }
    private boolean isOverlap(float x1, float y1, float x2, float y2) {
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return distance < 60;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font f1 = new Font("Times New Roman",Font.BOLD,20);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(f1);
        for (Node node : noduri) {
            String s = String.valueOf(node.getNumber());
            g2d.setColor(node.getBodyColor());
            g2d.fill(node.getBody());
            g2d.setColor(Color.black);
            float nodeBorderThickness = 3.0f; // Adjust as needed
            g2d.setStroke(new BasicStroke(nodeBorderThickness));
            g2d.drawString(s,node.getX_position()-5,node.getY_position()+6);
            g2d.draw(node.getBody());
            g2d.setStroke(new BasicStroke());
        }
        for (Edge edge : this.getEdges()) {
            g2d.setColor(Color.black);
            float lineThickness = 3.0f; // Adjust as needed
            g2d.setStroke(new BasicStroke(lineThickness));
            g2d.draw(edge.getLine());
            g2d.setStroke(new BasicStroke());
        }
    }
    private List<Boolean> visited = new ArrayList<>();
    public void DFS_UTIL(){
        if(!isOriented) {
            for (Node node : getNoduri()) {
                DFS(node,adjacencyList);
            }
        }
        else{
            Stack<Node> stack = new Stack<>();
            for(int i = 0; i<nrNodes; i++){
                visited.set(i,false);
            }
            for(Node node : noduri){
                if(!visited.get(node.getNumber())){
                    DFSOriented(node,stack);
                    System.out.println();
                }
            }
            HashMap<Node,List<Node>> transpose = getTranspose();
            for(int i = 0; i<nrNodes; i++){
                visited.set(i,false);
            }
            while(!stack.empty()){
                Node aux = stack.pop();
                if(!visited.get(aux.getNumber())){
                    DFS(aux,transpose);
                }
            }
        }

    }

    boolean isGraphConnected() {
        // Verifica conectivitatea grafului începand de la nodul de start
        for (Node node : getNoduri()) {
            DFS(node, adjacencyList);
        }
        // Verifica dacă toate nodurile au fost vizitate
        for (boolean isVisited : visited) {
            if (!isVisited) {
                return false; // Nu toate nodurile sunt conectate
            }
        }
        return true; // Toate nodurile sunt conectate
    }

    public boolean isTree() {

        return nrNodes == ((edges.size())/2)+1 && isGraphConnected();
    }

    private static final Random random = new Random();
    public static Color generateRandomColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        return new Color(red, green, blue);
    }
    public Color color = generateRandomColor();
    public void DFS(Node node,HashMap<Node,List<Node>> adjacency){
        if(!isOriented) {
            for (int i = 0; i < nrNodes; i++) {
                visited.set(i, false);
            }
        }
        Stack<Node> stack = new Stack<>();
        if (!visited.get(node.getNumber())) {
            color = generateRandomColor();
        }
        stack.push(node);
        while(!stack.empty()){
            Node aux = stack.pop();
            //if(!isOriented) {
                aux.setBodyColor(color);
            //}
            if(!visited.get(aux.getNumber())){
                System.out.println(aux.getNumber());
                visited.set(aux.getNumber(),true);
            }
            if(adjacency.get(aux)!=null) {
                for (Node neighbour : adjacency.get(aux)) {
                    if (!visited.get(neighbour.getNumber())) {
                        stack.push(neighbour);
                    }
                }
            }
        }
        System.out.println();
    }
    public void DFSOriented(Node node,Stack<Node> stack){
        visited.set(node.getNumber(),true);
        System.out.println(node.getNumber());
        if(adjacencyList.get(node)!=null) {
            for (Node neighbour : adjacencyList.get(node)) {
                if (!visited.get(neighbour.getNumber())) {
                    DFSOriented(neighbour, stack);
                }
            }
        }
        stack.push(node);
    }
    public HashMap<Node, List<Node>> getTranspose(){
        HashMap<Node, List<Node>> transpose = new HashMap<>();
        List<Node> currentNodes;
        for(Node node : noduri) {
            if(adjacencyList.get(node)!=null) {
                for (Node neighbour : adjacencyList.get(node)) {
                    currentNodes = transpose.get(neighbour);
                    if (currentNodes == null) {
                        currentNodes = new ArrayList<>();
                        currentNodes.add(node);
                        transpose.put(neighbour, currentNodes);
                    } else {
                        if (!currentNodes.contains(node)) {
                            currentNodes.add(node);
                        }
                        transpose.replace(neighbour, transpose.get(neighbour), currentNodes);
                    }
                }
            }
        }
        return transpose;
    }
    public void refreshColors(){
        generateRandomColor();
        visited = new ArrayList<>();
        for(int i=0;i<noduri.size();++i){
            visited.add(false);
        }
        for (Node node : noduri) {
            node.setBodyColor(Color.white);
        }
    }
    public List<Integer> topologicalOrdering() {
        List<Integer> in_degree = new ArrayList<>();
        for (int i = 0; i < nrNodes; i++)
            in_degree.add(0);
        for (Node nod : noduri) {
            if (adjacencyList.get(nod) != null) {
                for (Node aux : adjacencyList.get(nod)) {
                    in_degree.set(aux.getNumber(), in_degree.get(aux.getNumber()) + 1);
                }
            }
        }
        Queue<Node> queue = new LinkedList<>();
        for(Node nod : getNoduri()){
            if(in_degree.get(nod.getNumber()) == 0){
                queue.add(nod);
            }
        }
        int index = 0;
        List<Integer> topOrder = new ArrayList<>();
        while(!queue.isEmpty()){
            Node aux = queue.remove();
            topOrder.add(aux.getNumber());
            index++;
            if(adjacencyList.get(aux)!=null) {
                for (Node neighbour : adjacencyList.get(aux)) {
                    in_degree.set(neighbour.getNumber(), in_degree.get(neighbour.getNumber()) - 1);
                    if (in_degree.get(neighbour.getNumber()) == 0) {
                        queue.add(neighbour);
                    }
                }
            }
        }
        if(index != nrNodes){
            return null;
        }
        return topOrder;
    }

}

