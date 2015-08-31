#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <signal.h>
#include <time.h>
#include <math.h>
#include <GL/glut.h>
#include <GL/gl.h>
#include <GL/glu.h>
#include "mazeGenerator.h"
#include "sceneObjects.h"
#include "drawSceneObjects.h"

#define PI 3.14159265358979323846
#define PLAYER_ID 0
#define NUL_PAINTING_LIM 0
#define CLOSE_PAINTING_LIM 10
#define FAR_PAINTING_LIM 24

void *font = GLUT_BITMAP_TIMES_ROMAN_24;
void *afont = GLUT_BITMAP_TIMES_ROMAN_10;
char scoreMessage[13+10];
char timeMessage[14+10];
time_t tiempo;

// camara en primera persona, camara en segunda persona, camara cenital.
typedef enum cam {
  FPCAM, SPCAM, CCAM, DEAD, INIT, END
} Camera;

typedef enum dif {
  EASY = 1, NORMAL = 5, HARD = 10
} Dificultad;

typedef enum mazesz {
  VSMALL = 8, SMALL = 16, MEDIUM = 32, LARGE = 64
} MazeSize;

typedef enum trackspeed {
  SLOW = 60, AVERAGE = 40, FAST = 20
} TrackingSpeed;

const float farValue = 10 * MAZECELTOPIXEL;
const float nearValue = 0.1f;

int w_width = 500;
int w_height = 500;
// GODMODE == 1 -> ser etereo, aka no tener forma solida x)
char GODMODE = 0;
Dificultad dificultad = EASY; // dificultad del juego, Default 1
MazeSize newGameMSZ = VSMALL; // tamaño del laberinto, Default 8x8
MazeSize MSZ; // tamaño del laberinto, Default newGameMSZ
int OSZ; // numero de objetos (jugador incluido). MSZ / 2

// velocidades de persecucion de los objetos
TrackingSpeed newGameFS = SLOW;
TrackingSpeed F_SPEED;
int CF_SPEED;

// width / height
GLdouble ratio;

// ratio para la perspectiva ortografica
float lr;

// valores para los angulos de look
float angle = 0.0f, anglez = 0.0f;

// posicion de la camara
float cx, cy, cz;
float lcx, lcy;

// posicion del vector de "look"
float lx, ly, lz;

// variables auxiliares para emular los movimientos
float angleLook = 0.0f;
float angleLookz = 0.0f;
float distMove = 0;
float strafe;

// 1 para smooth, 0 para flat. Se cambia con la tecla F1
char shadeMode = 1;
Camera cam = INIT;

int ** maze;
SceneObject * so;
int * mazeRepl;

void playerDead() __attribute__((always_inline));
void mazeFinished() __attribute__((always_inline));
void switchTo2DMode() __attribute__((always_inline));
void switchTo3DMode() __attribute__((always_inline));
void set3DCamera() __attribute__((always_inline));
void set2DCamera() __attribute__((always_inline));
void movePers() __attribute__((always_inline));
void trackPlayer() __attribute__((always_inline));
void moveEnem(SceneObject * m_so, int m_i) __attribute__((always_inline));

void freeSceneObjects() {
  // free scene objects
  int i;
  if (so != NULL) {
    for (i = 0; i < OSZ; i++) {
      free(so[i].data);
    }
    free(so);
    so = NULL;
  }
}

void cleanAll() {
  int i;
  freeSceneObjects();

  if (maze != NULL) {
    for(i = 0; i < MSZ; i++) {
      if (maze[i] != NULL) {
        free(maze[i]);
      }
    }
    free(maze);
    maze = NULL;
  }

  if (mazeRepl != NULL) {
    free(mazeRepl);
    mazeRepl = NULL;
  }
}

// --------- Lights ------------

