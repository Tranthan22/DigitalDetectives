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
#include<stdint.h>
#include "sleeptimer_app.h"
#include "sl_sleeptimer.h"
#include "sl_simple_led_instances.h"
//#include "sl_simple_button_instances.h"
#include "sl_iostream_init_instances.h"
#include"sl_iostream.h"
#include"sl_iostream_init_instances.h"
#include"sl_iostream_handles.h"
#include"em_gpio.h"
#include"connect.h"
#include"crypt.h"
#ifndef BUFSIZE
#define BUFSIZE    10
#endif
int result;
extern char address[5];
extern void deleteString();
//static char buffer[BUFSIZE];
/*******************************************************************************
 *******************************   DEFINES   ***********************************
 ******************************************************************************/
#ifndef BUTTON_INSTANCE_0
#define BUTTON_INSTANCE_0   sl_button_btn0
#endif

#ifndef BUTTON_INSTANCE_1
#define BUTTON_INSTANCE_1   sl_button_btn1
#endif

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
#define TOGGLE_INT(value) ((value) == 0 ? 1 : 0)
int result;
//static char buffer[BUFSIZE];
char *manual;
char *autom;
static char input_string[21];
static char nhan_ve[17]={'2','0','0','0','2','5','8','0','0','0','0','0','0','0','0','0','0'};
 char transmit1[21]={'2','0','0','0','2','5','8','0','0','0','0','0','0','0','0','0','0','E','E','E','\0'};
//static char transmit2[20]={'2','0','0','0','2','5','8','0','0','0','0','0','0','0','0','0','0','E','E','E'};
int dem;


/*******************************************************************************
 ***************************  LOCAL VARIABLES   ********************************
 ******************************************************************************/

static sl_sleeptimer_timer_handle_t periodic_timer;
static sl_sleeptimer_timer_handle_t one_shot_timer;
static sl_sleeptimer_timer_handle_t status_timer;

/*******************************************************************************
 ************************   LOCAL FUNCTIONS ************************************
 ******************************************************************************/
//char* ascii_transmit(const char* data_sensor);
int *mo1,*mo2,*mo3;
char *check;


// Periodic timer callback
static void on_periodic_timeout(sl_sleeptimer_timer_handle_t *handle,
                                void *data)
{
  (void)&handle;
  (void)&data;
  if(*check=='1'){
      sl_led_turn_on(&LED_INSTANCE_0);
       //*mo1 = TOGGLE_INT(*mo1);
      *mo1=0;
}
  else{
      GPIO_PinModeSet (gpioPortC,0,gpioModePushPull,1);
      GPIO_PinModeSet (gpioPortC,8,gpioModePushPull,1);
      GPIO_PinModeSet (gpioPortA,0,gpioModePushPull,1);
      *mo2=0;
      *mo1=0;
      *mo3=0;
     // printf("%c%c%c%c",address[0],address[1],address[2],address[3]);
    char Mo2 = *mo2+'0';
    char Mo1 = *mo1+'0';
    char Mo3 = *mo3+'0';
    char transmit2[21]={'2','0','0','0','2','5','8','0','0','0','0','0','0','0','0','0','0','E','E','E','\0'};
    transmit2[1]=Mo1;
    transmit2[2]=Mo2;
    transmit2[3]=Mo3;
    //printf("%c%c%c",address[0],address[1],address[2]);
   // printf("%s",transmit2);
    unsigned char dataTo_Encrypt[16];
    unsigned char decrypted_Data[16];
            memcpy(dataTo_Encrypt, &transmit2[1], 16);
              EncryptDataECB(dataTo_Encrypt, decrypted_Data);
               memcpy(&transmit2[1], decrypted_Data, 16);
       //        printf("%c%c%c%c",address[0],address[1],address[2],address[3]);
  //  printf("%s\n",transmit2);
  }
sl_sleeptimer_stop_timer(&periodic_timer);
}

