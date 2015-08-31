#define MAZECELTOPIXEL 5

void initTextures();
void drawMazeAndScene (int ** maze, const int MSZ, SceneObject * objs, const int OSZ, int * mazeRepl, const int farPaintingVal, char paintPlayer);
void drawMaze (int ** maze, const int MSZ, const float pX, const float pY, const int farPaintingVal);
