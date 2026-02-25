from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from fastapi import Depends, HTTPException
from auth import verify_token

security = HTTPBearer()

def auth_required(credentials: HTTPAuthorizationCredentials = Depends(security)):
    try:
        verify_token(credentials.credentials)
    except:
        raise HTTPException(status_code=401, detail="Invalid token")