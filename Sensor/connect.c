/*
 * connect.c
 *
 *  Created on: Jul 21, 2023
 *      Author: PhongPham
 *
 */

#include "connect.h"

uint8_t work = 0;

void gpioSetup(void){

  CMU_ClockEnable(cmuClock_GPIO, true);
  GPIO_PinModeSet(LED0_PORT, LED0_PIN, gpioModePushPull, 0);
  GPIO_PinModeSet(LED1_PORT, LED1_PIN, gpioModePushPull, 0);

  /*Configure Button PB0 as input and enable interrupt*/
  GPIO_PinModeSet(BUTTON0_PORT, BUTTON0_PIN, gpioModeInputPull, 1);
  GPIO_ExtIntConfig(BUTTON0_PORT,
                    BUTTON0_PIN,
                    BUTTON0_PIN,
                    false,
                    true,
                    true);

  /*Configure Button PB1 as input and enable interrupt*/
  GPIO_PinModeSet(BUTTON1_PORT, BUTTON1_PIN, gpioModeInputPull, 1);
  GPIO_ExtIntConfig(BUTTON1_PORT,
                    BUTTON1_PIN,
                    BUTTON1_PIN,
                    false,
                    true,
                    true);

  NVIC_ClearPendingIRQ(GPIO_ODD_IRQn);
  NVIC_EnableIRQ(GPIO_ODD_IRQn);
}


void GPIO_ODD_IRQHandler(void)
{

  uint32_t interruptMask = GPIO_IntGet();
  GPIO_IntClear(interruptMask);

  /*Check if button 0 was pressed --> Connect to Station*/
  if (interruptMask & (1 << BUTTON0_PIN))
  {
    LETIMER_Reset(LETIMER0);
    letimer0Init();
    if(work==1) {
        work = 0;
        GPIO_PinOutClear(LED1_PORT, LED1_PIN);
    }
    char dataToConnect[] = {0xFF, 0xFF, 0x17, '1', 0x01, 0x03, 'E'};
    transmitData(dataToConnect, sizeof(dataToConnect));
    EUSART_IntEnable(EUSART0, EUSART_IEN_RXFL);
    letimer0Enable(); /*Bật Timer xử lý sự kiện kết nối*/

  }

  /*Check if button 1 was pressed --> Start/Stop*/
  else if (interruptMask & (1 << BUTTON1_PIN))
  {
      if(work==0){
          letimer0Enable();
          work = 1;
          GPIO_PinOutSet(LED1_PORT, LED1_PIN);
      }
      else {
          letimer0Disable();
          work = 0;
          GPIO_PinOutClear(LED1_PORT, LED1_PIN);
      }
  }
}



