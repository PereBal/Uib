#include <GL/gl.h>
#include <GL/glu.h>
#include <GL/glut.h>
#include <SOIL.h>
#include <math.h>

#include "mazeGenerator.h"

#ifndef SO_INCLUDED
#define SO_INCLUDED
#include "sceneObjects.h"
#endif

#include "drawSceneObjects.h"
#include "drawSpecialObjects.h"

#define CELLSZ 2.5
#define PI 3.14159265358979323846


void drawSO(SceneObject * o, float *player) __attribute((always_inline));
void drawCell(const int xm, const int ym, const int prop, const GLdouble color[3], const GLdouble norm[3]) __attribute((always_inline));
void drawWall(int xm, int ym) __attribute((always_inline));

//---------TEXTURES--------------
GLuint program_id;
GLuint textureFloor, textureWall;


GLuint loadTexture(char *filepath){
  glActiveTexture(GL_TEXTURE0);
  GLuint texture_id = SOIL_load_OGL_texture
    (
     filepath,
     SOIL_LOAD_AUTO,
     SOIL_CREATE_NEW_ID,
     SOIL_FLAG_INVERT_Y
    );

  int img_width, img_height;
  unsigned char* img = SOIL_load_image(filepath, &img_width, &img_height, NULL, 0);

  glGenTextures(1, &texture_id);
  glBindTexture(GL_TEXTURE_2D, texture_id);

  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, img_width, img_height, 0, GL_RGB, GL_UNSIGNED_BYTE, img);
  return texture_id;
}

void initTextures(){
  GLint uniformFloor, uniformWall;
  textureWall=loadTexture("textures/texture.bmp");
  glActiveTexture(GL_TEXTURE1);
  glBindTexture(GL_TEXTURE_2D, textureWall);
  uniformWall = glGetUniformLocation(program_id, "wall");
  glUniform1i(uniformWall, 1);

  textureFloor=loadTexture("textures/floor.jpg");
  glActiveTexture(GL_TEXTURE0);
  glBindTexture(GL_TEXTURE_2D, textureFloor);
  uniformFloor = glGetUniformLocation(program_id, "floor");
  glUniform1i(uniformFloor, 0);



  glUseProgram(program_id);
}


//-----------SCENE ELEMENTS---------------
void drawSquaredTiriangle(const GLdouble *n, const GLdouble *c, GLdouble *v1, GLdouble *v2, GLdouble *v3, GLdouble *v4) {

  glEnable(GL_TEXTURE_2D);
  glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
  glBindTexture(GL_TEXTURE_2D, textureFloor);

  glBegin(GL_TRIANGLE_FAN);
  glNormal3dv(n);
  glColor3dv(c);
  glTexCoord2f(0.0, 0.0);
  glVertex3dv(v1);
  glTexCoord2f(0.0, 1.0);
  glVertex3dv(v2);
  glTexCoord2f(1.0, 1.0);
  glVertex3dv(v3);
  glTexCoord2f(0.0, 0.0);
  glVertex3dv(v1);
  glTexCoord2f(1.0, 1.0);
  glVertex3dv(v3);
  glTexCoord2f(1.0, 0.0);
  glVertex3dv(v4);
  glEnd();

  glDisable(GL_TEXTURE_2D);
}

