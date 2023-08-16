/*
 * connect.c
 *
 *  Created on: Jul 21, 2023
 *      Author: PhongPham
 *
 */

#include "connect.h"

char address[5];

void connectToStation(void){

   char dataToConnect[] = { 0xFF, 0xFF, 0x17, '1', 0x00, 0x01};
   transmitData(dataToConnect, sizeof (dataToConnect));

   /* Nhận phản hồi từ Station xem có được ghép đôi? */
   char response[4];
   for (uint8_t i=0 ; i<4; i++){
       response[i] = USART_Rx(USART0);
   }

   /* Station phản hổi được ghép kèm theo địa chỉ Station */
   if ( response[0] == '1' && response[1] == '1'){
       address[0] = response[2]; address[1] = response[3];
       address[2] = 0x17, address[3] = '1';
       GPIO_PinOutToggle(GPIO_PORTB, 2); /* Bật LED0 (3s): Thông báo kết nối thành công */
       USTIMER_Init();
       USTIMER_DelayIntSafe(3000000);
       GPIO_PinOutToggle(GPIO_PORTB, 2);
       USTIMER_DeInit();
   }

   /* Station phản hổi không được ghép --> Vào EM4 */
   else if(response[0] == '1' && response[1] == '0'){
       GPIO_PinOutToggle(GPIO_PORTB, 4); /* Bật LED1 (3s): Thông báo kết nối không thành công */
       USTIMER_Init();
       USTIMER_DelayIntSafe(3000000);
       EMU_EnterEM4();
   }
}
