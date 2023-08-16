/*
 * uart.c
 *
 *  Created on: Jun 18, 2023
 *      Author: PhongPham
 */

#include "uart.h"

void uartInit(void){
  CMU_ClockEnable(cmuClock_GPIO, true);
  CMU_ClockEnable(cmuClock_USART0, true);

  GPIO_PinModeSet(gpioPortA, 8, gpioModePushPull, 0); /*TX*/ /*F6*/
  GPIO_PinModeSet(gpioPortA, 9, gpioModeInput, 0); /*RX - 4*/ /*F3*/
  GPIO_PinModeSet(gpioPortB, 0, gpioModePushPull, 1);  /* VCOM */

  USART_InitAsync_TypeDef uartinit = USART_INITASYNC_DEFAULT;
  uartinit.baudrate = 115200;

  GPIO->USARTROUTE[0].TXROUTE = (gpioPortA << _GPIO_USART_TXROUTE_PORT_SHIFT)
      | (8 << _GPIO_USART_TXROUTE_PIN_SHIFT);
  GPIO->USARTROUTE[0].RXROUTE = (gpioPortA << _GPIO_USART_RXROUTE_PORT_SHIFT)
      | (9 << _GPIO_USART_RXROUTE_PIN_SHIFT);
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


void uint16ToCharArray(uint16_t number, char* array, int arraySize) {
    snprintf(array, arraySize, "%03u", number);
}

uint8_t calculateLrc(const char* array, int size) {
    uint8_t lrc = 0;

    for (int i = 0; i < size; i++) {
        lrc ^= array[i];
    }
    return lrc;
}



















