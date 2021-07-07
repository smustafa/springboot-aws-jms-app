The application will do the following:

- Create API Endpoint of Creating a User
- Send a JMS Message to Amazon MQ to Queue [User.Queue]
- Receive JMS Message from Amazon MQ from Queue [User.Queue]
- Store User Information into MongoDB (In Memory)
- Create PDF Document with User Information
- Store User PDF Document into S3 Bucket in AWS


Purpose of this:
- Simple example to showcase:
- Send/Receive JMS Messages in SpringBoot
- Using MongoDB to store a Document
- Using iText to create a simple PDF
- Using Amazon MQ and S3 in AWS.

