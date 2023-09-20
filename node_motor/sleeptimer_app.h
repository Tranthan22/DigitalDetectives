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

#ifndef SLEEPTIMER_APP_H
#define SLEEPTIMER_APP_H

/***************************************************************************//**
 * Initialize example
 ******************************************************************************/
void sleeptimer_app_init(void);
void iostream_usart_init_sleep(void);
/***************************************************************************//**
 * BTicking function
 ******************************************************************************/
void sleeptimer_app_process_action(void);
void gpio_set(void);
void getData(void);
void getAES_data(void);
#endif  // SLEEPTIMER_APP_H
