#include "app.h"


void app_init(void)
{

  letimer0Init();
  uartInit();
  iadcInit();
  letimer0Enable();

}


void app_process_action(void)
{

}
