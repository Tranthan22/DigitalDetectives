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

data = "hehe"

try:
    while True:
        ser.write(data.encode())
        time.sleep(1)
        ser.flush()
        time.sleep(1)
        
except KeyboardInterrupt:
    ser.close()
