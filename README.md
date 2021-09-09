## Evening Brew Portal
Backend application for uploading and querying posts.

### Requirements
- docker and docker compose installed
### Build & Run
    docker-compose up

### Usage
Sign up

    First, create an invite code in redis.
    Then, Post /register
        {
            "useremail" : "example@gmail.com",
            "password" : "password",
            "username" : "username",
            "inviteCode": "code"
        } 

Sign in

    Post /signin
        {
            "useremail" : "example@gmail.com",
            "password" : "password"
        }    

Upload posts
    
    Post /posts
        {
            "title": "",
            "description": "",
            "contents": [],
            
            "tickers": ["AAPL", "MSFT"],
            "tags": ["bullish engulfing"],
        
            "processedAt": 1628547559002,
            "slaveId": 1,
            "userId": 1
        }
    
Query posts

    Get /post?daysAgo=1&tags=bullish engulfing&ticker=AAPL&pageNum=0&pageSize=10    


Due to the nature of the application, currently portal does not support any delete operation.
