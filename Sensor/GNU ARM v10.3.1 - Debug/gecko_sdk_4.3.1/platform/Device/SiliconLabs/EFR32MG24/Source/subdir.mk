################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk/platform/Device/SiliconLabs/EFR32MG24/Source/startup_efr32mg24.c \
C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk/platform/Device/SiliconLabs/EFR32MG24/Source/system_efr32mg24.c 

OBJS += \
./gecko_sdk_4.3.1/platform/Device/SiliconLabs/EFR32MG24/Source/startup_efr32mg24.o \
./gecko_sdk_4.3.1/platform/Device/SiliconLabs/EFR32MG24/Source/system_efr32mg24.o 

C_DEPS += \
./gecko_sdk_4.3.1/platform/Device/SiliconLabs/EFR32MG24/Source/startup_efr32mg24.d \
./gecko_sdk_4.3.1/platform/Device/SiliconLabs/EFR32MG24/Source/system_efr32mg24.d 


# Each subdirectory must supply rules for building sources it contributes
gecko_sdk_4.3.1/platform/Device/SiliconLabs/EFR32MG24/Source/startup_efr32mg24.o: C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk/platform/Device/SiliconLabs/EFR32MG24/Source/startup_efr32mg24.c gecko_sdk_4.3.1/platform/Device/SiliconLabs/EFR32MG24/Source/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: GNU ARM C Compiler'
	arm-none-eabi-gcc -g -gdwarf-2 -mcpu=cortex-m33 -mthumb -std=c99 '-DDEBUG=1' '-DDEBUG_EFM=1' '-DEFR32MG24B210F1536IM48=1' '-DHARDWARE_BOARD_DEFAULT_RF_BAND_2400=1' '-DHARDWARE_BOARD_SUPPORTS_1_RF_BAND=1' '-DHARDWARE_BOARD_SUPPORTS_RF_BAND_2400=1' '-DSL_BOARD_NAME="BRD4186C"' '-DSL_BOARD_REV="A01"' '-DSL_COMPONENT_CATALOG_PRESENT=1' -I"C:\Users\jengp\SimplicityStudio\v5_workspace\Node-Sensor_EM2_Backup\gecko_sdk_4.3.1\platform\Device\SiliconLabs\EFR32MG24\Include" -I"C:\Users\jengp\SimplicityStudio\v5_workspace\Node-Sensor_EM2_Backup\config" -I"C:\Users\jengp\SimplicityStudio\v5_workspace\Node-Sensor_EM2_Backup\autogen" -I"C:\Users\jengp\SimplicityStudio\v5_workspace\Node-Sensor_EM2_Backup" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/Device/SiliconLabs/EFR32MG24/Include" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//hardware/board/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/CMSIS/Core/Include" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//hardware/driver/configuration_over_swo/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/driver/debug/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/service/device_init/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/emdrv/common/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/emlib/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/common/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//hardware/driver/mx25_flash_shutdown/inc/sl_mx25_flash_shutdown_usart" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/common/toolchain/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/service/system/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/service/udelay/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/emdrv/ustimer/inc" -O0 -Wall -Wextra -mno-sched-prolog -fno-builtin -fomit-frame-pointer -ffunction-sections -fdata-sections -imacrossl_gcc_preinclude.h -mfpu=fpv5-sp-d16 -mfloat-abi=hard -mcmse --specs=nano.specs -c -fmessage-length=0 -MMD -MP -MF"gecko_sdk_4.3.1/platform/Device/SiliconLabs/EFR32MG24/Source/startup_efr32mg24.d" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

gecko_sdk_4.3.1/platform/Device/SiliconLabs/EFR32MG24/Source/system_efr32mg24.o: C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk/platform/Device/SiliconLabs/EFR32MG24/Source/system_efr32mg24.c gecko_sdk_4.3.1/platform/Device/SiliconLabs/EFR32MG24/Source/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: GNU ARM C Compiler'
	arm-none-eabi-gcc -g -gdwarf-2 -mcpu=cortex-m33 -mthumb -std=c99 '-DDEBUG=1' '-DDEBUG_EFM=1' '-DEFR32MG24B210F1536IM48=1' '-DHARDWARE_BOARD_DEFAULT_RF_BAND_2400=1' '-DHARDWARE_BOARD_SUPPORTS_1_RF_BAND=1' '-DHARDWARE_BOARD_SUPPORTS_RF_BAND_2400=1' '-DSL_BOARD_NAME="BRD4186C"' '-DSL_BOARD_REV="A01"' '-DSL_COMPONENT_CATALOG_PRESENT=1' -I"C:\Users\jengp\SimplicityStudio\v5_workspace\Node-Sensor_EM2_Backup\gecko_sdk_4.3.1\platform\Device\SiliconLabs\EFR32MG24\Include" -I"C:\Users\jengp\SimplicityStudio\v5_workspace\Node-Sensor_EM2_Backup\config" -I"C:\Users\jengp\SimplicityStudio\v5_workspace\Node-Sensor_EM2_Backup\autogen" -I"C:\Users\jengp\SimplicityStudio\v5_workspace\Node-Sensor_EM2_Backup" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/Device/SiliconLabs/EFR32MG24/Include" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//hardware/board/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/CMSIS/Core/Include" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//hardware/driver/configuration_over_swo/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/driver/debug/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/service/device_init/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/emdrv/common/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/emlib/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/common/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//hardware/driver/mx25_flash_shutdown/inc/sl_mx25_flash_shutdown_usart" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/common/toolchain/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/service/system/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/service/udelay/inc" -I"C:/Users/jengp/SimplicityStudio/SDKs/gecko_sdk//platform/emdrv/ustimer/inc" -O0 -Wall -Wextra -mno-sched-prolog -fno-builtin -fomit-frame-pointer -ffunction-sections -fdata-sections -imacrossl_gcc_preinclude.h -mfpu=fpv5-sp-d16 -mfloat-abi=hard -mcmse --specs=nano.specs -c -fmessage-length=0 -MMD -MP -MF"gecko_sdk_4.3.1/platform/Device/SiliconLabs/EFR32MG24/Source/system_efr32mg24.d" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