void drawCube(const double segmentSize){
  glColor3f(1.0f,1.0f,1.0f); 
  const double iSize = segmentSize / 2.0;
  glEnable(GL_TEXTURE_2D);
  glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
  glBindTexture(GL_TEXTURE_2D, textureWall);

  glBegin(GL_QUADS);

  glTexCoord2f(0.0f, 0.0f); glVertex3f(-iSize, -iSize,  iSize);  
  glTexCoord2f(1.0f, 0.0f); glVertex3f( iSize, -iSize,  iSize);  
  glTexCoord2f(1.0f, 1.0f); glVertex3f( iSize,  iSize,  iSize);  
  glTexCoord2f(0.0f, 1.0f); glVertex3f(-iSize,  iSize,  iSize);  

  glTexCoord2f(0.0f, 1.0f); glVertex3f(-iSize, -iSize, -iSize);  
  glTexCoord2f(1.0f, 1.0f); glVertex3f(-iSize,  iSize, -iSize);  
  glTexCoord2f(0.0f, 1.0f); glVertex3f( iSize,  iSize, -iSize);  
  glTexCoord2f(0.0f, 0.0f); glVertex3f( iSize, -iSize, -iSize);  

  glTexCoord2f(0.0f, 1.0f); glVertex3f(-iSize,  iSize, -iSize);  
  glTexCoord2f(0.0f, 0.0f); glVertex3f(-iSize,  iSize,  iSize);  
  glTexCoord2f(1.0f, 0.0f); glVertex3f( iSize,  iSize,  iSize);  
  glTexCoord2f(1.0f, 1.0f); glVertex3f( iSize,  iSize, -iSize);  

  glTexCoord2f(1.0f, 1.0f); glVertex3f(-iSize, -iSize, -iSize);  
  glTexCoord2f(0.0f, 1.0f); glVertex3f( iSize, -iSize, -iSize);  
  glTexCoord2f(0.0f, 0.0f); glVertex3f( iSize, -iSize,  iSize);  
  glTexCoord2f(1.0f, 0.0f); glVertex3f(-iSize, -iSize,  iSize);  

  glTexCoord2f(1.0f, 1.0f); glVertex3f( iSize, -iSize, -iSize);  
  glTexCoord2f(0.0f, 1.0f); glVertex3f( iSize,  iSize, -iSize);  
  glTexCoord2f(0.0f, 0.0f); glVertex3f( iSize,  iSize,  iSize);  
  glTexCoord2f(1.0f, 0.0f); glVertex3f( iSize, -iSize,  iSize);  

  glTexCoord2f(1.0f, 1.0f); glVertex3f(-iSize, -iSize, -iSize);  
  glTexCoord2f(1.0f, 0.0f); glVertex3f(-iSize, -iSize,  iSize);  
  glTexCoord2f(0.0f, 0.0f); glVertex3f(-iSize,  iSize,  iSize);  
  glTexCoord2f(0.0f, 1.0f); glVertex3f(-iSize,  iSize, -iSize);  

  glEnd();
  glDisable(GL_TEXTURE_2D);
}


void planeMaterial() {
  GLfloat no_mat[] = {0.0f, 0.0f, 0.0f, 1.0f};
  GLfloat mat_ambient[] = {0.2f, 0.2f, 0.2f, 1.0f};
  GLfloat mat_diffuse[] = {0.5f, 0.5f, 0.5f, 1.0f};
  GLfloat mat_specular[] = {0.2, 0.2f, 0.2f, 1.0f};
  GLfloat no_shininess = 0.0f;
  glMaterialfv(GL_FRONT, GL_AMBIENT, mat_ambient);
  glMaterialfv(GL_FRONT, GL_DIFFUSE, mat_diffuse);
  glMaterialfv(GL_FRONT, GL_SPECULAR, mat_specular);
  glMaterialf(GL_FRONT, GL_SHININESS, no_shininess);
  glMaterialfv(GL_FRONT, GL_EMISSION, no_mat);
}

void mazeWallMaterial() {
  GLfloat no_mat[] = {0.0f, 0.0f, 0.0f, 1.0f};
  GLfloat mat_ambient[] = {0.2f, 0.0f, 0.0f, 1.0f};
  GLfloat mat_diffuse[] = {0.7f, 0.0f, 0.0f, 1.0f};
  GLfloat mat_specular[] = {1.0f, 0.0f, 0.0f, 1.0f};
  GLfloat no_shininess = 0.0f;
  glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, mat_ambient);
  glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, mat_diffuse);
  glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, mat_specular);
  glMaterialf(GL_FRONT, GL_SHININESS, no_shininess);
  glMaterialfv(GL_FRONT, GL_EMISSION, no_mat);
}