void initLight0(){
  GLfloat positionL0[] = { cx, cy, 1.0f, 1.0f };
  GLfloat spotDirectionL0[] = {lx, ly, lz};

  GLfloat ambientLight[] = { 0.7f, 0.7f, 0.7f, 1.0f };
  GLfloat diffuseLight[] = { 0.5f, 0.5f, 0.5, 1.0f };
  GLfloat specularLight[] = { 1.0f, 1.0f, 1.0f, 1.0f };

  glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight);
  glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuseLight);
  glLightfv(GL_LIGHT0, GL_SPECULAR, specularLight);
  glLightfv(GL_LIGHT0, GL_POSITION, positionL0);
  glLightfv(GL_LIGHT0, GL_SPOT_DIRECTION, spotDirectionL0);
  glLightf(GL_LIGHT0, GL_SPOT_CUTOFF, 25.0f);
  glLightf(GL_LIGHT0, GL_SPOT_EXPONENT,50.0f);
  glLightf(GL_LIGHT0, GL_CONSTANT_ATTENUATION, 1.0f);
  glLightf(GL_LIGHT0, GL_LINEAR_ATTENUATION, 0.02f);
  glLightf(GL_LIGHT0, GL_QUADRATIC_ATTENUATION, 0.01f);

}

void light0(){
  GLfloat positionL0[] = { cx, cy, 1.0f, 1.0f };
  GLfloat spotDirectionL0[] = {lx, ly, lz};
  glLightfv(GL_LIGHT0, GL_POSITION, positionL0);
  glLightfv(GL_LIGHT0, GL_SPOT_DIRECTION, spotDirectionL0);
}

void initLight1(){
  GLfloat positionL1[] = { cx, cy, 1.0f, 1.0f };
  GLfloat spotDirectionL1[] = {lx, ly, lz};

  GLfloat ambientLight[] = { 0.8f, 0.8f, 0.8f, 1.0f };
  GLfloat diffuseLight[] = { 0.8f, 0.8f, 0.8, 1.0f };
  GLfloat specularLight[] = { 0.5f, 0.5f, 0.5f, 1.0f };

  glLightfv(GL_LIGHT1, GL_AMBIENT, ambientLight);
  glLightfv(GL_LIGHT1, GL_DIFFUSE, diffuseLight);
  glLightfv(GL_LIGHT1, GL_SPECULAR, specularLight);
  glLightfv(GL_LIGHT1, GL_POSITION, positionL1);
  glLightfv(GL_LIGHT1, GL_SPOT_DIRECTION, spotDirectionL1);
  glLightf(GL_LIGHT1, GL_SPOT_CUTOFF, 35.0f);
  glLightf(GL_LIGHT1, GL_CONSTANT_ATTENUATION, 1.0f);
  glLightf(GL_LIGHT0, GL_LINEAR_ATTENUATION, 0.02f);
  glLightf(GL_LIGHT1, GL_QUADRATIC_ATTENUATION, 0.01f);

}

void light1(){
  GLfloat positionL1[] = { cx, cy, 1.0f, 1.0f };
  GLfloat spotDirectionL1[] = {lx, ly, -1};
  glLightfv(GL_LIGHT1, GL_POSITION, positionL1);
  glLightfv(GL_LIGHT1, GL_SPOT_DIRECTION, spotDirectionL1);
}

void initLights() {
  GLfloat ambient [] = {0.1f, 0.1f, 0.1f, 1.0};
  glLightModelfv(GL_LIGHT_MODEL_AMBIENT,ambient);
  glEnable(GL_COLOR_MATERIAL);
  glEnable(GL_LIGHTING);
  glEnable(GL_CULL_FACE);
  glCullFace(GL_BACK);
  initLight0();
  initLight1();
  glEnable(GL_LIGHT1);
  glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
  glShadeModel(GL_SMOOTH);
}

// -------------------------------


void output(int x, int y, char * font, char *string) {
  int len, i;

  glRasterPos2f(x, y);
  len = (int) strlen(string);
  for (i = 0; i < len; i++) {
    glutBitmapCharacter(font, string[i]);
  }
}

