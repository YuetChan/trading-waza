## Trading Waza
Backend application for uploading and querying rows.

### Requirements
- docker and docker compose installed
### Build & Run
    docker-compose up
    
A "tw-db-data" directory would be created in parent directory for storing sql data
### Usage
**Sign up**

First, create an invite code in redis

        docker exec -it tw-redis sh
        redis-cli
        set useremail:INVITE_CODE <code>
        
Then, create a deafult master in mariadb

        docker exec -it tw-mariadb sh
        mysql -u <username> -p <password> 
        use tw
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

**Upload rows, Post /rows**

        {
            "tickers": ["AAPL", "MSFT"],
            "indicators": ["bullish engulfing"],
        
            "processedAt": 1628547559002,
            "slaveId": 1,
            "userId": 1
        }
        
<br/>
    
**Query rows, Get /rows**

    ?daysAgo=1&indicators=td_9&pageNum=0&pageSize=10    


Due to the nature of the application, currently portal does not support any delete operations.
