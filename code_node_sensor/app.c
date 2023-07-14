#include "app.h"


void app_init(void)
{

  letimer0Init();
  uartInit();
/*
    uint32_t k = 100000000;
    uint8_t  i = 1;
    char receivedData;

    while (i){
        k--;
        if( USART0->STATUS & USART_STATUS_RXDATAV ){
        receivedData = USART0->RXDATA;

            i=0;
            USART_Tx(USART0, receivedData);

        }

    }
*/

  iadcInit();
  letimer0Enable();

}


void app_process_action(void)
{

}