inline void set3DCamera() {
  glMatrixMode(GL_PROJECTION);

  glLoadIdentity();
  gluPerspective(45.0f, ratio, nearValue, farValue);
  gluLookAt(cx, cy, cz,
      cx + lx, cy + ly, 1 + lz,
      0.0f, 0.0f, 1.0f);

  glMatrixMode(GL_MODELVIEW);
}

inline void set2DCamera() {
  glMatrixMode(GL_PROJECTION);

  glLoadIdentity();
  gluOrtho2D(0, w_width, w_height, 0);

  glMatrixMode(GL_MODELVIEW);
}

inline void switchTo2DMode() {
  glDisable(GL_LIGHTING);
  glDisable(GL_NORMALIZE);
  glDisable(GL_DEPTH_TEST);

  w_width = glutGet(GLUT_WINDOW_WIDTH);
  w_height = glutGet(GLUT_WINDOW_HEIGHT);

  set2DCamera();
}

inline void switchTo3DMode() {
  glEnable(GL_LIGHTING);
  glEnable(GL_NORMALIZE);


  glEnable(GL_DEPTH_TEST);

  glClearDepth(1);

  w_width = glutGet(GLUT_WINDOW_WIDTH);
  w_height = glutGet(GLUT_WINDOW_HEIGHT);

  initLights();
  light0();
  set3DCamera();
}

inline void mazeFinished() {
  time_t t = time(NULL);
  tiempo = t - tiempo;
  sprintf(timeMessage, "YOUR TIME: %lds", tiempo);
  sprintf(scoreMessage, "YOUR SCORE: %ld", 100000/tiempo);
  cam = END;
  switchTo2DMode();
}

inline void playerDead() {
  cam = DEAD;
  switchTo2DMode();
}

void closeHit(SceneObject * m_so) {
  if (GODMODE) {
    return;
  }
  float x = m_so->x - cx;
  float y = m_so->y - cy;

  x *= x; y *= y;

  float r;
  switch(m_so->type) {
    case pig:
      r = ((Pig *) m_so->data)->size / 1.8;
      break;
    case human:
      r = ((Human *) m_so->data)->size / 1.8;
      break;
    default:
      // ENUM_LEN case, skip
      break;
  }
  float r1;

  // el factor de escalado es un valor experimental dependiente
  // del tamaño del jugador (2.0f)
  r1 = ((Human *) so[PLAYER_ID].data)->size / 6.5;
  if (sqrt(x + y) < (r + r1)) {
    playerDead();
  }
}

char colision(float x, float y, int objID) {
  int ix = lround(x / MAZECELTOPIXEL);
  int iy = lround(y / MAZECELTOPIXEL);
  int cell;

  cell = maze[ix][iy];

  if (objID == PLAYER_ID) {

    if (cell == ECELL) {
      mazeFinished();
    } else if(cell == WALL) {
      return WALL;
    } else if (cell > PLAYER_ID) {
      closeHit(&so[cell]);
    } else if (mazeRepl[PLAYER_ID] > WALL) {
      closeHit(&so[mazeRepl[PLAYER_ID]]);
    }
    return 0;

  } else {

    if (cell == ECELL) {
      return WALL;
    } else if (cell == WALL) {
      return WALL;
    } else if (cell == PLAYER_ID) {
      closeHit(&so[objID]);
    } else if (mazeRepl[objID] == PLAYER_ID) {
      closeHit(&so[objID]);
    }
    return 0;
  }
}