void setMat(GLfloat spec [3], float sh) {
  GLfloat amb [3];
  GLfloat dif [3];

  // desempaquetamos la informacion del material
  int i;
  for (i = 0; i < 3; i++) {
    if (spec[i] > 0) {
      amb[i] = spec[i] - 0.8;
      dif[i] = spec[i] - 0.2;
    } else {
      amb[i] = 0;
      dif[i] = 0;
    }
  }

  glMaterialf (GL_FRONT, GL_SHININESS, sh);
  glMaterialfv(GL_FRONT, GL_AMBIENT, amb);
  glMaterialfv(GL_FRONT, GL_DIFFUSE, dif);
  glMaterialfv(GL_FRONT, GL_SPECULAR, spec);
}

// Limitamos el area de pintado a farPaintingVal
void limitPaintArea(const int MSZ, const int farPaintingVal, float pX, float pY, int * linfX, int * linfY, int * lsupX, int * lsupY) {
  int x = lround(pX / MAZECELTOPIXEL);
  int y = lround(pY / MAZECELTOPIXEL);

  *linfX = x - farPaintingVal;
  *linfY = y - farPaintingVal;

  *lsupX = x + farPaintingVal;
  *lsupY = y + farPaintingVal;

  if (*linfX < 0) {
    *linfX = 0;
  }
  if (*linfY < 0) {
    *linfY = 0;
  }
  if (*lsupX > MSZ) {
    *lsupX = MSZ;
  }
  if (*lsupY > MSZ) {
    *lsupY = MSZ;
  }
}

inline void drawCell(const int xm, const int ym, const int prop, const GLdouble color[3], const GLdouble norm[3]) {
  int innX, innY;

  GLdouble v1 [] = {0, 0, -0.5};
  GLdouble v2 [] = {0, 0, -0.5};
  GLdouble v3 [] = {0, 0, -0.5};
  GLdouble v4 [] = {0, 0, -0.5};


  glPushMatrix();
  glTranslatef(xm, ym, 0);
  planeMaterial();
  // Dibujamos los subelementos que conforman la casilla
  for (innX = 0; innX < prop; innX++) {
    for (innY = 0; innY < prop; innY++) {
      v1[0] = (innX)*CELLSZ;
      v1[1] = (innY)*CELLSZ;

      v2[0] = (innX+1)*CELLSZ;
      v2[1] = (innY)*CELLSZ;

      v3[0] = (innX+1)*CELLSZ;
      v3[1] = (innY+1)*CELLSZ;

      v4[0] = (innX)*CELLSZ;
      v4[1] = (innY+1)*CELLSZ;

      drawSquaredTiriangle(norm, color, v1, v2, v3, v4);
    }
  }
  glPopMatrix();
}

inline void drawWall(int xm, int ym) {
  glPushMatrix();
  glTranslatef(xm ,ym ,0);
  drawCube(MAZECELTOPIXEL);
  glPopMatrix();
}

//Point-> x, y, z
float defAngle(float x1, float y1, float x2, float y2){
  float angleDg=atan2( x1-x2, y1-y2)*(180/PI); 
  return -angleDg;
}

inline void drawSO(SceneObject * o, float *player) {
  float look [] = {o->ux, o->uy, o->uz};
  float angle=defAngle(player[0], player[1], o->x, o->y);
  switch (o->type) {
    case pig:
      glTranslatef(o->x, o->y, o->z);
      glRotatef(angle, 0, 0, 1);
      drawPig((Pig *) o->data);
      glRotatef(-angle, 0, 0, 1);
      glTranslatef(-o->x, -o->y, -o->z);
      break;
    case human:
      glTranslatef(o->x, o->y, o->z);
      glRotatef(angle, 0, 0, 1);
      drawHuman((Human *) o->data);
      glRotatef(-angle, 0, 0, 1);
      glTranslatef(-o->x, -o->y, -o->z);
      break;
    default:
      //ENUM_LEN case, skip
      break;
  }
}

