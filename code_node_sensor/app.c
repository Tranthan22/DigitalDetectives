#include "app.h"


void app_init(void)
{

  letimer0Init();
  uartInit();
  iadcInit();
  letimer0Enable();
  char data[21] = "00002311987654321987";
  uint8_t length = sizeof(data);
  transmitData(data, length);

}


void app_process_action(void)
{

}
