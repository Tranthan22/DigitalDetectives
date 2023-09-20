/*
 * connect.c
 *
 *  Created on: Sep 15, 2023
 *      Author: assus
 */
#include "connect.h"
#include "sl_status.h"
#include "sleeptimer_app.h"
#include "sl_sleeptimer.h"
#include  <stdio.h>
#include <string.h>
char address[3];
static int t=0;
static int count=0;
static sl_sleeptimer_timer_handle_t one_shot_timer;
void deleteString(char *str) {
    memset(str, '\0', strlen(str));
}
static void on_one_shot_timeout(sl_sleeptimer_timer_handle_t *handle,
                                void *data)
{
  (void)&handle;
  (void)&data;
  if(count<3){
  char dataToConnect[] = { 0xFF, 0xFF, 0x17,'2',0x01,0x04,'E','\0'};
    printf(dataToConnect);
    sl_sleeptimer_restart_timer_ms(&one_shot_timer,
                                  5000,
                                  on_one_shot_timeout, NULL,
                                  0,
                                  SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
    count++;}
    if(count==3){
        count=0;
        //GPIO_PinModeSet(gpioPortA, 7, gpioModePushPull, 1);
        //GPIO_PinModeSet(gpioPortA, 4, gpioModePushPull, 1);
        GPIO_PinModeSet (gpioPortB,5,gpioModePushPull,0);//gui nhung khong co phan hoi
        GPIO_PinModeSet (gpioPortB,4,gpioModePushPull,1);
        sl_sleeptimer_restart_timer_ms(&one_shot_timer,
                                        1000,
                                        on_one_shot_timeout, NULL,
                                        0,
                                        SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);


        sl_sleeptimer_stop_timer(&one_shot_timer);

    }
  }


void timer(void){

  sl_sleeptimer_restart_timer_ms(&one_shot_timer,
                                500,
                                on_one_shot_timeout, NULL,
                                0,
                                SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
}


void connectToStation(void){
 sl_sleeptimer_delay_millisecond(200);
   //char dataToConnect[] = { 0xFF, 0xFF, 0x17,'2',0x01,0x02,'E'};
  // printf(dataToConnect);
timer();
t=0;
int index=0;
   /* Nhận phản hồi từ Station xem có được ghép đôi? */
 static  char response[5];
 deleteString(response);
   sl_status_t status;
   char c;
   index=0;
   while (index < 5) {
      int a=GPIO_PinInGet(gpioPortB, 00);
           status = sl_iostream_getchar(sl_iostream_vcom_handle, &c);
           if(!a){
           if (status == SL_STATUS_OK) {
               if((c == '2')&&(index==0)){
                   response[0] = c;  //starting data
                   index = 1;
               }
               else if(index>0){
                   response[index] = c;
                   index++;
               }
               else if(c=='E'){
                   break;
               }
           }
           }
           else if(a){
              // GPIO_PinModeSet(gpioPortA, 7, gpioModePushPull, 0);
               //GPIO_PinModeSet(gpioPortA, 4, gpioModePushPull, 0);
               GPIO_PinModeSet (gpioPortB,4,gpioModePushPull,0);//ket noi nguon
               GPIO_PinModeSet (gpioPortC,2,gpioModePushPull,1);//ket noi gateway thanh cong
               GPIO_PinModeSet (gpioPortB,5,gpioModePushPull,1);//gui nhung khong co phan hoi
               connectToStation();
               break;
           }
       }
   //GPIO_PinModeSet(gpioPortA, 7, gpioModePushPull, 1);
   /* Station phản hổi được ghép kèm theo địa chỉ Station */
   if ( response[0] == '2'&&response[1]=='1'){
       count=0;
       sl_sleeptimer_stop_timer(&one_shot_timer);
       address[0] = response[2]; address[1] = response[3];
       address[2] = 0x17;
      // GPIO_PinModeSet(gpioPortA, 4, gpioModePushPull, 0);
      // GPIO_PinModeSet(gpioPortA, 7, gpioModePushPull, 0);
      // GPIO_PinOutToggle(gpioPortA, 7); /* Bật LED0 (1.5s): Thông báo kết nối thành công */
       GPIO_PinModeSet(gpioPortC, 2, gpioModePushPull, 0);
       GPIO_PinModeSet (gpioPortB,5,gpioModePushPull,1);
       GPIO_PinModeSet (gpioPortB,4,gpioModePushPull,1);
                GPIO_PinOutToggle(gpioPortC, 2);
         sl_sleeptimer_delay_millisecond(1000);
       GPIO_PinModeSet(gpioPortC, 2, gpioModePushPull, 1);
         GPIO_PinOutToggle(gpioPortC, 2);
       sl_sleeptimer_delay_millisecond(1000);
       GPIO_PinOutToggle(gpioPortC, 2);
      // GPIO_PinOutToggle(gpioPortA, 7);
       iostream_usart_init_sleep();
   }

   /* Station phản hổi không được ghép --> Vào EM4 */
   else if(response[0] == '2'&&response[1]=='0'){
       sl_sleeptimer_stop_timer(&one_shot_timer);
          // GPIO_PinModeSet(gpioPortA,7, gpioModePushPull,0);
          // GPIO_PinModeSet (gpioPortA,4,gpioModePushPull,1);
           GPIO_PinModeSet (gpioPortB,4,gpioModePushPull,1);
           GPIO_PinModeSet (gpioPortB,5,gpioModePushPull,0);
           sl_sleeptimer_delay_millisecond(1000);
           GPIO_PinModeSet (gpioPortB,5,gpioModePushPull,1);
           sl_sleeptimer_delay_millisecond(1000);
           GPIO_PinModeSet (gpioPortB,5,gpioModePushPull,0);
           sl_sleeptimer_delay_millisecond(1000);
           GPIO_PinModeSet (gpioPortB,5,gpioModePushPull,1);
           sl_sleeptimer_delay_millisecond(1000);
           GPIO_PinModeSet (gpioPortB,4,gpioModePushPull,0);
           sl_sleeptimer_delay_millisecond(1000);
           while(1){
           int a=GPIO_PinInGet(gpioPortB, 00);
            if(a){
                GPIO_PinModeSet (gpioPortA,4,gpioModePushPull,0);
                connectToStation();
                break;
            }
       }
   }
   else{
       deleteString(response);
       connectToStation();
   }
}

