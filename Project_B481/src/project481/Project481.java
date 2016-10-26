package project481;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Project481 extends Application
{
    int rotate = 0;
    Pane root = new Pane(); //Root created to hold all objects/Panes
    Pane menuPane = new Pane(); //Menu Pane created to hold directions on how to play game
    Pane cubePane = new Pane(); //Cube Pane created to hold Rubik's cube
    Scene scene = new Scene(root, 1400, 900, true); //Scene created, contains root, has size 1400 X 900, and correctly shows overlap of objects
    private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    private double mousePosX = 0;
    private double mousePosY = 0;

    private void handleMouseEvents()
    {
        scene.setOnMousePressed((MouseEvent me) -> 
                {
                    mousePosX = me.getSceneX();
                    mousePosY = me.getSceneY();
        });

        scene.setOnMouseDragged((MouseEvent me) -> 
                {
                    double dx = (mousePosX - me.getSceneX());
                    double dy = (mousePosY - me.getSceneY());
                    if (me.isPrimaryButtonDown())
                    {
                        rotateX.setAngle(rotateX.getAngle() - (dy / 30 * 360) * (Math.PI / 180)); //30 on both is the speed to turn (lower is faster)
                        rotateY.setAngle(rotateY.getAngle() - (dx / 30 * -360) * (Math.PI / 180));
                    }
                    mousePosX = me.getSceneX();
                    mousePosY = me.getSceneY();
        });
    }

    @Override
    public void start(Stage primaryStage)
    {
        Camera camera = new PerspectiveCamera(false); //Camera created to rotate around Rubik's Cube
        cubePane.getTransforms().addAll(rotateZ, rotateY, rotateX);
        scene.setFill(Color.GRAY); //Background of Scene changed to Gray
        scene.setCamera(camera); //Scene's Camera set

        camera.relocate(-450, -450); //Center of Focus for Camera changed to -700, -450

        ImagePattern blueFace = new ImagePattern(new Image("file:src/project481/blue_face.png")); //Individual cube size for blue face
        ImagePattern greenFace = new ImagePattern(new Image("file:src/project481/green_face.png")); //Individual cube size for green face
        ImagePattern orangeFace = new ImagePattern(new Image("file:src/project481/orange_face.png")); //Individual cube size for orange face
        ImagePattern redFace = new ImagePattern(new Image("file:src/project481/red_face.png")); //Individual cube size for red face
        ImagePattern whiteFace = new ImagePattern(new Image("file:src/project481/white_face.png")); //Individual cube size for white face
        ImagePattern yellowFace = new ImagePattern(new Image("file:src/project481/yellow_face.png")); //Individual cube size for yellow face
        //------------------------------------------------------------------------------------------------------------------------------------------------

        Text title = new Text("Rubik's Cube");
        title.relocate(500, -405);
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Lucida Calligraphy", 50));
        menuPane.getChildren().add(title);
        
        Text directions = new Text("Directions for use:");
        directions.relocate(500, -350);
        directions.setFill(Color.WHITE);
        directions.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directions);
        
        Text directionsRow1 = new Text("Hold Shift + Primary Mouse Button");
        directionsRow1.relocate(500, -295);
        directionsRow1.setFill(Color.WHITE);
        directionsRow1.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow1);
        
        Text directionsRow2 = new Text("on a cube to move row/column");
        directionsRow2.relocate(500, -270);
        directionsRow2.setFill(Color.WHITE);
        directionsRow2.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow2);
        
        Text directionsRow3 = new Text("Pull mouse left/right to move row,");
        directionsRow3.relocate(500, -220);
        directionsRow3.setFill(Color.WHITE);
        directionsRow3.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow3);
        
        Text directionsRow4 = new Text("or up/down to move column");
        directionsRow4.relocate(500, -195);
        directionsRow4.setFill(Color.WHITE);
        directionsRow4.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow4);
        
        //------------------------------------------------------------------------------------------------------------------------------------------------
        PhongMaterial blackMaterial = new PhongMaterial(); //Material created for Center of Rubik's Cube
        blackMaterial.setDiffuseColor(Color.BLACK); //Base Color of Center Cube is Black
        blackMaterial.setSpecularColor(Color.DARKGRAY); //Reflectivity of Center Cube is Dark Gray

        Box centerCube = new Box(445, 445, 445); //Large Center cube created to give illusion that Rubik's Cube is one piece
        centerCube.setMaterial(blackMaterial); //Center cube's material set to black material created above
        centerCube.setTranslateX(0); //Center Cube's X Location set to 0
        centerCube.setTranslateY(0); //Center Cube's Y Location set to 0
        centerCube.setTranslateZ(0); //Center Cube's Z Location set to 0
        cubePane.getChildren().add(centerCube); //Center cube added to cube Pane

        //Faces of Rubik's Cube created in Rectangular pieces to allow individual textures
        Rectangle[][][] frontFace = new Rectangle[3][3][6]; //"Front" faces of Rubik's Cube
        int xAdj = -235; //xAdj variable created to allow for adjusting during creation of all faces
        int yAdj = -235; //yAdj variable created to allow for adjusting during creation of all faces
        int zAdj = -235; //zAdj variable created to allow for adjusting during creation of all faces
        //Loop through frontFace and create Rectangles, apply texture and set location
        for (int i = 0; i < 3; i++) //Width of Rubik's cube
        {
            for (int j = 0; j < 3; j++) //Height of Rubik's cube
            {
                for (int k = 0; k < 6; k++) //6 layers of front/back cube pairs
                {
                    frontFace[i][j][k] = new Rectangle(150, 150);
                    frontFace[i][j][k].setTranslateX(xAdj); //Give current rectangle x location
                    frontFace[i][j][k].setTranslateY(yAdj); //Give current rectangle y location
                    frontFace[i][j][k].setTranslateZ(zAdj); //Give current rectangle z location
                    switch (k)
                    {
                        case 0: frontFace[i][j][k].setFill(redFace); //"Front" of First face given redFace texture
                            break;
                        case 5: frontFace[i][j][k].setFill(orangeFace); //"Back" of last face given orangeFace texture
                            break;
                        default: frontFace[i][j][k].setFill(Color.BLACK); //All others are filled with black to give the illusion of the inside of the Rubik's cube
                            break;
                    }
                    if (k % 2 == 0) //If k is even...
                    {
                        zAdj += 150; //Increase zAdj for next rectangle z location
                    }
                    if (k % 2 != 0) //If k is odd...
                    {
                        zAdj += 10; //Increase zAdj for space between cubes
                    }
                    cubePane.getChildren().add(frontFace[i][j][k]); //Add current rectangle to cubePane
                }
                zAdj = -235; //Reset zAdj for next iteration
                xAdj += 160; //Increment xAdj by 160 for next row of rectangles
            }
            xAdj = -235; //Reset xAdj for next iteration
            yAdj += 160; //Increment yAdj by 160
        }

        Rectangle[][][] topFace = new Rectangle[3][3][6]; //"Top" Face of Rubik's Cube
        xAdj = -235; //xAdj variable rest to allow for adjusting during creation of all faces
        yAdj = -310; //yAdj variable rest to allow for adjusting during creation of all faces
        zAdj = -160; //zAdj variable rest to allow for adjusting during creation of all faces
        //Loop through topFace and create Rectangles, apply texture and set location
        for (int i = 0; i < 3; i++) //Width of Rubik's cube
        {
            for (int j = 0; j < 3; j++) //Height of Rubik's cube
            {
                for (int k = 0; k < 6; k++) //6 layers of front/back cube pairs
                {
                    topFace[i][j][k] = new Rectangle(150, 150);
                    topFace[i][j][k].setTranslateX(xAdj); //Give current rectangle x location
                    topFace[i][j][k].setTranslateY(yAdj); //Give current rectangle y location
                    topFace[i][j][k].setTranslateZ(zAdj); //Give current rectangle z location
                    switch (k)
                    {
                        case 0: topFace[i][j][k].setFill(blueFace); //"Front" of First face given blueFace texture
                            break;
                        case 5: topFace[i][j][k].setFill(greenFace); //"Back" of last face given greenFace texture
                            break;
                        default: topFace[i][j][k].setFill(Color.BLACK); //All others are filled with black to give the illusion of the inside of the Rubik's cube
                            break;
                    }
                    topFace[i][j][k].setRotationAxis(Rotate.X_AXIS); //Set Rotation Axis to X Axis
                    topFace[i][j][k].setRotate(90); //Rotate by 90 degrees
                    if (k % 2 == 0) //If k is even...
                    {
                        yAdj += 150; //Increment yAdj for next Rectangle location
                    }
                    if (k % 2 != 0) //If k is odd...
                    {
                        yAdj += 10; //Increment Adj for space between cubes
                    }
                    cubePane.getChildren().add(topFace[i][j][k]); //Add current Rectangle to cube Pane
                }
                yAdj = -310; //Reset yAdj for next iteration
                xAdj += 160; //Increment xAdj for next row
            }
            xAdj = -235; //Reset xAdj for next iteration
            zAdj += 160; //Increment zAdj for next row
        }

        Rectangle[][][] sideFace = new Rectangle[3][3][6]; //"Side" face of Rubik's Cube
        xAdj = -310; //xAdj variable rest to allow for adjusting during creation of all faces
        yAdj = -235; //yAdj variable rest to allow for adjusting during creation of all faces
        zAdj = -160; //zAdj variable rest to allow for adjusting during creation of all faces
        for (int i = 0; i < 3; i++) //Width of Rubik's cube
        {
            for (int j = 0; j < 3; j++) //Height of Rubik's cube
            {
                for (int k = 0; k < 6; k++) //6 layers of front/back cube pairs
                {
                    sideFace[i][j][k] = new Rectangle(150, 150);
                    sideFace[i][j][k].setTranslateX(xAdj); //Give current rectangle x location
                    sideFace[i][j][k].setTranslateY(yAdj); //Give current rectangle y location
                    sideFace[i][j][k].setTranslateZ(zAdj); //Give current rectangle z location
                    switch (k)
                    {
                        case 0: sideFace[i][j][k].setFill(yellowFace); //"Front" of First face given yellowFace texture
                            break;
                        case 5: sideFace[i][j][k].setFill(whiteFace); //"Back" of last face given whiteFace texture
                            break;
                        default: sideFace[i][j][k].setFill(Color.BLACK); //All others are filled with black to give the illusion of the inside of the Rubik's cube
                            break;
                    }
                    sideFace[i][j][k].setRotationAxis(Rotate.Y_AXIS); //Set Rotation Axis to Y Axis
                    sideFace[i][j][k].setRotate(90); //Rotate by 90 degrees
                    if (k % 2 == 0) //If k is even...
                    {
                        xAdj += 150; //Increment yAdj for next Rectangle location
                    }
                    if (k % 2 != 0) //If k is odd...
                    {
                        xAdj += 10; //Increment Adj for space between cubes
                    }
                    cubePane.getChildren().add(sideFace[i][j][k]); //Add current Rectangle to cube Pane
                }
                xAdj = -310; //Reset yAdj for next iteration
                yAdj += 160; //Increment xAdj for next row
            }
            yAdj = -235; //Reset xAdj for next iteration
            zAdj += 160; //Increment zAdj for next row
        }

        handleMouseEvents();
        root.getChildren().addAll(menuPane, cubePane); //Add subpanes menuPane and cubePane
        primaryStage.setTitle("Project B481 - Rubik's Cube"); //Set Title of window
        primaryStage.setScene(scene); //Set scene of window to scene
        primaryStage.show(); //Show window
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

}
