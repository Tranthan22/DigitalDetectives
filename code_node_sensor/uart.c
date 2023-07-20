/*
 * uart.c
 *
 *  Created on: Jun 18, 2023
 *      Author: KarimPham
 */

#include "uart.h"

void uartInit(void){
  CMU_ClockEnable(cmuClock_GPIO, true);
  CMU_ClockEnable(cmuClock_USART0, true);

  GPIO_PinModeSet(gpioPortA, 8, gpioModePushPull, 0); /*TX*/ /*F6*/
  GPIO_PinModeSet(gpioPortA, 4, gpioModeInput, 0); /*RX - 4*/ /*F3*/
  GPIO_PinModeSet(gpioPortC, 6, gpioModePushPull, 0); /*M0*/ /*P33*/
  GPIO_PinModeSet(gpioPortC, 8, gpioModePushPull, 0); /*M1*/ /*P31*/
  GPIO_PinModeSet(gpioPortB, 0, gpioModePushPull, 1);  /* VCOM */

  USART_InitAsync_TypeDef uartinit = USART_INITASYNC_DEFAULT;
  uartinit.baudrate = 9600;

  GPIO->USARTROUTE[0].TXROUTE = (gpioPortA << _GPIO_USART_TXROUTE_PORT_SHIFT)
      | (8 << _GPIO_USART_TXROUTE_PIN_SHIFT);
  GPIO->USARTROUTE[0].RXROUTE = (gpioPortA << _GPIO_USART_RXROUTE_PORT_SHIFT)
      | (4 << _GPIO_USART_RXROUTE_PIN_SHIFT);
  GPIO->USARTROUTE[0].ROUTEEN = GPIO_USART_ROUTEEN_RXPEN | GPIO_USART_ROUTEEN_TXPEN;

  USART_InitAsync(USART0, &uartinit);

}


void transmitData(char* dataArray, uint8_t length)
{
  for (uint8_t i = 0; i < length; i++)
  {
    USART_Tx(USART0, dataArray[i]);

  }
}

void data(const char* data_sensor, char* Data) {
    for (uint8_t i = 0, j = 0; i < 20; i += 2, j++) {
        char asciiData[3] = { data_sensor[i], data_sensor[i + 1], '\0' };
        Data[j] = (char)atoi(asciiData);
    }
}

void insertStringAtBeginning(char* inputArray, char* outputArray, char* stringToInsert)
{
    strcpy(outputArray, stringToInsert);
    strcat(outputArray, inputArray);
}

void concatenateArrays(const char* a, const char* b, const char* c, const char* d, char* sum)
{
    strcpy(sum, a);
    strcat(sum, b);
    strcat(sum, c);
    strcat(sum, d);
}

void doubleToCharArray(double number, char* array, int arraySize) {
  int integerPart = (int)number;
  double decimalPart = number - integerPart;

  if (number < 1) {
    array[0] = '0';
    array[1] = '.';
    array += 2;
    arraySize -= 2;
    int index = 0;

    while (decimalPart > 0 && index < arraySize - 1) {
      decimalPart *= 10;
      int digit = (int)decimalPart;
      array[index] = '0' + digit;
      decimalPart -= digit;
      index++;
    }

    array[index] = '\0';
  } else {
    int index = 0;

    while (integerPart > 0 && index < 3) {
      array[index] = '0' + (integerPart % 10);
      integerPart /= 10;
      index++;
    }

    array[index] = '\0';
    int i = 0;
    int j = index - 1;

    while (i < j) {
      char temp = array[i];
      array[i] = array[j];
      array[j] = temp;
      i++;
      j--;
    }

    array[index] = '.';
    index++;
    int decimalIndex = index;

    while (decimalPart > 0 && index < arraySize - 1) {
      decimalPart *= 10;
      int digit = (int)decimalPart;
      array[index] = '0' + digit;
      decimalPart -= digit;
      index++;
    }

    array[index] = '\0';

    if (index - decimalIndex > arraySize - 1 - decimalIndex) {
      array[arraySize - 1] = '\0';
    } else {
      array[index] = '\0';
    }
  }
}


























