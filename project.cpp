#include <iostream>
#include "opengl.h"

using namespace std;

GLint winWidth = 800, winHeight = 800;
GLfloat eyeX = 0.0, eyeY = 0.0, eyeZ = 2.0;
GLfloat theta = 60, phi = 135;
GLfloat upX = 0.0, upY = 1.0, upZ = 0.0;
GLfloat r = 7;

GLfloat clickTheta, clickPhi;
int clickX, clickY;

int sides[6][3][3];

void initSides() {
	// Note: White is considered the Front face, while Blue is Left and Red is Top
	for (int color = 0; color < 6; color++)
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				sides[color][x][y] = color;
}

void simpleRotate(int front[3][3], int bottom[3][3], int top[3][3], int left[3][3], int right[3][3], int FRONT[3][3], int BOTTOM[3][3], int TOP[3][3], int LEFT[3][3], int RIGHT[3][3]) {
	for (int y = 0; y < 3; y++) {
		for (int x = 0; x < 3; x++) {
			// Simple clockwise rotation
			front[x][2 - y] = FRONT[y][x];
			// Copy the other arrays while we're here
			bottom[x][y] = BOTTOM[x][y];
			top[x][y] = TOP[x][y];
			left[x][y] = LEFT[x][y];
			right[x][y] = RIGHT[x][y];
		}
	}
}

void simpleUpdate(int front[3][3], int bottom[3][3], int top[3][3], int left[3][3], int right[3][3], int FRONT[3][3], int BOTTOM[3][3], int TOP[3][3], int LEFT[3][3], int RIGHT[3][3]) {
	// Update copied values
	for (int y = 0; y < 3; y++) {
		for (int x = 0; x < 3; x++) {
			FRONT[x][y] = front[x][y];
			BOTTOM[x][y] = bottom[x][y];
			TOP[x][y] = top[x][y];
			LEFT[x][y] = left[x][y];
			RIGHT[x][y] = right[x][y];
		}
	}
}

void rotateFixed(int x, int y, int bottom[3][3], int left[3][3], int top[3][3], int right[3][3], int BOTTOM[3][3], int LEFT[3][3], int TOP[3][3], int RIGHT[3][3]) {
	bottom[x][y] = RIGHT[x][y]; // right -> bottom
	left[x][y] = BOTTOM[x][y]; // bottom -> left
	top[x][y] = LEFT[x][y]; // left -> top
	right[x][y] = TOP[x][y]; // top -> right
}

// Rotates the face (e.g. front or back) clockwise
void clockwiseFace(int layer, int FRONT[3][3], int BOTTOM[3][3], int TOP[3][3], int LEFT[3][3], int RIGHT[3][3]) {
	int front[3][3], bottom[3][3], top[3][3], left[3][3], right[3][3];

	simpleRotate(front, bottom, top, left, right, FRONT, BOTTOM, TOP, LEFT, RIGHT);

	for (int i = 0; i < 3; i++) {
		bottom[i][2 - layer] = RIGHT[0][i]; // right -> bottom
		left[2][i] = BOTTOM[abs(2 - i - layer)][2 - layer]; // bottom -> left
		top[abs(layer - i)][layer] = LEFT[2][i]; // left -> top
		right[0][i] = TOP[i][layer]; // top -> right
	}

	simpleUpdate(front, bottom, top, left, right, FRONT, BOTTOM, TOP, LEFT, RIGHT);
}

// Rotates a vertical layer (e.g. left or right) clockwise
void clockwiseVertical(int layer, int FRONT[3][3], int BOTTOM[3][3], int TOP[3][3], int LEFT[3][3], int RIGHT[3][3]) {
	int front[3][3], bottom[3][3], top[3][3], left[3][3], right[3][3];

	simpleRotate(front, bottom, top, left, right, FRONT, BOTTOM, TOP, LEFT, RIGHT);

	for (int i = 0; i < 3; i++)
		rotateFixed(layer, i, bottom, left, top, right, BOTTOM, LEFT, TOP, RIGHT);

	simpleUpdate(front, bottom, top, left, right, FRONT, BOTTOM, TOP, LEFT, RIGHT);
}

// Rotates a horizontal layer (e.g. top or bottom) clockwise
void clockwiseHorizontal(int layer, int FRONT[3][3], int BOTTOM[3][3], int TOP[3][3], int LEFT[3][3], int RIGHT[3][3]) {
	int front[3][3], bottom[3][3], top[3][3], left[3][3], right[3][3];

	simpleRotate(front, bottom, top, left, right, FRONT, BOTTOM, TOP, LEFT, RIGHT);

	for (int i = 0; i < 3; i++)
		rotateFixed(i, layer, bottom, left, top, right, BOTTOM, LEFT, TOP, RIGHT);

	simpleUpdate(front, bottom, top, left, right, FRONT, BOTTOM, TOP, LEFT, RIGHT);
}

