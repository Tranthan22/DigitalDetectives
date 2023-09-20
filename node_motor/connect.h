/*
 * connect.h
 *
 *  Created on: Sep 15, 2023
 *      Author: assus
 */

#ifndef CONNECT_H_
#define CONNECT_H_
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "sleeptimer_app.h"
#include "sl_sleeptimer.h"
#include "sl_simple_led_instances.h"
//#include "sl_simple_button_instances.h"
#include "sl_iostream_init_instances.h"
#include"sl_iostream.h"
#include"sl_iostream_init_instances.h"
#include"sl_iostream_handles.h"
#include"em_gpio.h"
void connectToStation(void);
void timer(void);


#endif /* CONNECT_H_ */
