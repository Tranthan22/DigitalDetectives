#include "app.h"

void app_init(void)
{
  uartInit();

  CMU_ClockEnable(cmuClock_GPIO, true);
  GPIO_PinModeSet(BUTTON0_PORT, BUTTON0_PIN, gpioModeInput, 1);
  GPIO_PinModeSet(LED0_PORT, LED0_PIN, gpioModePushPull, 0);
  GPIO_PinModeSet(LED1_PORT, LED1_PIN, gpioModePushPull, 0);
  while(1){
      /* Đợi người dùng nhấn BUTTON_0 để kết nối tới Station */
      if(!GPIO_PinInGet(GPIO_PORTB, 1)){
          connectToStation();
          break;
      }
  }

  letimer0Init();
  iadcInit();
  letimer0Enable();

}

void app_process_action(void)
{

}


