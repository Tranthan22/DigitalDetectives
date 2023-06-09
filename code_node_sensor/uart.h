/*
 * uart.h
 *
 *  Created on: Jun 18, 2023
 *      Author: KarimPham
 */

#ifndef UART_H_
#define UART_H_

#include "em_usart.h"
#include "em_cmu.h"
#include "em_gpio.h"
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>
#include <stdlib.h>

void uartInit(void);
void transmitData(char* dataArray, uint16_t length);
void doubleToCharArray(double number, char* array, int arraySize);
void insertStringAtBeginning(char* inputArray, char* outputArray, char* stringToInsert);
void concatenateArrays(const char* a, const char* b, const char* c, const char* d, char* sum);
void data(const char* data_sensor, char* Data);


#endif /* UART_H_ */