// One-shot timer callback
static void on_one_shot_timeout(sl_sleeptimer_timer_handle_t *handle,
                                void *data)
{
  (void)&handle;
  (void)&data;
  if(*check=='1'){
  sl_led_turn_on(&LED_INSTANCE_1);
 // *mo2 = TOGGLE_INT(*mo2);
  *mo2=0;
}
  else{
      GPIO_PinModeSet (gpioPortC,0,gpioModePushPull,1);
      GPIO_PinModeSet (gpioPortC,8,gpioModePushPull,1);
      GPIO_PinModeSet (gpioPortA,0,gpioModePushPull,1);
      *mo2=0;
      *mo1=0;
      *mo3=0;
     // printf("%c%c%c%c",address[0],address[1],address[2],address[3]);
    char Mo2 = *mo2+'0';
    char Mo1 = *mo1+'0';
    char Mo3 = *mo3+'0';
    char transmit2[21]={'2','0','0','0','2','5','8','0','0','0','0','0','0','0','0','0','0','E','E','E','\0'};
    transmit2[1]=Mo1;
    transmit2[2]=Mo2;
    transmit2[3]=Mo3;
  //  printf("%s\n",transmit2);
    unsigned char dataTo_Encrypt[16];
    unsigned char decrypted_Data[16];
            memcpy(dataTo_Encrypt, &transmit2[1], 16);
              EncryptDataECB(dataTo_Encrypt, decrypted_Data);
               memcpy(&transmit2[1], decrypted_Data, 16);
          //     printf("%c%c%c%c",address[0],address[1],address[2],address[3]);
  //  printf("%s\n",transmit2);
  }
  }

static void on_status_timeout(sl_sleeptimer_timer_handle_t *handle,
                              void *data)
{
  (void)&handle;
  (void)&data;
  sl_sleeptimer_stop_timer(&status_timer);
  if(*check=='1'){
      GPIO_PinModeSet (gpioPortA,0,gpioModePushPull,1);

      *mo3=0;
char Mo2 = *mo2+'0';
char Mo1 = *mo1+'0';
char Mo3 = *mo3+'0';
char transmit2[21]={'2','0','0','0','2','5','8','0','0','0','0','0','0','0','0','0','0','E','E','E','\0'};
transmit2[1]=Mo1;
transmit2[2]=Mo2;
transmit2[3]=Mo3;

unsigned char dataTo_Encrypt[16];
unsigned char decrypted_Data[16];
        memcpy(dataTo_Encrypt, &transmit2[1], 16);
          EncryptDataECB(dataTo_Encrypt, decrypted_Data);
           memcpy(&transmit2[1], decrypted_Data, 16);
           printf("%c%c%c",address[0],address[1],address[2]);
printf("%s",transmit2);
  }
  else{
      GPIO_PinModeSet (gpioPortC,0,gpioModePushPull,1);
      GPIO_PinModeSet (gpioPortC,8,gpioModePushPull,1);
      GPIO_PinModeSet (gpioPortA,0,gpioModePushPull,1);
      *mo2=0;
      *mo1=0;
      *mo3=0;
      //printf("%c%c%c%c",address[0],address[1],address[2],address[3]);
    char Mo2 = *mo2+'0';
    char Mo1 = *mo1+'0';
    char Mo3 = *mo3+'0';
    char transmit2[21]={'2','0','0','0','2','5','8','0','0','0','0','0','0','0','0','0','0','E','E','E','\0'};
    transmit2[1]=Mo1;
    transmit2[2]=Mo2;
    transmit2[3]=Mo3;

    unsigned char dataTo_Encrypt[16];
    unsigned char decrypted_Data[16];
            memcpy(dataTo_Encrypt, &transmit2[1], 16);
              EncryptDataECB(dataTo_Encrypt, decrypted_Data);
               memcpy(&transmit2[1], decrypted_Data, 16);
               printf("%c%c%c",address[0],address[1],address[2]);
    printf("%s",transmit2);
  }

}
/*******************************************************************************
 **************************   GLOBAL FUNCTIONS   *******************************
 ******************************************************************************/

/***************************************************************************//**
 * Initialize example.
 ******************************************************************************/
void sleeptimer_app_init(void)
{

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
                                        TIMEOUT_MS,
                                        on_status_timeout, NULL,
                                        0,
                                        SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
}
void AESen(char *str1,char *str2){
  unsigned char dataTo_Encrypt[16];
         unsigned char encrypted_Data[16];
         memcpy(dataTo_Encrypt, &str2[1], 16);
           EncryptDataECB(dataTo_Encrypt, encrypted_Data);
            memcpy(&str1[1], encrypted_Data, 16);
            printf("%c%c%c%c",address[0],address[1],address[2],address[3]);
}
void AESde(char *str1,char *str2){
  unsigned char dataToEncrypt[16];
         unsigned char decryptedData[16];
         memcpy(dataToEncrypt, &str2[1], 16);
           DecryptDataECB(dataToEncrypt, decryptedData);
            memcpy(&str1[1], decryptedData, 16);
            printf("%c%c%c%c",address[0],address[1],address[2],address[3]);
}