// intentamos interseccionar con el jugador.
inline void trackPlayer() {
  int px = lround(cx / MAZECELTOPIXEL);
  int py = lround(cy / MAZECELTOPIXEL);
  float ux, uy;
  float norm;
  SceneObject * s;

  if (maze[px][py] > PLAYER_ID) {
    s = &so[maze[px][py]];
    ux = cx - s->x;
    uy = cy - s->y;
    norm = sqrt(ux*ux + uy*uy);
    s->ux = ux / norm / CF_SPEED;
    s->uy = uy / norm / CF_SPEED;
  }
  if (px > 1 && maze[px-1][py] > PLAYER_ID) {
    s = &so[maze[px-1][py]];
    ux = cx - s->x;
    uy = cy - s->y;
    norm = sqrt(ux*ux + uy*uy);
    s->ux = ux / norm / F_SPEED;
    s->uy = uy / norm / F_SPEED;
  }
  if (py > 1 && maze[px][py-1] > PLAYER_ID) {
    s = &so[maze[px][py-1]];
    ux = cx - s->x;
    uy = cy - s->y;
    norm = sqrt(ux*ux + uy*uy);
    s->ux = ux / norm / F_SPEED;
    s->uy = uy / norm / F_SPEED;
  }
  if (px < MSZ-1 && maze[px+1][py] > PLAYER_ID) {
    s = &so[maze[px+1][py]];
    ux = cx - s->x;
    uy = cy - s->y;
    norm = sqrt(ux*ux + uy*uy);
    s->ux = ux / norm / F_SPEED;
    s->uy = uy / norm / F_SPEED;
  }
  if (py < MSZ-1 && maze[px][py+1] > PLAYER_ID) {
    s = &so[maze[px][py+1]];
    ux = cx - s->x;
    uy = cy - s->y;
    norm = sqrt(ux*ux + uy*uy);
    s->ux = ux / norm / F_SPEED;
    s->uy = uy / norm / F_SPEED;
  }
  if (mazeRepl[PLAYER_ID] > WALL) {
    s = &so[mazeRepl[PLAYER_ID]];
    ux = cx - s->x;
    uy = cy - s->y;
    norm = sqrt(ux*ux + uy*uy);
    s->ux = ux / norm / CF_SPEED;
    s->uy = uy / norm / CF_SPEED;
  }
}

inline void movePers() {
  float nx, ny;

  if(distMove) {
    nx = cx + distMove * lx;
    ny = cy + distMove * ly;

    if (!colision(nx, ny, PLAYER_ID)) {
      // nobody is allowed to leave!
      if (nx > 0 && nx < MSZ * MAZECELTOPIXEL && ny > 0 && ny < MSZ * MAZECELTOPIXEL) {
        cx = nx;
        cy = ny;

        so[PLAYER_ID].x = nx;
        so[PLAYER_ID].y = ny;
        so[PLAYER_ID].ux = nx+lx;
        so[PLAYER_ID].uy = ny+ly;

      }
    }
    distMove = 0;
  }

  if(strafe) {
    float a=-(1.0f*(ly)),
          b=(1.0f*(lx)),
    nx = cx+strafe*a;
    ny = cy+strafe*b;

    if (!colision(nx, ny, PLAYER_ID)) {
      if (nx > 0 && nx < MSZ * MAZECELTOPIXEL && ny > 0 && ny < MSZ * MAZECELTOPIXEL) {
        cx = nx;
        cy = ny;

        so[PLAYER_ID].x = nx;
        so[PLAYER_ID].y = ny;
        so[PLAYER_ID].ux = nx+lx;
        so[PLAYER_ID].uy = ny+ly;

      }
    }
    strafe = 0;
  }

  //Mirar hacia al lado
  if(angleLook) {
    angle += angleLook;
    lx = +cos(angle); ly = -sin(angle);
    so[PLAYER_ID].ux = nx+lx;
    so[PLAYER_ID].uy = ny+ly;
    angleLook = 0;
  }
  //Mirar hacia arriba
  if(angleLookz) {
    anglez += angleLookz;
    lz = sin(anglez);
    angleLookz = 0;
  }
}

