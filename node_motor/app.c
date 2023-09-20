/***************************************************************************//**
 * @file
 * @brief Top level application functions
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
#include "sleeptimer_app.h"
#include "connect.h"
#include"em_gpio.h"
#include "sl_simple_led_instances.h"
#include "sl_simple_button_instances.h"
#include "sl_iostream_init_instances.h"
#include"crypt.h"
//#include"crypt.h"
#ifndef BUTTON_INSTANCE_1
#define BUTTON_INSTANCE_1   sl_button_btn1
#endif
/***************************************************************************//**
 * Initialize application.
 ******************************************************************************/
void app_init(void)
{
  gpio_set();

 GPIO_PinModeSet (gpioPortB,0,gpioModePushPull,0);
 sl_sleeptimer_delay_millisecond(500);
       /* Nhấn BUTTON_0 để kết nối tới Station */
     while(1){
         int a=GPIO_PinInGet(gpioPortB, 00);
             if (a) {
          connectToStation();
             }
      }


  //iostream_usart_init_sleep();
   }


/***************************************************************************//**
 * App ticking function.
 ******************************************************************************/
void app_process_action(void)
{
}