void re_connect(void){
  sl_sleeptimer_stop_timer(&periodic_timer);
  sl_sleeptimer_stop_timer(&one_shot_timer);
  sl_sleeptimer_stop_timer(&status_timer);
  //GPIO_PinModeSet (gpioPortA,4,gpioModePushPull,1);//nguon
  GPIO_PinModeSet (gpioPortB,4,gpioModePushPull,0);//ket noi nguon
  GPIO_PinModeSet (gpioPortC,2,gpioModePushPull,1);//ket noi gateway thanh cong
  GPIO_PinModeSet (gpioPortB,5,gpioModePushPull,1);//gui nhung khong co phan hoi
  GPIO_PinModeSet (gpioPortC,0,gpioModePushPull,1);
  GPIO_PinModeSet (gpioPortC,8,gpioModePushPull,1);
  GPIO_PinModeSet (gpioPortA,0,gpioModePushPull,1);
  char dataToConnect[] = { 0xFF, 0xFF, 0x17,'2',0x01,0x02,'E','\0'};
      printf(dataToConnect);

}
void getData(){
    sl_status_t status;
    dem = 0;
    char c;
    while (dem < 8) {
        int a=GPIO_PinInGet(gpioPortB, 00);
        status = sl_iostream_getchar(sl_iostream_vcom_handle, &c);
        if(!a){
        if (status == SL_STATUS_OK) {
            if(c == 'S'){
                input_string[0] = c;  //starting data
                dem = 1;
            }
            else if ((c == 'E')&&(input_string[0]=='S')) {
                input_string[dem] = c;  //Ending data
                break;
            }
            else if(dem>0){
                input_string[dem] = c;
                dem++;
            }
        }
        }
        else{
            re_connect();
            connectToStation();
        }
    }
}

void getAES_data(void){
deleteString(input_string);
input_string[20]='\0';
     sl_status_t status;
    int dem = 0;
     char c;
     while (dem < 20) {
         int a=GPIO_PinInGet(gpioPortB, 00);
         status = sl_iostream_getchar(sl_iostream_vcom_handle, &c);
         if(!a){
         if (status == SL_STATUS_OK) {
             if((c == 'S')&&(dem==0)){
                 input_string[0] = c;  //starting data
                 dem = 1;
                // GPIO_PinModeSet(gpioPortA, 4, gpioModePushPull, 1);
             }
             else if ((c == 'E')&&(input_string[0]=='S')&&(input_string[17]=='E')&&(input_string[18]=='E')) {
                 input_string[dem] = c;  //Ending data
                 break;
             }
             else if(dem>0){
                 input_string[dem] = c;
                 dem++;
             }
         }
         }
             else{
                       re_connect();
                       connectToStation();
                   }
     }
 nhan_ve[0]='S';
 GPIO_PinModeSet(gpioPortA, 7, gpioModePushPull, 1);
    // printf("%s\n",input_string);
     unsigned char dataToEncrypt[16];
     unsigned char decryptedData[16];
     memcpy(dataToEncrypt, &input_string[1], 16);
       DecryptDataECB(dataToEncrypt, decryptedData);
        memcpy(&nhan_ve[1], decryptedData, 16);
        //printf("%c%c%c%c",address[0],address[1],address[2],address[3]);
        //printf("%s\n",nhan_ve);

}
int calculate_time(char a,char b){
  return ((a-'0')*10*60+(b-'0')*60)*1000/6;
}

