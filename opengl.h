// =============================================================================
// 
//       Filename:  opengl.h
// 
//    Description:  Standard includes for OpenGL to avoid having to update this 
//                  in each file.
//
//                  Include using `#include "opengl.h"`
// 
//        Version:  1.0
//        Created:  09/06/2010 19:17:16
//       Revision:  none
//       Compiler:  g++
// 
//         Author:  Chris Sexton (cwx44), cwx44@unh.edu
//        Company:  UNH, CS 870, Fall 2010
// 
// =============================================================================

#pragma once

#if defined(_WIN32)

#include <windows.h>
#include <gl/Gl.h>
#include <gl/Glu.h>
#include <gl/glut.h>

#endif

#if defined(__APPLE__) || defined(MACOSX)

#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#include <GLUT/glut.h>

#else //linux as default

#include <GL/gl.h>
#include <GL/glu.h>
#include <GL/glut.h>

#endif
