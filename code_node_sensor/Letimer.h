/*
 * Letimer.h
 *
 *  Created on: Jun 19, 2023
 *      Author: KarimPham
 */

#ifndef LETIMER_H_
#define LETIMER_H_

#include "uart.h"
#include "DHT22.h"
#include "em_letimer.h"
#include "em_cmu.h"
#include "em_gpio.h"
#include "iadc.h"


void letimer0Init(void);
void letimer0Enable(void);
void letimer0Disable(void);
void LETIMER0_IRQHandler(void);
void batteryLevel(uint8_t* count, double* battery);



#endif /* LETIMER_H_ */