inline void moveEnem(SceneObject * m_so, int m_i) {
  float ax = m_so->x + m_so->ux;
  float ay = m_so->y + m_so->uy;
  int m_oid;

  if (ax <= 0 || ay <= 0 || ax >= MSZ * MAZECELTOPIXEL || ay >= MSZ * MAZECELTOPIXEL) {
    // sin esto, al rebotar en la pared los objetos
    // serian inocuos
    colision(m_so->x, m_so->y, m_i);

    m_so->x = m_so->x - m_so->ux;
    m_so->y = m_so->y - m_so->uy;
    m_so->ux = -m_so->ux;
    m_so->uy = -m_so->uy;

  } else {
    if ((m_oid = colision(ax, ay, m_i))) {
      if (m_oid == WALL) {
        m_so->x = m_so->x - m_so->ux;
        m_so->y = m_so->y - m_so->uy;
        m_so->ux = -m_so->ux;
        m_so->uy = -m_so->uy;
      } else {
        m_so->x = ax;
        m_so->y = ay;
      }
    } else {
      m_so->x = ax;
      m_so->y = ay;
    }
  }
}

void move() {
  int i, j;
  int cell;
  int ix, iy;

  movePers();

  trackPlayer();

  for (i = 1; i < MSZ - 1; i++) {
    for (j = 1; j < MSZ - 1; j++) {
      cell = maze[i][j];
      if (cell == PLAYER_ID) {
        if(mazeRepl[PLAYER_ID] > PLAYER_ID) {
          moveEnem(&so[mazeRepl[PLAYER_ID]], mazeRepl[PLAYER_ID]);
        }
        ix = lround(so[cell].x / MAZECELTOPIXEL);
        iy = lround(so[cell].y / MAZECELTOPIXEL);
        if ((i != ix && i > 0 && i < MSZ) || (j != iy && j > 0 && j < MSZ)) {
          maze[i][j] = mazeRepl[cell];
          mazeRepl[cell] = maze[ix][iy];
          maze[ix][iy] = cell;
        }
      } else if (cell > PLAYER_ID) {
        moveEnem(&so[cell], cell);
        if (mazeRepl[cell] > PLAYER_ID) {
          moveEnem(&so[mazeRepl[cell]], mazeRepl[cell]);
        }
        ix = lround(so[cell].x / MAZECELTOPIXEL);
        iy = lround(so[cell].y / MAZECELTOPIXEL);
        if ((i != ix && i > 0 && i < MSZ) || (j != iy && j > 0 && j < MSZ)) {
          maze[i][j] = mazeRepl[cell];
          mazeRepl[cell] = maze[ix][iy];
          maze[ix][iy] = cell;
        }
      }
    }
  }
}

void displayInitMessage() {
  glColor3f(1, 0, 0);
  float t_width = 12 * 26; // size(font) / 2 * strlen(WELCOME...)
  float t_height = 12;
  output(w_width / 2.2 - t_width / 2, w_height / 2 - t_height / 2, font, "WELCOME AND PREPARE TO DIE");
  t_width = 5 * 48; // size(afont) / 2 + strlen(PRESS...)
  t_height -= 48;
  output(w_width / 2.2 - t_width / 2, w_height / 2 - t_height / 2, afont, "PRESS THE RIGHT MOUSE BUTTON TO DISPLAY THE MENU");
}

void displayDeadMessage() {
  glColor3f(1, 0, 0);
  float t_width = 12 * 12;
  float t_height = 12;
  output(w_width / 2.1 - t_width / 2, w_height / 2 - t_height / 2, font, "YOU ARE DEAD");
}

void displayEndMessage() {
  glColor3f(1, 0, 0);
  float t_width = 12 * 16;
  float t_height = 12;
  output(w_width / 2.2 - t_width / 2, w_height / 2 - t_height / 2, font, "CONGRATULATIONS!");
  t_width = 12 * 24;
  t_height -= 60;
  output(w_width / 2.2 - t_width / 2, w_height / 2 - t_height / 2, font, "YOU HAVE ESCAPED FROM US");
  t_width = 5 * strlen(timeMessage);
  t_height -= 48;
  output(w_width / 2.1 - t_width / 2, w_height / 2 - t_height / 2, afont, timeMessage);
  t_width = 5 * strlen(scoreMessage);
  t_height -= 48;
  output(w_width / 2.1 - t_width / 2, w_height / 2 - t_height / 2, afont, scoreMessage);
}

