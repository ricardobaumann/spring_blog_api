# spring_blog_api

"Implement a restful blogging API using spring boot.

The structure of the blog is as follows: A blog belongs to a user who can modify it’s contents through authentication. A blog is comprised of categories each which of is host to one or more posts. A post can be commented on by anyone and does not require authentication however only the owner of the blog can delete existing comments. The owner can also upload files to a post.

The API should be entirely consumable through postman, that is no front end is required. UML diagrams and unit tests as well as the use of postgres are a bonus."

Stories breakdown
1. As a user, I want to create my own posts. The post should contain a title (string, 100 characters maximum), content (string, 1000 characters maximum) and a category (string, 50 characters). All fields are required, and the post must be assigned to an id.
2. As a user, I want to access a list of my posts, ordered from newest to oldest. The list should show the post title and category, with pagination. 
3. As a user, I want to authenticate myself to have access to my posts, so no other user will be able to modify my content. 
