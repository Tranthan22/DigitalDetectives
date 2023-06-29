import requests

api_url = 'https://digitaldectectives.azdigi.blog/main/update_data.php'

data = {
    'user' : 'tien',
    'node_id' : 'node_01',
    'node_auto' : 'false',
    'node_air' : '60',
    'node_soil' : '80',
    'node_PH' : '8',
    'bump_1' : '0',
    'bump_2' : '0',
    'bump_3' : '0',
    'battery' : '100'
}

try:
    response = requests.post(api_url, data=data)

    if response.status_code == 200:
        print('Du lieu da duoc gui len co so du lieu')
    else:
        print('Co loi xay ra khi gui du lieu')
    print(response.request.body)
except requests.exceptions.RequestException as e:
    print('Loi ket noi', str(e))
    

