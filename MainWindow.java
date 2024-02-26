package org.example.frontend;

import org.example.backend.Edge;
import org.example.backend.Graph;
import org.example.backend.Node;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MainWindow extends JFrame {
    private final Graph graph = new Graph();
    public MainWindow() {
        setTitle("Drawing area");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graph.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON3) {
                    for (Node node : graph.getNoduri()) {
                        if (isMouseOnNode(e.getX(), e.getY(), node)) {
                            handleNodeClick(node);
                            break;
                        }
                    }
                }
                else{
                    if(e.getButton()==MouseEvent.BUTTON1) {
                        graph.addNode(new Node(e.getX(), e.getY()));
                        //graph.repaint();
                    }
                }
            }
            private boolean isMouseOnNode(int mouseX, int mouseY, Node node) {
                double distance = Math.sqrt(Math.pow(node.getX_position() - mouseX, 2) + Math.pow(node.getY_position() - mouseY, 2));
                return distance < 50;
            }
            private Node selectedNode = null;
            private void handleNodeClick(Node clickedNode) {
                if(selectedNode == null)
                    selectedNode = clickedNode;
                else {
                    if(clickedNode!=selectedNode){
                        Edge newEdge = new Edge(selectedNode,clickedNode,graph.getOrientation());
                        graph.addEdge(newEdge);
                        selectedNode = null;
                        //graph.repaint();
                    }
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                graph.repaint();
            }

        });
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> saveToFile("F:\\AG\\TemaGrafuri\\src\\main\\resources\\adjancyList.txt"));
        JMenuItem unorientedGraphOptions = new JMenuItem("Show connected components");
        unorientedGraphOptions.addActionListener(e -> {
            if(!graph.getOrientation()){
                graph.refreshColors();
                graph.repaint();
                graph.DFS_UTIL();
                graph.repaint();
            }
            else{
                JOptionPane.showMessageDialog(MainWindow.this, "Can only get connected components if the graph is unoriented");
            }
        });
        JMenuItem isTreeOption = new JMenuItem("Show if graph is a tree");
        isTreeOption.addActionListener(e -> {
            graph.refreshColors();
            graph.repaint();
            if(graph.isTree()){
                graph.repaint();
                JOptionPane.showMessageDialog(MainWindow.this, "Created graph is a tree");
            }
            else{
                JOptionPane.showMessageDialog(MainWindow.this, "Created graph is NOT a tree");
            }
        });
        JMenuItem orientedGraphOptions = new JMenuItem("Show strongly connected components");
        orientedGraphOptions.addActionListener(e -> {
            if(graph.getOrientation()){
                graph.refreshColors();
                graph.repaint();
                graph.DFS_UTIL();
                graph.repaint();
            }
            else{
                JOptionPane.showMessageDialog(MainWindow.this, "Can only get strongly connected components if the graph is oriented");
            }
        });
        JMenuItem topologicalSort = new JMenuItem("Show topological ordering");
        topologicalSort.addActionListener(e -> {
            if(graph.getOrientation()) {
                if(graph.topologicalOrdering()==null) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Topological ordering can only be done if there are no cycles in the oriented graph");
                }
                else {
                    JOptionPane.showMessageDialog(MainWindow.this, "Topological ordering: " + graph.topologicalOrdering());
                    graph.topologicalOrdering();
                }
            }
            else{
                JOptionPane.showMessageDialog(MainWindow.this, "Topological ordering can only be done if the graph is oriented");
            }
        });
        JButton toggleOrientationButton = new JButton("Toggle Orientation");
        toggleOrientationButton.addActionListener(e -> {
            if(graph.getEdges().isEmpty()) {
                graph.setOriented(!graph.getOrientation());
                JOptionPane.showMessageDialog(MainWindow.this, "Graph orientation has been changed to " + graph.getOrientation());
            }
            else{
                JOptionPane.showMessageDialog(MainWindow.this, "Graph orientation cannot be changed after its creation");
            }
        });

        JMenuItem toggleOrientationMenuItem = new JMenuItem("Toggle Orientation");
        toggleOrientationMenuItem.add(toggleOrientationButton);

        // Create a JMenu

        JMenuBar menuBar = new JMenuBar();

        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        JMenu graphMenu = new JMenu("Type");
        JMenu componentsMenu = new JMenu("Components");
        graphMenu.add(toggleOrientationMenuItem);
        fileMenu.add(saveMenuItem);
        componentsMenu.add(unorientedGraphOptions);
        componentsMenu.add(orientedGraphOptions);
        componentsMenu.add(topologicalSort);
        componentsMenu.add(isTreeOption);
        menuBar.add(fileMenu);
        menuBar.add(graphMenu);
        menuBar.add(componentsMenu);
        setJMenuBar(menuBar);

        getContentPane().add(graph);
        setSize(1080, 720);
        setLocationRelativeTo(null);
    }

    private void saveToFile(String filePath) {
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (HashMap.Entry<Node, List<Node>> entry : graph.getAdjacencyList().entrySet()) {
                writer.write(entry.getKey().getNumber() + ": ");
                for (Node node: entry.getValue()) {
                    writer.write(node.getNumber() + " ");
                }
                    writer.newLine();
            }
                JOptionPane.showMessageDialog(this, "File saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving the file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}