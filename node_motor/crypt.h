/*
 * crypt.h
 *
 *  Created on: Sep 15, 2023
 *      Author: assus
 */

#ifndef CRYPT_H_
#define CRYPT_H_
#include "em_device.h"
#include "em_chip.h"
#include "em_cmu.h"
#include "mbedtls/aes.h"
#include <string.h>

void EncryptDataECB(unsigned char *data, unsigned char *encryptedData);
void DecryptDataECB(unsigned char *encryptedData, unsigned char *decryptedData);



#endif /* CRYPT_H_ */
