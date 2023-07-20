/*
 * adcScan.h
 *
 *  Created on: Jun 22, 2023
 *      Author: jengp
 */

#ifndef ADCSCAN_H_
#define ADCSCAN_H_

#include "em_iadc.h"
#include "em_cmu.h"

void initIadcScan(void);
void iadcStartScan(void);
double get_Moisture(void);
double get_Temp(void);


#endif /* ADCSCAN_H_ */
