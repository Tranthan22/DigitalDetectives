/*
 * adc.h
 *
 *  Created on: Jun 10, 2023
 *      Author: PhongPham
 */

#ifndef IADC_H_
#define IADC_H_

#include "em_iadc.h"
#include "uart.h"


void iadcInit(void);
uint16_t getMoisture(void);

#endif /* IADC_H_ */
