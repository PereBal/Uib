#include <GL/gl.h>
#include <GL/glu.h>
#include <GL/glut.h>

#include "drawSpecialObjects.h"

float human_angles[]={0.0f, 30.0f, -30.0f};
float pig_angles[]={0.0f, 30.0f, 0.0f, -30.0f};

inline void movePigGIF(Pig * p) __attribute__((always_inline)); 
inline void moveHumanGIF(Human * h) __attribute__((always_inline));

inline void movePigGIF(Pig * p) {
  p->c = p->c + 1;
  if(p->c == 9){
    p->arm = (p->arm+1)%4;
    p->c = 0;
  }
}

void drawPig(Pig * p) {
  movePigGIF(p);
  GLfloat *pig_color=p->colores[0];
  GLfloat *pig_dark=p->colores[1];
  GLfloat *pig_decor=p->colores[2];
  
  
  
  float particle=p->size/16.0f;


  //Head
  glBegin(GL_QUADS);
  //eyes
  glColor3f(1.0f, 1.0f, 1.0f);
  glVertex3f(0.75f*particle,  2.0f*particle+0.0001f, 5.5f*particle);
  glVertex3f(0.75f*particle,  2.0f*particle+0.0001f, 6.0f*particle);
  glVertex3f( 1.75f*particle,  2.0f*particle+0.0001f, 6.0f*particle);
  glVertex3f( 1.75f*particle,  2.0f*particle+0.0001f, 5.5f*particle);

  glColor3f(0.0f, 0.0f, 0.0f);
  glVertex3f(1.0f*particle,  2.0f*particle+0.0002f, 5.5f*particle);
  glVertex3f(1.0f*particle,  2.0f*particle+0.0002f, 6.0f*particle);
  glVertex3f( 1.5f*particle,  2.0f*particle+0.0002f, 6.0f*particle);
  glVertex3f( 1.5f*particle,  2.0f*particle+0.0002f, 5.5f*particle);

  glColor3f(1.0f, 1.0f, 1.0f);
  glVertex3f(-1.75f*particle,  2.0f*particle+0.0001f, 5.5f*particle);
  glVertex3f(-1.75f*particle,  2.0f*particle+0.0001f, 6.0f*particle);
  glVertex3f(-0.75f*particle,  2.0f*particle+0.0001f, 6.0f*particle);
  glVertex3f(-0.75f*particle,  2.0f*particle+0.0001f, 5.5f*particle);

  glColor3f(0.0f, 0.0f, 0.0f);
  glVertex3f(-1.5f*particle,  2.0f*particle+0.0002f, 5.5f*particle);
  glVertex3f(-1.5f*particle,  2.0f*particle+0.0002f, 6.0f*particle);
  glVertex3f(-1.0f*particle,  2.0f*particle+0.0002f, 6.0f*particle);
  glVertex3f(-1.0f*particle,  2.0f*particle+0.0002f, 5.5f*particle);

  //Head
  glColor3f(pig_color[0], pig_color[1], pig_color[2]);
  glNormal3f(0,0,1);
  glVertex3f(-2.0f*particle, -2.0f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle, -2.0f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle,  2.0f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle,  2.0f*particle,  7.0f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(-2.0f*particle, -2.0f*particle, 3.0f*particle);
  glVertex3f(-2.0f*particle,  2.0f*particle, 3.0f*particle);
  glVertex3f( 2.0f*particle,  2.0f*particle, 3.0f*particle);
  glVertex3f( 2.0f*particle, -2.0f*particle, 3.0f*particle);

  glNormal3f(0,1,0);
  glVertex3f(-2.0f*particle,  2.0f*particle, 3.0f*particle);
  glVertex3f(-2.0f*particle,  2.0f*particle, 7.0f*particle);
  glVertex3f( 2.0f*particle,  2.0f*particle, 7.0f*particle);
  glVertex3f( 2.0f*particle,  2.0f*particle, 3.0f*particle);

  glNormal3f(0,-1,0);
  glVertex3f(-2.0f*particle, -2.0f*particle, 3.0f*particle);
  glVertex3f( 2.0f*particle, -2.0f*particle, 3.0f*particle);
  glVertex3f( 2.0f*particle, -2.0f*particle, 7.0f*particle);
  glVertex3f(-2.0f*particle, -2.0f*particle, 7.0f*particle);

  glNormal3f(1,0,0);
  glVertex3f( 2.0f*particle, -2.0f*particle, 3.0f*particle);
  glVertex3f( 2.0f*particle,  2.0f*particle, 3.0f*particle);
  glVertex3f( 2.0f*particle,  2.0f*particle, 7.0f*particle);
  glVertex3f( 2.0f*particle, -2.0f*particle, 7.0f*particle);

  glNormal3f(-1,0,0);
  glVertex3f(-2.0f*particle, -2.0f*particle, 3.0f*particle);
  glVertex3f(-2.0f*particle, -2.0f*particle, 7.0f*particle);
  glVertex3f(-2.0f*particle,  2.0f*particle, 7.0f*particle);
  glVertex3f(-2.0f*particle,  2.0f*particle, 3.0f*particle);
  glEnd();

  glBegin(GL_QUADS);

  glColor3f(pig_dark[0], pig_dark[1], pig_dark[2]);
  glNormal3f(0,1,0);
  glVertex3f(-1.25f*particle, 2.5f*particle+0.0001f, 3.95f*particle);
  glVertex3f(-1.25f*particle, 2.5f*particle+0.0001f, 4.55f*particle);
  glVertex3f(-0.5f*particle, 2.5f*particle+0.0001f, 4.55f*particle);
  glVertex3f(-0.5f*particle, 2.5f*particle+0.0001f, 3.95f*particle);

  glNormal3f(0,1,0);
  glVertex3f( 0.5f*particle, 2.5f*particle+0.0001f, 3.95f*particle);
  glVertex3f( 0.5f*particle, 2.5f*particle+0.0001f, 4.55f*particle);
  glVertex3f( 1.25f*particle, 2.5f*particle+0.0001f, 4.55f*particle);
  glVertex3f( 1.25f*particle, 2.5f*particle+0.0001f, 3.95f*particle);

  glColor3f(pig_color[0], pig_color[1], pig_color[2]);
  glNormal3f(0,0,1);
  glVertex3f(-1.25f*particle, 2.0f*particle,  5.0f*particle);
  glVertex3f( 1.25f*particle, 2.0f*particle,  5.0f*particle);
  glVertex3f( 1.25f*particle, 2.5f*particle,  5.0f*particle);
  glVertex3f(-1.25f*particle, 2.5f*particle,  5.0f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(-1.25f*particle, 2.0f*particle, 3.5f*particle);
  glVertex3f(-1.25f*particle, 2.5f*particle, 3.5f*particle);
  glVertex3f( 1.25f*particle, 2.5f*particle, 3.5f*particle);
  glVertex3f( 1.25f*particle, 2.0f*particle, 3.5f*particle);

  glNormal3f(0,1,0);
  glVertex3f(-1.25f*particle, 2.5f*particle, 3.5f*particle);
  glVertex3f(-1.25f*particle, 2.5f*particle, 5.0f*particle);
  glVertex3f( 1.25f*particle, 2.5f*particle, 5.0f*particle);
  glVertex3f( 1.25f*particle, 2.5f*particle, 3.5f*particle);

  glNormal3f(0,-1,0);
  glVertex3f(-1.25f*particle, 2.0f*particle, 3.5f*particle);
  glVertex3f( 1.25f*particle, 2.0f*particle, 3.5f*particle);
  glVertex3f( 1.25f*particle, 2.0f*particle, 5.0f*particle);
  glVertex3f(-1.25f*particle, 2.0f*particle, 5.0f*particle);

  glNormal3f(1,0,0);
  glVertex3f( 1.25f*particle, 2.0f*particle, 3.5f*particle);
  glVertex3f( 1.25f*particle, 2.5f*particle, 3.5f*particle);
  glVertex3f( 1.25f*particle, 2.5f*particle, 5.0f*particle);
  glVertex3f( 1.25f*particle, 2.0f*particle, 5.0f*particle);

  glNormal3f(-1,0,0);
  glVertex3f(-1.25f*particle, 2.0f*particle, 3.5f*particle);
  glVertex3f(-1.25f*particle, 2.0f*particle, 5.0f*particle);
  glVertex3f(-1.25f*particle, 2.5f*particle, 5.0f*particle);
  glVertex3f(-1.25f*particle, 2.5f*particle, 3.5f*particle);
  glEnd();



  //Body
  glBegin(GL_QUADS);
  glColor3f(pig_color[0], pig_color[1], pig_color[2]);
  glNormal3f(0,0,1);
  glVertex3f(-3.0f*particle, -10.0f*particle,  6.0f*particle);
  glVertex3f( 3.0f*particle, -10.0f*particle,  6.0f*particle);
  glVertex3f( 3.0f*particle, -1.0f*particle,  6.0f*particle);
  glVertex3f(-3.0f*particle, -1.0f*particle,  6.0f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(-3.0f*particle, -10.0f*particle, 2.0f*particle);
  glVertex3f(-3.0f*particle, -1.0f*particle, 2.0f*particle);
  glVertex3f( 3.0f*particle, -1.0f*particle, 2.0f*particle);
  glVertex3f( 3.0f*particle, -10.0f*particle, 2.0f*particle);

  glNormal3f(0,0,1);
  glVertex3f(-3.0f*particle, -1.0f*particle, 2.0f*particle);
  glVertex3f(-3.0f*particle, -1.0f*particle, 6.0f*particle);
  glVertex3f( 3.0f*particle, -1.0f*particle, 6.0f*particle);
  glVertex3f( 3.0f*particle, -1.0f*particle, 2.0f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(-3.0f*particle, -10.0f*particle, 2.0f*particle);
  glVertex3f( 3.0f*particle, -10.0f*particle, 2.0f*particle);
  glVertex3f( 3.0f*particle, -10.0f*particle, 6.0f*particle);
  glVertex3f(-3.0f*particle, -10.0f*particle, 6.0f*particle);

  glNormal3f(1,0,0);
  glVertex3f( 3.0f*particle, -10.0f*particle, 2.0f*particle);
  glVertex3f( 3.0f*particle, -1.0f*particle, 2.0f*particle);
  glVertex3f( 3.0f*particle, -1.0f*particle, 6.0f*particle);
  glVertex3f( 3.0f*particle, -10.0f*particle, 6.0f*particle);

  glNormal3f(-1,0,0);
  glVertex3f(-3.0f*particle, -10.0f*particle, 2.0f*particle);
  glVertex3f(-3.0f*particle, -10.0f*particle, 6.0f*particle);
  glVertex3f(-3.0f*particle, -1.0f*particle, 6.0f*particle);
  glVertex3f(-3.0f*particle, -1.0f*particle, 2.0f*particle);

  //Decorations
  glColor3f(pig_decor[0], pig_decor[1], pig_decor[2]);
  glNormal3f(-1,0,0);
  glVertex3f(-3.0f*particle-0.0001f, -8.0f*particle, 3.5f*particle);
  glVertex3f(-3.0f*particle-0.0001f, -8.0f*particle, 3.8f*particle);
  glVertex3f(-3.0f*particle-0.0001f, -3.0f*particle, 3.8f*particle);
  glVertex3f(-3.0f*particle-0.0001f, -3.0f*particle, 3.5f*particle);

  glNormal3f(-1,0,0);
  glVertex3f(-3.0f*particle-0.0001f, -8.0f*particle, 4.0f*particle);
  glVertex3f(-3.0f*particle-0.0001f, -8.0f*particle, 4.3f*particle);
  glVertex3f(-3.0f*particle-0.0001f, -3.0f*particle, 4.3f*particle);
  glVertex3f(-3.0f*particle-0.0001f, -3.0f*particle, 4.0f*particle);

  glNormal3f(-1,0,0);
  glVertex3f(-3.0f*particle-0.0001f, -8.0f*particle, 4.5f*particle);
  glVertex3f(-3.0f*particle-0.0001f, -8.0f*particle, 4.8f*particle);
  glVertex3f(-3.0f*particle-0.0001f, -3.0f*particle, 4.8f*particle);
  glVertex3f(-3.0f*particle-0.0001f, -3.0f*particle, 4.5f*particle);

  glColor3f(pig_decor[0], pig_decor[1], pig_decor[2]);
  glNormal3f(1,0,0);
  glVertex3f( 3.0f*particle+0.0001f, -8.0f*particle, 3.5f*particle);
  glVertex3f( 3.0f*particle+0.0001f, -3.0f*particle, 3.5f*particle);
  glVertex3f( 3.0f*particle+0.0001f, -3.0f*particle, 3.8f*particle);
  glVertex3f( 3.0f*particle+0.0001f, -8.0f*particle, 3.8f*particle);

  glNormal3f(1,0,0);
  glVertex3f( 3.0f*particle+0.0001f, -8.0f*particle, 4.0f*particle);
  glVertex3f( 3.0f*particle+0.0001f, -3.0f*particle, 4.0f*particle);
  glVertex3f( 3.0f*particle+0.0001f, -3.0f*particle, 4.3f*particle);
  glVertex3f( 3.0f*particle+0.0001f, -8.0f*particle, 4.3f*particle);

  glNormal3f(1,0,0);
  glVertex3f( 3.0f*particle+0.0001f, -8.0f*particle, 4.5f*particle);
  glVertex3f( 3.0f*particle+0.0001f, -3.0f*particle, 4.5f*particle);
  glVertex3f( 3.0f*particle+0.0001f, -3.0f*particle, 4.8f*particle);
  glVertex3f( 3.0f*particle+0.0001f, -8.0f*particle, 4.8f*particle);

  //Tail
  glColor3f(pig_decor[0], pig_decor[1], pig_decor[2]);
  glNormal3f(0,0,-1);
  glVertex3f(-0.2f*particle, -10.0f*particle-0.0001f, 3.0f*particle);
  glVertex3f( 0.0f*particle, -10.0f*particle-0.0001f, 3.0f*particle);
  glVertex3f( 0.0f*particle, -10.0f*particle-0.0001f, 3.2f*particle);
  glVertex3f(-0.2f*particle, -10.0f*particle-0.0001f, 3.2f*particle);

  glNormal3f(0,0,-1);
  glVertex3f( 0.0f*particle, -10.0f*particle-0.0001f, 3.0f*particle);
  glVertex3f( 0.2f*particle, -10.0f*particle-0.0001f, 3.0f*particle);
  glVertex3f( 0.2f*particle, -10.0f*particle-0.0001f, 3.2f*particle);
  glVertex3f( 0.0f*particle, -10.0f*particle-0.0001f, 3.2f*particle);

  glNormal3f(0,0,-1);
  glVertex3f( 0.2f*particle, -10.0f*particle-0.0001f, 3.2f*particle);
  glVertex3f( 0.4f*particle, -10.0f*particle-0.0001f, 3.2f*particle);
  glVertex3f( 0.4f*particle, -10.0f*particle-0.0001f, 3.4f*particle);
  glVertex3f( 0.2f*particle, -10.0f*particle-0.0001f, 3.4f*particle);

  glNormal3f(0,0,-1);
  glVertex3f( 0.4f*particle, -10.0f*particle-0.0001f, 3.4f*particle);
  glVertex3f( 0.6f*particle, -10.0f*particle-0.0001f, 3.4f*particle);
  glVertex3f( 0.6f*particle, -10.0f*particle-0.0001f, 3.6f*particle);
  glVertex3f( 0.4f*particle, -10.0f*particle-0.0001f, 3.6f*particle);

  glNormal3f(0,0,-1);
  glVertex3f( 0.6f*particle, -10.0f*particle-0.0001f, 3.6f*particle);
  glVertex3f( 0.8f*particle, -10.0f*particle-0.0001f, 3.6f*particle);
  glVertex3f( 0.8f*particle, -10.0f*particle-0.0001f, 3.8f*particle);
  glVertex3f( 0.6f*particle, -10.0f*particle-0.0001f, 3.8f*particle);

  glNormal3f(0,0,-1);
  glVertex3f( 0.6f*particle, -10.0f*particle-0.0001f, 3.8f*particle);
  glVertex3f( 0.8f*particle, -10.0f*particle-0.0001f, 3.8f*particle);
  glVertex3f( 0.8f*particle, -10.0f*particle-0.0001f, 4.0f*particle);
  glVertex3f( 0.6f*particle, -10.0f*particle-0.0001f, 4.0f*particle);

  glNormal3f(0,0,-1);
  glVertex3f( 0.4f*particle, -10.0f*particle-0.0001f, 4.0f*particle);
  glVertex3f( 0.6f*particle, -10.0f*particle-0.0001f, 4.0f*particle);
  glVertex3f( 0.6f*particle, -10.0f*particle-0.0001f, 4.2f*particle);
  glVertex3f( 0.4f*particle, -10.0f*particle-0.0001f, 4.2f*particle);

  glNormal3f(0,0,-1);
  glVertex3f( 0.2f*particle, -10.0f*particle-0.0001f, 4.2f*particle);
  glVertex3f( 0.4f*particle, -10.0f*particle-0.0001f, 4.2f*particle);
  glVertex3f( 0.4f*particle, -10.0f*particle-0.0001f, 4.4f*particle);
  glVertex3f( 0.2f*particle, -10.0f*particle-0.0001f, 4.4f*particle);

  glNormal3f(0,0,-1);
  glVertex3f( 0.0f*particle, -10.0f*particle-0.0001f, 4.2f*particle);
  glVertex3f( 0.2f*particle, -10.0f*particle-0.0001f, 4.2f*particle);
  glVertex3f( 0.2f*particle, -10.0f*particle-0.0001f, 4.4f*particle);
  glVertex3f( 0.0f*particle, -10.0f*particle-0.0001f, 4.4f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(-0.2f*particle, -10.0f*particle-0.0001f, 4.2f*particle);
  glVertex3f( 0.0f*particle, -10.0f*particle-0.0001f, 4.2f*particle);
  glVertex3f( 0.0f*particle, -10.0f*particle-0.0001f, 4.4f*particle);
  glVertex3f(-0.2f*particle, -10.0f*particle-0.0001f, 4.4f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(-0.4f*particle, -10.0f*particle-0.0001f, 4.2f*particle);
  glVertex3f(-0.2f*particle, -10.0f*particle-0.0001f, 4.2f*particle);
  glVertex3f(-0.2f*particle, -10.0f*particle-0.0001f, 4.4f*particle);
  glVertex3f(-0.4f*particle, -10.0f*particle-0.0001f, 4.4f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(-0.6f*particle, -10.0f*particle-0.0001f, 4.0f*particle);
  glVertex3f(-0.4f*particle, -10.0f*particle-0.0001f, 4.0f*particle);
  glVertex3f(-0.4f*particle, -10.0f*particle-0.0001f, 4.2f*particle);
  glVertex3f(-0.6f*particle, -10.0f*particle-0.0001f, 4.2f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(-0.6f*particle, -10.0f*particle-0.0001f, 3.8f*particle);
  glVertex3f(-0.4f*particle, -10.0f*particle-0.0001f, 3.8f*particle);
  glVertex3f(-0.4f*particle, -10.0f*particle-0.0001f, 4.0f*particle);
  glVertex3f(-0.6f*particle, -10.0f*particle-0.0001f, 4.0f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(-0.4f*particle, -10.0f*particle-0.0001f, 3.6f*particle);
  glVertex3f(-0.2f*particle, -10.0f*particle-0.0001f, 3.6f*particle);
  glVertex3f(-0.2f*particle, -10.0f*particle-0.0001f, 3.8f*particle);
  glVertex3f(-0.4f*particle, -10.0f*particle-0.0001f, 3.8f*particle);
  glEnd();



  //Right front leg
  glPushMatrix();
  glTranslatef(-3.0f*particle,-2.75f*particle, 2.0f*particle);
  glRotatef(pig_angles[p->arm],1,0,0);
  glTranslatef( 3.0f*particle, 2.75f*particle,-2.0f*particle);
  glBegin(GL_QUADS);
  //foot
  glColor3f(pig_dark[0], pig_dark[1], pig_dark[2]);
  glNormal3f(0,1,0);
  glVertex3f(1.25f*particle,-1.5f*particle+0.0001f,  0.0f*particle);
  glVertex3f(1.25f*particle,-1.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(1.75f*particle,-1.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(1.75f*particle,-1.5f*particle+0.0001f,  0.0f*particle);

  glVertex3f(2.25f*particle,-1.5f*particle+0.0001f,  0.0f*particle);
  glVertex3f(2.25f*particle,-1.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(2.75f*particle,-1.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(2.75f*particle,-1.5f*particle+0.0001f,  0.0f*particle);

  //leg
  glColor3f(pig_color[0], pig_color[1], pig_color[2]);
  glVertex3f(1.0f*particle, -3.5f*particle,  2.0f*particle);
  glVertex3f(3.0f*particle, -3.5f*particle,  2.0f*particle);
  glVertex3f(3.0f*particle, -1.5f*particle,  2.0f*particle);
  glVertex3f(1.0f*particle, -1.5f*particle,  2.0f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(1.0f*particle, -3.5f*particle, 0.0f*particle);
  glVertex3f(1.0f*particle, -1.5f*particle, 0.0f*particle);
  glVertex3f(3.0f*particle, -1.5f*particle, 0.0f*particle);
  glVertex3f(3.0f*particle, -3.5f*particle, 0.0f*particle);

  glNormal3f(0,1,0);
  glVertex3f(1.0f*particle,-1.5f*particle,  0.0f*particle);
  glVertex3f(1.0f*particle,-1.5f*particle,  2.0f*particle);
  glVertex3f(3.0f*particle,-1.5f*particle,  2.0f*particle);
  glVertex3f(3.0f*particle,-1.5f*particle,  0.0f*particle);

  glNormal3f(0,-1,0);
  glVertex3f(1.0f*particle, -3.5f*particle,  0.0f*particle);
  glVertex3f(3.0f*particle, -3.5f*particle,  0.0f*particle);
  glVertex3f(3.0f*particle, -3.5f*particle,  2.0f*particle);
  glVertex3f(1.0f*particle, -3.5f*particle,  2.0f*particle);

  glNormal3f(1,0,0);
  glVertex3f(3.0f*particle, -3.5f*particle,  0.0f*particle);
  glVertex3f(3.0f*particle, -1.5f*particle,  0.0f*particle);
  glVertex3f(3.0f*particle, -1.5f*particle,  2.0f*particle);
  glVertex3f(3.0f*particle, -3.5f*particle,  2.0f*particle);

  glNormal3f(-1,0,0);
  glVertex3f(1.0f*particle, -3.5f*particle,  0.0f*particle);
  glVertex3f(1.0f*particle, -3.5f*particle,  2.0f*particle);
  glVertex3f(1.0f*particle, -1.5f*particle,  2.0f*particle);
  glVertex3f(1.0f*particle, -1.5f*particle,  0.0f*particle);
  glEnd();

  glPopMatrix();

  //Left front leg
  glPushMatrix();
  glTranslatef( 3.0f*particle,-2.75f*particle, 2.0f*particle);
  glRotatef(pig_angles[(p->arm+2)%4],1,0,0);
  glTranslatef(-3.0f*particle, 2.75f*particle,-2.0f*particle);
  glBegin(GL_QUADS);
  //Foot
  glColor3f(pig_dark[0], pig_dark[1], pig_dark[2]);
  glNormal3f(0,1,0);
  glVertex3f(-1.75f*particle,-1.5f*particle+0.0001f,  0.0f*particle);
  glVertex3f(-1.75f*particle,-1.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(-1.25f*particle,-1.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(-1.25f*particle,-1.5f*particle+0.0001f,  0.0f*particle);

  glVertex3f(-2.75f*particle,-1.5f*particle+0.0001f,  0.0f*particle);
  glVertex3f(-2.75f*particle,-1.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(-2.25f*particle,-1.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(-2.25f*particle,-1.5f*particle+0.0001f,  0.0f*particle);

  //Leg
  glColor3f(pig_color[0], pig_color[1], pig_color[2]);
  glNormal3f(0,0,1);
  glVertex3f(-3.0f*particle, -3.5f*particle,  2.0f*particle);
  glVertex3f(-1.0f*particle, -3.5f*particle,  2.0f*particle);
  glVertex3f(-1.0f*particle, -1.5f*particle,  2.0f*particle);
  glVertex3f(-3.0f*particle, -1.5f*particle,  2.0f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(-3.0f*particle, -3.5f*particle, 0.0f*particle);
  glVertex3f(-3.0f*particle, -1.5f*particle, 0.0f*particle);
  glVertex3f(-1.0f*particle, -1.5f*particle, 0.0f*particle);
  glVertex3f(-1.0f*particle, -3.5f*particle, 0.0f*particle);

  glNormal3f(0,1,0);
  glVertex3f(-3.0f*particle,-1.5f*particle,  0.0f*particle);
  glVertex3f(-3.0f*particle,-1.5f*particle,  2.0f*particle);
  glVertex3f(-1.0f*particle,-1.5f*particle,  2.0f*particle);
  glVertex3f(-1.0f*particle,-1.5f*particle,  0.0f*particle);

  glNormal3f(0,-1,0);
  glVertex3f(-3.0f*particle, -3.5f*particle,  0.0f*particle);
  glVertex3f(-1.0f*particle, -3.5f*particle,  0.0f*particle);
  glVertex3f(-1.0f*particle, -3.5f*particle,  2.0f*particle);
  glVertex3f(-3.0f*particle, -3.5f*particle,  2.0f*particle);

  glNormal3f(1,0,0);
  glVertex3f(-1.0f*particle, -3.5f*particle,  0.0f*particle);
  glVertex3f(-1.0f*particle, -1.5f*particle,  0.0f*particle);
  glVertex3f(-1.0f*particle, -1.5f*particle,  2.0f*particle);
  glVertex3f(-1.0f*particle, -3.5f*particle,  2.0f*particle);

  glNormal3f(-1,0,0);
  glVertex3f(-3.0f*particle, -3.5f*particle,  0.0f*particle);
  glVertex3f(-3.0f*particle, -3.5f*particle,  2.0f*particle);
  glVertex3f(-3.0f*particle, -1.5f*particle,  2.0f*particle);
  glVertex3f(-3.0f*particle, -1.5f*particle,  0.0f*particle);
  glEnd();
  glPopMatrix();

  //Right back leg
  glPushMatrix();
  glPushMatrix();
  glTranslatef( 3.0f*particle,-9.75f*particle, 2.0f*particle);
  glRotatef(pig_angles[(p->arm+2)%4],1,0,0);
  glTranslatef(-3.0f*particle, 9.75f*particle,-2.0f*particle);
  glBegin(GL_QUADS);
  //foot
  glColor3f(pig_dark[0], pig_dark[1], pig_dark[2]);
  glNormal3f(0,1,0);
  glVertex3f(1.25f*particle,-8.5f*particle+0.0001f,  0.0f*particle);
  glVertex3f(1.25f*particle,-8.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(1.75f*particle,-8.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(1.75f*particle,-8.5f*particle+0.0001f,  0.0f*particle);

  glVertex3f(2.25f*particle,-8.5f*particle+0.0001f,  0.0f*particle);
  glVertex3f(2.25f*particle,-8.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(2.75f*particle,-8.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(2.75f*particle,-8.5f*particle+0.0001f,  0.0f*particle);

  //leg
  glColor3f(pig_color[0], pig_color[1], pig_color[2]);
  glNormal3f(0,0,1);
  glVertex3f(1.0f*particle, -10.5f*particle,  2.0f*particle);
  glVertex3f(3.0f*particle, -10.5f*particle,  2.0f*particle);
  glVertex3f(3.0f*particle, -8.5f*particle,  2.0f*particle);
  glVertex3f(1.0f*particle, -8.5f*particle,  2.0f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(1.0f*particle, -10.5f*particle, 0.0f*particle);
  glVertex3f(1.0f*particle, -8.5f*particle, 0.0f*particle);
  glVertex3f(3.0f*particle, -8.5f*particle, 0.0f*particle);
  glVertex3f(3.0f*particle, -10.5f*particle, 0.0f*particle);

  glNormal3f(0,1,0);
  glVertex3f(1.0f*particle,-8.5f*particle,  0.0f*particle);
  glVertex3f(1.0f*particle,-8.5f*particle,  2.0f*particle);
  glVertex3f(3.0f*particle,-8.5f*particle,  2.0f*particle);
  glVertex3f(3.0f*particle,-8.5f*particle,  0.0f*particle);

  glNormal3f(0,-1,0);
  glVertex3f(1.0f*particle, -10.5f*particle,  0.0f*particle);
  glVertex3f(3.0f*particle, -10.5f*particle,  0.0f*particle);
  glVertex3f(3.0f*particle, -10.5f*particle,  2.0f*particle);
  glVertex3f(1.0f*particle, -10.5f*particle,  2.0f*particle);

  glNormal3f(1,0,0);
  glVertex3f(3.0f*particle, -10.5f*particle,  0.0f*particle);
  glVertex3f(3.0f*particle, -8.5f*particle,  0.0f*particle);
  glVertex3f(3.0f*particle, -8.5f*particle,  2.0f*particle);
  glVertex3f(3.0f*particle, -10.5f*particle,  2.0f*particle);

  glNormal3f(-1,0,0);
  glVertex3f(1.0f*particle, -10.5f*particle,  0.0f*particle);
  glVertex3f(1.0f*particle, -10.5f*particle,  2.0f*particle);
  glVertex3f(1.0f*particle, -8.5f*particle,  2.0f*particle);
  glVertex3f(1.0f*particle, -8.5f*particle,  0.0f*particle);
  glEnd();
  glPopMatrix();

  //Left back leg
  glPushMatrix();
  glTranslatef(-3.0f*particle,-9.75f*particle, 2.0f*particle);
  glRotatef(pig_angles[p->arm],1,0,0);
  glTranslatef( 3.0f*particle, 9.75f*particle,-2.0f*particle);
  glBegin(GL_QUADS);
  //Foot
  glColor3f(pig_dark[0], pig_dark[1], pig_dark[2]);
  glNormal3f(0,1,0);
  glVertex3f(-1.75f*particle,-8.5f*particle+0.0001f,  0.0f*particle);
  glVertex3f(-1.75f*particle,-8.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(-1.25f*particle,-8.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(-1.25f*particle,-8.5f*particle+0.0001f,  0.0f*particle);

  glVertex3f(-2.75f*particle,-8.5f*particle+0.0001f,  0.0f*particle);
  glVertex3f(-2.75f*particle,-8.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(-2.25f*particle,-8.5f*particle+0.0001f,  0.5f*particle);
  glVertex3f(-2.25f*particle,-8.5f*particle+0.0001f,  0.0f*particle);

  //Leg
  glColor3f(pig_color[0], pig_color[1], pig_color[2]);
  glNormal3f(0,0,1);
  glVertex3f(-3.0f*particle, -10.5f*particle,  2.0f*particle);
  glVertex3f(-1.0f*particle, -10.5f*particle,  2.0f*particle);
  glVertex3f(-1.0f*particle, -8.5f*particle,  2.0f*particle);
  glVertex3f(-3.0f*particle, -8.5f*particle,  2.0f*particle);

  glNormal3f(0,0,-1);
  glVertex3f(-3.0f*particle, -10.5f*particle, 0.0f*particle);
  glVertex3f(-3.0f*particle, -8.5f*particle, 0.0f*particle);
  glVertex3f(-1.0f*particle, -8.5f*particle, 0.0f*particle);
  glVertex3f(-1.0f*particle, -10.5f*particle, 0.0f*particle);

  glNormal3f(0,1,0);
  glVertex3f(-3.0f*particle, -8.5f*particle,  0.0f*particle);
  glVertex3f(-3.0f*particle, -8.5f*particle,  2.0f*particle);
  glVertex3f(-1.0f*particle, -8.5f*particle,  2.0f*particle);
  glVertex3f(-1.0f*particle, -8.5f*particle,  0.0f*particle);

  glNormal3f(0,-1,0);
  glVertex3f(-3.0f*particle, -10.5f*particle,  0.0f*particle);
  glVertex3f(-1.0f*particle, -10.5f*particle,  0.0f*particle);
  glVertex3f(-1.0f*particle, -10.5f*particle,  2.0f*particle);
  glVertex3f(-3.0f*particle, -10.5f*particle,  2.0f*particle);

  glNormal3f(1,0,0);
  glVertex3f(-1.0f*particle, -10.5f*particle,  0.0f*particle);
  glVertex3f(-1.0f*particle, -8.5f*particle,  0.0f*particle);
  glVertex3f(-1.0f*particle, -8.5f*particle,  2.0f*particle);
  glVertex3f(-1.0f*particle, -10.5f*particle,  2.0f*particle);

  glNormal3f(-1,0,0);
  glVertex3f(-3.0f*particle, -10.5f*particle,  0.0f*particle);
  glVertex3f(-3.0f*particle, -10.5f*particle,  2.0f*particle);
  glVertex3f(-3.0f*particle, -8.5f*particle,  2.0f*particle);
  glVertex3f(-3.0f*particle, -8.5f*particle,  0.0f*particle);
  glEnd();
  glPopMatrix();
}

inline void moveHumanGIF(Human * h) {
  h->c = h->c + 1;
  if(h->c == 9){
    h->arm = (h->arm+1)%3;
    h->c = 0;
  }
}

void drawHuman(Human * h) {
  moveHumanGIF(h);
  float particle = h->size/16.0f;
 
  float *hair=h->colores[0];
  float *skin=h->colores[1];
  float *mouth=h->colores[2];
  float *body=h->colores[3];
  float	*pants=h->colores[4];
  float	*shoes=h->colores[5];
  
  //Head
  glBegin(GL_QUADS);
  glColor3f(hair[0], hair[1], hair[2]);
  glNormal3f(0,0,1);
  glVertex3f(-2.0f*particle, -2.0f*particle,  16.0f*particle);
  glVertex3f( 2.0f*particle, -2.0f*particle,  16.0f*particle);
  glVertex3f( 2.0f*particle,  2.0f*particle,  16.0f*particle);
  glVertex3f(-2.0f*particle,  2.0f*particle,  16.0f*particle);

  glColor3f(skin[0], skin[1], skin[2]);
  glNormal3f(0,0,-1);
  glVertex3f(-2.0f*particle, -2.0f*particle, 12.0f*particle);
  glVertex3f(-2.0f*particle,  2.0f*particle, 12.0f*particle);
  glVertex3f( 2.0f*particle,  2.0f*particle, 12.0f*particle);
  glVertex3f( 2.0f*particle, -2.0f*particle, 12.0f*particle);

  glNormal3f(0,0,1);
  glVertex3f(-2.0f*particle,  2.0f*particle, 12.0f*particle);
  glVertex3f(-2.0f*particle,  2.0f*particle, 16.0f*particle);
  glVertex3f( 2.0f*particle,  2.0f*particle, 16.0f*particle);
  glVertex3f( 2.0f*particle,  2.0f*particle, 12.0f*particle);

  glColor3f(hair[0], hair[1], hair[2]);
  glNormal3f(0,0,-1);
  glVertex3f(-2.0f*particle, -2.0f*particle, 12.0f*particle);
  glVertex3f( 2.0f*particle, -2.0f*particle, 12.0f*particle);
  glVertex3f( 2.0f*particle, -2.0f*particle, 16.0f*particle);
  glVertex3f(-2.0f*particle, -2.0f*particle, 16.0f*particle);

  glNormal3f(1,0,0);
  glVertex3f( 2.0f*particle, -2.0f*particle, 12.0f*particle);
  glVertex3f( 2.0f*particle,  2.0f*particle, 12.0f*particle);
  glVertex3f( 2.0f*particle,  2.0f*particle, 16.0f*particle);
  glVertex3f( 2.0f*particle, -2.0f*particle, 16.0f*particle);

  glNormal3f(-1,0,0);
  glVertex3f(-2.0f*particle, -2.0f*particle, 12.0f*particle);
  glVertex3f(-2.0f*particle, -2.0f*particle, 16.0f*particle);
  glVertex3f(-2.0f*particle,  2.0f*particle, 16.0f*particle);
  glVertex3f(-2.0f*particle,  2.0f*particle, 12.0f*particle);
  glEnd();

  //Face
  //Hair
  glBegin(GL_QUADS);
  glNormal3f(0,0,1);
  glVertex3f(-2.0f*particle,  2.01f*particle, 15.5f*particle);
  glVertex3f(-2.0f*particle,  2.01f*particle, 16.0f*particle);
  glVertex3f( 2.0f*particle,  2.01f*particle, 16.0f*particle);
  glVertex3f( 2.0f*particle,  2.01f*particle, 15.0f*particle);
  glEnd();

  //Eyes
  glBegin(GL_QUADS);
  glNormal3f(0,0,1);
  glColor3f(1.0f,1.0f,1.0f);
  glVertex3f(0.5f*particle,  2.01f*particle, 14.0f*particle);
  glVertex3f(0.5f*particle,  2.01f*particle, 14.5f*particle);
  glVertex3f(1.5f*particle,  2.01f*particle, 14.5f*particle);
  glVertex3f(1.5f*particle,  2.01f*particle, 14.0f*particle);

  glVertex3f(-1.5f*particle,  2.01f*particle, 14.0f*particle);
  glVertex3f(-1.5f*particle,  2.01f*particle, 14.5f*particle);
  glVertex3f(-0.5f*particle,  2.01f*particle, 14.5f*particle);
  glVertex3f(-0.5f*particle,  2.01f*particle, 14.0f*particle);

  glColor3f(0.0f,0.0f,0.0f);
  glVertex3f(0.65f*particle,  2.011f*particle, 14.1f*particle);
  glVertex3f(0.65f*particle,  2.011f*particle, 14.4f*particle);
  glVertex3f(1.05f*particle,  2.011f*particle, 14.4f*particle);
  glVertex3f(1.05f*particle,  2.011f*particle, 14.1f*particle);

  glVertex3f(-1.05f*particle,  2.011f*particle, 14.1f*particle);
  glVertex3f(-1.05f*particle,  2.011f*particle, 14.4f*particle);
  glVertex3f(-0.65f*particle,  2.011f*particle, 14.4f*particle);
  glVertex3f(-0.65f*particle,  2.011f*particle, 14.1f*particle);
  glEnd();


  //Mouth
  glColor3f(mouth[0], mouth[1], mouth[2]);
  glBegin(GL_QUADS);
  glVertex3f( -0.75f*particle,  2.01f*particle, 12.75f*particle);
  glVertex3f( -0.75f*particle,  2.01f*particle, 13.0f*particle);
  glVertex3f( 0.75f*particle,  2.01f*particle, 13.0f*particle);
  glVertex3f( 0.75f*particle,  2.01f*particle, 12.75f*particle);

  glVertex3f( 0.0f*particle,  2.01f*particle, 12.5f*particle);
  glVertex3f( 0.0f*particle,  2.01f*particle, 12.75f*particle);
  glVertex3f( 0.75f*particle,  2.01f*particle, 12.75f*particle);
  glVertex3f( 0.75f*particle,  2.01f*particle, 12.75f*particle);

  glVertex3f( -0.75f*particle,  2.01f*particle, 12.75f*particle);
  glVertex3f( -0.75f*particle,  2.01f*particle, 12.75f*particle);
  glVertex3f( 0.0f*particle,  2.01f*particle, 12.75f*particle);
  glVertex3f( 0.0f*particle,  2.01f*particle, 12.5f*particle);

  glEnd();

  //Body
  glBegin(GL_QUADS);
  glColor3f(body[0], body[1], body[2]);
  glNormal3f(0,0,1);
  glVertex3f(-2.0f*particle, -0.5f*particle,  12.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle,  12.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  12.0f*particle);

  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(0,0,-1);
  glVertex3f(-2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle,  7.0f*particle);

  glColor3f(body[0], body[1], body[2]);
  glNormal3f(0,1,0);
  glVertex3f(-2.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle,  7.0f*particle);

  glNormal3f(0,1,0);
  glColor3f(pants[0], pants[1], pants[2]);
  glVertex3f(-2.0f*particle,  1.0f*particle+0.001f,  7.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle+0.001f,  7.5f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle+0.001f,  7.5f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle+0.001f,  7.0f*particle);

  glColor3f(body[0], body[1], body[2]);
  glNormal3f(0,-1,0);
  glVertex3f(-2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle,  12.0f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle,  12.0f*particle);

  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(0,-1,0);
  glVertex3f(-2.0f*particle, -0.5f*particle-0.001f,  7.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle-0.001f,  7.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle-0.001f,  7.5f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle-0.001f,  7.5f*particle);

  glColor3f(body[0], body[1], body[2]);
  glNormal3f(1,0,0);
  glVertex3f( 2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle,  12.0f*particle);

  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(1,0,0);
  glVertex3f( 2.0f*particle+0.001f, -0.5f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle+0.001f,  1.0f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle+0.001f,  1.0f*particle,  7.5f*particle);
  glVertex3f( 2.0f*particle+0.001f, -0.5f*particle,  7.5f*particle);

  glColor3f(body[0], body[1], body[2]);
  glNormal3f(-1,0,0);
  glVertex3f(-2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle,  12.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  7.0f*particle);

  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(-1,0,0);
  glVertex3f(-2.0f*particle-0.001f, -0.5f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle-0.001f, -0.5f*particle,  7.5f*particle);
  glVertex3f(-2.0f*particle-0.001f,  1.0f*particle,  7.5f*particle);
  glVertex3f(-2.0f*particle-0.001f,  1.0f*particle,  7.0f*particle);

  glEnd();

  //Left Arm
  glPushMatrix();
  glTranslatef(2.75f*particle,0.25f*particle,11.25f*particle);
  glRotatef(human_angles[h->arm],1,0,0);
  glTranslatef(-2.75f*particle,-0.25f*particle,-11.25f*particle);
  glBegin(GL_QUADS);
  glColor3f(body[0], body[1], body[2]);
  glNormal3f(0,0,1);
  glVertex3f(2.0f*particle, -0.5f*particle,  12.0f*particle);
  glVertex3f(3.5f*particle, -0.5f*particle,  12.0f*particle);
  glVertex3f(3.5f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f(2.0f*particle,  1.0f*particle,  12.0f*particle);

  glColor3f(skin[0], skin[1], skin[2]);
  glNormal3f(0,0,-1);
  glVertex3f(2.0f*particle, -0.5f*particle, 7.0f*particle);
  glVertex3f(2.0f*particle,  1.0f*particle, 7.0f*particle);
  glVertex3f(3.5f*particle,  1.0f*particle, 7.0f*particle);
  glVertex3f(3.5f*particle, -0.5f*particle, 7.0f*particle);

  glColor3f(body[0], body[1], body[2]);
  glNormal3f(0,1,0);
  glVertex3f(2.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f(2.0f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f(3.5f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f(3.5f*particle,  1.0f*particle,  7.0f*particle);


  glColor3f(skin[0], skin[1], skin[2]);
  glNormal3f(0,1,0);
  glVertex3f(2.0f*particle,  1.0f*particle+0.001f,  7.0f*particle);
  glVertex3f(2.0f*particle,  1.0f*particle+0.001f,  10.5f*particle);
  glVertex3f(3.5f*particle,  1.0f*particle+0.001f,  10.5f*particle);
  glVertex3f(3.5f*particle,  1.0f*particle+0.001f,  7.0f*particle);

  glColor3f(body[0], body[1], body[2]);
  glNormal3f(0,-1,0);
  glVertex3f(2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(3.5f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(3.5f*particle, -0.5f*particle,  12.0f*particle);
  glVertex3f(2.0f*particle, -0.5f*particle,  12.0f*particle);

  glColor3f(skin[0], skin[1], skin[2]);
  glNormal3f(0,-1,0);
  glVertex3f(2.0f*particle, -0.5f*particle-0.001f,  7.0f*particle);
  glVertex3f(3.5f*particle, -0.5f*particle-0.001f,  7.0f*particle);
  glVertex3f(3.5f*particle, -0.5f*particle-0.001f,  10.5f*particle);
  glVertex3f(2.0f*particle, -0.5f*particle-0.001f,  10.5f*particle);

  glColor3f(body[0], body[1], body[2]);
  glNormal3f(1,0,0);
  glVertex3f(3.5f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(3.5f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f(3.5f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f(3.5f*particle, -0.5f*particle,  12.0f*particle);

  glColor3f(skin[0], skin[1], skin[2]);
  glNormal3f(1,0,0);
  glVertex3f(3.5f*particle+0.001f, -0.5f*particle, 7.0f*particle);
  glVertex3f(3.5f*particle+0.001f,  1.0f*particle, 7.0f*particle);
  glVertex3f(3.5f*particle+0.001f,  1.0f*particle, 10.5f*particle);
  glVertex3f(3.5f*particle+0.001f, -0.5f*particle, 10.5f*particle);

  glColor3f(body[0], body[1], body[2]);
  glNormal3f(-1,0,0);
  glVertex3f(2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(2.0f*particle, -0.5f*particle,  12.0f*particle);
  glVertex3f(2.0f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f(2.0f*particle,  1.0f*particle,  7.0f*particle);

  glColor3f(skin[0], skin[1], skin[2]);
  glNormal3f(-1,0,0);
  glVertex3f(2.0f*particle-0.001f, -0.5f*particle,  7.0f*particle);
  glVertex3f(2.0f*particle-0.001f, -0.5f*particle,  10.5f*particle);
  glVertex3f(2.0f*particle-0.001f,  1.0f*particle,  10.5f*particle);
  glVertex3f(2.0f*particle-0.001f,  1.0f*particle,  7.0f*particle);
  glEnd();

  glPopMatrix();

  //Right Arm
  glPushMatrix();
  glTranslatef(2.75f*particle,0.25f*particle,11.25f*particle);
  glRotatef(human_angles[(h->arm+1)%3],1,0,0);
  glTranslatef(-2.75f*particle,-0.25f*particle,-11.25f*particle);
  glBegin(GL_QUADS);
  glColor3f(body[0], body[1], body[2]);
  glNormal3f(0,0,1);
  glVertex3f(-3.5f*particle, -0.5f*particle,  12.0f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle,  12.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f(-3.5f*particle,  1.0f*particle,  12.0f*particle);

  glColor3f(skin[0], skin[1], skin[2]);
  glNormal3f(0,0,-1);
  glVertex3f(-3.5f*particle, -0.5f*particle, 7.0f*particle);
  glVertex3f(-3.5f*particle,  1.0f*particle, 7.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle, 7.0f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle, 7.0f*particle);

  glColor3f(body[0], body[1], body[2]);
  glNormal3f(0,1,0);
  glVertex3f(-3.5f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f(-3.5f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  7.0f*particle);


  glColor3f(skin[0], skin[1], skin[2]);
  glNormal3f(0,1,0);
  glVertex3f(-3.5f*particle,  1.0f*particle+0.001f,  7.0f*particle);
  glVertex3f(-3.5f*particle,  1.0f*particle+0.001f,  10.5f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle+0.001f,  10.5f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle+0.001f,  7.0f*particle);

  glColor3f(body[0], body[1], body[2]);
  glNormal3f(0,-1,0);
  glVertex3f(-3.5f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle,  12.0f*particle);
  glVertex3f(-3.5f*particle, -0.5f*particle,  12.0f*particle);

  glColor3f(skin[0], skin[1], skin[2]);
  glNormal3f(0,-1,0);
  glVertex3f(-3.5f*particle, -0.5f*particle-0.001f,  7.0f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle-0.001f,  7.0f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle-0.001f,  10.5f*particle);
  glVertex3f(-3.5f*particle, -0.5f*particle-0.001f,  10.5f*particle);

  glColor3f(body[0], body[1], body[2]);
  glNormal3f(1,0,0);
  glVertex3f(-2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle,  12.0f*particle);

  glColor3f(skin[0], skin[1], skin[2]);
  glNormal3f(1,0,0);
  glVertex3f(-2.0f*particle+0.001f, -0.5f*particle, 7.0f*particle);
  glVertex3f(-2.0f*particle+0.001f,  1.0f*particle, 7.0f*particle);
  glVertex3f(-2.0f*particle+0.001f,  1.0f*particle, 10.5f*particle);
  glVertex3f(-2.0f*particle+0.001f, -0.5f*particle, 10.5f*particle);

  glColor3f(body[0], body[1], body[2]);
  glNormal3f(-1,0,0);
  glVertex3f(-3.5f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(-3.5f*particle, -0.5f*particle,  12.0f*particle);
  glVertex3f(-3.5f*particle,  1.0f*particle,  12.0f*particle);
  glVertex3f(-3.5f*particle,  1.0f*particle,  7.0f*particle);

  glColor3f(skin[0], skin[1], skin[2]);
  glNormal3f(-1,0,0);
  glVertex3f(-3.5f*particle-0.001f, -0.5f*particle,  7.0f*particle);
  glVertex3f(-3.5f*particle-0.001f, -0.5f*particle,  10.5f*particle);
  glVertex3f(-3.5f*particle-0.001f,  1.0f*particle,  10.5f*particle);
  glVertex3f(-3.5f*particle-0.001f,  1.0f*particle,  7.0f*particle);
  glEnd();
  glPopMatrix();

  //Left Leg
  glPushMatrix();
  glTranslatef(2.75f*particle,0.25f*particle,6.5f*particle);
  glRotatef(human_angles[(h->arm+1)%3],1,0,0);
  glTranslatef(-2.75f*particle,-0.25f*particle,-6.5f*particle);
  glBegin(GL_QUADS);
  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(0,0,1);
  glVertex3f( 0.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f( 0.0f*particle,  1.0f*particle,  7.0f*particle);

  glColor3f(shoes[0], shoes[1], shoes[2]);
  glNormal3f(0,0,-1);
  glVertex3f( 0.0f*particle, -0.5f*particle,  0.0f*particle);
  glVertex3f( 0.0f*particle,  1.0f*particle,  0.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle,  0.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle,  0.0f*particle);

  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(0,1,0);
  glVertex3f( 0.0f* particle,  1.0f*particle,  0.0f*particle);
  glVertex3f( 0.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle,  0.0f*particle);

  glColor3f(shoes[0], shoes[1], shoes[2]);
  glNormal3f(0,1,0);
  glVertex3f( 0.0f*particle,  1.0f*particle+0.001f,  0.0f*particle);
  glVertex3f( 0.0f*particle,  1.0f*particle+0.001f,  1.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle+0.001f,  1.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle+0.001f,  0.0f*particle);

  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(0,-1,0);
  glVertex3f( 0.0f*particle, -0.5f*particle,  0.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle,  0.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f( 0.0f*particle, -0.5f*particle,  7.0f*particle);

  glColor3f(shoes[0], shoes[1], shoes[2]);
  glNormal3f(0,-1,0);
  glVertex3f( 0.0f*particle, -0.5f*particle-0.001f,  0.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle-0.001f,  0.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle-0.001f,  0.5f*particle);
  glVertex3f( 0.0f*particle, -0.5f*particle-0.001f,  0.5f*particle);

  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(1,0,0);
  glVertex3f( 2.0f*particle, -0.5f*particle,  0.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle,  0.0f*particle);
  glVertex3f( 2.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f( 2.0f*particle, -0.5f*particle,  7.0f*particle);

  glColor3f(shoes[0], shoes[1], shoes[2]);
  glNormal3f(1,0,0);
  glVertex3f( 2.0f*particle+0.001f, -0.5f*particle,  0.0f*particle);
  glVertex3f( 2.0f*particle+0.001f,  1.0f*particle,  0.0f*particle);
  glVertex3f( 2.0f*particle+0.001f,  1.0f*particle,  0.5f*particle);
  glVertex3f( 2.0f*particle+0.001f, -0.5f*particle,  0.5f*particle);

  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(-1,0,0);
  glVertex3f( 0.0f*particle, -0.5f*particle,  0.0f*particle);
  glVertex3f( 0.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f( 0.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f( 0.0f*particle,  1.0f*particle,  0.0f*particle);

  glColor3f(shoes[0], shoes[1], shoes[2]);
  glNormal3f(-1,0,0);
  glVertex3f( 0.0f*particle-0.001f, -0.5f*particle,  0.0f*particle);
  glVertex3f( 0.0f*particle-0.001f, -0.5f*particle,  0.5f*particle);
  glVertex3f( 0.0f*particle-0.001f,  1.0f*particle,  0.5f*particle);
  glVertex3f( 0.0f*particle-0.001f,  1.0f*particle,  0.0f*particle);

  glEnd();
  glPopMatrix();

  //Right Leg
  glPushMatrix();
  glTranslatef(2.75f*particle,0.25f*particle,6.5f*particle);
  glRotatef(human_angles[h->arm],1,0,0);
  glTranslatef(-2.75f*particle,-0.25f*particle,-6.5f*particle);
  glBegin(GL_QUADS);
  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(0,0,1);
  glVertex3f(-2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(0.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(0.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  7.0f*particle);

  glColor3f(shoes[0], shoes[1], shoes[2]);
  glNormal3f(0,0,-1);
  glVertex3f(-2.0f*particle, -0.5f*particle, -2.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle, -2.0f*particle);
  glVertex3f(0.0f*particle,  1.0f*particle, -2.0f*particle);
  glVertex3f(0.0f*particle, -0.5f*particle, -2.0f*particle);

  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(0,1,0);
  glVertex3f(-2.0f* particle,  1.0f*particle, -2.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f(0.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f(0.0f*particle,  1.0f*particle, -2.0f*particle);

  glColor3f(shoes[0], shoes[1], shoes[2]);
  glNormal3f(0,1,0);
  glVertex3f(-2.0f*particle,  1.0f*particle+0.001f, -2.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle+0.001f,  1.0f*particle);
  glVertex3f(0.0f*particle,  1.0f*particle+0.001f,  1.0f*particle);
  glVertex3f(0.0f*particle,  1.0f*particle+0.001f, -2.0f*particle);

  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(0,-1,0);
  glVertex3f(-2.0f*particle, -0.5f*particle, -2.0f*particle);
  glVertex3f(0.0f*particle, -0.5f*particle, -2.0f*particle);
  glVertex3f(0.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle,  7.0f*particle);

  glColor3f(shoes[0], shoes[1], shoes[2]);
  glNormal3f(0,-1,0);
  glVertex3f(-2.0f*particle, -0.5f*particle-0.001f, -2.0f*particle);
  glVertex3f(0.0f*particle, -0.5f*particle-0.001f, -2.0f*particle);
  glVertex3f(0.0f*particle, -0.5f*particle-0.001f,  0.5f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle-0.001f,  0.5f*particle);

  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(1,0,0);
  glVertex3f(0.0f*particle, -0.5f*particle, -2.0f*particle);
  glVertex3f(0.0f*particle,  1.0f*particle, -2.0f*particle);
  glVertex3f(0.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f(0.0f*particle, -0.5f*particle,  7.0f*particle);

  glColor3f(shoes[0], shoes[1], shoes[2]);
  glNormal3f(1,0,0);
  glVertex3f(0.0f*particle+0.001f, -0.5f*particle, -2.0f*particle);
  glVertex3f(0.0f*particle+0.001f,  1.0f*particle, -2.0f*particle);
  glVertex3f(0.0f*particle+0.001f,  1.0f*particle,  0.5f*particle);
  glVertex3f(0.0f*particle+0.001f, -0.5f*particle,  0.5f*particle);

  glColor3f(pants[0], pants[1], pants[2]);
  glNormal3f(-1,0,0);
  glVertex3f(-2.0f*particle, -0.5f*particle, -2.0f*particle);
  glVertex3f(-2.0f*particle, -0.5f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle,  7.0f*particle);
  glVertex3f(-2.0f*particle,  1.0f*particle, -2.0f*particle);

  glColor3f(shoes[0], shoes[1], shoes[2]);
  glNormal3f(-1,0,0);
  glVertex3f(-2.0f*particle-0.001f, -0.5f*particle, -2.0f*particle);
  glVertex3f(-2.0f*particle-0.001f, -0.5f*particle,  0.5f*particle);
  glVertex3f(-2.0f*particle-0.001f,  1.0f*particle,  0.5f*particle);
  glVertex3f(-2.0f*particle-0.001f,  1.0f*particle, -2.0f*particle);

  glEnd();
  glPopMatrix();
}