// Solo pintamos los limites parametrizados
void drawPlane(const int linfX, const int linfY, const int lsupX, const int lsupY) {
  int x;
  int y;
  const int prop = lround(MAZECELTOPIXEL / CELLSZ);
  const GLdouble norm [] = {0, 0, 1};

  GLdouble color [] = {1,1,1};

  planeMaterial();

  int xm;
  for (x = linfX; x < lsupX; x++) {
    xm = x * MAZECELTOPIXEL;
    for (y = linfY; y < lsupY; y++) {
      drawCell(xm, y * MAZECELTOPIXEL, prop, color, norm);
    }
  }
}

// Para poder pintar laberintos de tamaño ilimitado, restringimos el area de pintado del laberinto segun farPaintingVal(debe ser un entero)
void drawMaze (int ** maze, const int MSZ, const float pX, const float pY, const int farPaintingVal) {
  int x;
  int y;
  int lsupX;
  int lsupY;
  int cell;

  int linfX;
  int linfY;

  if (farPaintingVal > 0 && MSZ > farPaintingVal) {
    limitPaintArea(MSZ, farPaintingVal, pX, pY, &linfX, &linfY, &lsupX, &lsupY);
  } else {
    linfX = 0;
    linfY = 0;
    lsupX = MSZ;
    lsupY = MSZ;
  }

  glTranslatef(-CELLSZ, -CELLSZ, 0);
  drawPlane(linfX, linfY, lsupX, lsupY);
  glTranslatef(CELLSZ, CELLSZ, 0);

  int xm, ym;
  for (x = linfX; x < lsupX; x++) {
    xm = x * MAZECELTOPIXEL;
    for (y = linfY; y < lsupY; y++) {
      cell = maze[x][y];
      ym = y * MAZECELTOPIXEL;
      if (cell == WALL) {
        drawWall(xm, ym);
      }
    }
  }
}

void drawMazeAndScene (int ** maze, const int MSZ, SceneObject * objs, const int OSZ, int * mazeRepl, const int farPaintingVal, char paintPlayer) {

  const GLdouble norm [] = {0, 0, 1};
  const GLdouble color [] = {1, 1, 1};
  const int prop = lround(MAZECELTOPIXEL / CELLSZ);

  int linfX, linfY;
  int lsupX, lsupY;
  int x, y;
  int xm, ym;

  int cell;
  

  float plPos [] = {objs[0].x, objs[0].y, objs[0].z};
  float plSpeed [] = {objs[0].ux, objs[0].uy, objs[0].uz};
  
  float angle=defAngle(plPos[0], plPos[1], plSpeed[0], plSpeed[1]);
  if (paintPlayer) {
    glTranslatef(plPos[0], plPos[1], plPos[2]); 
    glRotatef(angle, 0, 0, 1);
    drawHuman((Human *) objs[0].data);
    glRotatef(-angle, 0, 0, 1);
    glTranslatef(-plPos[0], -plPos[1], -plPos[2]); 
  }

  if (farPaintingVal > 0 && MSZ > farPaintingVal) {
    limitPaintArea(MSZ, farPaintingVal, plPos[0], plPos[1], &linfX, &linfY, &lsupX, &lsupY);
  } else {
    linfX = 0;
    linfY = 0;
    lsupX = MSZ;
    lsupY = MSZ;
  }

  for (x = linfX; x < lsupX; x++) {
    xm = x * MAZECELTOPIXEL;
    for (y = linfY; y < lsupY; y++) {
      cell = maze[x][y];
      ym = y * MAZECELTOPIXEL;
      if (cell > WALL) {
        if( cell!=0){
        drawSO(&objs[cell], plPos);
        if (mazeRepl[cell] > WALL&&mazeRepl[cell]!=0) { // dos objetos en la misma celda
          drawSO(&objs[mazeRepl[cell]], plPos);
        }
        }
      } else if (cell == WALL){ // es un objeto
        drawWall(xm, ym);
      }
      glTranslatef(-CELLSZ, -CELLSZ, 0);
      drawCell(xm, ym, prop, color, norm);
      glTranslatef(CELLSZ, CELLSZ, 0);
    }
  }
}
