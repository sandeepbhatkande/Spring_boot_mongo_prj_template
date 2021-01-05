import devops_junitresult_parser as junit
from flask import Flask, request

app = Flask(__name__)

@app.route ('/send_junit_mail', methods=['POST'])
def send_junit_mail():
    build_no = request.headers['CI_JOB_ID']
    return junit.send_junit_mail(build_no)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5021, debug=True)

