## Evening Brew Portal
Backend application for uploading and querying posts.

### Requirements
- docker and docker compose installed
### Build & Run
    docker-compose up

### Usage
Signing up

    First, create an invite code in redis.
    Then, hit the sign up api with the useremail, password, username and invite code.

Signing in

    Hit the sign in api with the credential from sign up 

Uploading posts
    
    Hit the create post api with reqired request body.
    
Querying posts

    Hit the get post by filter api with reqired query params.    


Due to the nature of the application, currently portal does not support any delete operation.
