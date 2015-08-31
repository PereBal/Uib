#include <stdlib.h>
#include <time.h>
#include "sceneObjects.h"


void initPig(Pig ** p, float size) {
  if (*p == NULL) {
    *p = (Pig *) malloc(sizeof(Pig));
    (*p)->size = size;
    (*p)->c = 0;
    (*p)->arm = 0;
    int i,j;
    for(i=0; i<3;i++){
      for(j=0; j<3; j++){
        (*p)->colores[i][j]=(rand()%9999)/1000.0;  
      }
    }
  }
}

void initHuman(Human ** h, float size) {
  if (*h == NULL) {
    *h = (Human *) malloc(sizeof(Human));
    (*h)->size = size;
    (*h)->c = 0;
    (*h)->arm = 0;
    int i,j;
    for(i=0; i<6;i++){
      for(j=0; j<3; j++){
        (*h)->colores[i][j]=(rand()%999)/1000.0;  
      }
    }
  }
}

void initSceneObject(SceneObject * so, ObjectT type, float p [3], float u [3], void * data) {
  if (so != NULL) {
    so->type = type;
    so->x = p[0]; so->y = p[1]; so->z = p[2];
    so->ux = u[0]; so->uy = u[1]; so->uz = u[2];
    so->data = data;
  }
}
