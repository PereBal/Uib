CC=gcc
LFLAGS= -lGL -lGLU -lglut -lm ./SOIL/lib/libSOIL.a
CFLAGS= -O2 -I./SOIL/includes
LST=list.o
MG=mazeGenerator.o
SO=sceneObjects.o
DS=drawSceneObjects.o
DSP=drawSpecialObjects.o
E7=etapa7.o
OBJ=$(LST) $(MG) $(SO) $(DS) $(DSP) $(E7)
EXE=etapa7
SOILC=soil

all: soilc $(EXE)


$(EXE): $(OBJ)
	$(CC) $^ -o $@ $(LFLAGS)

soilc:
	$(MAKE) -C ./SOIL/

%.o: %.c
	$(CC) -c $(CFLAGS) $< -o $@

.PHONY: all clean soilc
clean:
	$(MAKE) -C ./SOIL/ clean
	rm -f $(OBJ) $(EXE)
