# SOIL makefile for linux (based on the AngelScript makefile)

LIB = libSOIL.a

SRCDIR = ./src
LIBDIR = ./lib
OBJDIR = ./obj

CXX = gcc
CXXFLAGS = -O2 -s
DELETER = rm -f

SRCNAMES = \
  image_helper.c \
  stb_image_aug.c  \
  image_DXT.c \
  SOIL.c \

OBJ = $(addprefix $(OBJDIR)/, $(notdir $(SRCNAMES:.c=.o)))
BIN = $(LIBDIR)/$(LIB)

all: $(BIN)

$(BIN): $(OBJ)
	ar r $(BIN) $(OBJ)
	ranlib $(BIN)

$(OBJDIR)/%.o: $(SRCDIR)/%.c
	$(CXX) $(CXXFLAGS) -o $@ -c $<

.PHONY: all clean
clean:
	$(DELETER) $(OBJ) $(BIN)

