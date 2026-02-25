from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
from kafka_producer import send_event
import uuid

app = FastAPI()

class Order(BaseModel):
    product_id: str = Field(..., min_length=1)
    quantity: int = Field(..., gt=0)
    price: float = Field(..., gt=0)

@app.get("/health")
def health():
    return {"status": "command-service healthy"}

@app.post("/create-order")
def create_order(order: Order):
    try:
        order_id = str(uuid.uuid4())

        event = {
            "order_id": order_id,
            "product_id": order.product_id,
            "quantity": order.quantity,
            "price": order.price
        }

        send_event("order-events", event)

        return {
            "status": "success",
            "order_id": order_id
        }

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))