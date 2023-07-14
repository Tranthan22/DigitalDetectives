/*
 * DHT22.h
 *
 *  Created on: Jun 20, 2023
 *      Author: KarimPham
 */

#include "em_cmu.h"
#include "ustimer.h"
#include "em_gpio.h"

#ifndef DHT22_H_
#define DHT22_H_

typedef struct
{
  double Temperature;
  double Humidity;
}DHT_DataTypedef;


void DHT_GetData (DHT_DataTypedef *DHT_Data);

#endif /* DHT22_H_ */
