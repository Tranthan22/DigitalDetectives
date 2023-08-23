/*
 * uart.c
 *
 *  Created on: Jun 18, 2023
 *      Author: PhongPham
 */

#include "uart.h"

void uartInit(void){
  CMU_LFXOInit_TypeDef lfxoInit = CMU_LFXOINIT_DEFAULT;
  /* Select LFXO for the EUSART */
  CMU_LFXOInit(&lfxoInit);
  CMU_ClockSelectSet(cmuClock_EUSART0, cmuSelect_LFXO);

  CMU_ClockEnable(cmuClock_GPIO, true);
  CMU_ClockEnable(cmuClock_EUSART0, true);

  GPIO_PinModeSet(gpioPortA, 8, gpioModePushPull, 1); /*TX*/ /*F6*/
  GPIO_PinModeSet(gpioPortA, 4, gpioModeInput, 0); /*RX - 4*/ /*F3*/
  GPIO_PinModeSet(gpioPortB, 0, gpioModePushPull, 1);  /* VCOM */

  EUSART_UartInit_TypeDef init = EUSART_UART_INIT_DEFAULT_LF;
  init.baudrate = 9600;
  GPIO->EUSARTROUTE[0].TXROUTE = (gpioPortA << _GPIO_EUSART_TXROUTE_PORT_SHIFT)
      | (8 << _GPIO_EUSART_TXROUTE_PIN_SHIFT);
  GPIO->EUSARTROUTE[0].RXROUTE = (gpioPortA << _GPIO_EUSART_RXROUTE_PORT_SHIFT)
      | (4 << _GPIO_EUSART_RXROUTE_PIN_SHIFT);

  GPIO->EUSARTROUTE[0].ROUTEEN = GPIO_EUSART_ROUTEEN_RXPEN | GPIO_EUSART_ROUTEEN_TXPEN;

  EUSART_UartInitLf(EUSART0, &init);

  NVIC_ClearPendingIRQ(EUSART0_RX_IRQn);
  NVIC_EnableIRQ(EUSART0_RX_IRQn);
}

void EUSART0_RX_IRQHandler(void)
{
  interrupt = 0;
  char respornse = EUSART0->RXDATA;
  if (respornse == '1'){
  
  }
  else if(respornse == '0'){
   transmitData(dataTransmit, sizeof(dataTransmit)-1);
  }
  EUSART_IntClear(EUSART0, EUSART_IF_RXFL);
  EUSART_IntDisable(EUSART0, EUSART_IEN_RXFL);
}

void transmitData(char* dataArray, uint8_t length)
{
  for (uint8_t i = 0; i < length; i++)
  {
      EUSART_Tx(EUSART0, dataArray[i]);

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


void receiveData(char *buffer) {
    const int BUFLEN = 80;
    int i;

    for (i = 0; i < BUFLEN; ++i) buffer[i] = 0;

    i = 0;

    do {

        buffer[i] = EUSART_Rx(EUSART0);

        if (buffer[i] != '\r') {
            i++;
        } else {
            break;
        }
    } while (i < BUFLEN);
}
