void frontClockwise(int i = 1) {
	// FRONT, BOTTOM, TOP, LEFT, RIGHT
	while (i-- > 0)
		clockwiseFace(0, sides[0], sides[2], sides[3], sides[4], sides[5]);
}

void backClockwise(int i = 1) {
	// FRONT, BOTTOM, TOP, LEFT, RIGHT
	while (i-- > 0)
		clockwiseFace(2, sides[1], sides[2], sides[3], sides[5], sides[4]);
}

void leftClockwise(int i = 1) {
	// FRONT, BOTTOM, TOP, LEFT, RIGHT
	while (i-- > 0)
		clockwiseVertical(0, sides[4], sides[2], sides[3], sides[1], sides[0]);
}

void rightClockwise(int i = 1) {
	// FRONT, BOTTOM, TOP, LEFT, RIGHT
	while (i-- > 0)
		clockwiseVertical(2, sides[5], sides[2], sides[3], sides[0], sides[1]);
}

void topClockwise(int i = 1) {
	// FRONT, BOTTOM, TOP, LEFT, RIGHT
	while (i-- > 0)
		clockwiseHorizontal(2, sides[3], sides[0], sides[1], sides[4], sides[5]);
}

void bottomClockwise(int i = 1) {
	// FRONT, BOTTOM, TOP, LEFT, RIGHT
	while (i-- > 0)
		clockwiseHorizontal(0, sides[2], sides[1], sides[0], sides[4], sides[5]);
}

void scrambleSides() {
	//Pattern 1
	//leftClockwise(2);
	//rightClockwise(2);
	//frontClockwise(2);
	//backClockwise(2);	
	//topClockwise(2);
	//bottomClockwise(2);	

	//Pattern 2
	leftClockwise(1);
	rightClockwise(3); // lazy man's counterclockwise
	frontClockwise(3);
	backClockwise(1);
	topClockwise(1);
	bottomClockwise(3);
	leftClockwise(1);
	rightClockwise(3);
}

void loadColor(int color) {
	switch (color) {
	case 0: // front, white
		glColor3f(1, 1, 1);
		break;
	case 1: // back, yellow
		glColor3f(1, 1, 0);
		break;
	case 2: // bottom, orange
		glColor3f(1, 0.5, 0);
		break;
	case 3: // top, red
		glColor3f(1, 0, 0);
		break;
	case 4: // left, blue
		glColor3f(0, 0.3, 1);
		break;
	case 5: // right, green
		glColor3f(0, 1, 0);
		break;
	}
}

void drawSides(bool drawLines = false) {
	glColor3f(0, 0, 0);
	glColorMaterial(GL_FRONT, GL_DIFFUSE);
	glEnable(GL_COLOR_MATERIAL);
	glLineWidth(6);
	glBegin(GL_QUADS);
	int x, y, z, side, i;
	for (side = 0; side < 6; side++) {
		switch (side) {
		case 0: case 1: // front, back
			z = side == 0 ? 0 : 3;
			for (x = 0; x < 3; x++) {
				for (y = 0; y < 3; y++) {
					if (!drawLines)
						loadColor(sides[side][x][y]);
					glNormal3f(0, 0, z == 0 ? -1 : 1);
					glVertex3f(x, y, z);
					glVertex3f(x + 1, y, z);
					glVertex3f(x + 1, y + 1, z);
					glVertex3f(x, y + 1, z);
				}
			}
			break;
		case 2: case 3: // bottom, top
			y = side == 2 ? 0 : 3;
			i = side == 2 ? 2 : 0;
			for (x = 0; x < 3; x++) {
				for (z = 0; z < 3; z++) {
					if (!drawLines)
						loadColor(sides[side][x][abs(i - z)]);
					glNormal3f(0, y == 0 ? -1 : 1, 0);
					glVertex3f(x, y, z);
					glVertex3f(x + 1, y, z);
					glVertex3f(x + 1, y, z + 1);
					glVertex3f(x, y, z + 1);
				}
			}
			break;
		case 4: case 5: // left, right
			x = side == 4 ? 3 : 0;
			i = side == 4 ? 2 : 0;
			for (z = 0; z < 3; z++) {
				for (y = 0; y < 3; y++) {
					if (!drawLines)
						loadColor(sides[side][abs(i - z)][y]);
					glNormal3f(x == 0 ? -1 : 1, 0, 0);
					glVertex3f(x, y + 1, z);
					glVertex3f(x, y, z);
					glVertex3f(x, y, z + 1);
					glVertex3f(x, y + 1, z + 1);
				}
			}
			break;
		}
	}
	glEnd();
}

void display() {
	glMatrixMode(GL_MODELVIEW);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();

	gluLookAt(eyeX, eyeY, eyeZ, 0, 0, 0, upX, upY, upZ);

	glTranslatef(-1.5, -1.5, -1.5);

	// filled
	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	drawSides();

	// lined
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	drawSides(true);

	glutSwapBuffers();
}

