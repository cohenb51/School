
#include <stdlib.h>
#include <stdio.h>
#include <mem.h>
#include "dataStore.h"

struct Car* cars; //pointer to an ordered array from LRU to MRU
int carsInRam = 0;
size_t size = 0;
int carsOnDisk;

struct Car* readFromDisk(int id) {
    struct Car* car= (struct Car*) malloc(sizeof(struct Car) * carsOnDisk);
    if(!car) {
        printf("Malloc is broken\n");

        return NULL;
    }
    FILE * file= fopen("cars1.txt", "r");
    while (fread(car, (sizeof(struct Car)), 1, file)) {
        if (car->uniqueId == id) {
            fclose(file);
            return car;

        }
    }
    fclose(file);

    return NULL;
}


int writeToDisk(struct Car car) {
    FILE *f = fopen("cars1.txt", "a");
    fseek(f, (carsOnDisk * sizeof(struct Car)), 0);
    fwrite(&car, sizeof(car), 1, f);
    fclose(f);
    return 0;
}


int getAmountOfUsedMemory() {
    return carsInRam * sizeof(struct Car);
}


struct Car* ReadLastCarFromDisk() {

    struct Car* car= (struct Car*) malloc(sizeof(struct Car) * carsOnDisk);
    if(!car) {
        printf("Malloc is broken\n");
        return NULL;
    }
    FILE * file= fopen("cars1.txt", "a+");
    fread(car, (sizeof(struct Car)), 1, file);
    deleteCarById(car->uniqueId); // I could just fill with 0's but this gets rid of it completly.
    fclose(file);
    return car;

}
int deleteFromMemory(int id) {
    struct Car *cars3 = getAllCarsOnDisk();
    for (int i = 0; i < carsOnDisk; i++) {
        struct Car *car = (&cars3[i]);
        if (car->uniqueId == id) {
            carsOnDisk--;
            for (int j = i; j <= carsOnDisk; j++) {
                cars3[j] = cars3[j + 1];
            }
            struct Car car;
            cars3[carsOnDisk] = car;
            break;
        }
    }
    FILE *f = fopen("cars1.txt", "w");
    for (int i = 0; i < getNumberOfCarsOnDisk(); i++) {
        fwrite(&cars3[i], sizeof(struct Car), 1, f);    }
    fclose(f);
    free(cars3);
    return 0;
}
int deleteCarById(int id) {

    for (int i = 0; i < carsInRam; i++) {

        struct Car car = cars[i];

        if (car.uniqueId == id) {
            int j;
            for (j = i; j <= carsInRam; j++) {
                cars[j] = cars[j + 1];
            }
            struct Car car;
            cars[carsInRam] = car;
            carsInRam--;
            if(carsOnDisk > 0) {
                struct Car* temp = ReadLastCarFromDisk();
                //cars[carsInRam++] = *temp; // puts in MRU spot.
                for(int k = j-1; k > 0; k--) {
                    cars[k] = cars[k-1];
                }
                cars[0] = *temp; //puts in LRu spot
                carsInRam++;
                //FINDME
            }
            return 0;
        }
    }
    deleteFromMemory(id);
    return 0;
}

struct Car* shiftAllCarsLeft(struct Car* toPut){
    struct Car toWrite = cars[0];
    int i =0;
    for(i = 0; i < carsInRam; i++) {
        cars[i] = cars[i+1];
    }
    if(toPut != NULL) {
        cars[carsInRam - 1] = *toPut;
    }

    writeToDisk(toWrite);

    carsOnDisk++;
    return &cars[carsInRam-1];

}

struct Car *getAllCarsOnDisk() {
    if(carsOnDisk <= 0) {
        return NULL;
    }

    struct Car* car= malloc(sizeof(struct Car) * carsOnDisk);
    if(!car) {
        printf("Malloc is broken\n");
        return NULL;
    }
    struct Car* cars2= (struct Car*) malloc(sizeof(struct Car) * carsOnDisk);
    if(!cars2) {
        printf("Malloc is broken\n");
        return NULL;
    }
    FILE *f = fopen("cars1.txt", "r");

    int i =0;

    while (fread(car, (sizeof(struct Car)), 1, f)) {
        cars2[i] = *car;
        i++;
    }
    fclose(f);

    return cars2;
}




int getNumberOfCarsOnDisk(){
    return carsOnDisk;
};

int getNumberOfCarsinMemory(){
    return carsInRam;
};

struct Car* getAllCarsInMemory() {
    if(carsInRam <= 0) {
        return NULL;
    }
    return cars;
}
int Room() {
    if (!size) return 0;

    return ((size / sizeof(struct Car)) > carsInRam);
}

