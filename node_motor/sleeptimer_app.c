/***************************************************************************//**
 * @file
 * @brief Sleeptimer examples functions
 *******************************************************************************
 * # License
 * <b>Copyright 2020 Silicon Laboratories Inc. www.silabs.com</b>
 *******************************************************************************
 *
 * The licensor of this software is Silicon Laboratories Inc. Your use of this
 * software is governed by the terms of Silicon Labs Master Software License
 * Agreement (MSLA) available at
 * www.silabs.com/about-us/legal/master-software-license-agreement. This
 * software is distributed to you in Source Code format and is governed by the
 * sections of the MSLA applicable to Source Code.
 *
 ******************************************************************************/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "sleeptimer_app.h"
#include "sl_sleeptimer.h"
#include "sl_simple_led_instances.h"
//#include "sl_simple_button_instances.h"
#include "sl_iostream_init_instances.h"
#include"sl_iostream.h"
#include"sl_iostream_init_instances.h"
#include"sl_iostream_handles.h"
#include"em_gpio.h"
#ifndef BUFSIZE
#define BUFSIZE    80
#endif
int result;
static char buffer[BUFSIZE];
/*******************************************************************************
 *******************************   DEFINES   ***********************************
 ******************************************************************************/

//#ifndef BUTTON_INSTANCE_0
//#define BUTTON_INSTANCE_0   sl_button_btn0
//#endif

//#ifndef BUTTON_INSTANCE_1
//#define BUTTON_INSTANCE_1   sl_button_btn1
//#endif

#ifndef LED_INSTANCE_0
#define LED_INSTANCE_0      sl_led_led0
#endif

#ifndef LED_INSTANCE_1
#define LED_INSTANCE_1      sl_led_led1
#endif

#define TIMEOUT_MS 15000
#ifndef BUFSIZE
#define BUFSIZE    80
#endif
int result;
static char buffer[BUFSIZE];
char *manual;
char *autom;

/*******************************************************************************
 ***************************  LOCAL VARIABLES   ********************************
 ******************************************************************************/

static sl_sleeptimer_timer_handle_t periodic_timer;
static sl_sleeptimer_timer_handle_t one_shot_timer;
static sl_sleeptimer_timer_handle_t status_timer;
static bool print_status = false;

/*******************************************************************************
 ************************   LOCAL FUNCTIONS ************************************
 ******************************************************************************/
//char* ascii_transmit(const char* data_sensor);