void displayFunc() {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  switch(cam) {
    case INIT:
      displayInitMessage();
      break;
    case END:
      displayEndMessage();
      break;
    case DEAD:
      displayDeadMessage();
      break;
    case FPCAM:
      light0();
      set3DCamera();

      drawMazeAndScene(maze, MSZ, so, OSZ, mazeRepl, FAR_PAINTING_LIM, 0);
      break;
    case SPCAM:
      light1();
      set3DCamera();

      drawMazeAndScene(maze, MSZ, so, OSZ, mazeRepl, CLOSE_PAINTING_LIM, 1);
      break;
    case CCAM:
      drawMaze(maze, MSZ, cx, cy, NUL_PAINTING_LIM);
      break;
  }
  glutSwapBuffers();
}

void idleFunc ()
{
  if (cam == FPCAM || cam == SPCAM) {
    move();
  }
  glutPostRedisplay();
}

void reshapeFunc(int w, int h)
{
  switch(MSZ) {
    case VSMALL:
      lr = 45;
      break;
    case SMALL:
      lr = 65;
      break;
    case MEDIUM:
      lr = 105;
      break;
    case LARGE:
      lr = 185;
      break;
  }
  if(h == 0)
    h = 1;

  w_width = w;
  w_height = h;

  ratio = (float) w / h;

  glViewport(0, 0, w, h);

  switch(cam) {
    case FPCAM:
    case SPCAM:
      if (cam == FPCAM) cz = 1;
      else cz = 20;
      set3DCamera();
      break;
    case CCAM:
      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      if(ratio>= 1) {
        glOrtho  (-lr * ratio, lr * ratio, -lr, lr, nearValue, farValue);
      }else{
        glOrtho  (-lr, lr, -lr / ratio, lr / ratio, nearValue, farValue);
      }

      gluLookAt(MSZ*MAZECELTOPIXEL*0.5, MSZ*MAZECELTOPIXEL*0.5, farValue-10,
                MSZ*MAZECELTOPIXEL*0.5, MSZ*MAZECELTOPIXEL*0.5, 1,
                0, 1, 0);

      glMatrixMode(GL_MODELVIEW);
      break;
    case INIT:
    case DEAD:
    case END:
      set2DCamera();
      break;
  }

}


void specialFunc(int key, int x, int y) {
  float fraction = 0.05f;

  switch(key) {
    case GLUT_KEY_UP:
      //El angulo por arriba es limitado
      if(anglez < PI / 2)
        angleLookz = fraction;
      break;
    case GLUT_KEY_DOWN:
      //El angulo por abajo es limitado
      if(anglez > -PI / 2)
        angleLookz = -fraction;
      break;
    case GLUT_KEY_LEFT:
      angleLook = -0.1f;
      break;
    case GLUT_KEY_RIGHT:
      angleLook = 0.1f;
      break;
  }
}

