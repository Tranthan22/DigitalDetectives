/*
 * connect.h
 *
 *  Created on: Jul 21, 2023
 *      Author: PhongPham
 */

#ifndef CONNECT_H_
#define CONNECT_H_

#define LED0_PORT           gpioPortB
#define LED0_PIN            2
#define LED1_PORT           gpioPortB
#define LED1_PIN            4
#define BUTTON0_PORT        gpioPortB
#define BUTTON0_PIN         1
#define BUTTON1_PORT        gpioPortB
#define BUTTON1_PIN         3
#define TX_PORT             gpioPortA
#define TX_PIN              8
#define RX_PORT             gpioPortA
#define RX_PIN              4
#define VCOM_Enable_PORT    gpioPortB
#define VCOM_Enable_PIN     0

#include "uart.h"
#include "em_emu.h"

void connectToStation(void);

#endif /* CONNECT_H_ */