struct Car* mygetCarByID(int id, int push, int add) {
    if(carsInRam == 0 && !Room() && add) {
        printf("No room in memory. Implicitly setting max memory to allow for 1 car %d", push);
        setMaxMemory(sizeof(struct Car));
    }

    for(int i =0; i < carsInRam; i++) {
        struct Car car = cars[i];
        if(car.uniqueId == id) {
            //ph2
            for(int j = i; j< carsInRam-1; j++) {
                cars[j] = cars[j+1];
            }
            cars[carsInRam-1] = car;
            return &cars[carsInRam-1];
        }

    }
    if(carsOnDisk > 0 && carsInRam >0) {
        struct Car* car = readFromDisk(id); // poh
        if(car!= NULL && push) {
            deleteCarById(id);
            car = shiftAllCarsLeft(car);
        }
        //printf("cars in ram%d\n", carsInRam);
        return car; // this will always be true since it's now the mru and we need to return the exact copy
    }
    return NULL;

}
int modifyCarById(int id, struct Car *myCar) {
    struct Car *car = getCarById(id); // this brings the car into memory, if not in there already
    if(car == NULL) {
        printf("CANT FIND CAR");
        return -64;
    }

    if(myCar->uniqueId != id) {
        printf("INVALID ID %d, \n", id);
        return -1;
    }
    for (int i = 0; i < carsInRam; i++) {
        struct Car *car = &(cars[i]);
        //printf("Car id in modify: %d\n", car->uniqueId);

        if (car->uniqueId == id) {
            strncpy(car->make, myCar->make, sizeof(car->make));
            car->make[sizeof(car->make) - 1] = '\0';
            strncpy(car->model, myCar->model, sizeof(car->model));
            car->model[sizeof(car->model) - 1] = '\0';
            car->year = myCar->year;
            car->price = myCar->price;
            car->uniqueId = myCar->uniqueId;
        }
    }
    return 0;

}




int addCar(char* make, char* model, short year, long price, int uniqueID) {

    struct Car* t = mygetCarByID(uniqueID, 0, 0);
    if(t != NULL) {
        printf("This car ID is not unique \n");
        return 0;
    }
    if(strlen(make) > 8 || strlen(model) > 8) {
        printf("char strings entered are too long\n");
        return -1;
    }

    struct Car car = *(struct Car*) malloc(sizeof(struct Car));


    strncpy(car.model, model, sizeof(car.model));
    car.model[sizeof(car.model) - 1] = '\0';

    strncpy(car.make, make, sizeof(car.make));
    car.model[sizeof(car.model) - 1] = '\0';
    car.year = year;
    car.price = price;
    car.uniqueId = uniqueID;
    if(Room()) {
        cars[carsInRam] = car;
        carsInRam++;
    }
    else {
        carsOnDisk++;
        writeToDisk(car);
        mygetCarByID(car.uniqueId,1,0); // kind of terrible design but works.
    }
    return 0;
}

int setMaxMemory(size_t bytes) {
    //int flag = 0;
    if(getAmountOfUsedMemory() > bytes) {
        //flag = 1;
        while (getAmountOfUsedMemory() > bytes) {
            shiftAllCarsLeft(NULL);
            carsInRam--;
        }
    }

    cars =  (struct Car*) realloc(cars, bytes + sizeof(struct Car));
    if(cars == NULL) {
        printf("Realloc() ruined the whole program\n");
        return -1;

    }
    size = bytes;



    /*   while(Room() && carsOnDisk > 0) {
           struct Car* temp = ReadLastCarFromDisk();
           cars[carsInRam++] = *temp; // Works but puts the cars in the MRU spot
       }*/
    // first we have to shift all cars right to free up the left side
    int numberToShift = 0;

    numberToShift = size/sizeof(struct Car) - carsInRam;
    if(numberToShift > carsOnDisk) {
        numberToShift = carsOnDisk;
    }
    //printf("number %d", numberToShift);
    for(int i=carsInRam; i>= 0; i--) {
        cars[i + numberToShift] = cars[i];
    }

    int placer = 0;
    for(int i = 0; i< numberToShift; i++) {
        struct Car* temp = ReadLastCarFromDisk();
        cars[placer++] = *temp;
        carsInRam++;
    }
    return 0;

}

struct Car* getCarById(int id) {
    return mygetCarByID(id, 1,1);
}

int getNumberOfCarsInMemory() {
    return carsInRam;
}

struct Car* getAllCarsOnMemory() {
    return cars;
}
