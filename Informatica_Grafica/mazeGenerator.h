#define CELL -5
#define SCELL -4
#define ECELL -3
#define APPENDED -2
#define WALL -1

//#define CELLSZ 16

#define NORTH 0
#define SOUTH 1
#define EAST 2
#define WEST 3

void generateMaze(int ** maze, const int MSZ, int * sci, int * scj);
void removeMaze();
void printMaze(int ** maze, const int MSZ);
