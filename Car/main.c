#include <avr/io.h>
#include <avr/interrupt.h>

#include <util/delay.h>

#include <stdlib.h>
#include <stdio.h>

#include "usart.h"

#define HEADLIGHTS PA0
#define REVERSELIGHTS PA1
#define LEFTSIGNAL PA2
#define RIGHTSIGNAL PA3
#define FRONTSIREN PA4
#define LEFTSIREN PA5
#define RIGHTSIREN PA6

#define TRACTIONA PB0
#define TRACTIONB PB1
#define TRACTIONPWM PB3
#define STEERINGPWM PB7
#define STEERINGA PB5
#define STEERINGB PB6

#define MINSPEED 30
#define MAXSPEED 100

#define GO_FORWARD 'W'
#define GO_BACKWARD 'S'
#define STOP_TRACTION 'Z'
#define GO_LEFT 'A'
#define GO_RIGHT 'D'
#define STOP_STEERING 'X'
#define GO_FASTER 'F'
#define GO_SLOWER 'V'
#define START_POLICELIGHTS 'P'
#define STOP_POLICELIGHTS 'L'
#define START_HEADLIGHTS 'H'
#define STOP_HEADLIGHTS 'N'
#define NO_COMMAND '\0'

typedef enum {
	FALSE = 0,
	TRUE = 1
} BOOL;

typedef enum {
	NONE = 0,
	FORWARD = 1,
	REVERSE = 2
} Traction;

typedef enum {
	FRONT = 0,
	LEFT = 1,
	RIGHT = 2
} Steering;

typedef struct {
	BOOL headlights;
	BOOL reverseLights;
	BOOL siren;
	BOOL oldSiren;
	BOOL leftSignal;
	BOOL rightSignal;
	
	Traction traction;
	Steering steering;
	int speed;
} CarState;

void initLights()
{
	DDRA |= (1 << HEADLIGHTS);
	DDRA |= (1 << REVERSELIGHTS);
	DDRA |= (1 << LEFTSIGNAL);
	DDRA |= (1 << RIGHTSIGNAL);
	DDRA |= (1 << FRONTSIREN);
	DDRA |= (1 << LEFTSIREN);
	DDRA |= (1 << RIGHTSIREN);
}

void initPwmTimer()
{
    TCCR0A |= (1 << WGM10) | (1 << COM0A1) | (1 << COM0B1);
	TCCR0B |= (1 << CS10) | (1 << CS12) | (1 << WGM12);

    OCR0A = 255 * MINSPEED / MAXSPEED;
}

void initMotors() {
	DDRB |= (1 << TRACTIONPWM);
	DDRB |= (1 << STEERINGPWM);
	
	DDRB |= (1 << TRACTIONA);
	DDRB |= (1 << TRACTIONB);
	DDRB |= (1 << STEERINGA);
	DDRB |= (1 << STEERINGB);
	
	PORTB &= ~(1 << TRACTIONA);
	PORTB &= ~(1 << TRACTIONB);
	PORTB &= ~(1 << STEERINGA);
	PORTB &= ~(1 << STEERINGB);
	
	PORTB |= (1 << STEERINGPWM);
}

void defaultCarState(CarState *carState) {
	carState->headlights = FALSE;
	carState->reverseLights = FALSE;
	carState->siren = FALSE;
	carState->oldSiren = FALSE;
	carState->leftSignal = FALSE;
	carState->rightSignal = FALSE;
	carState->steering = FRONT;
	carState->traction = NONE;
	carState->speed = MINSPEED;
}

void init(CarState *carState) {
	USART0_init();
	defaultCarState(carState);
	
	initLights();
	initMotors();
	initPwmTimer();
}

