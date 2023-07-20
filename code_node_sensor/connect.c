/*
 * connect.c
 *
 *  Created on: Jul 21, 2023
 *      Author: KarimPham
 *
 */

#include "connect.h"
void connectToStation(void){

   char dataToConnect[] = { 0xFF, 0xFF, 0x17, '1', '1', '0', '0', '0', '1'};
   uint8_t length = sizeof (dataToConnect);
   transmitData(dataToConnect, length);

   /* Nhận phản hồi từ station xem có được ghép đôi ? */
   char response[7];
   for (uint8_t i=0 ; i<8; i++){
       response[i] = USART_Rx(USART0);
   }
   /* Station gửi phản hồi được ghép kèm theo địa chỉ của station */
   if ( response[0] == '1' && response[1] == '1'){
       char address[9] = { response[2], response[3], response[4], response[5], '2', '3', '1', '1','\0'};
       GPIO_PinModeSet(GPIO_PORTB, 2, gpioModeInputPull, 0);
       GPIO_PinOutToggle(GPIO_PORTB, 2); /* Bật LEDO (2s): Thông báo kết nối thành công */
       USTIMER_Init();
       USTIMER_DelayIntSafe(2000000);
       GPIO_PinOutToggle(GPIO_PORTB, 2);
       USTIMER_DeInit();
   }
   /* Station gửi phản hồi không được ghép  */
   else if(response[0] == '0' && response[1] == '0'){
       GPIO_PinModeSet(GPIO_PORTB, 4, gpioModeInputPull, 0);
       GPIO_PinOutToggle(GPIO_PORTB, 4); /* Bật LED1 (2s): Thông báo kết nối không thành công */
       USTIMER_Init();
       USTIMER_DelayIntSafe(2000000);
       EMU_EnterEM4();
   }

}
