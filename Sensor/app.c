#include "app.h"

void app_init(void)
{

  gpioInit();
  uartInit();
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


