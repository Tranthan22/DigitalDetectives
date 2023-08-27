/*
 * connect.c
 *
 *  Created on: Jul 21, 2023
 *      Author: PhongPham
 *
 */

#include "connect.h"

char address[5];
char response[10];
void connectToStation(void){
   bool connect = false;
   char dataToConnect[] = { 0xFF, 0xFF, 0x17, '1', 0x01, 0x03, 'E'};
   transmitData(dataToConnect, sizeof (dataToConnect));

   /* Nhận phản hồi từ Station xem có được ghép đôi? */
   uint8_t i;
   while(!connect){
     for ( i=0 ; i< 10; i++){
         response[i] = EUSART_Rx(EUSART0);
         if (response[i] == 'E')
           break;
   }
   if(i == 4){
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
       USTIMER_DeInit();
       EMU_EnterEM4();
   }
   connect = true;
}
}
}
