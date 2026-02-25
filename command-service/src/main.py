import logging
import json
from datetime import datetime

logging.basicConfig(level=logging.INFO)

logger = logging.getLogger("command-service")

def log_json(message, level="info"):
    log_entry = {
        "timestamp": datetime.utcnow().isoformat(),
        "service": "command-service",
        "level": level,
        "message": message
    }
    logger.info(json.dumps(log_entry))
from fastapi import Request
import time

@app.middleware("http")
async def log_requests(request: Request, call_next):
    start_time = time.time()

    response = await call_next(request)

    duration = round(time.time() - start_time, 4)

    log_json(
        f"{request.method} {request.url.path} "
        f"status={response.status_code} "
        f"duration={duration}s"
    )

    return response

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
    from prometheus_client import Counter, generate_latest
from fastapi.responses import Response

REQUEST_COUNT = Counter("command_requests_total", "Total command service requests")

@app.get("/metrics")
def metrics():
    return Response(generate_latest(), media_type="text/plain")