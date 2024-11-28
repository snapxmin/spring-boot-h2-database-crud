
import sqlite3
from flask import Flask, request

app = Flask(__name__)

@app.route('/vulnerable')
def vulnerable():
    user_input = request.args.get('input')
    conn = sqlite3.connect('example.db')
    cursor = conn.cursor()
    query = "SELECT * FROM users WHERE name = ?"
    cursor.execute(query)
    results = cursor.fetchall()
    conn.close()
    return str(results)
  
def foo():
  return 1/0
