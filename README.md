## Trading Board
Backend application for uploading and querying posts.

### Requirements
- docker and docker compose installed
### Build & Run
    docker-compose up
    
A "tbdbdata" directory would be created in parent directory for storing sql data
### Usage
**Sign up**

First, create an invite code in redis

        docker exec -it tb-redis sh
        redis-cli
        set useremail:INVITE_CODE <code>
        
Then, create a deafult master in mariadb

        docker exec -it tb-mariadb sh
        mysql -u <username> -p <password> 
        use tb
        insert into master(master_id, name, uploaded_at, uploaded_by) values (1, "default_master", -1, -1);    

**Finally, Post /register**

        {
            "inviteCode": <code>,
            "useremail" : <useremail>,
            "password" : <password>,
            "username" : <username>
        } 

<br/>  

**Sign in, Post /signin**

Then, replace Bearer token with returned jwt token
    
        {
            "useremail" : <useremail>,
            "password" : <password>
        }    

<br/> 

**Upload posts, Post /posts**

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
        
<br/>
    
**Query posts, Get /posts**

    ?daysAgo=1&tags=td_9&pageNum=0&pageSize=10    


Due to the nature of the application, currently portal does not support any delete operations.
