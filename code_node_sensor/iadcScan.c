/*
 * iadcScan.c
 *
 *  Created on: Jun 22, 2023
 *      Author: KarimPham
 */


#include "iadcScan.h"

/* A0: Moisture, A5: pH */

void initIadcScan(void){

  CMU_ClockEnable(cmuClock_IADC0, true);
  CMU_ClockSelectSet(cmuClock_IADCCLK, cmuSelect_FSRCO); /* can run in EM2 */

  IADC_Init_t init = IADC_INIT_DEFAULT;
  IADC_AllConfigs_t initAllConfigs = IADC_ALLCONFIGS_DEFAULT;
  IADC_InitScan_t initScan = IADC_INITSCAN_DEFAULT;
  IADC_ScanTable_t scanTable = IADC_SCANTABLE_DEFAULT;

  init.srcClkPrescale = IADC_calcSrcClkPrescale(IADC0, 20000000, 0);
  init.warmup = iadcWarmupNormal;
  initAllConfigs.configs[0].reference = iadcCfgReferenceExt2V5;
  initAllConfigs.configs[0].vRef = 2500;
  initAllConfigs.configs[0].analogGain = iadcCfgAnalogGain0P5x;
  initAllConfigs.configs[0].osrHighSpeed = iadcCfgOsrHighSpeed32x;
  initAllConfigs.configs[0].adcClkPrescale = IADC_calcAdcClkPrescale(IADC0, 10000000, 0, iadcCfgModeNormal, init.srcClkPrescale);
  initScan.showId = true;
  initScan.start = true;

  scanTable.entries[0].posInput = iadcPosInputPortAPin0;
  scanTable.entries[0].negInput = iadcNegInputGnd;
  scanTable.entries[0].includeInScan = true;

  scanTable.entries[1].posInput = iadcPosInputPortAPin5;
  scanTable.entries[1].negInput = iadcNegInputGnd;
  scanTable.entries[1].includeInScan = true;

  IADC_init(IADC0, &init, &initAllConfigs);
  IADC_initScan(IADC0, &initScan, &scanTable);

  GPIO->ABUSALLOC |= GPIO_ABUSALLOC_AEVEN0_ADC0;
  GPIO->ABUSALLOC |= GPIO_ABUSALLOC_AODD0_ADC0;

  IADC0->EN=IADC_EN_EN;
}

void iadcStartScan(void){

  IADC_command(IADC0, iadcCmdStartScan);
  for(uint16_t i = 0; i<2000; i++);

}

double Mois;
double get_Moisture(void){

  IADC_Result_t result = {0, 0};
  result = IADC_pullScanFifoResult(IADC0);
  Mois = (double)result.data * 100 / 0xFFF;
  Mois =  Mois * 10;
  return Mois;

}

double Temp;
double get_Temp(void){

  IADC_Result_t result_2 = {0, 0};
  result_2 = IADC_pullScanFifoResult(IADC0);
  Temp = (double)result_2.data * 5 * 100 / 0xFFF  ;
  /* Temp = 11 * ( result_2.data * 5 / 0xFFF ) +5; */
  return Temp;

}
