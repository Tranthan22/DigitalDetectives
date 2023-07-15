import time
import serial
import RPi.GPIO as GPIO

ser = serial.Serial(
    port = '/dev/ttyS0',
    baudrate = 9600,
    parity = serial.PARITY_NONE,
    stopbits = serial.STOPBITS_ONE,
    bytesize = serial.EIGHTBITS,
    timeout = 1
)

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)


GPIO.setup(16, GPIO.OUT) 
GPIO.setup(18, GPIO.OUT)
GPIO.output(16, GPIO.LOW)
GPIO.output(18, GPIO.LOW)

print("Raspberry's sending: ")

try:
    while True:
        data = "000223754543473652"

        if len(data) % 2 != 0:
            print("Loi chuoi hex khong hop le")
            exit()

        asciiData = ""
        
        for i in range(0, len(data), 2):
            decPair = data[i:i+2]
            asciiData += chr(int(decPair))
        
        print(asciiData.encode())
        ser.write(asciiData.encode())
        time.sleep(1)
        ser.flush()
        time.sleep(1)
        
except KeyboardInterrupt:
    ser.close()


