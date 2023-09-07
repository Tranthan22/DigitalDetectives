/*
 * uart.c
 *
 *  Created on: Jun 18, 2023
 *      Author: PhongPham
 */

#include "uart.h"

void gpioInit(void){
  CMU_ClockEnable(cmuClock_GPIO, true);
  GPIO_PinModeSet(BUTTON0_PORT, BUTTON0_PIN, gpioModeInput, 1);
  GPIO_PinModeSet(LED0_PORT, LED0_PIN, gpioModePushPull, 0);
  GPIO_PinModeSet(LED1_PORT, LED1_PIN, gpioModePushPull, 0);
}

void uartInit(void){

  /* Select LFXO for the EUSART0 */
  CMU_LFXOInit_TypeDef lfxoInit = CMU_LFXOINIT_DEFAULT;
  CMU_LFXOInit(&lfxoInit);
  CMU_ClockSelectSet(cmuClock_EUSART0, cmuSelect_LFXO);
  CMU_ClockEnable(cmuClock_EUSART0, true);

  GPIO_PinModeSet(TX_PORT, TX_PIN, gpioModePushPull, 1); /*F6*/
  GPIO_PinModeSet(RX_PORT, RX_PIN, gpioModeInput, 0);    /*F3*/
  GPIO_PinModeSet(VCOM_Enable_PORT, VCOM_Enable_PIN, gpioModePushPull, 1); /* VCOM enable*/

  EUSART_UartInit_TypeDef init = EUSART_UART_INIT_DEFAULT_LF;
  GPIO->EUSARTROUTE[0].TXROUTE = (TX_PORT << _GPIO_EUSART_TXROUTE_PORT_SHIFT)
      | (TX_PIN << _GPIO_EUSART_TXROUTE_PIN_SHIFT);
  GPIO->EUSARTROUTE[0].RXROUTE = (RX_PORT << _GPIO_EUSART_RXROUTE_PORT_SHIFT)
      | (RX_PIN << _GPIO_EUSART_RXROUTE_PIN_SHIFT);

  GPIO->EUSARTROUTE[0].ROUTEEN = GPIO_EUSART_ROUTEEN_RXPEN | GPIO_EUSART_ROUTEEN_TXPEN;

  EUSART_UartInitLf(EUSART0, &init);

  NVIC_ClearPendingIRQ(EUSART0_RX_IRQn);
  NVIC_EnableIRQ(EUSART0_RX_IRQn);

}

uint8_t j = 0;
void EUSART0_RX_IRQHandler(void)
{

  response[j] = EUSART0->RXDATA;
  if ((response[j] != 'E')){
      j++;
  }
  else {
      if (j == 1){
          if (response[0] == '1'){
              EUSART_IntDisable(EUSART0, EUSART_IEN_RXFL);
          }
          else if (response[0] == '0'){
              transmitData(dataTransmit, sizeof(dataTransmit));
              EUSART_IntDisable(EUSART0, EUSART_IEN_RXFL);
          }
          j = 0;
          interrupt = 0;
      }
      else j = 0;

  }
  EUSART_IntClear(EUSART0, EUSART_IF_RXFL);

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


















