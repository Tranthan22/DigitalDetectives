/*
 * adc.h
 *
 *  Created on: Jun 18, 2023
 *      Author: KarimPham
 */

#ifndef ADC_H_
#define ADC_H_

#include "em_iadc.h"
#include "em_cmu.h"
#include <string.h>
#include "uart.h"

void iadcInit(void);
void iadcStartsingle(void);
double getMoisture(void);



#endif /* ADC_H_ */