void updateLights(CarState *carState, uint32_t iteration) {
	if (carState->headlights) {
		PORTA |= (1 << HEADLIGHTS);
	} else {
		PORTA &= ~(1 << HEADLIGHTS);
	}
	
	if (carState->siren) {
		if (!carState->oldSiren) {
			PORTA |= (1 << LEFTSIREN);
			carState->oldSiren = TRUE;
		}
		
		if (iteration % 1500 == 0) {
			PORTA ^= (1 << FRONTSIREN);
			PORTA ^= (1 << LEFTSIREN);
			PORTA ^= (1 << RIGHTSIREN);
		}
	} else {
		PORTA &= ~(1 << LEFTSIREN);
		PORTA &= ~(1 << RIGHTSIREN);
		PORTA &= ~(1 << FRONTSIREN);
	}
	
	if (carState->leftSignal) {
		if (iteration % 5000 == 0) {
			PORTA ^= (1 << LEFTSIGNAL);
		}
	} else {
		PORTA &= ~(1 << LEFTSIGNAL);
	}
	
	if (carState->rightSignal) {
		if (iteration % 5000 == 0) {
			PORTA ^= (1 << RIGHTSIGNAL);
		}
	} else {
		PORTA &= ~(1 << RIGHTSIGNAL);
	}
	
	if (carState->reverseLights) {
		PORTA |= (1 << REVERSELIGHTS);
	} else {
		PORTA &= ~(1 << REVERSELIGHTS);
	}
}

void updateMovement(CarState *carState) {
	if (carState->traction == NONE) {
		PORTB &= ~(1 << TRACTIONA);
		PORTB &= ~(1 << TRACTIONB);
	} else if (carState->traction == FORWARD) {
		PORTB |= (1 << TRACTIONA);
		PORTB &= ~(1 << TRACTIONB);
	} else if (carState->traction == REVERSE) {
		PORTB &= ~(1 << TRACTIONA);
		PORTB |= (1 << TRACTIONB);
	}
	
	if (carState->steering == FRONT) {
		PORTB &= ~(1 << STEERINGA);
		PORTB &= ~(1 << STEERINGB);
	} else if (carState->steering == LEFT) {
		PORTB |= (1 << STEERINGA);
		PORTB &= ~(1 << STEERINGB);
	} else if (carState->steering == RIGHT) {
		PORTB &= ~(1 << STEERINGA);
		PORTB |= (1 << STEERINGB);
	}
	
	OCR0A = 255 * carState->speed / MAXSPEED;
}
 
void updateCarState(CarState *carState) {
	char command = USART0_receive();
	if (command == GO_FORWARD) {
		carState->traction = FORWARD;
	}
	
	if (command == GO_BACKWARD) {
		carState->reverseLights = TRUE;
		carState->traction = REVERSE;
	}
	
	if (command == STOP_TRACTION) {
		carState->traction = NONE;
		carState->reverseLights = FALSE;
	}
	
	if (command == GO_LEFT) {
		carState->leftSignal = TRUE;
		carState->steering = LEFT;
	}
	
	if (command == GO_RIGHT) {
		carState->rightSignal = TRUE;
		carState->steering = RIGHT;
	}
	
	if (command == STOP_STEERING) {
		carState->steering = NONE;
		carState->leftSignal = FALSE;
		carState->rightSignal = FALSE;
	}
	
	if (command == GO_FASTER) {
		carState->speed += 10;
		if (carState->speed > MAXSPEED) {
			carState->speed = MAXSPEED;
		}
	}
	
	if (command == GO_SLOWER) {
		carState->speed -= 10;
		if (carState->speed < MINSPEED) {
			carState->speed = MINSPEED;
		}
	}
	
	if (command == START_POLICELIGHTS) {
		carState->siren = TRUE;
	}
	
	if (command == STOP_POLICELIGHTS) {
		carState->siren = FALSE;
	}
	
	if (command == START_HEADLIGHTS) {
		carState->headlights = TRUE;
	}
	
	if (command == STOP_HEADLIGHTS) {
		carState->headlights = FALSE;
	}
	
	if (command == NO_COMMAND) {
		//defaultCarState(carState);
	}
}

int main(void)
{
	CarState carState;
	init(&carState);
	
    uint32_t iterations = 0;
		
	while(1)
    {
            iterations = (iterations + 1) % 65535;			
			updateCarState(&carState);
			updateLights(&carState, iterations);
			updateMovement(&carState);
    }

    return 0;
}

