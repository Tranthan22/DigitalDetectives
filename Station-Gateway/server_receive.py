import requests

api_url = 'https://digitaldectectives.azdigi.blog/main/get_status.php'


data = {
    'user' : 'tien',
    'node_id' : 'node_01'
}

try:
    response = requests.get(api_url, params=data)

    if response.status_code == 200:
        print('Du lieu da duoc gui ve thanh cong')
    else:
        print('Co loi xay ra khi lay du lieu')
    
except requests.exceptions.RequestException as e:
    print('Loi ket noi', str(e))
    
receive = response.json()
Id = receive['bump_1']
print(Id)