// Periodic timer callback
static void on_periodic_timeout(sl_sleeptimer_timer_handle_t *handle,
                                void *data)
{
  (void)&handle;
  (void)&data;
  sl_led_turn_off(&LED_INSTANCE_0);
  //printf("den led 0 tat");
  sl_sleeptimer_restart_periodic_timer_ms(&periodic_timer,
                                                   5000,
                                                    on_periodic_timeout, NULL,
                                                    0,
                                                   SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
}

// One-shot timer callback
static void on_one_shot_timeout(sl_sleeptimer_timer_handle_t *handle,
                                void *data)
{
  (void)&handle;
  (void)&data;
  sl_led_turn_off(&LED_INSTANCE_1);
  //printf("den led 1 tat");
    sl_sleeptimer_restart_periodic_timer_ms(&one_shot_timer,
                                                     5000,
                                                      on_one_shot_timeout, NULL,
                                                      0,
                                                     SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
  }


// Status timer callback
static void on_status_timeout(sl_sleeptimer_timer_handle_t *handle,
                              void *data)
{
  (void)&handle;
  (void)&data;
  print_status = true;
}

/*******************************************************************************
 **************************   GLOBAL FUNCTIONS   *******************************
 ******************************************************************************/

/***************************************************************************//**
 * Initialize example.
 ******************************************************************************/
void sleeptimer_app_init(void)
{
  /* Output on vcom usart instance */
  //printf("Sleeptimer example\r\n\r\n");

 // printf("m1101\r\n");
 // printf("LED1 is controlled by a one-shot timer\r\n");
  //printf("Use buttons to start and restart timers\r\n");

  // Create timer for waking up the system periodically.
  sl_sleeptimer_start_periodic_timer_ms(&periodic_timer,
                                        TIMEOUT_MS,
                                        on_periodic_timeout, NULL,
                                        0,
                                       SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);

  // Create one-shot timer for waking up the system.
  sl_sleeptimer_start_timer_ms(&one_shot_timer,
                               TIMEOUT_MS,
                               on_one_shot_timeout, NULL,
                               0,
                               SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);

  // Create periodic timer to report status of other timers
  sl_sleeptimer_start_periodic_timer_ms(&status_timer,
                                        1000,
                                        on_status_timeout, NULL,
                                        0,
                                        SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
}

/***************************************************************************//**
 * Ticking function.
 ******************************************************************************/
void sleeptimer_app_process_action(void)
{
  uint32_t remaining;

  // Periodically report the status of the other timers
  if (print_status == true) {
    print_status = false;
    if (0 == sl_sleeptimer_get_timer_time_remaining(&one_shot_timer, &remaining) ) {
      printf("One shot timer has %lu ms remaining\r\n", sl_sleeptimer_tick_to_ms(remaining));
    }
    if (0 == sl_sleeptimer_get_timer_time_remaining(&periodic_timer, &remaining) ) {
      printf("Periodic timer has %lu ms remaining\r\n", sl_sleeptimer_tick_to_ms(remaining));
   }
  }
}

/***************************************************************************//**
 * Function called on button change
 ******************************************************************************/
void sl_button_on_change(int*k,int*f,int*l,int*g)
{
  bool is_running = false;
    // Button 1 controls the one-shot timer
    if (*k==1) {
      if (sl_sleeptimer_is_timer_running(&one_shot_timer, &is_running) == 0) {
        if (*l==0) {
          // If timer is running, stop it
          sl_sleeptimer_stop_timer(&one_shot_timer);
        } else {
          // If timer is stopped, restart it
          sl_sleeptimer_restart_timer_ms(&one_shot_timer,
                                         TIMEOUT_MS,
                                         on_one_shot_timeout, NULL,
                                         0,
                                         SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
        }
      }
    }
    if(*f==1){
       //Button 0 controls the periodic timer
      if (sl_sleeptimer_is_timer_running(&periodic_timer, &is_running) == 0) {

        if (*g==1) {
          // If timer is running, stop it
         sl_sleeptimer_stop_timer(&periodic_timer);
       } else {
          // If timer is stopped, restart it
          sl_sleeptimer_restart_periodic_timer_ms(&periodic_timer,
                                                 TIMEOUT_MS,
                                                  on_periodic_timeout, NULL,
                                                  0,
                                                 SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
       }
      }
    }

}

void iostream_usart_init_sleep(void){
  //int8_t c = 0;
   uint8_t index = 0;
   //uint32_t remaining;
  // if (print_status == true) {
   //    print_status = false;
   //char buffer[BUFSIZE];
   //sleeptimer_app_init();
  //const char str1[]="123456789\r\n";
  //har str[sizeof(str1)];
 // strcpy(str, str1);
  //sl_sleeptimer_delay_millisecond(500);
      //sl_iostream_write(sl_iostream_vcom_handle,str,strlen(str));

  //sl_iostream_set_default(sl_iostream_vcom_handle);
  //char str2[]="m1101";
printf("m1101");
  //sl_iostream_write(SL_IOSTREAM_STDOUT,str2,strlen(str2));
  //printf("Printf uses the default stream, as long as iostream_retarget_stdio is included.\r\n");
  sleeptimer_app_init();
  // printf("> ");
 sl_sleeptimer_delay_millisecond(500);
  //printf("m1101");
   /* Retrieve characters, print local echo and full line back */
   while (1) {


       char c;
       sl_status_t status = sl_iostream_getchar(sl_iostream_vcom_handle, &c);
       if (status == SL_STATUS_OK) {
         if (c == '\r' || c == '\n'||c=='H'||c=='\0') {
           // Ký tự kết thúc dòng được nhận, xử lý dữ liệu đã đọc
             //sleeptimer_app_init();
           buffer[index] = '\0';
          // printf("Received data: %s\n", buffer);
           char ascii[3] = "ABC";
            char decimals[7]; // Chuỗi decimal sẽ có độ dài tối đa 6 ký tự (3 cặp số)

            size_t asciiLength = 3;
            size_t decimalLength = asciiLength * 2;

            for (size_t i = 0; i < asciiLength; i++) {
                int value = (int)ascii[i];
                decimals[i * 2] = '0' + (value / 10);
                decimals[i * 2 + 1] = '0' + (value % 10);
            }

            decimals[decimalLength] = '\0';

         //   printf("Decimal: %s\n", decimals);



           const char str1[] = "m10";//manual
           const char str2[] = "m11";//autom
          // volatile int a,b ;
                               //const char *str2 = "World";

           manual = strstr(buffer,str1);
           autom = strstr( buffer,str2);
           char sensor_1=buffer[3];
           char sensor_2=buffer[6];
           volatile int mo1,mo2;
                                          if (manual != NULL) {
                                             // printf("Chuoi str1 nho hon str2\n");
                                            if(buffer[3]=='1'&& buffer[4]=='1'){
                                                mo1=1;
                                                mo2=1;
                                              GPIO_PinModeSet (gpioPortA,4,gpioModePushPull,mo1);
                                              GPIO_PinModeSet (gpioPortA,7,gpioModePushPull,mo2);

                                            }
                                             if(buffer[3]=='1'&& buffer[4]=='0'){
                                                 mo1 =1;
                                                 mo2=0;
                                            GPIO_PinModeSet (gpioPortA,4,gpioModePushPull,mo1);
                                             GPIO_PinModeSet (gpioPortA,7,gpioModePushPull,mo2);
                                            }
                                             if(buffer[3]=='0'&& buffer[4]=='1'){
                                                 mo1 =0;
                                                 mo2=1;
                                              GPIO_PinModeSet (gpioPortA,7,gpioModePushPull,mo1);
                                              GPIO_PinModeSet (gpioPortA,4,gpioModePushPull,mo2);
                                             }
                                             if(buffer[3]=='0'&& buffer[4]=='0'){
                                                 mo1 =0;
                                                 mo2=0;
                                                GPIO_PinModeSet (gpioPortA,4,gpioModePushPull,mo1);
                                                GPIO_PinModeSet (gpioPortA,7,gpioModePushPull,mo2);
                                             }
                                             sl_sleeptimer_stop_timer(&periodic_timer);
                                             sl_sleeptimer_stop_timer(&one_shot_timer);
                                          } else if (autom != NULL) {
                                             // printf("Chuoi str1 lon hon str2\n");
                                              if(sensor_1<='4'&&sensor_2<='4'){
                                                  mo1=1;
                                                  mo2=1;
                                              GPIO_PinModeSet (gpioPortA,4,gpioModePushPull,mo1);
                                              GPIO_PinModeSet (gpioPortA,7,gpioModePushPull,mo2);


                                             sl_sleeptimer_restart_periodic_timer_ms(&periodic_timer,
                                                                                                     15000,
                                                                                                     on_periodic_timeout, NULL,
                                                                                                      0,
                                                                                                    SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
                                             sl_sleeptimer_restart_timer_ms(&one_shot_timer,
                                                                                              15000,
                                                                                              on_one_shot_timeout, NULL,
                                                                                               0,
                                                                                             SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
                                              }
                                              if(sensor_1<='4'&&sensor_2>'4') {
                                                  mo1=1;
                                                  mo2=0;
                                                GPIO_PinModeSet (gpioPortA,4,gpioModePushPull,mo1);
                                                GPIO_PinModeSet (gpioPortA,7,gpioModePushPull,mo2);
                                                sl_sleeptimer_restart_periodic_timer_ms(&periodic_timer,
                                                                                                      15000,
                                                                                                      on_periodic_timeout, NULL,
                                                                                                      0,
                                                                                                     SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
                                              }
                                              if(sensor_1>'4'&&sensor_2<='4'){
                                                  mo1=0;
                                                  mo2=1;
                                              GPIO_PinModeSet (gpioPortA,4,gpioModePushPull,mo1);
                                              GPIO_PinModeSet (gpioPortA,7,gpioModePushPull,mo2);
                                              sl_sleeptimer_restart_timer_ms(&one_shot_timer,
                                                                                             15000,
                                                                                             on_one_shot_timeout, NULL,
                                                                                             0,
                                                                                             SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
                                              }
                                              if(sensor_1>'4'&&sensor_2>'4'){
                                                  mo1=0;
                                                  mo2=0;
                                                  GPIO_PinModeSet (gpioPortA,4,gpioModePushPull,mo1);
                                                  GPIO_PinModeSet (gpioPortA,7,gpioModePushPull,mo2);
                                              }

                                          }
                                          char data[12] ="65666768";
                                          char MO1= '0'+mo1;
                                          char MO2= '0'+mo2;
                                          char MO3= '0'+0;
                                          data[8]=MO1;
                                          data[9]= MO2;
                                         data[10]= MO3;
                                         //data[11]='0';
                                         // printf("%s",data);
                                         //char data_sensor[12];


                                          //const char* data_sensor = "6566676869";
                                          char decimal[12];
                                            size_t i;
                                            for (i = 0; i < 11; i++) {
                                              decimal[i] = data[i];
                                            }
                                            decimal[i] = '\0';
                                            size_t length = strlen(decimal);
                                            char Data[10] = "";
                                             uint8_t j = 0;
                                             for (uint8_t i = 0; i < length; i += 2) {
                                               char asciiData[3] = "";
                                               asciiData[0] = decimal[i];
                                               asciiData[1] = decimal[i + 1];
                                               asciiData[2] = '\0';
                                               int value = (int)(asciiData[0] - '0') * 10 + (int)(asciiData[1] - '0');
                                               Data[j] = (char)value;
                                               j++;
                                             }

                                             // Đoạn code dưới đây làm gì đó với mảng Data chứa kết quả ASCII
                                             // Ví dụ: In kết quả ASCII
                                           for (uint8_t i = 0; i < j; i++) {
                                               printf("%c",Data[i]);
                                             }




           index = 0;
             //sl_button_on_change();
         }
         else {
           // Lưu trữ ký tự vào bộ đệm
           if (index < BUFSIZE - 1) {
             buffer[index] = c;
             index++;
           }
         }
       }

   }
   }

void gpio_set(void){
  //GPIO_PinModeSet (gpioPortA,7,gpioModePushPull,1);
  GPIO_PinModeSet (gpioPortC,1,gpioModePushPull,0);
  GPIO_PinModeSet (gpioPortC,3,gpioModePushPull,0);
}


//void set_up_pin(int *mo1,int*mo2){

//}
