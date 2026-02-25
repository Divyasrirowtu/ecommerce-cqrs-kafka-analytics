from fastapi import FastAPI
from database import get_connection
from consumer import start_consumer
import threading

app = FastAPI()

@app.on_event("startup")
def startup_event():
    thread = threading.Thread(target=start_consumer)
    thread.daemon = True
    thread.start()

@app.get("/orders")
def get_orders():
    conn = get_connection()
    cur = conn.cursor()

    cur.execute("SELECT * FROM orders")
    rows = cur.fetchall()

    cur.close()
    conn.close()

    return {"orders": rows}