from fastapi import FastAPI
from database import get_connection
from consumer import start_consumer
import threading

app = FastAPI()

@app.get("/health")
def health():
    return {"status": "query-service healthy"}

@app.on_event("startup")
def startup_event():
    thread = threading.Thread(target=start_consumer)
    thread.daemon = True
    thread.start()

@app.get("/orders")
def get_orders():
    conn = get_connection()
    cur = conn.cursor()

    cur.execute("SELECT order_id, product_id, quantity, price FROM orders")
    rows = cur.fetchall()

    cur.close()
    conn.close()

    orders = [
        {
            "order_id": r[0],
            "product_id": r[1],
            "quantity": r[2],
            "price": r[3]
        }
        for r in rows
    ]

    return {"orders": orders}
    from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from fastapi import Depends, HTTPException
from auth import verify_token

security = HTTPBearer()

def auth_required(credentials: HTTPAuthorizationCredentials = Depends(security)):
    try:
        verify_token(credentials.credentials)
    except:
        raise HTTPException(status_code=401, detail="Invalid token")
    @app.get("/orders")
def get_orders(credentials: HTTPAuthorizationCredentials = Depends(auth_required)):