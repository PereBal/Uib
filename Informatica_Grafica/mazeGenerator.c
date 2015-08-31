#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <time.h>
#include <math.h>

#include "list.h"
#include "mazeGenerator.h"

#define DEBUG 0

typedef struct Cell {
  int i;
  int j;
} Cell;

List * l;

double dist(Cell * c, Cell * o) {
  int pi = c->i - o->i;
  int pj = c->j - o->j;
  pi *= pi; pj *= pj;
  return sqrt(pi+pj);
}

void printMaze(int ** maze, const int MSZ) {
  int i;
  int j;
  int c;
  for (i = 0; i<MSZ; i++) {
    for (j = 0; j<MSZ; j++) {
      c = maze[i][j];
      if(c > CELL) {
        if (c < 0) {
          printf("%c ",(48+c));
        } else if (c > 0) {
          printf("E ");
        } else {
          printf("P ");
        }
      } else {
        printf("  ");
      }
    }
    printf("\n");
  }
  printf("\n----------\n");
}

void addNeighbourgs(List * l, Cell * c, int ** maze, const int MSZ) {
  const int i = c->i;
  const int j = c->j;
  if (j > 2 && maze[i][j-2] == WALL) {
    L_append(&l, i, j-2);
    maze[i][j-1] = CELL;
    maze[i][j-2] = APPENDED;
  } else if (j == 2 && maze[i][j-2] == WALL) {
    maze[i][j-1] = CELL;
  }

  if (j < (MSZ-3) && maze[i][j+2] == WALL) {
    L_append(&l, i, j+2);
    maze[i][j+1] = CELL;
    maze[i][j+2] = APPENDED;
  } else if(j == (MSZ-3) && maze[i][j+2] == WALL){
    maze[i][j+1] = CELL;
  }

  if (i > 2 && maze[i-2][j] == WALL) {
    L_append(&l, i-2, j);
    maze[i-1][j] = CELL;
    maze[i-2][j] = APPENDED;
  } else if (i == 2 && maze[i-2][j] == WALL) {
    maze[i-1][j] = CELL;
  }

  if (i < (MSZ-3) && maze[i+2][j] == WALL) {
    L_append(&l, i+2, j);
    maze[i+1][j] = CELL;
    maze[i+2][j] = APPENDED;
  } else if(i == (MSZ-3) && maze[i+2][j] == WALL){
    maze[i+1][j] = CELL;
  }
}


void generateMaze(int ** maze, const int MSZ, int * sci, int * scj) {
  int i,j;
  int sp;

  Cell cell;
  Cell scell;

  L_create(&l, MSZ * MSZ);

  // cols
  for (i = 0; i < MSZ; i++) {
    // rows
    for (j = 0; j < MSZ; j++) {
      maze[i][j] = WALL;
    }
  }

  if (DEBUG) printMaze(maze, MSZ);

  srandom(time(NULL));

  // seleccionamos el lado de salida
  switch(sp=(random() % 4)) {
    case NORTH: //north
      cell.i = 1;
      cell.j = random() % (MSZ-2);
      if (cell.j <= 1) cell.j = 2;
      break;
    case SOUTH: // south
      cell.i = MSZ - 2;
      cell.j = random() % (MSZ-2);
      if (cell.j <= 1) cell.j = 2;
      break;
    case EAST: // east
      cell.i = random() % (MSZ-2);
      cell.j = MSZ - 2;
      if (cell.i <= 1) cell.i = 2;
      break;
    case WEST: // west
      cell.i = random() % (MSZ-2);
      cell.j = 1;
      if (cell.i <= 0) cell.i = 2;
      break;
  }
  scell = cell;

  if (DEBUG) {
    printf("sx:%d,sy:%d\n", scell.i, scell.j);
    printMaze(maze, MSZ);
  }

  *sci = scell.i; *scj = scell.j;

  maze[scell.i][scell.j] = SCELL;

  if (DEBUG) printMaze(maze, MSZ);

  addNeighbourgs(l, &cell, maze, MSZ);

  if (DEBUG) printMaze(maze, MSZ);

  while (!L_isEmpty(l)) {
    L_chooseRandom(&l, &cell.i, &cell.j);
    if (maze[cell.i][cell.j] == APPENDED) {
      maze[cell.i][cell.j] = CELL;
      addNeighbourgs(l, &cell, maze, MSZ);
      if (DEBUG) printMaze(maze, MSZ);
    }
    L_remove(&l, cell.i, cell.j);
  }

  // seleccionamos la salida del laberinto
  double dM = 0;
  double d;
  Cell ecell;
  switch(sp) {
    case NORTH:
      i = MSZ - 1;
      cell.i = i;
      ecell.i = i;
      for (j=0; j<MSZ; j++) {
        cell.j = j;
        d = dist(&scell, &cell);
        if (d > dM && maze[i-1][j] == CELL) {
          ecell.j = j;
          dM = d;
        }
      }
      break;
    case SOUTH:
      i = 0;
      cell.i = i;
      ecell.i = i;
      for (j=0; j<MSZ; j++) {
        cell.j = j;
        d = dist(&scell, &cell);
        if (d > dM && maze[i+1][j] == CELL) {
          ecell.j = j;
          dM = d;
        }
      }
      break;
    case EAST:
      j = 0;
      cell.j = j;
      ecell.j = j;
      for (i=0; i<MSZ; i++) {
        cell.i = i;
        d = dist(&scell, &cell);
        if (d > dM && maze[i][j+1] == CELL) {
          ecell.i = i;
          dM = d;
        }
      }
      break;
    case WEST:
      j = MSZ - 1;
      cell.j = j;
      ecell.j = j;
      for (i=0; i<MSZ; i++) {
        cell.i = i;
        d = dist(&scell, &cell);
        if (d > dM && maze[i][j-1] == CELL) {
          ecell.i = i;
          dM = d;
        }
      }
      break;
  }

  maze[ecell.i][ecell.j] = ECELL;

  if (DEBUG) printMaze(maze, MSZ);

  L_destroy(l);
}

// per si fan un Ctrl+C enmig de la creacio del laberint
void removeMaze() {
  if (l != NULL) {
    L_destroy(l);
  }
}
