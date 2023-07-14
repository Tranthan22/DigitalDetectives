/*
 * adc.c
 *
 *  Created on: Jun 18, 2023
 *      Author: KarimPham
 */

#include "adc.h"


void iadcInit(void){

  CMU_ClockEnable(cmuClock_IADC0,true);

  IADC_Init_t init = IADC_INIT_DEFAULT;
  init.srcClkPrescale = IADC_calcSrcClkPrescale(IADC0, 20000000, 0);

  IADC_AllConfigs_t initAllConfigs = IADC_ALLCONFIGS_DEFAULT;
  IADC_InitSingle_t initSingle = IADC_INITSINGLE_DEFAULT;
  IADC_SingleInput_t initSingleInput = IADC_SINGLEINPUT_DEFAULT;

  IADC_reset(IADC0);

  initAllConfigs.configs[0].reference = iadcCfgReferenceVddx;
  initAllConfigs.configs[0].vRef = 3300;

  initAllConfigs.configs[0].adcClkPrescale = IADC_calcAdcClkPrescale(IADC0,
                                                                     10000000,
                                                                     0,iadcCfgModeNormal,
                                                                     init.srcClkPrescale);
  initAllConfigs.configs[0].osrHighSpeed = iadcCfgOsrHighSpeed32x;
  initAllConfigs.configs[0].digAvg = iadcDigitalAverage16;

  initSingle.triggerAction = iadcTriggerActionOnce;

  initSingleInput.posInput = iadcPosInputPortAPin5; /*P43*/
  initSingleInput.negInput = iadcNegInputGnd;

  IADC_init(IADC0, &init, &initAllConfigs);
  IADC_initSingle(IADC0, &initSingle, &initSingleInput);
  GPIO->ABUSALLOC |= GPIO_ABUSALLOC_AODD0_ADC0;



}

void iadcStartsingle(void){

  IADC_command(IADC0, iadcCmdStartSingle);
  for(uint16_t i = 0; i<800; i++);
}

double Moisture;
double voltage;
double getMoisture(void){

  IADC_Result_t sample = IADC_readSingleResult(IADC0);
  voltage = (double)sample.data * 3.3 / 0xFFF;
  Moisture = ((3.3 - voltage) / 3.3) * 100;
  return Moisture;

}


