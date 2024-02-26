package org.example;

import org.example.frontend.MainWindow;

import javax.swing.*;

public class App
{
    public static void main( String[] args ) {
        SwingUtilities.invokeLater(() -> {
            MainWindow app= new MainWindow();
            app.setVisible(true);
        });
    }
}
