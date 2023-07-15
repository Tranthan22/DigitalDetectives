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

print("Raspberry's receiving: ")

def ascii_to_decimal(data):
    decimal_data = ""
    for char in data:      
        decimal = ord(char)
        if decimal < 10:
            decimal_data += "0" + str(decimal)
        else:
            decimal_data += str(decimal)
    return decimal_data

try:
    while True:
        s = ser.readline()
        data = s.decode()
        print(ascii_to_decimal(data))
        
except KeyboardInterrupt:
    ser.close()

