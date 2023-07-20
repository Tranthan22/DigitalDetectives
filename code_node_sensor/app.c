#include "app.h"

void app_init(void)
{
  uartInit();
  CMU_ClockEnable(cmuClock_GPIO, true);
  GPIO_PinModeSet(GPIO_PORTB, 1, gpioModeInput, 1); /* BUTTON_0 */
  while(1){
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
