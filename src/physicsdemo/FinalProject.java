/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsdemo;

import java.awt.MouseInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.Point;
import simulation.Simulation;

public class FinalProject extends Application {

    @Override
    public void start(Stage primaryStage) {
        GamePane root = new GamePane();
        Simulation sim = new Simulation(600, 500, 2, 2);
        root.setShapes(sim.setUpShapes());
        
        Scene scene = new Scene(root, 600, 500);
        new Thread( () -> {while(true){
            Point p = MouseInfo.getPointerInfo().getLocation();
            
            Point pos = sim.getPosition();
            try{
                Thread.sleep(25);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            double x = p.x;
            double y = p.y;
            int dx=0;
            int dy=0;
            //sim.moveInner((int)x - pos.x, (int)y - pos.y);
            if(x<pos.x && Math.abs((int)x- pos.x) >3)
                //sim.moveInner(-3, 0);
                dx=-3;
            if(x>pos.x && Math.abs((int)x- pos.x) >3)
                //sim.moveInner(3, 0);
                dx=3;
            if(y>pos.y && Math.abs((int)y- pos.y) >3)
                //sim.moveInner(0, 3);
                dy=3;
            if(y<pos.y && Math.abs((int)y- pos.y) >3)
                //sim.moveInner(0, -3);
                dy=-3;
            sim.moveInner(dx, dy);
        }}).start();

        root.requestFocus(); 
        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case DOWN:
                    sim.moveInner(0, 3);
                    break;
                case UP:
                    sim.moveInner(0, -3);
                    break;
                case LEFT:
                    sim.moveInner(-3, 0);
                    break;
                case RIGHT:
                    sim.moveInner(3, 0);
                    break;
            }
        });
        root.requestFocus(); 

        primaryStage.setTitle("Game Physics");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((event)->System.exit(0));
        primaryStage.show();
        
        // This is the main animation thread
        new Thread(() -> {
            while (true) {
                sim.evolve(1.0);
                Platform.runLater(()->sim.updateShapes());
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ex) {

                }
            }
        }).start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
