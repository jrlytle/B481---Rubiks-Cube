package project481;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
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
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Project481 extends Application
{
    int rotate = 0;
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
    //Facing Face (ha, ha)
    Pane cubeOne = new Pane(); //Top Left
    Pane cubeTwo = new Pane(); //Top Middle
    Pane cubeThree = new Pane(); //Top Right
    Pane cubeFour = new Pane(); //Middle Left
    Pane cubeFive = new Pane(); //Middle Middle
    Pane cubeSix = new Pane(); //Middle Right
    Pane cubeSeven = new Pane(); //Bottom Left
    Pane cubeEight = new Pane(); //Bottom Middle
    Pane cubeNine = new Pane(); //Bottom Right
    //Middle Face
    Pane cubeTen = new Pane(); //Top Left
    Pane cubeEleven = new Pane(); //Top Middle
    Pane cubeTwelve = new Pane(); //Top Right
    Pane cubeThirteen = new Pane(); //Middle Left
    Pane cubeFourteen = new Pane(); //Middle Middle (Redundant?)
    Pane cubeFifteen = new Pane(); //Middle Right
    Pane cubeSixteen = new Pane(); //Bottom Left
    Pane cubeSeventeen = new Pane(); //Bottom Middle
    Pane cubeEighteen = new Pane(); //Bottom Right
    //Back Face
    Pane cubeNineteen = new Pane(); //Top Left
    Pane cubeTwenty = new Pane(); //Top Middle
    Pane cubeTwentyOne = new Pane(); //Top Right
    Pane cubeTwentyTwo = new Pane(); //Middle Left
    Pane cubeTwentyThree = new Pane(); //Middle Middle
    Pane cubeTwentyFour = new Pane(); //Middle Right
    Pane cubeTwentyFive = new Pane(); //Bottom Left
    Pane cubeTwentySix = new Pane(); //Bottom Middle
    Pane cubeTwentySeven = new Pane(); //Bottom Right
    ////////////////////////////////////////////////////////////////////////////
    ArrayList<Rotate>[] cubeRotations = new ArrayList[27];
    ////////////////////////////////////////////////////////////////////////////
    Scene scene = new Scene(root, 1400, 900, true); //Scene created, contains root, has size 1400 X 900, and correctly shows overlap of objects
    private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    private double mousePosX = 0;
    private double mousePosY = 0;

    boolean isShift = false;
    boolean isCtrl = false;
    boolean lock = false;
    
    int[] previousRotations = new int[9];
    int[] rotations = new int[9];
    //rotations in order are:
    //Row One
    //Row Two
    //Row Three
    //Column Front/Back One
    //Column Front/Back Two
    //Column Front/Back Three
    //Column Left/Right One
    //Column Left/Right Two
    //Column Left/Right Three
    
    Text toggle1 = new Text("Off");
    
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
    
    int arrayLocation = 0;
    int rotationsLocation = 0;
    int modifier = 1;
    Point3D p = Rotate.Y_AXIS;
        
    private void handleMouseEvents()
    {
        scene.setOnMousePressed((MouseEvent me)
                -> 
                {
                    mousePosX = me.getSceneX();
                    mousePosY = me.getSceneY();
                    
        });

        scene.setOnMouseDragged((MouseEvent me)
                -> 
                {
                    if (!isShift && !isCtrl)
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
                    }
        });
    }

    private void handleKeyEvents()
    {
        scene.setOnKeyPressed(k
                -> 
                {
                    switch(k.getCode().toString()) //Add mod for rotation -1 or 1
                    {
                        case "Q":
                        {
                            for (int a = 0; a < 3; a++)
                            {
                                for (int b = 0; b < 3; b++)
                                {
                                    cubeRotations[cubeLocations[a][0][b]-1].add(new Rotate(90, 0, 0, 0, Rotate.Y_AXIS));
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
                                    cubeRotations[cubeLocations[a][0][b]-1].add(new Rotate(-90, 0, 0, 0, Rotate.Y_AXIS));
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
                                    cubeRotations[cubeLocations[a][1][b]-1].add(new Rotate(90, 0, 0, 0, Rotate.Y_AXIS));
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
                                    cubeRotations[cubeLocations[a][1][b]-1].add(new Rotate(-90, 0, 0, 0, Rotate.Y_AXIS));
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
                                    cubeRotations[cubeLocations[a][2][b]-1].add(new Rotate(90, 0, 0, 0, Rotate.Y_AXIS));
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
                                    cubeRotations[cubeLocations[a][2][b]-1].add(new Rotate(-90, 0, 0, 0, Rotate.Y_AXIS));
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
                                    cubeRotations[cubeLocations[a][b][0]-1].add(new Rotate(-90, 0, 0, 0, Rotate.X_AXIS));
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
                                    cubeRotations[cubeLocations[a][b][0]-1].add(new Rotate(90, 0, 0, 0, Rotate.X_AXIS));
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
                                    cubeRotations[cubeLocations[a][b][1]-1].add(new Rotate(-90, 0, 0, 0, Rotate.X_AXIS));
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
                                    cubeRotations[cubeLocations[a][b][1]-1].add(new Rotate(90, 0, 0, 0, Rotate.X_AXIS));
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
                                    cubeRotations[cubeLocations[a][b][2]-1].add(new Rotate(-90, 0, 0, 0, Rotate.X_AXIS));
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
                                    cubeRotations[cubeLocations[a][b][2]-1].add(new Rotate(90, 0, 0, 0, Rotate.X_AXIS));
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
                                    cubeRotations[cubeLocations[0][a][b]-1].add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
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
                                    cubeRotations[cubeLocations[0][a][b]-1].add(new Rotate(-90, 0, 0, 0, Rotate.Z_AXIS));
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
                                    cubeRotations[cubeLocations[1][a][b]-1].add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
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
                                    cubeRotations[cubeLocations[1][a][b]-1].add(new Rotate(-90, 0, 0, 0, Rotate.Z_AXIS));
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
                                    cubeRotations[cubeLocations[2][a][b]-1].add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
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
                                    cubeRotations[cubeLocations[2][a][b]-1].add(new Rotate(-90, 0, 0, 0, Rotate.Z_AXIS));
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
                    }
                    
                    cubeOne.getTransforms().clear();
                    cubeOne.getTransforms().addAll(cubeRotations[0]);
                    cubeTwo.getTransforms().clear();
                    cubeTwo.getTransforms().addAll(cubeRotations[1]);
                    cubeThree.getTransforms().clear();
                    cubeThree.getTransforms().addAll(cubeRotations[2]);
                    cubeFour.getTransforms().clear();
                    cubeFour.getTransforms().addAll(cubeRotations[3]);
                    cubeFive.getTransforms().clear();
                    cubeFive.getTransforms().addAll(cubeRotations[4]);
                    cubeSix.getTransforms().clear();
                    cubeSix.getTransforms().addAll(cubeRotations[5]);
                    cubeSeven.getTransforms().clear();
                    cubeSeven.getTransforms().addAll(cubeRotations[6]);
                    cubeEight.getTransforms().clear();
                    cubeEight.getTransforms().addAll(cubeRotations[7]);
                    cubeNine.getTransforms().clear();
                    cubeNine.getTransforms().addAll(cubeRotations[8]);
                    cubeTen.getTransforms().clear();
                    cubeTen.getTransforms().addAll(cubeRotations[9]);
                    cubeEleven.getTransforms().clear();
                    cubeEleven.getTransforms().addAll(cubeRotations[10]);
                    cubeTwelve.getTransforms().clear();
                    cubeTwelve.getTransforms().addAll(cubeRotations[11]);
                    cubeThirteen.getTransforms().clear();
                    cubeThirteen.getTransforms().addAll(cubeRotations[12]);
                    cubeFourteen.getTransforms().clear();
                    cubeFourteen.getTransforms().addAll(cubeRotations[13]);
                    cubeFifteen.getTransforms().clear();
                    cubeFifteen.getTransforms().addAll(cubeRotations[14]);
                    cubeSixteen.getTransforms().clear();
                    cubeSixteen.getTransforms().addAll(cubeRotations[15]);
                    cubeSeventeen.getTransforms().clear();
                    cubeSeventeen.getTransforms().addAll(cubeRotations[16]);
                    cubeEighteen.getTransforms().clear();
                    cubeEighteen.getTransforms().addAll(cubeRotations[17]);
                    cubeNineteen.getTransforms().clear();
                    cubeNineteen.getTransforms().addAll(cubeRotations[18]);
                    cubeTwenty.getTransforms().clear();
                    cubeTwenty.getTransforms().addAll(cubeRotations[19]);
                    cubeTwentyOne.getTransforms().clear();
                    cubeTwentyOne.getTransforms().addAll(cubeRotations[20]);
                    cubeTwentyTwo.getTransforms().clear();
                    cubeTwentyTwo.getTransforms().addAll(cubeRotations[21]);
                    cubeTwentyThree.getTransforms().clear();
                    cubeTwentyThree.getTransforms().addAll(cubeRotations[22]);
                    cubeTwentyFour.getTransforms().clear();
                    cubeTwentyFour.getTransforms().addAll(cubeRotations[23]);
                    cubeTwentyFive.getTransforms().clear();
                    cubeTwentyFive.getTransforms().addAll(cubeRotations[24]);
                    cubeTwentySix.getTransforms().clear();
                    cubeTwentySix.getTransforms().addAll(cubeRotations[25]);
                    cubeTwentySeven.getTransforms().clear();
                    cubeTwentySeven.getTransforms().addAll(cubeRotations[26]);
                                        
                    if (rotations[0] % 90 == 0 && rotations[1] % 90 == 0 && rotations[2] % 90 == 0 && rotations[3] % 90 == 0 && rotations[4] % 90 == 0 && rotations[5] % 90 == 0 && rotations[6] % 90 == 0 && rotations[7] % 90 == 0 && rotations[8] % 90 == 0)
                    {
                        lock = false;
                        toggle1.setText("Off");
                        toggle1.setFill(Color.GREEN);
                    }
                    else
                    {
                        lock = true;
                        toggle1.setText("On");
                        toggle1.setFill(Color.RED);
                    }
        });
    }

    @Override
    public void start(Stage primaryStage)
    {        
        for (int a = 0; a < 3; a++)
        {
            for (int b = 0; b < 3; b++)
            {
                for (int c = 0; c < 3; c++)
                {
                    System.out.print(cubeLocations[a][b][c] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
        System.out.println();
        for(int i = 0; i < cubeRotations.length; i++)
        {
            cubeRotations[i] = new ArrayList();
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
        
        toggle1.setFill(Color.GREEN);
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
        
        /*Text test = new Text("1");
        test.setFont(Font.font("Arial", 100));
        test.setFill(Color.BLACK);
        test.setTranslateX(-235);
        test.setTranslateY(-235);
        test.setTranslateZ(-235);
        cubeOne.getChildren().add(test);*/
        
        MeshView cube1 = new MeshView(m[0][0][0]);
        cube1.setCullFace(CullFace.FRONT);
        cube1.setDrawMode(DrawMode.FILL);
        cube1.setMaterial(cubeFace);
        cubeOne.getChildren().add(cube1);
        cubePane.getChildren().add(cubeOne);
        
        MeshView cube2 = new MeshView(m[0][0][1]);
        cube2.setCullFace(CullFace.FRONT);
        cube2.setDrawMode(DrawMode.FILL);
        cube2.setMaterial(cubeFace);
        cubeTwo.getChildren().add(cube2);
        cubePane.getChildren().add(cubeTwo);
        
        MeshView cube3 = new MeshView(m[0][0][2]);
        cube3.setCullFace(CullFace.FRONT);
        cube3.setDrawMode(DrawMode.FILL);
        cube3.setMaterial(cubeFace);
        cubeThree.getChildren().add(cube3);
        cubePane.getChildren().add(cubeThree);
        
        MeshView cube4 = new MeshView(m[0][1][0]);
        cube4.setCullFace(CullFace.FRONT);
        cube4.setDrawMode(DrawMode.FILL);
        cube4.setMaterial(cubeFace);
        cubeFour.getChildren().add(cube4);
        cubePane.getChildren().add(cubeFour);
        
        MeshView cube5 = new MeshView(m[0][1][1]);
        cube5.setCullFace(CullFace.FRONT);
        cube5.setDrawMode(DrawMode.FILL);
        cube5.setMaterial(cubeFace);
        cubeFive.getChildren().add(cube5);
        cubePane.getChildren().add(cubeFive);
        
        MeshView cube6 = new MeshView(m[0][1][2]);
        cube6.setCullFace(CullFace.FRONT);
        cube6.setDrawMode(DrawMode.FILL);
        cube6.setMaterial(cubeFace);
        cubeSix.getChildren().add(cube6);
        cubePane.getChildren().add(cubeSix);
        
        MeshView cube7 = new MeshView(m[0][2][0]);
        cube7.setCullFace(CullFace.FRONT);
        cube7.setDrawMode(DrawMode.FILL);
        cube7.setMaterial(cubeFace);
        cubeSeven.getChildren().add(cube7);
        cubePane.getChildren().add(cubeSeven);
        
        MeshView cube8 = new MeshView(m[0][2][1]);
        cube8.setCullFace(CullFace.FRONT);
        cube8.setDrawMode(DrawMode.FILL);
        cube8.setMaterial(cubeFace);
        cubeEight.getChildren().add(cube8);
        cubePane.getChildren().add(cubeEight);
        
        MeshView cube9 = new MeshView(m[0][2][2]);
        cube9.setCullFace(CullFace.FRONT);
        cube9.setDrawMode(DrawMode.FILL);
        cube9.setMaterial(cubeFace);
        cubeNine.getChildren().add(cube9);
        cubePane.getChildren().add(cubeNine);
        
        MeshView cube10 = new MeshView(m[1][0][0]);
        cube10.setCullFace(CullFace.FRONT);
        cube10.setDrawMode(DrawMode.FILL);
        cube10.setMaterial(cubeFace);
        cubeTen.getChildren().add(cube10);
        cubePane.getChildren().add(cubeTen);
        
        MeshView cube11 = new MeshView(m[1][0][1]);
        cube11.setCullFace(CullFace.FRONT);
        cube11.setDrawMode(DrawMode.FILL);
        cube11.setMaterial(cubeFace);
        cubeEleven.getChildren().add(cube11);
        cubePane.getChildren().add(cubeEleven);
        
        MeshView cube12 = new MeshView(m[1][0][2]);
        cube12.setCullFace(CullFace.FRONT);
        cube12.setDrawMode(DrawMode.FILL);
        cube12.setMaterial(cubeFace);
        cubeTwelve.getChildren().add(cube12);
        cubePane.getChildren().add(cubeTwelve);
        
        MeshView cube13 = new MeshView(m[1][1][0]);
        cube13.setCullFace(CullFace.FRONT);
        cube13.setDrawMode(DrawMode.FILL);
        cube13.setMaterial(cubeFace);
        cubeThirteen.getChildren().add(cube13);
        cubePane.getChildren().add(cubeThirteen);
        
        /*MeshView cube14 = new MeshView(m[1][1][1]); //Redundant?
        cube14.setCullFace(CullFace.FRONT);
        cube14.setDrawMode(DrawMode.FILL);
        cube14.setMaterial(cubeFace);
        cubeFourteen.getChildren().add(cube14);
        cubePane.getChildren().add(cubeFourteen);*/
        
        MeshView cube15 = new MeshView(m[1][1][2]);
        cube15.setCullFace(CullFace.FRONT);
        cube15.setDrawMode(DrawMode.FILL);
        cube15.setMaterial(cubeFace);
        cubeFifteen.getChildren().add(cube15);
        cubePane.getChildren().add(cubeFifteen);
        
        MeshView cube16 = new MeshView(m[1][2][0]);
        cube16.setCullFace(CullFace.FRONT);
        cube16.setDrawMode(DrawMode.FILL);
        cube16.setMaterial(cubeFace);
        cubeSixteen.getChildren().add(cube16);
        cubePane.getChildren().add(cubeSixteen);
        
        MeshView cube17 = new MeshView(m[1][2][1]);
        cube17.setCullFace(CullFace.FRONT);
        cube17.setDrawMode(DrawMode.FILL);
        cube17.setMaterial(cubeFace);
        cubeSeventeen.getChildren().add(cube17);
        cubePane.getChildren().add(cubeSeventeen);
        
        MeshView cube18 = new MeshView(m[1][2][2]);
        cube18.setCullFace(CullFace.FRONT);
        cube18.setDrawMode(DrawMode.FILL);
        cube18.setMaterial(cubeFace);
        cubeEighteen.getChildren().add(cube18);
        cubePane.getChildren().add(cubeEighteen);
        
        MeshView cube19 = new MeshView(m[2][0][0]);
        cube19.setCullFace(CullFace.FRONT);
        cube19.setDrawMode(DrawMode.FILL);
        cube19.setMaterial(cubeFace);
        cubeNineteen.getChildren().add(cube19);
        cubePane.getChildren().add(cubeNineteen);
        
        MeshView cube20 = new MeshView(m[2][0][1]);
        cube20.setCullFace(CullFace.FRONT);
        cube20.setDrawMode(DrawMode.FILL);
        cube20.setMaterial(cubeFace);
        cubeTwenty.getChildren().add(cube20);
        cubePane.getChildren().add(cubeTwenty);
        
        MeshView cube21 = new MeshView(m[2][0][2]);
        cube21.setCullFace(CullFace.FRONT);
        cube21.setDrawMode(DrawMode.FILL);
        cube21.setMaterial(cubeFace);
        cubeTwentyOne.getChildren().add(cube21);
        cubePane.getChildren().add(cubeTwentyOne);
        
        MeshView cube22 = new MeshView(m[2][1][0]);
        cube22.setCullFace(CullFace.FRONT);
        cube22.setDrawMode(DrawMode.FILL);
        cube22.setMaterial(cubeFace);
        cubeTwentyTwo.getChildren().add(cube22);
        cubePane.getChildren().add(cubeTwentyTwo);
        
        MeshView cube23 = new MeshView(m[2][1][1]);
        cube23.setCullFace(CullFace.FRONT);
        cube23.setDrawMode(DrawMode.FILL);
        cube23.setMaterial(cubeFace);
        cubeTwentyThree.getChildren().add(cube23);
        cubePane.getChildren().add(cubeTwentyThree);
        
        MeshView cube24 = new MeshView(m[2][1][2]);
        cube24.setCullFace(CullFace.FRONT);
        cube24.setDrawMode(DrawMode.FILL);
        cube24.setMaterial(cubeFace);
        cubeTwentyFour.getChildren().add(cube24);
        cubePane.getChildren().add(cubeTwentyFour);
        
        MeshView cube25 = new MeshView(m[2][2][0]);
        cube25.setCullFace(CullFace.FRONT);
        cube25.setDrawMode(DrawMode.FILL);
        cube25.setMaterial(cubeFace);
        cubeTwentyFive.getChildren().add(cube25);
        cubePane.getChildren().add(cubeTwentyFive);
        
        MeshView cube26 = new MeshView(m[2][2][1]);
        cube26.setCullFace(CullFace.FRONT);
        cube26.setDrawMode(DrawMode.FILL);
        cube26.setMaterial(cubeFace);
        cubeTwentySix.getChildren().add(cube26);
        cubePane.getChildren().add(cubeTwentySix);
        
        MeshView cube27 = new MeshView(m[2][2][2]);
        cube27.setCullFace(CullFace.FRONT);
        cube27.setDrawMode(DrawMode.FILL);
        cube27.setMaterial(cubeFace);
        cubeTwentySeven.getChildren().add(cube27);
        cubePane.getChildren().add(cubeTwentySeven);
        //------------------------------------------------------------------------------------------------------------------------------------------------
        PhongMaterial blackMaterial = new PhongMaterial(); //Material created for Center of Rubik's Cube
        blackMaterial.setDiffuseColor(Color.BLACK); //Base Color of Center Cube is Black
        blackMaterial.setSpecularColor(Color.DARKGRAY); //Reflectivity of Center Cube is Dark Gray

        Box centerCube = new Box(445, 445, 445); //Large Center cube created to give illusion that Rubik's Cube is one piece
        centerCube.setMaterial(blackMaterial); //Center cube's material set to black material created above
        centerCube.setTranslateX(0); //Center Cube's X Location set to 0
        centerCube.setTranslateY(0); //Center Cube's Y Location set to 0
        centerCube.setTranslateZ(0); //Center Cube's Z Location set to 0
        //cubePane.getChildren().add(centerCube); //Center cube added to cube Pane

        handleMouseEvents();
        handleKeyEvents();
        root.getChildren().addAll(menuPane, cubePane, topRowPane, middleRowPane, bottomRowPane, frontLeftColumnPane, frontMiddleColumnPane, frontRightColumnPane, sideLeftColumnPane, sideMiddleColumnPane, sideRightColumnPane);
        primaryStage.setTitle("Project B481 - Rubik's Cube"); //Set Title of window
        primaryStage.setScene(scene); //Set scene of window to scene
        primaryStage.show(); //Show window
        
        Text lockText = new Text("Lock: ");
        lockText.relocate(810, 425);
        lockText.setFill(Color.WHITE);
        lockText.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(lockText);
        
        toggle1.relocate(880, 425);
        toggle1.setFont(Font.font("Arial", 25));
        menuPane.getChildren().add(toggle1);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}