void keyboardFunc(unsigned char key, int x, int y) {
  GLfloat ambient [] = {0.025f, 0.025f, 0.025f, 1.0};
    switch(key) {
    case '1':
      glLightModelfv(GL_LIGHT_MODEL_AMBIENT,ambient);
      glEnable(GL_LIGHT0);
      glDisable(GL_LIGHT1);
      cam = FPCAM;
      reshapeFunc(glutGet(GLUT_WINDOW_WIDTH), glutGet(GLUT_WINDOW_HEIGHT));
      break;
    
    case '2':
      ambient[0]=0.1f;
      ambient[1]=0.1f;
      ambient[2]=0.1f;
      glLightModelfv(GL_LIGHT_MODEL_AMBIENT,ambient);
      glEnable(GL_LIGHT1);
      glDisable(GL_LIGHT0);
      cam = SPCAM;
      reshapeFunc(glutGet(GLUT_WINDOW_WIDTH), glutGet(GLUT_WINDOW_HEIGHT));
      break;
    
    case '3':
      cam = CCAM;
      glDisable(GL_LIGHT0);
      glDisable(GL_LIGHT1);
      ambient[0]=0.5f;
      ambient[1]=0.5f;
      ambient[2]=0.5f;
      glLightModelfv(GL_LIGHT_MODEL_AMBIENT,ambient);
      reshapeFunc(glutGet(GLUT_WINDOW_WIDTH), glutGet(GLUT_WINDOW_HEIGHT));
      break;
    
    case 27: // esc key -> goto initial screen
        cam = INIT;
        switchTo2DMode();
        break;
    
    case 'w':
    case 'W':
        if (cam == FPCAM || cam == SPCAM) 
          distMove = 1.0f;
        break;
    
     case 'a':
     case 'A':
        if (cam == FPCAM || cam == SPCAM) 
          strafe += 1.0f;
     
        break;
    case 's':
    case 'S':
        if (cam == FPCAM || cam == SPCAM) 
          distMove = -1.0f;
        break;
    
    case 'd':
    case 'D':
        if (cam == FPCAM || cam == SPCAM) 
          strafe -= 1.0f;
        break;
    
     case 'g':
     case 'G':
        GODMODE = (GODMODE+1)%2;
        break;
     
     case 'm':
     case 'M':
        if(shadeMode == 1) {
          glShadeModel(GL_FLAT);
          shadeMode = 0;
        } else {
          glShadeModel(GL_SMOOTH);
          shadeMode = 1;
        }
      default:
        break;
    }
  }


void chooseLocation(int * x, int * y) {
  int ix = random() % (MSZ - 1);
  int iy = random() % (MSZ - 1);

  while (maze[ix][iy] != CELL) {
    if (iy-1 > 0 && maze[ix][iy-1] == CELL) {
      iy--;
    } else if (ix+1 < MSZ && maze[ix+1][iy] == CELL) {
      ix++;
    } else if (iy+1 < MSZ && maze[ix][iy+1] == CELL) {
      iy++;
    } else if(ix-1 > 0 && maze[ix-1][iy] == CELL) {
      ix--;
    } else {
      ix = random() % (MSZ - 1);
      iy = random() % (MSZ - 1);
    }
  }
  *x = ix; *y = iy;
}

void initSceneObjects(float ix, float iy, float iz) {
  int i = 0;
  ObjectT type;
  void * aux;
  Pig * pg = NULL;
  Human * h = NULL;
  GLfloat spec [3] = {1, 0, 0};
  float pos [] = {ix, iy, iz};
  float acc [] = {0, 0, 0}; // en el caso del jugador, este vector no tiene utilidad
  float r;
  int x = lround(ix / MAZECELTOPIXEL);
  int y = lround(iy / MAZECELTOPIXEL);

  mazeRepl = (int *) calloc(OSZ, sizeof(int));

  so = (SceneObject *) calloc(OSZ, sizeof(SceneObject));

  srandom(time(NULL)); // se usa en chooseLocation pero como se llamara muchas veces, es inutil hacerlo directamente alli
  initHuman(&h, 2.0);

  initSceneObject(&so[i], human, pos, acc, (void *) h);

  h = NULL;

  mazeRepl[i] = maze[x][y];
  maze[x][y] = i; // id del jugador

  // inicializamos los demas objetos
  for (i = 1; i < OSZ; i++) {
    type = random() % ENUM_LEN;
    chooseLocation(&x, &y);
    pos[0] = x * MAZECELTOPIXEL; pos[1] = y * MAZECELTOPIXEL;
    r = (random() % 9 + 2) / 10.0;
    switch(type) {
      case pig:
        initPig(&pg, 3*r);
        aux = (void *) pg;
        pg = NULL;
        break;
      case human:
        r = 0.6;
        initHuman(&h, 3*r);
        aux = (void *) h;
        h = NULL;
        break;
      default:
        //ENUM_LEN case, skip
        break;
    }
    pos[2]=-(r/2);
    initSceneObject(&so[i], type, pos, acc, aux);

    mazeRepl[i] = maze[x][y];
    maze[x][y] = i; // id del objeto
  }
  printMaze(maze, MSZ);
}

