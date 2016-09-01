# spring_blog_api

## Implement a restful blogging API using spring boot.

The structure of the blog is as follows: A blog belongs to a user who can modify it’s contents through authentication. A blog is comprised of categories each which of is host to one or more posts. A post can be commented on by anyone and does not require authentication however only the owner of the blog can delete existing comments.

## Implementation
This application was developed using spring-boot framework (http://projects.spring.io/spring-boot/), and its a REST backend for posts and comments on a blog. Every user on dabatase has its own blog, and its acessible through the endpoints. I based the implementation on spring-boot rest and security examples from https://github.com/royclarkson/spring-rest-service-oauth. 

## Installation
Steps to install and run the application. 
1. Git clone it
2. Install Java 8+ and Maven 3+
3. Run <strong>clean package install spring-boot:run</strong> on root folder
4. This application will be available on http://localhost:8080

## Usage
1. With a running application, the first step is to authenticate:

`curl -X POST -vu clientapp:123456 http://localhost:8080/oauth/token -H "Accept: application/json" -d "password=spring&username=roy&grant_type=password&scope=read%20write&client_secret=123456&client_id=clientapp"`

If the authentication was succesfull, you are going to receive something like that

`{
  "access_token": "b98ed5b3-7c5f-4445-9298-74619824ca28",
  "token_type": "bearer",
  "refresh_token": "f554d386-0b0a-461b-bdb2-292831cecd57",
  "expires_in": 43199,
  "scope": "read write"
}`

2. After authentication, you can create your posts. Posts creation requires authentication. 

`curl -X POST http://localhost:8080/posts -H "Accept: application/json" -H "Content-Type: application/json" -H "Accept: application/json" -H "Authorization: Bearer b98ed5b3-7c5f-4445-9298-74619824ca28" -d '{"title" : "title","category" : "category","content" : "content"}'`

3. After that, you can navigate through your posts using pagination:

`curl -H "Accept: application/json" -X GET http://localhost:8080/posts/users/roy?page=0&size=0`

4. Or access a specific post through its id:

`curl -H "Accept: application/json" -X GET http://localhost:8080/posts/1`

5. You can add comments to any posts you want. If you send your authorization key, the comment will have your name on it. Otherwise, it will be anonymous. 

`curl -X POST http://localhost:8080/posts/1/comments -H "Accept: application/jAccept: application/json" -H "Authorization: Bearer b98ed5b3-7c5f-4445-9298-74619824ca28" -d '{"content" : "content"}'`

6. You can also remove comments from YOUR posts, not from other user's posts

`curl -H "Authorization: Bearer b98ed5b3-7c5f-4445-9298-74619824ca28" -X DELETE http://localhost:8080/posts/1/comments/1`

