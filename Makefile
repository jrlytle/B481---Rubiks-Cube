CC = g++
CFLAGS = -pedantic -Wall -Wno-deprecated-declarations -Wno-deprecated -g
LDFLAGS = -framework GLUT -framework OpenGL -framework Cocoa

# Linux: LDFLAGS = -lGL -lGLU -lglut
# Possibly Windows: LDFLAGS = -lopengl32 -lglu32 -lglut32
# OSX: LDFLAGS = -framework GLUT -framework OpenGL -framework Cocoa

all: project

project: project.cpp
	g++ $(CFLAGS) -o $@ project.cpp $(LDFLAGS)

clean:
	@rm -f project
