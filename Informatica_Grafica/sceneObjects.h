// el jugador debe ser siempre el penultimo elemento del enumerado (el ultimo es la longitud del mismo)
typedef enum objectT {
  pig, human, ENUM_LEN
} ObjectT;

// esto seria lo correcto en terminos de privacidad
// (la implementacion se haria en sceneObjects.c)

// pero usaremos esto para evitar el overhead de llamadas a funciones 
// innecesarias

// mas adelante introducire color para cada individuo?
typedef struct pig {
  // el tamaño del objeto
  float size;
  // un contador para calcular la posicion de los brazos/piernas
  int c;
  // la posicion de los brazos/piernas
  int arm;
  float colores[3][3];
} Pig;

// mas adelante introducire color para cada individuo?
typedef struct human {
  // el tamaño del objeto
  float size;
  // un contador para calcular la posicion de los brazos/piernas
  int c;
  // la posicion de los brazos/piernas
  int arm;
  float colores[6][3];
} Human;

typedef struct sceneobject {
  ObjectT type; // tipo x)
  float x, y, z; // posicion
  float ux, uy, uz; // aceleracion
  void * data; // otros datos del objeto
} SceneObject;

void initPig(Pig ** p, float size);
void initHuman(Human ** h, float size);
void initSceneObject(SceneObject * so, ObjectT type, float p [3], float u [3], void * data);

