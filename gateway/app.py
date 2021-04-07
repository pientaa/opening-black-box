import requests
from flask import Flask, jsonify, request

app = Flask(__name__)


@app.route('/submit', methods=['post'])
def submit():
    try:
        try:
            data = request.get_json()
        except:
            raise ValueError

        if data is None:
            raise ValueError

    except ValueError:
        return 'Request data is not in json or is null', 400

    json_to_send = {
        "appResource": "/opt/spark-apps/black-box-assembly-1.0.jar",
        "sparkProperties": {
            "spark.master": "spark://spark-master:7077",
            "spark.driver.memory": "4g",
            "spark.driver.cores": "2",
            "spark.app.name": "BlackBox",
            "spark.submit.deployMode": "cluster",
            "spark.driver.supervise": "true"
        },
        "clientSparkVersion": "2.4.7",
        "mainClass": "BlackBox",
        "environmentVariables": {
            "SPARK_ENV_LOADED": "1"
        },
        "action": "CreateSubmissionRequest",
        "appArgs": [
            "postgres",
            data['function_name']
        ]
    }
    response = requests.post('http://10.5.0.2:6066/v1/submissions/create', json=json_to_send)
    print('response from server:', response.text)
    return response.json()


@app.route('/')
def hello_world():
    return jsonify({'status': 'api working'})


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