void initMaze() {
  int sci;
  int scj;
  int mzit;

  maze = (int **) calloc(MSZ, sizeof(int *));
  for (mzit = 0; mzit < MSZ; mzit++) {
    maze[mzit] =  (int *) calloc(MSZ, sizeof(int));
  }

  generateMaze(maze,  MSZ, &sci, &scj);

  cz = 20; lz = 0; // arrancamos en 2Persona
  angle = 0; lx = 1; ly = 0;
  cx = sci * MAZECELTOPIXEL; cy = scj * MAZECELTOPIXEL;
}

void changeDificulty(int dif) {
  switch (dif) {
    case 1:
      dificultad = EASY;
      newGameFS = SLOW;
      break;
    case 2:
      dificultad = NORMAL;
      newGameFS = AVERAGE;
      break;
    case 3:
      dificultad = HARD;
      newGameFS = FAST;
    break;
  }
}

void changeMazeSize(int msz) {
  switch (msz) {
    case 1:
      newGameMSZ = VSMALL;
      break;
    case 2:
      newGameMSZ = SMALL;
      break;
    case 3:
      newGameMSZ = MEDIUM;
      break;
    case 4:
      newGameMSZ = LARGE;
      break;
  }
}

void newGame() {

  cleanAll();

  MSZ = newGameMSZ;
  if (MSZ < MEDIUM) {
    OSZ = MSZ / 2;
  } else if (MSZ == MEDIUM) {
    OSZ = MSZ * 2;
  } else {
    OSZ = MSZ * 4;
  }

  F_SPEED = newGameFS;
  CF_SPEED = F_SPEED / 2;

  initMaze();
  initSceneObjects(cx, cy, 0.5);

  switchTo3DMode();

  lcx = cx; lcy = cy;

  cam = SPCAM;

  tiempo = time(NULL);
}

// de moment al arrancar apareix amb una pantalla negra, que desapareix un cop s'ha fet el primer moviment, don't know why
int main  (int argc, char **argv)
{
  int difficulty_submenu, size_submenu;
  signal(SIGTERM, cleanAll);
  signal(SIGINT, cleanAll);

  glutInit(&argc, argv);

  glutInitWindowPosition(-1, -1);
  glutInitWindowSize(w_width, w_height);

  glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH );
  glEnable(GL_MULTISAMPLE);
  glEnable(GL_MULTISAMPLE_ARB);
  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  glEnable(GL_LINE_SMOOTH);
  glEnable(GL_POINT_SMOOTH);
  glEnable(GL_POLYGON_SMOOTH);

  glutCreateWindow("Maze Of Doom");

  initTextures();

  glutDisplayFunc(displayFunc);
  glutIdleFunc(idleFunc);
  glutReshapeFunc(reshapeFunc);
  glutKeyboardFunc(keyboardFunc);
  glutSpecialFunc(specialFunc);
  glutCloseFunc(cleanAll);

  glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

  difficulty_submenu = glutCreateMenu(changeDificulty);
  glutAddMenuEntry("Easy", 1);
  glutAddMenuEntry("Normal", 2);
  glutAddMenuEntry("Hard", 3);

  size_submenu = glutCreateMenu(changeMazeSize);
  glutAddMenuEntry("8x8", 1);
  glutAddMenuEntry("16x16", 2);
  glutAddMenuEntry("32x32", 3);
  glutAddMenuEntry("64x64", 4);

  glutCreateMenu(newGame);
  glutAddMenuEntry("New Game", 1);

  glutAddSubMenu("Set Enemy Level", difficulty_submenu);
  glutAddSubMenu("Set Maze Size", size_submenu);

  glutAttachMenu(GLUT_RIGHT_BUTTON);

  glutMainLoop();
  return 0;
}
