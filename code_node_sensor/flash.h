/*
 * flash.h
 *
 *  Created on: Jul 19, 2023
 *      Author: KarimPham
 */

#ifndef FLASH_H_
#define FLASH_H_
#include "em_msc.h"
#include "em_cmu.h"

void writeDataToUserFlash(uint32_t *address,void const *data,uint32_t numBytes);
void eraseUserFlashData(uint32_t *address);
uint32_t flashReadData(uint32_t *address);
#endif /* FLASH_H_ */