void reshapeFcn(GLint w, GLint h) {
	glViewport(0, 0, w, h);

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();

	double factor = 0.4;

	if (w > h)
		glFrustum(-factor * w / h, factor * w / h, -factor, factor, 0.6, 100);
	else
		glFrustum(-factor, factor, -factor * h / w, factor * h / w, 0.6, 100);

	winWidth = w;
	winHeight = h;
}

void eyePosition(void) {
	// Spherical to Cartesian conversion.   
	eyeX = r * sin(theta*0.0174532) * sin(phi*0.0174532);
	eyeY = r * cos(theta*0.0174532);
	eyeZ = r * sin(theta*0.0174532) * cos(phi*0.0174532);
	// Reduce theta slightly to obtain another point on the same longitude line on the sphere.
	GLfloat dt = 1.0;
	GLfloat eyeXtemp = r * sin(theta*0.0174532 - dt) * sin(phi*0.0174532);
	GLfloat eyeYtemp = r * cos(theta*0.0174532 - dt);
	GLfloat eyeZtemp = r * sin(theta*0.0174532 - dt) * cos(phi*0.0174532);

	upX = eyeXtemp - eyeX;
	upY = eyeYtemp - eyeY;
	upZ = eyeZtemp - eyeZ;

	glutPostRedisplay();
}

void onMouseMove(int x, int y) {
	// Mouse point to angle conversion
	theta = clickTheta - (360.0 / (double)winHeight)*(double)(y - clickY)*3.0; // 3.0 rotations possible
	theta = fmod((double)theta, 360); // Restrict the angles within 0~360 deg (optional)

	while (theta < 0)
		theta += 360;

	phi = clickPhi + (theta > 180 ? 1 : -1) * (360.0 / (double)winWidth)*(double)(x - clickX)*3.0;	
	phi = fmod((double)phi, 360);

	eyePosition();
}

void mouse(int button, int state, int x, int y)
{ // 3 = scroll up, 4 = scroll down	
	if (button == 3 || button == 4)
	{
		if (state == GLUT_UP)
			return;
		if (button == 3) // Zoom in
			r -= 0.2;
		else // Zoom out
			r += 0.2;

		eyePosition();
	}
	else if (button == GLUT_LEFT_BUTTON) {
		if (state == GLUT_DOWN)
		{ // Preserve the graph's position so it doesn't "jump" when the mouse is dragged			
			clickX = x;
			clickY = y;
			clickTheta = theta; // Theta is towards the screen
			clickPhi = phi; // Phi is counterclockwise about the "up" axis

			glutMotionFunc(onMouseMove);
		}
		else
			glutMotionFunc(0);
	}
}

void SpecialKeys(int key, int x, int y) {	
	if (key == GLUT_KEY_UP) // Zoom in
		r -= 0.2;	
	if (key == GLUT_KEY_DOWN) // Zoom out
		r += 0.2;

	eyePosition();
}

void init(void) {
	initSides();
	scrambleSides();

	glClearColor(0.6, 0.6, 0.6, 0.0);

	// Lighting
	GLfloat light_specular[] = { 1.0, 1.0, 1.0, 1.0 };
	GLfloat light_ambient[] = { 1, 1, 1, 1 };
	GLfloat light_diffuse[] = { 1, 1, 1, 1 };
	GLfloat light_position[] = { 0.0, 2.0, 4.0, 1.0 };

	glLightfv(GL_LIGHT0, GL_AMBIENT, light_ambient);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, light_diffuse);
	glLightfv(GL_LIGHT0, GL_SPECULAR, light_specular);
	glLightfv(GL_LIGHT0, GL_POSITION, light_position);

	GLfloat mat_shininess[] = { 150 }; // 1 < f < 200

	GLfloat mat_specular[] = { 0.5, 0.5, 0.5, 1 };
	GLfloat mat_ambient[] = { 0.2, 0.2, 0.2, 1 };
	GLfloat mat_diffuse[] = { 0.1, 0.1, 0.1, 1 };

	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, mat_ambient);
	glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, mat_diffuse);
	glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, mat_specular);
	glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, mat_shininess);

	glShadeModel(GL_SMOOTH);

	glEnable(GL_LIGHT0);
	glEnable(GL_LIGHTING);
	glEnable(GL_DEPTH_TEST);
}

int main(int argc, char **argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH | GLUT_MULTISAMPLE);
	glutInitWindowSize(winWidth, winHeight);
	glutCreateWindow("Rubik's Cube");

	init();
	glutDisplayFunc(display);
	glutReshapeFunc(reshapeFcn);

	glutMouseFunc(mouse);

	glutSpecialFunc(SpecialKeys);

	eyePosition();

	glutMainLoop();
}
