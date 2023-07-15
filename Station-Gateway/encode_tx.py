import time
import serial
import RPi.GPIO as GPIO
from Crypto.Cipher import AES

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

key = bytes([0x00, 0x01, 0x02, 0x03,
             0x04, 0x05, 0x06, 0x07,
             0x08, 0x09, 0x0A, 0x0B,
             0x0C, 0x0D, 0x0E, 0x0F])

iv = bytes([0x10, 0x11, 0x12, 0x13,
            0x14, 0x15, 0x16, 0x17,
            0x18, 0x19, 0x1A, 0x1B,
            0x1C, 0x1D, 0x1E, 0x1F])

cipher = AES.new(key, AES.MODE_CBC, iv)

print("Raspberry's sending: ")
data = "hehehehehehehehe"

try:
    while True:
        encrypted_data = cipher.encrypt(data.encode())
        ser.write(encrypted_data)
        ser.flush()
        time.sleep(1)
        
except KeyboardInterrupt:
    ser.close()