void gpio_set(void){
  GPIO_PinModeSet (gpioPortA,4,gpioModePushPull,1);
 GPIO_PinModeSet (gpioPortB,4,gpioModePushPull,0);//ket noi nguon
 GPIO_PinModeSet (gpioPortC,2,gpioModePushPull,1);//ket noi gateway thanh cong
 GPIO_PinModeSet (gpioPortB,5,gpioModePushPull,1);//gui nhung khong co phan hoi
  GPIO_PinModeSet (gpioPortC,0,gpioModePushPull,1);//motor
 GPIO_PinModeSet (gpioPortC,8,gpioModePushPull,1);//motor
  GPIO_PinModeSet (gpioPortA,0,gpioModePushPull,1);//motor
  GPIO_PinModeSet (gpioPortC,1,gpioModePushPull,0);//lora
  GPIO_PinModeSet (gpioPortC,3,gpioModePushPull,0);//lora
}
//char daily_before[3]={54,48,48};
void iostream_usart_init_sleep(void){
 sl_sleeptimer_delay_millisecond(500);
 GPIO_PinModeSet (gpioPortC,2,gpioModePushPull,0);
 GPIO_PinModeSet (gpioPortB,4,gpioModePushPull,1);
 GPIO_PinModeSet (gpioPortC,0,gpioModePushPull,1);
 GPIO_PinModeSet (gpioPortC,8,gpioModePushPull,1);
 GPIO_PinModeSet (gpioPortA,0,gpioModePushPull,1);
 char gui[17]={'2','0','0','0','2','5','8','0','0','0','0','0','0','0','0','0','0'};
 unsigned char dataTo_Encrypt[16];
        unsigned char decrypted_Data[16];
        memcpy(dataTo_Encrypt, &gui[1], 16);
          EncryptDataECB(dataTo_Encrypt, decrypted_Data);
           memcpy(&transmit1[1], decrypted_Data, 16);

           printf("%c%c%c",address[0],address[1],address[2]);

         printf("%s",transmit1);



   while (1) {


      static int m01,m02,m03,m011,m022,m033;
                 mo1=&m01;
                 mo2=&m02;
                 mo3=&m03;
   //getData();//receive data from uart
     getAES_data();
//     unsigned char dataToEncrypt[16];
//       unsigned char decryptedData[16];
//       memcpy(dataToEncrypt, &nhan_ve[1], 16);
//          EncryptDataECB(dataToEncrypt, decryptedData);
//          memcpy(&nhan_ve[1], decryptedData, 16);
//          printf("%c%c%c%c",address[0],address[1],address[2],address[3]);
//
       //  printf("%s\n",nhan_ve);
        // printf("%s\n",input_string);
           //printf("Received data: %s\n", input_string);
           char a_m = nhan_ve[1];
           if((nhan_ve[0]=='S')&&(input_string[17]=='E')&&(input_string[18]=='E')&&(input_string[19]=='E')){
              // deleteString(nhan_ve);
                                                  check=&a_m;
                                                //  printf("%c\n",*check);
                                                  m01= nhan_ve[2]-'0';
                                                  m02= nhan_ve[3]-'0';
                                                  m03= nhan_ve[4]-'0';
                                                  m011=abs(m01-1);
                                                  m022=abs(m02-1);
                                                  m033=abs(m03-1);
                                                  GPIO_PinModeSet (gpioPortC,0,gpioModePushPull,m011);
                                                  GPIO_PinModeSet (gpioPortC,8,gpioModePushPull,m022);
                                                  GPIO_PinModeSet (gpioPortA,0,gpioModePushPull,m033);
                                              if(nhan_ve[1]=='1'){
                                   GPIO_PinModeSet(gpioPortA, 4, gpioModePushPull, 1);
                                 int time_out_1=calculate_time(nhan_ve[5],nhan_ve[6]);
                                 sl_sleeptimer_restart_periodic_timer_ms(&periodic_timer,
                                                                              time_out_1,
                                                                              on_periodic_timeout, NULL,
                                                                              0,
                                                                              SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
                                 sl_sleeptimer_restart_timer_ms(&one_shot_timer,
                                                                     time_out_1,
                                                                     on_one_shot_timeout, NULL,
                                                                     0,
                                                                     SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);


                                 sl_sleeptimer_restart_timer_ms(&status_timer,
                                                                time_out_1,
                                                                on_status_timeout, NULL,
                                                                0,
                                                                SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
                                                                              }
                                              else {
                                     int time_out_2=calculate_time(nhan_ve[5],nhan_ve[6]);
                                     sl_sleeptimer_restart_periodic_timer_ms(&periodic_timer,
                                                                             time_out_2,
                                                                             on_periodic_timeout, NULL,
                                                                             0,
                                                                             SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);

                                     sl_sleeptimer_restart_timer_ms(&one_shot_timer,
                                                                    time_out_2,
                                                                    on_one_shot_timeout, NULL,
                                                                    0,
                                                                    SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);
                                     sl_sleeptimer_restart_timer_ms(&status_timer,
                                                                    time_out_2,
                                                                    on_status_timeout, NULL,
                                                                    0,
                                                                    SL_SLEEPTIMER_NO_HIGH_PRECISION_HF_CLOCKS_REQUIRED_FLAG);

                                              }
char Mo1 =m01+'0';
char Mo2 =m02+'0';
char Mo3 =m03+'0';
char transmit2[21]={'2','0','0','0','2','5','8','0','0','0','0','0','0','0','0','0','0','E','E','E','\0'};
transmit2[1]=Mo1;
transmit2[2]=Mo2;
transmit2[3]=Mo3;
//printf("%s\n",transmit2);
unsigned char dataTo_encrypt[16];
unsigned char decrypted_data[16];
        memcpy(dataTo_encrypt, &transmit2[1], 16);
          EncryptDataECB(dataTo_encrypt, decrypted_data);
           memcpy(&transmit2[1], decrypted_data, 16);
           printf("%c%c%c",address[0],address[1],address[2]);
printf("%s",transmit2);
        }
   }
}
