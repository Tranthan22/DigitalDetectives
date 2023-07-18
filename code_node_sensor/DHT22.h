/*
 * DHT22.h
 *
 *  Created on: Jun 20, 2023
 *      Author: KarimPham
 */

#ifndef DHT22_H_
#define DHT22_H_

#include "em_cmu.h"
#include "ustimer.h"
#include "em_gpio.h"

typedef struct
{
  double Temperature;
  double Humidity;
}DHT_DataTypedef;


void DHT_GetData (DHT_DataTypedef *DHT_Data);

#endif /* DHT22_H_ */
