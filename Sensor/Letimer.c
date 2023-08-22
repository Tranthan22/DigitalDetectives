/*
 * Letimer.c
 *
 *  Created on: Jun 18, 2023
 *      Author: PhongPham
 */

#include "Letimer.h"

#define Time_underflow   10  /* 10s */
extern char address[5];
char dataTransmit[18] = {0};
uint8_t interrupt = 0;
uint16_t battery = 100;
uint8_t count = 0;

void letimer0Init(void){

  CMU_ClockEnable(cmuClock_LETIMER0, true);
  LETIMER_Init_TypeDef letimerInit = LETIMER_INIT_DEFAULT;
  letimerInit.enable = false;
  LETIMER_Init(LETIMER0, &letimerInit);
  LETIMER_IntClear(LETIMER0, _LETIMER_IF_MASK);
  LETIMER_TopSet(LETIMER0, CMU_ClockFreqGet(cmuClock_LETIMER0) / 1 * Time_underflow);
  LETIMER_IntEnable(LETIMER0, LETIMER_IEN_UF);
  NVIC_ClearPendingIRQ(LETIMER0_IRQn);
  NVIC_EnableIRQ(LETIMER0_IRQn);

}

void letimer0Enable(void){

  uint32_t flags=LETIMER_IntGet(LETIMER0);
  LETIMER_IntClear(LETIMER0, flags);
  LETIMER_Enable(LETIMER0, true);

}

void letimer0Disable(void){

  uint32_t flags=LETIMER_IntGet(LETIMER0);
  LETIMER_IntClear(LETIMER0, flags);
  LETIMER_Enable(LETIMER0, false);

}

void batteryLevel(uint8_t* count, uint16_t* battery) {
    (*count)++;
    if (*count == 200) {
        *battery = *battery - 1;
        *count = 0;
    }
}

void LETIMER0_IRQHandler(void) {
    uint32_t flags = LETIMER_IntGet(LETIMER0);
    LETIMER_IntClear(LETIMER0, flags);
    interrupt++;

    if(interrupt == 2){

    char data_sensor[17] = {0};
    memcpy(data_sensor, address, sizeof(address));

    iadcStartsingle();
    uint16_t Moisture = getMoisture();
    uint16ToCharArray(Moisture, &data_sensor[4], 4);

    DHT_DataTypedef DHT_data;
    DHT_GetData(&DHT_data);
    uint16ToCharArray(DHT_data.Temperature, &data_sensor[7], 4);
    uint16ToCharArray(DHT_data.Humidity, &data_sensor[10], 4);

<<<<<<< HEAD
    batteryLevel(&count, &battery);
    uint16_t batLevel = battery;
    uint16ToCharArray(batLevel, &data_sensor[13], 4);

    char lrc = calculateLrc(data_sensor, sizeof(data_sensor));
=======
  /* Get battery level */
  batteryLevel(&count, &battery);
  uint16_t batLevel = battery;
 
  /* Create a data string */
  uint16ToCharArray(Humidity, Humi, 4);
  uint16ToCharArray(Moisture, Mois, 4);
  uint16ToCharArray(Temperature, Temp, 4);
  uint16ToCharArray(batLevel, Cell, 4);
  char data_sensor[17] = {address[0], address[1], address[2], address[3],
                                               Mois[0], Mois[1], Mois[2],
                                               Temp[0], Temp[1], Temp[2],
                                               Humi[0], Humi[1], Humi[2],
                                               Cell[0], Cell[1], Cell[2]};
  /* Calculate Lrc */
  char lrc = calculateLrc(data_sensor, sizeof(data_sensor));
>>>>>>> a48967ecf9d5cfa4535ecfe3ca4c748ab343f2f2

    memcpy(dataTransmit, data_sensor, sizeof(data_sensor));
    dataTransmit[16] = lrc;
    dataTransmit[17] = 0;

    transmitData(dataTransmit, sizeof(dataTransmit) - 1);
    EUSART_IntClear(EUSART0, EUSART_IF_RXFL);
    EUSART_IntEnable(EUSART0, EUSART_IEN_RXFL);

    }
    if(interrupt == 3){
        transmitData(dataTransmit, sizeof(dataTransmit)-1);
        interrupt = 0;
        EUSART_IntDisable(EUSART0, EUSART_IEN_RXFL);
    }
}

