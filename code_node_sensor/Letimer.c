/*
 * Letimer.c
 *
 *  Created on: Jun 18, 2023
 *      Author: KarimPham
 */

#include "Letimer.h"

#define Time_underflow   10  /* 20s */

double battery = 999;
uint8_t count = 0;

void letimer0Init(void){

  CMU_ClockEnable(cmuClock_LETIMER0, true);
  LETIMER_Init_TypeDef letimerInit = LETIMER_INIT_DEFAULT;
  letimerInit.enable = false;

  LETIMER_Init(LETIMER0, &letimerInit);
  LETIMER_IntClear(LETIMER0, _LETIMER_IF_MASK);
  LETIMER_TopSet(LETIMER0, CMU_ClockFreqGet(cmuClock_LETIMER0) / 1 * Time_underflow);

  LETIMER_IntEnable(LETIMER0, LETIMER_IEN_UF);
  NVIC_EnableIRQ(LETIMER0_IRQn);

}

void letimer0Enable(void){

  uint32_t flags=LETIMER_IntGet(LETIMER0);
  LETIMER_IntClear(LETIMER0, flags);
  LETIMER_Enable(LETIMER0, true);

}

void batteryLevel(uint8_t* count, double* battery) {
    (*count)++;
    if (*count == 200) {
        *battery = *battery - 1;
        *count = 0;
    }
}

void letimer0Disable(void){

  uint32_t flags=LETIMER_IntGet(LETIMER0);
  LETIMER_IntClear(LETIMER0, flags);
  LETIMER_Enable(LETIMER0, false);

}

void LETIMER0_IRQHandler(void){

  uint32_t flags=LETIMER_IntGet(LETIMER0);
  LETIMER_IntClear(LETIMER0, flags);

  DHT_DataTypedef DHT_data;
  char Mois[4], addressAndMois[12], Temp[4], Humi[4], data_sensor[21], cell[4], Data[10];
  char address[9]= "00002311"; /* 000023: Station's address */
  double batLevel;

  /* Get Moisture */
  iadcStartsingle();
  double Moisture = getMoisture();

  /* Get Temperature and Humidity  */
  DHT_GetData(&DHT_data);
  double Temperature = DHT_data.Temperature;
  double Humidity = DHT_data.Humidity;

  /* Create a data string */
  doubleToCharArray(Humidity, Humi, 4);
  doubleToCharArray(Moisture, Mois, 4);
  doubleToCharArray(Temperature, Temp, 4);
  insertStringAtBeginning(Mois, addressAndMois, address);

  batteryLevel(&count, &battery);
  batLevel = battery;
  doubleToCharArray(batLevel, cell, 4);
  concatenateArrays(addressAndMois, Temp, Humi, cell, data_sensor);
  data(data_sensor, Data);


  uint8_t length = sizeof(Data);
  transmitData(Data, length);
  uint32_t k = 8000000; /* Wait for a period of time to receive a signal response from the station */
  bool i = true;
  char receivedData;
  while (i){
      k--;
      if( USART0->STATUS & USART_STATUS_RXDATAV ){
      receivedData = (uint8_t)USART0->RXDATA;
      if( receivedData == '1' ){
          i= false;
      }
      else if ( receivedData == '0' ){
          i= false;
          transmitData(Data, length); /* Retransmit the data because the previous data may be corrupted. */
      }
      }
      else if(k==0){
          i=false;
          transmitData(Data, length); /* Retransmit the data as there is no response */
      }
  }

}






























/* Transmit data */
/*  Data[0] = 0xFF;
Data[1] = 0xFF;
Data[2] = 0x17;
Data[3] = 0x32;
Data[4] = 0x33;
Data[5] = 0x34;
Data[6] = 0x35;
Data[7] = 0x36;
Data[8] = 0x37;
Data[9] = 0x38;
*/



