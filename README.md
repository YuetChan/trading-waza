## Evening Brew Portal
Backend application for uploading and querying posts.

### Requirements
- docker and docker compose installed
### Build & Run
    docker-compose up
    
A "ebdbdata" directory would be created in parent directory for storing sql data
### Usage
Sign up

    First, create an invite code in redis.
        $ docker exec -it eb-redis sh
        $ redis-cli
        $ set useremail:INVITE_CODE <value>
    Then, create a deafult master in mariadb
        $ docker exec -it eb-mariadb sh
        $ mysql -u <username> -p <password> 
        $ use eb
        $ insert into master(master_id, name, uploaded_at, uploaded_by) values (1, "default_master", -1, -1);    
    Finally, Post /register
        {
            "inviteCode": "code",
            "useremail" : "example@gmail.com",
            "password" : "password",
            "username" : "username"
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
