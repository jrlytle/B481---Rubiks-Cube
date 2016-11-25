package project481;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Project481 extends Application
{
    PointLight light = new PointLight();
    
    Pane root = new Pane(); //Root created to hold all objects/Panes
    Pane menuPane = new Pane(); //Menu Pane created to hold directions on how to play game
    Pane cubePane = new Pane(); //Cube Pane created to hold Rubik's cube
    //For Tips:
    Pane topRowPane = new Pane(); //Pane for Top Row Tip
    Pane middleRowPane = new Pane(); //Pane for Middle Row Tip
    Pane bottomRowPane = new Pane(); //Pane for Bottom Row Tip
    Pane frontLeftColumnPane = new Pane(); //Pane for Front Left Column Tip
    Pane frontMiddleColumnPane = new Pane(); //Pane for Front Middle Column Tip
    Pane frontRightColumnPane = new Pane(); //Pane for Front Right Column Tip
    Pane sideLeftColumnPane = new Pane(); //Pane for Side Left Column Tip
    Pane sideMiddleColumnPane = new Pane(); //Pane for Side Middle Column Tip
    Pane sideRightColumnPane = new Pane(); //Pane for Side Right Column Tip
    //For individual Cubes:
    Pane[] allCubes = new Pane[27];
    Affine[] affineTransforms = new Affine[27];
    ////////////////////////////////////////////////////////////////////////////
    Scene scene = new Scene(root, 1400, 900, true); //Scene created, contains root, has size 1400 X 900, and correctly shows overlap of objects
    private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    private double mousePosX = 0;
    private double mousePosY = 0;

    boolean isShift = false;
    boolean isCtrl = false;
    
    TriangleMesh[][][] m = new TriangleMesh[3][3][3];
    
    PhongMaterial cubeFace = new PhongMaterial();
    Image rubik = new Image(getClass().getResource("rubik.png").toExternalForm());
    MeshView[][][] meshView = new MeshView[3][3][3];
    
    int[][] temp = new int[3][3];
    
    int[][][] cubeLocations = 
    {
        {
            {1,2,3},
            {4,5,6},
            {7,8,9}
        },
        {
            {10,11,12},
            {13,14,15},
            {16,17,18}
        },
        {
            {19,20,21},
            {22,23,24},
            {25,26,27}
        }
    };
    int[][][] completeCube = 
    {
        {
            {1,2,3},
            {4,5,6},
            {7,8,9}
        },
        {
            {10,11,12},
            {13,14,15},
            {16,17,18}
        },
        {
            {19,20,21},
            {22,23,24},
            {25,26,27}
        }
    };
    public boolean cubeComplete()
    {
        for (int a = 0; a < 3; a++)
        {
            for (int b = 0; b < 3; b++)
            {
                for (int c = 0; c < 3; c++)
                {
                    if(cubeLocations[a][b][c] != completeCube[a][b][c])
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public void randomize(int steps) throws AWTException
    {
        Robot robot = new Robot();
        Random r = new Random();
        int counter = 0;
        while(counter < steps)
        {
            counter++;
            switch(r.nextInt(18))
            {
                case 0: robot.keyPress(KeyEvent.VK_Q); break;
                case 1: robot.keyPress(KeyEvent.VK_W); break;
                case 2: robot.keyPress(KeyEvent.VK_A); break;
                case 3: robot.keyPress(KeyEvent.VK_S); break;
                case 4: robot.keyPress(KeyEvent.VK_Z); break;
                case 5: robot.keyPress(KeyEvent.VK_X); break;
                case 6: robot.keyPress(KeyEvent.VK_E); break;
                case 7: robot.keyPress(KeyEvent.VK_D); break;
                case 8: robot.keyPress(KeyEvent.VK_R); break;
                case 9: robot.keyPress(KeyEvent.VK_F); break;
                case 10: robot.keyPress(KeyEvent.VK_T); break;
                case 11: robot.keyPress(KeyEvent.VK_G); break;
                case 12: robot.keyPress(KeyEvent.VK_Y); break;
                case 13: robot.keyPress(KeyEvent.VK_H); break;
                case 14: robot.keyPress(KeyEvent.VK_U); break;
                case 15: robot.keyPress(KeyEvent.VK_J); break;
                case 16: robot.keyPress(KeyEvent.VK_I); break;
                case 17: robot.keyPress(KeyEvent.VK_K); break;
            }
        }
        robot.keyPress(KeyEvent.VK_Q);
    }
    private void handleMouseEvents()
    {
        scene.setOnMousePressed((MouseEvent me)
                -> 
                {
                    mousePosX = me.getSceneX();
                    mousePosY = me.getSceneY();
                    
        });

        scene.setOnMouseDragged((MouseEvent me)
        ->  {
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

    private void handleKeyEvents()
    {
        scene.setOnKeyPressed(k
                -> 
                {
                    switch(k.getCode().toString())
                    {
                        case "Q":
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[a][0][b]-1].prepend(new Rotate(90, 0, 0, 0, Rotate.Y_AXIS));
                                    temp[a][b] = cubeLocations[a][0][b];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[a][0][b] = temp[b][2-a];
                                }
                            }
                        }    
                        break;
                        case "W": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[a][0][b]-1].prepend(new Rotate(-90, 0, 0, 0, Rotate.Y_AXIS));
                                    temp[a][b] = cubeLocations[a][0][b];
                                }
                            }
                            for(int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[a][0][b] = temp[2-b][a];
                                }
                            }
                        }
                        break;
                        case "A": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[a][1][b]-1].prepend(new Rotate(90, 0, 0, 0, Rotate.Y_AXIS));
                                    temp[a][b] = cubeLocations[a][1][b];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[a][1][b] = temp[b][2 - a];
                                }
                            }
                        }
                        break;
                        case "S": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[a][1][b]-1].prepend(new Rotate(-90, 0, 0, 0, Rotate.Y_AXIS));
                                    temp[a][b] = cubeLocations[a][1][b];
                                }
                            }
                            for(int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[a][1][b] = temp[2 - b][a];
                                }
                            }
                        }
                        break;
                        case "Z": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[a][2][b]-1].prepend(new Rotate(90, 0, 0, 0, Rotate.Y_AXIS));
                                    temp[a][b] = cubeLocations[a][2][b];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[a][2][b] = temp[b][2 - a];
                                }
                            }
                        }
                        break;
                        case "X": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[a][2][b]-1].prepend(new Rotate(-90, 0, 0, 0, Rotate.Y_AXIS));
                                    temp[a][b] = cubeLocations[a][2][b];
                                }
                            }
                            for(int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[a][2][b] = temp[2 - b][a];
                                }
                            }
                        }
                        break;
                        case "E": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[a][b][0]-1].prepend(new Rotate(-90, 0, 0, 0, Rotate.X_AXIS));
                                    temp[a][b] = cubeLocations[a][b][0];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[a][b][0] = temp[b][2 - a];
                                }
                            }
                        }
                        break;
                        case "D": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[a][b][0]-1].prepend(new Rotate(90, 0, 0, 0, Rotate.X_AXIS));
                                    temp[a][b] = cubeLocations[a][b][0];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[a][b][0] = temp[2-b][a];
                                }
                            }
                        }
                        break;
                        case "R": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[a][b][1]-1].prepend(new Rotate(-90, 0, 0, 0, Rotate.X_AXIS));
                                    temp[a][b] = cubeLocations[a][b][1];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[a][b][1] = temp[b][2 - a];
                                }
                            }
                        }
                        break;
                        case "F": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[a][b][1]-1].prepend(new Rotate(90, 0, 0, 0, Rotate.X_AXIS));
                                    temp[a][b] = cubeLocations[a][b][1];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[a][b][1] = temp[2-b][a];
                                }
                            }
                        }
                        break;
                        case "T": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[a][b][2]-1].prepend(new Rotate(-90, 0, 0, 0, Rotate.X_AXIS));
                                    temp[a][b] = cubeLocations[a][b][2];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[a][b][2] = temp[b][2 - a];
                                }
                            }
                        }
                        break;
                        case "G":
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[a][b][2]-1].prepend(new Rotate(90, 0, 0, 0, Rotate.X_AXIS));
                                    temp[a][b] = cubeLocations[a][b][2];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[a][b][2] = temp[2-b][a];
                                }
                            }
                        }
                        break;
                        case "Y": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[0][a][b]-1].prepend(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
                                    temp[a][b] = cubeLocations[0][a][b];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[0][a][b] = temp[2-b][a];
                                }
                            }
                        }
                        break;
                        case "H": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[0][a][b]-1].prepend(new Rotate(-90, 0, 0, 0, Rotate.Z_AXIS));
                                    temp[a][b] = cubeLocations[0][a][b];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[0][a][b] = temp[b][2-a];
                                }
                            }
                        }
                        break;
                        case "U": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[1][a][b]-1].prepend(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
                                    temp[a][b] = cubeLocations[1][a][b];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[1][a][b] = temp[2-b][a];
                                }
                            }
                        }
                        break;
                        case "J": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[1][a][b]-1].prepend(new Rotate(-90, 0, 0, 0, Rotate.Z_AXIS));
                                    temp[a][b] = cubeLocations[1][a][b];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[1][a][b] = temp[b][2-a];
                                }
                            }
                        }
                        break;
                        case "I": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[2][a][b]-1].prepend(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
                                    temp[a][b] = cubeLocations[2][a][b];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[2][a][b] = temp[2-b][a];
                                }
                            }
                        }
                        break;
                        case "K": 
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    affineTransforms[cubeLocations[2][a][b]-1].prepend(new Rotate(-90, 0, 0, 0, Rotate.Z_AXIS));
                                    temp[a][b] = cubeLocations[2][a][b];
                                }
                            }
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeLocations[2][a][b] = temp[b][2-a];
                                }
                            }
                        }
                        break;
                        case "ENTER":
                        {
                            if (cubeComplete())
                            {
                                try
                                {
                                    randomize(100);
                                }
                                catch (AWTException ex)
                                {

                                }
                            }
                        }
                        break;
                    }
                    for (int a = 0; a < allCubes.length; a++)
                    {
                        allCubes[a].getTransforms().clear();
                        allCubes[a].getTransforms().add(affineTransforms[a]);
                    }
        });
    }

    @Override
    public void start(Stage primaryStage)
    {
        for (int i = 0; i < affineTransforms.length; i++)
        {
            affineTransforms[i] = new Affine();
            allCubes[i] = new Pane();
        }
        root.getChildren().add(light);
        
        topRowPane.relocate(660, -295);
        middleRowPane.relocate(660, -240);
        bottomRowPane.relocate(660, -185);
        frontLeftColumnPane.relocate(660, -130);
        frontMiddleColumnPane.relocate(660, -75);
        frontRightColumnPane.relocate(660, -20);
        sideLeftColumnPane.relocate(660, 35);
        sideMiddleColumnPane.relocate(660, 90);
        sideRightColumnPane.relocate(660, 145);
        
        Camera camera = new PerspectiveCamera(false); //Camera created to rotate around Rubik's Cube
        cubePane.getTransforms().addAll(rotateZ, rotateY, rotateX);
        topRowPane.getTransforms().addAll(rotateZ, rotateY, rotateX);
        middleRowPane.getTransforms().addAll(rotateZ, rotateY, rotateX);
        bottomRowPane.getTransforms().addAll(rotateZ, rotateY, rotateX);
        frontLeftColumnPane.getTransforms().addAll(rotateZ, rotateY, rotateX);
        frontMiddleColumnPane.getTransforms().addAll(rotateZ, rotateY, rotateX);
        frontRightColumnPane.getTransforms().addAll(rotateZ, rotateY, rotateX);
        sideLeftColumnPane.getTransforms().addAll(rotateZ, rotateY, rotateX);
        sideMiddleColumnPane.getTransforms().addAll(rotateZ, rotateY, rotateX);
        sideRightColumnPane.getTransforms().addAll(rotateZ, rotateY, rotateX);
        scene.setFill(Color.GRAY); //Background of Scene changed to Gray
        scene.setCamera(camera); //Scene's Camera set

        camera.relocate(-450, -450); //Center of Focus for Camera changed to -700, -450
        
        PhongMaterial fuchsiaMaterial = new PhongMaterial();
        fuchsiaMaterial.setDiffuseColor(Color.FUCHSIA);
        fuchsiaMaterial.setSpecularColor(Color.DEEPPINK);
        //------------------------------------------------------------------------------------------------------------------------------------------------
        Text title = new Text("Rubik's Cube");
        title.relocate(500, -405);
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Lucida Calligraphy", 50));
        menuPane.getChildren().add(title);
        
        Text toRandomize = new Text("Press Enter to Randomize Cube");
        toRandomize.relocate(500, 405);
        toRandomize.setFill(Color.WHITE);
        toRandomize.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(toRandomize);

        Text directions = new Text("Directions for use:");
        directions.relocate(500, -350);
        directions.setFill(Color.WHITE);
        directions.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directions);
        
        Text directionsRow1 = new Text("Press Q/W:");
        directionsRow1.relocate(500, -295);
        directionsRow1.setFill(Color.WHITE);
        directionsRow1.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow1);
        
        Box outerCube1 = new Box(36,36,36);
        topRowPane.getChildren().add(outerCube1);
        
        Box topRow = new Box(38,13,38);
        topRow.setTranslateY(-13);
        topRow.setMaterial(fuchsiaMaterial);
        topRowPane.getChildren().add(topRow);
        
        Text directionsRow2 = new Text("Press A/S:");
        directionsRow2.relocate(500, -245);
        directionsRow2.setFill(Color.WHITE);
        directionsRow2.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow2);
        
        Box outerCube2 = new Box(35,35,35);
        middleRowPane.getChildren().add(outerCube2);
        
        Box middleRow = new Box(38,12,38);
        middleRow.setMaterial(fuchsiaMaterial);
        middleRowPane.getChildren().add(middleRow);
        
        Text directionsRow3 = new Text("Press Z/X:");
        directionsRow3.relocate(500, -188);
        directionsRow3.setFill(Color.WHITE);
        directionsRow3.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow3);
        
        Box outerCube3 = new Box(35,35,35);
        bottomRowPane.getChildren().add(outerCube3);
        
        Box bottomRow = new Box(38,13,38);
        bottomRow.setMaterial(fuchsiaMaterial);
        bottomRow.setTranslateY(14);
        bottomRowPane.getChildren().add(bottomRow);
        //----------------------------------------------------------------------
        Text directionsRow4 = new Text("Press E/D:");
        directionsRow4.relocate(500, -136);
        directionsRow4.setFill(Color.WHITE);
        directionsRow4.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow4);
        
        Box outerCube4 = new Box(35,35,35);
        frontLeftColumnPane.getChildren().add(outerCube4);
        
        Box frontLeftColumn = new Box(13,38,38);
        frontLeftColumn.setMaterial(fuchsiaMaterial);
        frontLeftColumn.setTranslateX(-14);
        frontLeftColumnPane.getChildren().add(frontLeftColumn);
        
        Text directionsRow5 = new Text("Press R/F:");
        directionsRow5.relocate(500, -82);
        directionsRow5.setFill(Color.WHITE);
        directionsRow5.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow5);
        
        Box outerCube5 = new Box(35,35,35);
        frontMiddleColumnPane.getChildren().add(outerCube5);
        
        Box frontMiddleColumn = new Box(12,38,38);
        frontMiddleColumn.setMaterial(fuchsiaMaterial);
        frontMiddleColumnPane.getChildren().add(frontMiddleColumn);
        
        Text directionsRow6 = new Text("Press T/G:");
        directionsRow6.relocate(500, -27);
        directionsRow6.setFill(Color.WHITE);
        directionsRow6.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow6);
        
        Box outerCube6 = new Box(35,35,35);
        frontRightColumnPane.getChildren().add(outerCube6);
        
        Box frontRightColumn = new Box(13,38,38);
        frontRightColumn.setMaterial(fuchsiaMaterial);
        frontRightColumn.setTranslateX(14);
        frontRightColumnPane.getChildren().add(frontRightColumn);
        
        //----------------------------------------------------------------------
        
        Text directionsRow7 = new Text("Press Y/H:");
        directionsRow7.relocate(500, 29);
        directionsRow7.setFill(Color.WHITE);
        directionsRow7.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow7);
        
        Box outerCube7 = new Box(35,35,35);
        sideLeftColumnPane.getChildren().add(outerCube7);
        
        Box sideLeftColumn = new Box(38,38,13);
        sideLeftColumn.setMaterial(fuchsiaMaterial);
        sideLeftColumn.setTranslateZ(-14);
        sideLeftColumnPane.getChildren().add(sideLeftColumn);
        
        Text directionsRow8 = new Text("Press U/J:");
        directionsRow8.relocate(500, 86);
        directionsRow8.setFill(Color.WHITE);
        directionsRow8.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow8);
        
        Box outerCube8 = new Box(35,35,35);
        sideMiddleColumnPane.getChildren().add(outerCube8);
        
        Box sideMiddleColumn = new Box(38,38,13);
        sideMiddleColumn.setMaterial(fuchsiaMaterial);
        sideMiddleColumnPane.getChildren().add(sideMiddleColumn);
        
        Text directionsRow9 = new Text("Press I/K:"); //Key words: Perpendicular/Facing & right - left
        directionsRow9.relocate(500, 138);
        directionsRow9.setFill(Color.WHITE);
        directionsRow9.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(directionsRow9);
        
        Box outerCube9 = new Box(35,35,35);
        sideRightColumnPane.getChildren().add(outerCube9);
        
        Box sideRightColumn = new Box(38,38,13);
        sideRightColumn.setMaterial(fuchsiaMaterial);
        sideRightColumn.setTranslateZ(14);
        sideRightColumnPane.getChildren().add(sideRightColumn);
        //----------------------------------------------------------------------
        int xAdj = 0;
        int yAdj = 0;
        int zAdj = 0;
        for(int a = 0; a < 3; a++)
        {
            for(int b = 0; b < 3; b++)
            {
                for(int c = 0; c < 3; c++)
                {
                    m[a][b][c] = new TriangleMesh();
                    m[a][b][c].getPoints().addAll(
                            -235 + xAdj, -85 + yAdj, -235 + zAdj,
                            -85 + xAdj, -85 + yAdj, -235 + zAdj,
                            -235 + xAdj, -235 + yAdj, -235 + zAdj,
                            -85 + xAdj, -235 + yAdj, -235 + zAdj,
                            -235 + xAdj, -85 + yAdj, -85 + zAdj,
                            -85 + xAdj, -85 + yAdj, -85 + zAdj,
                            -235 + xAdj, -235 + yAdj, -85 + zAdj,
                            -85 + xAdj, -235 + yAdj, -85 + zAdj
                    );
                    m[a][b][c].getTexCoords().addAll(
                            0.25f, 0, //Face 1 Lower Left Corner
                            0.5f, 0, //Face 1 Lower Right Corner
                            0, 0.25f, //Face 2 Lower Left Corner
                            0.25f, 0.25f, //Face 1 Upper Left Corner OR Face 2 Lower Right Corner OR Face 5 Lower Left Corner
                            0.5f, 0.25f, //Face 1 Upper Right Corner OR Face 3 Lower Left Corner OR Face 5 Lower Right Corner
                            0.75f, 0.25f, //Face 3 Lower Right Corner OR Face 4 Lower Left Corner
                            1, 0.25f, //Face 4 Lower Right Corner
                            0, 0.5f, //Face 2 Upper Left Corner
                            0.25f, 0.5f, //Face 2 Upper Right Corner OR Face 5 Upper Left Corner OR Face 6 Lower Left Corner
                            0.5f, 0.5f, //Face 3 Upper Left Corner OR Face 5 Upper Right Corner OR Face 6 Lower Right Corner
                            0.75f, 0.5f, //Face 3 Upper Right Corner OR Face 4 Upper Left Corner
                            1, 0.5f, //Face 4 Upper Right Corner
                            0.25f, 0.75f, //Face 6 Upper Left Corner
                            0.5f, 0.75f //Face 6 Upper Right Corner
                    );

                    m[a][b][c].getFaces().addAll(
                            5, 1, 4, 0, 0, 3,
                            5, 1, 0, 3, 1, 4,
                            0, 3, 4, 2, 6, 7,
                            0, 3, 6, 7, 2, 8,
                            1, 4, 0, 3, 2, 8,
                            1, 4, 2, 8, 3, 9,
                            5, 5, 1, 4, 3, 9,
                            5, 5, 3, 9, 7, 10,
                            4, 6, 5, 5, 7, 10,
                            4, 6, 7, 10, 6, 11,
                            3, 9, 2, 8, 6, 12,
                            3, 9, 6, 12, 7, 13
                    );
                    xAdj += 160;
                }
                xAdj = 0;
                yAdj += 160;
            }
            yAdj = 0;
            zAdj += 160;
        }
        cubeFace.setDiffuseMap(rubik);
        MeshView[][][] meshCubes = new MeshView[3][3][3];
        for(int a = 0; a < 3; a++)
        {
            for(int b = 0; b < 3; b++)
            {
                for(int c = 0; c < 3; c++)
                {
                    meshCubes[a][b][c] = new MeshView(m[a][b][c]);
                    meshCubes[a][b][c].setCullFace(CullFace.FRONT);
                    meshCubes[a][b][c].setDrawMode(DrawMode.FILL);
                    meshCubes[a][b][c].setMaterial(cubeFace);
                    allCubes[(9 * a) + (b * 3) + c].getChildren().add(meshCubes[a][b][c]);
                    cubePane.getChildren().add(allCubes[(9 * a) + (b * 3) + c]);
                }
            }
        }
        //------------------------------------------------------------------------------------------------------------------------------------------------
        PhongMaterial blackMaterial = new PhongMaterial(); //Material created for Center of Rubik's Cube
        blackMaterial.setDiffuseColor(Color.BLACK); //Base Color of Center Cube is Black
        blackMaterial.setSpecularColor(Color.DARKGRAY); //Reflectivity of Center Cube is Dark Gray

        Box centerCube = new Box(463, 463, 463); //Large Center cube created to give illusion that Rubik's Cube is one piece
        centerCube.setMaterial(blackMaterial); //Center cube's material set to black material created above
        centerCube.setTranslateX(0); //Center Cube's X Location set to 0
        centerCube.setTranslateY(0); //Center Cube's Y Location set to 0
        centerCube.setTranslateZ(0); //Center Cube's Z Location set to 0
        cubePane.getChildren().add(centerCube);

        handleMouseEvents();
        handleKeyEvents();
        root.getChildren().addAll(menuPane, cubePane, topRowPane, middleRowPane, bottomRowPane, frontLeftColumnPane, frontMiddleColumnPane, frontRightColumnPane, sideLeftColumnPane, sideMiddleColumnPane, sideRightColumnPane);
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