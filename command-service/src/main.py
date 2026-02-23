from fastapi import FastAPI
from pydantic import BaseModel
from kafka_producer import send_event
import uuid

app = FastAPI()

class Order(BaseModel):
    product_id: str
    quantity: int
    price: float

@app.post("/create-order")
def create_order(order: Order):
    order_id = str(uuid.uuid4())

    event = {
        "order_id": order_id,
        "product_id": order.product_id,
        "quantity": order.quantity,
        "price": order.price
    }

    send_event("order-events", event)

    return {"message": "Order created", "order_id": order_id}